package com.mirceabucerzan.kotlinmachine

import com.mirceabucerzan.kotlinmachine.internal.DefaultStateMachineBuilder
import com.mirceabucerzan.kotlinmachine.internal.DefaultTransition

/**
 * Provider for abstract library components.
 */
class Provider {
    companion object {
        /**
         * Provider function for [StateMachineBuilder]s.
         *
         * @param S the type of a state in which the FSM can be in, at any time
         * @param I the type of an input to a FSM
         *
         * @return [StateMachineBuilder] for building [StateMachine]s
         */
        fun <S : Any, I : Any> provideBuilder(): StateMachineBuilder<S, I> =
            DefaultStateMachineBuilder()

        /**
         * Provider function for [Transition]s.
         *
         * @param S the type of a state in which the FSM can be in, at any time
         * @param I the type of an input to a FSM
         *
         * @param state the state in which the FSM must be in for it to occur
         * @param nextState the state in which the FSM must be in after it occurs
         * @param input the input on the FSM which triggers it
         * @param output function, the "side-effect" of the [Transition], or null
         *
         * @return [Transition]
         */
        fun <S : Any, I : Any> provideTransition(
            state: S,
            nextState: S,
            input: I,
            output: (@Throws(Exception::class) (input: I) -> Unit)? = null
        ): Transition<S, I> = DefaultTransition(state, nextState, input, output)
    }
}