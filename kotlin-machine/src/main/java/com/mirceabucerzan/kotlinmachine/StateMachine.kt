package com.mirceabucerzan.kotlinmachine

/**
 * Interface definition for a Finite-State-Machine (FSM).
 *
 * @param S the type of a state in which the FSM can be in, at any time
 * @param I the type of an input to a FSM
 */
interface StateMachine<S : Any, I : Any> {

    /** The starting state for the FSM */
    val initialState: S

    /** The state in which the FSM is currently in */
    val currentState: S

    /** All possible states in which the FSM can be in */
    val allStates: Set<S>

    /** All final states in which the FSM can be in */
    val finalStates: Set<S>

    /** All possible [Transition]s the FSM can go through */
    val transitions: Set<Transition<S, I>>

    /**
     * Thread-safe method for processing inputs.
     *
     * @param input based on which the appropriate [Transition] is triggered
     * @return the next state the FSM transitioned to
     * @throws [Exception] if there was a problem during [Transition.output] execution
     */
    @Throws(Exception::class)
    fun process(input: I): S

    companion object {
        /**
         * True to enable logging, false otherwise.
         */
        var LOGGING_ENABLED = false
    }
}

/**
 * Interface definition for a FSM transition.
 *
 * @param S the type of a state in which the FSM can be in, at any time
 * @param I the type of an input to a FSM
 *
 * @property state the state in which the FSM must be in for this to occur
 * @property nextState the state in which the FSM must be in after this occurs
 * @property input the input on the FSM which triggers this
 * @property output Function for input processing, or null (a.k.a. the "side-effect")
 */
interface Transition<S : Any, I : Any> {
    val state: S
    val nextState: S
    val input: I
    val output: (@Throws(Exception::class) (input: I) -> Unit)?
}