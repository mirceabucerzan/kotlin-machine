package com.mirceabucerzan.kotlinmachine.internal

import com.mirceabucerzan.kotlinmachine.StateMachine
import com.mirceabucerzan.kotlinmachine.StateMachineBuilder

internal class DefaultStateMachineBuilder<S : Any, I : Any> : StateMachineBuilder<S, I>() {
    override fun buildStateMachine(): StateMachine<S, I> =
        DefaultStateMachine(initialState!!, allStates, finalStates, transitions)
}