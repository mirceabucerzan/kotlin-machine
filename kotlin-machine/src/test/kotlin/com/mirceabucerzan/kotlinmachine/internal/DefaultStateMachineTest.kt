package com.mirceabucerzan.kotlinmachine.internal

import com.mirceabucerzan.kotlinmachine.Provider
import com.mirceabucerzan.kotlinmachine.StateMachine
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultStateMachineTest {

    private var t01Calls: Int = 0
    private var t10Calls: Int = 0
    private var t12Calls: Int = 0
    private var t21Calls: Int = 0
    private var t23Calls: Int = 0
    private var t32Calls: Int = 0
    private lateinit var machine: StateMachine<State, Input>

    @Before
    fun setUp() {
        StateMachine.LOGGING_ENABLED = true

        val s0 = State.S0()
        val s1 = State.S1()
        val s2 = State.S2()
        val s3 = State.S3()

        val t01 = Provider.provideTransition<State, Input>(s0, s1, Input.I0()) { t01Calls++ }
        val t10 = Provider.provideTransition<State, Input>(s1, s0, Input.I1()) { t10Calls++ }
        val t12 = Provider.provideTransition<State, Input>(s1, s2, Input.I2()) { t12Calls++ }
        val t21 = Provider.provideTransition<State, Input>(s2, s1, Input.I3()) { t21Calls++ }
        val t23 = Provider.provideTransition<State, Input>(s2, s3, Input.I4()) { t23Calls++ }
        val t32 = Provider.provideTransition<State, Input>(s3, s2, Input.I5()) { t32Calls++ }
        val tException = Provider.provideTransition<State, Input>(s0, s2, Input.I6()) {
            throw RuntimeException()
        }

        machine = Provider.provideBuilder<State, Input>().run {
            addInitialState(s0)
            addStates(setOf(s1, s2))
            addFinalState(s3)
            addTransitions(setOf(t01, t10, t12, t21, t23, t32, tException))
            build()
        }
    }

    @Test
    fun testTransitionSuccessful() {
        val state = machine.process(Input.I0())

        assertEquals("Invalid state after transition", State.S1(), state)
        assertEquals("Invalid state after transition", State.S1(), machine.currentState)
        assertEquals("Transition output not called", 1, t01Calls)
    }

    @Test
    fun testTransitionNotPerformed() {
        val state = machine.process(Input.I1())

        assertEquals("Invalid state after transition not performed", State.S0(), state)
        assertEquals(
            "Invalid state after transition not performed",
            State.S0(),
            machine.currentState
        )
        assertEquals("Transition output called", 0, t10Calls)
    }

    @Test
    fun testFinalStateTransitionNotPerformed() {
        machine.process(Input.I0())
        machine.process(Input.I2())
        machine.process(Input.I4())
        val state = machine.process(Input.I5())

        assertEquals("Not in final state", State.S3(), state)
        assertEquals("Not in final state", State.S3(), machine.currentState)
        assertEquals("Transition output from final state called", 0, t32Calls)
    }

    @Test
    fun testMultipleTransitionsSuccessful() {
        machine.process(Input.I0())
        machine.process(Input.I1())
        machine.process(Input.I0())
        machine.process(Input.I2())
        machine.process(Input.I3())
        val state = machine.process(Input.I2())

        assertEquals("Invalid initial state", State.S0(), machine.initialState)
        assertEquals("Invalid current state", State.S2(), state)
        assertEquals("Invalid current state", State.S2(), machine.currentState)
        assertEquals("Invalid t01Calls", 2, t01Calls)
        assertEquals("Invalid t10Calls", 1, t10Calls)
        assertEquals("Invalid t12Calls", 2, t12Calls)
        assertEquals("Invalid t21Calls", 1, t21Calls)
        assertEquals("Invalid t23Calls", 0, t23Calls)
        assertEquals("Invalid t32Calls", 0, t32Calls)
    }

    @Test(expected = RuntimeException::class)
    fun testTransitionException() {
        machine.process(Input.I6())
    }
}