package com.mirceabucerzan.kotlinmachineexample

import android.os.Handler
import android.util.Log
import com.mirceabucerzan.kotlinmachine.Provider
import com.mirceabucerzan.kotlinmachine.StateMachine
import com.mirceabucerzan.kotlinmachine.Transition

/**
 * [Door] implementation which uses a [StateMachine] for processing inputs.
 */
class DefaultDoor : Door {
    companion object {
        private const val TAG = "DefaultDoor"
    }

    private var listener: Door.Listener? = null

    private val transitions: Set<Transition<DoorState, DoorInput>> = setOf(
        Provider.provideTransition<DoorState, DoorInput>(
            state = DoorState.Open(),
            nextState = DoorState.Closed(),
            input = DoorInput.Close(),
            output = { input -> simulateOutput(input) }
        ),
        Provider.provideTransition<DoorState, DoorInput>(
            state = DoorState.Closed(),
            nextState = DoorState.Open(),
            input = DoorInput.Open(),
            output = { input -> simulateOutput(input) }
        ),
        Provider.provideTransition<DoorState, DoorInput>(
            state = DoorState.Closed(),
            nextState = DoorState.Locked(),
            input = DoorInput.Lock(),
            output = { input -> simulateOutput(input) }
        ),
        Provider.provideTransition<DoorState, DoorInput>(
            state = DoorState.Locked(),
            nextState = DoorState.Closed(),
            input = DoorInput.Unlock(),
            output = { input -> simulateOutput(input) }
        ),
        Provider.provideTransition<DoorState, DoorInput>(
            state = DoorState.Locked(),
            nextState = DoorState.Stuck(),
            input = DoorInput.StrikeLock(),
            output = { input -> simulateOutput(input) }
        )
    )

    private val stateMachine: StateMachine<DoorState, DoorInput> =
        Provider.provideBuilder<DoorState, DoorInput>().run {
            addInitialState(DoorState.Open())
            addStates(setOf(DoorState.Closed(), DoorState.Locked()))
            addFinalState(DoorState.Stuck())
            addTransitions(transitions)
            build()
        }

    private val handler = Handler()

    override fun subscribe(listener: Door.Listener) {
        this.listener = listener
        simulateOutput(null)
    }

    override fun unsubscribe() {
        listener = null
    }

    override fun onInput(input: DoorInput) {
        val prevState = stateMachine.currentState
        stateMachine.process(input)

        if (prevState == stateMachine.currentState) {
            // no transition took place
            notifyListener()
        }
    }

    private fun simulateOutput(input: DoorInput?) {
        Log.d(TAG, "Simulating output for input: ${input?.javaClass?.simpleName}")
        handler.postDelayed({ notifyListener() }, 1000)
    }

    private fun notifyListener() {
        listener?.onStateChange(stateMachine.currentState)
    }
}