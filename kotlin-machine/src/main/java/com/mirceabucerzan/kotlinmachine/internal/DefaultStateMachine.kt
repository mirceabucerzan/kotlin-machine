package com.mirceabucerzan.kotlinmachine.internal

import com.mirceabucerzan.kotlinmachine.StateMachine
import com.mirceabucerzan.kotlinmachine.Transition
import java.util.logging.Level
import java.util.logging.Logger

internal class DefaultStateMachine<S : Any, I : Any>(
    override val initialState: S,
    override val allStates: Set<S>,
    override val finalStates: Set<S>,
    override val transitions: Set<Transition<S, I>>
) : StateMachine<S, I> {

    override var currentState: S = initialState

    private val logger: Logger? = if (StateMachine.LOGGING_ENABLED) {
        Logger.getLogger(DefaultStateMachine::class.java.name)
    } else {
        null
    }

    @Synchronized
    @Throws(Exception::class)
    override fun process(input: I): S {
        logger?.log(Level.INFO, "Processing input: [${input.javaClass.simpleName}]")
        if (!inFinalState()) {
            getTransition(input)?.let { transition ->
                try {
                    // run the output, if any
                    transition.output?.invoke(input)
                    // transition to the next state
                    currentState = transition.nextState
                    logger?.log(
                        Level.INFO,
                        "Transitioned from state [${transition.state.javaClass.simpleName}] " +
                                "to state [${currentState.javaClass.simpleName}]"
                    )
                } catch (e: Exception) {
                    logger?.log(Level.SEVERE, "Exception occurred during transition: [$e]")
                    throw e
                }
            } ?: logger?.log(
                Level.WARNING,
                "Cannot find matching transition for input [${input.javaClass.simpleName}] and " +
                        "current state [${currentState.javaClass.simpleName}]"
            )
        } else {
            logger?.log(
                Level.WARNING,
                "Cannot process, already in final state: [${currentState.javaClass.simpleName}]"
            )
        }
        return currentState
    }

    private fun inFinalState() = finalStates.contains(currentState)

    private fun getTransition(input: I): Transition<S, I>? = transitions.find {
        it.state == currentState && it.input == input
    }
}