package com.mirceabucerzan.kotlinmachine.internal

import com.mirceabucerzan.kotlinmachine.Provider
import com.mirceabucerzan.kotlinmachine.StateMachineBuilder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultStateMachineBuilderTest {

    private lateinit var builder: StateMachineBuilder<State, Input>

    @Before
    fun setUp() {
        builder = DefaultStateMachineBuilder()
    }

    @Test
    fun testAddInitialState() {
        val state = State.S0()
        builder.addInitialState(state)

        val machine = builder.build()

        assertEquals("Invalid initialState", state, machine.initialState)
        assertEquals("allStates size is not 1", 1, machine.allStates.size)
        assertEquals("initialState not contained in allStates", state, machine.allStates.first())
    }

    @Test
    fun testAddState() {
        val s0 = State.S0()
        val s1 = State.S1()

        val machine = builder.run {
            addInitialState(s0)
            addState(s1)
            build()
        }

        assertEquals("allStates size is not 2", 2, machine.allStates.size)
        assertEquals("Invalid allStates", setOf(s0, s1), machine.allStates)
    }

    @Test
    fun testAddStates() {
        val s0 = State.S0()
        val s1 = State.S1()
        val s2 = State.S2()

        val machine = builder.run {
            addInitialState(s0)
            addStates(setOf(s1, s2))
            build()
        }

        assertEquals("allStates size is not 3", 3, machine.allStates.size)
        assertEquals("Invalid allStates", setOf(s0, s1, s2), machine.allStates)
    }

    @Test
    fun testAddFinalState() {
        val s0 = State.S0()
        val s1 = State.S1()
        val s2 = State.S2()

        val machine = builder.run {
            addInitialState(s0)
            addState(s1)
            addFinalState(s2)
            build()
        }

        assertEquals("finalStates size is not 1", 1, machine.finalStates.size)
        assertEquals("Invalid finalStates", setOf(s2), machine.finalStates)
        assertEquals("allStates size is not 3", 3, machine.allStates.size)
        assertEquals("Invalid allStates", setOf(s0, s1, s2), machine.allStates)
    }

    @Test
    fun testAddFinalStates() {
        val s0 = State.S0()
        val s1 = State.S1()
        val s2 = State.S2()
        val s3 = State.S3()

        val machine = builder.run {
            addInitialState(s0)
            addState(s1)
            addFinalStates(setOf(s2, s3))
            build()
        }

        assertEquals("finalStates size is not 2", 2, machine.finalStates.size)
        assertEquals("Invalid finalStates", setOf(s2, s3), machine.finalStates)
        assertEquals("allStates size is not 4", 4, machine.allStates.size)
        assertEquals("Invalid allStates", setOf(s0, s1, s2, s3), machine.allStates)
    }

    @Test
    fun testAddTransition() {
        val s0 = State.S0()
        val s1 = State.S1()
        val t = Provider.provideTransition<State, Input>(s0, s1, Input.I0())

        val machine = builder.run {
            addInitialState(s0)
            addState(s1)
            addTransition(t)
            build()
        }

        assertEquals("transitions size is not 1", 1, machine.transitions.size)
        assertEquals("Invalid transition", t, machine.transitions.first())
    }

    @Test
    fun testAddTransitions() {
        val s0 = State.S0()
        val s1 = State.S1()
        val s2 = State.S2()
        val t0 = Provider.provideTransition<State, Input>(s0, s1, Input.I0())
        val t1 = Provider.provideTransition<State, Input>(s1, s2, Input.I1())

        val machine = builder.run {
            addInitialState(s0)
            addState(s0)
            addState(s1)
            addState(s2)
            addTransitions(setOf(t0, t1))
            build()
        }

        assertEquals("transitions size is not 2", 2, machine.transitions.size)
        assertEquals("Invalid transitions", setOf(t0, t1), machine.transitions)
    }

    @Test(expected = IllegalStateException::class)
    fun testBuildExceptionNoInitialState() {
        builder.build()
    }

    @Test(expected = IllegalStateException::class)
    fun testBuildExceptionUnknownTransitionState() {
        val s0 = State.S0()
        val s1 = State.S1()
        val t0 = Provider.provideTransition<State, Input>(s0, s1, Input.I0())

        builder.run {
            addInitialState(s1)
            addTransition(t0)
            build()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testBuildExceptionUnknownTransitionNextState() {
        val s0 = State.S0()
        val s1 = State.S1()
        val s2 = State.S2()
        val t0 = Provider.provideTransition<State, Input>(s1, s2, Input.I1())

        builder.run {
            addInitialState(s0)
            addState(s1)
            addTransition(t0)
            build()
        }
    }
}