package com.mirceabucerzan.kotlinmachine

/**
 * Abstract Builder class for [StateMachine]s.
 *
 * @param S the type of a state in which the FSM can be in, at any time
 * @param I the type of an input to a FSM
 */
abstract class StateMachineBuilder<S : Any, I : Any> {

    protected var initialState: S? = null

    protected val allStates: MutableSet<S> = mutableSetOf()

    protected val finalStates: MutableSet<S> = mutableSetOf()

    protected val transitions: MutableSet<Transition<S, I>> = mutableSetOf()

    /**
     * Adds [state] as the [StateMachine]'s initial state.
     *
     * @throws IllegalStateException if called more than once
     */
    fun addInitialState(state: S): StateMachineBuilder<S, I> =
        apply {
            initialState?.let { throw IllegalStateException("Initial state already set") }
            allStates += state
            initialState = state
        }

    /**
     * Adds [state] to the [StateMachine].
     */
    fun addState(state: S): StateMachineBuilder<S, I> = apply { allStates += state }

    /**
     * Adds [states] to the [StateMachine].
     */
    fun addStates(states: Set<S>): StateMachineBuilder<S, I> =
        apply { states.forEach { addState(it) } }

    /**
     * Adds [state] to the [StateMachine] and marks it as a final.
     */
    fun addFinalState(state: S): StateMachineBuilder<S, I> =
        apply {
            allStates += state
            finalStates += state
        }

    /**
     * Adds [states] to the [StateMachine] and marks them as a final.
     */
    fun addFinalStates(states: Set<S>): StateMachineBuilder<S, I> =
        apply { states.forEach { addFinalState(it) } }

    /**
     * Adds [transition] to the [StateMachine].
     */
    fun addTransition(transition: Transition<S, I>): StateMachineBuilder<S, I> =
        apply { transitions += transition }

    /**
     * Adds [transitions] to the [StateMachine].
     */
    fun addTransitions(transitions: Set<Transition<S, I>>): StateMachineBuilder<S, I> =
        apply { transitions.forEach { addTransition(it) } }

    /**
     * Builds the [StateMachine].
     *
     * @return [StateMachine]
     * @throws IllegalStateException if the initial state has not
     * been added or any of the transitions contain unknown states.
     */
    fun build(): StateMachine<S, I> {
        initialState ?: throw IllegalStateException("Initial state not added")
        transitions.forEach {
            if (!allStates.contains(it.state)) {
                throw IllegalStateException(
                    "Unknown transition state: [${it.state.javaClass.simpleName}]"
                )
            }
            if (!allStates.contains(it.nextState)) {
                throw IllegalStateException(
                    "Unknown transition nextState: [${it.nextState.javaClass.simpleName}]"
                )
            }
        }
        return buildStateMachine()
    }

    protected abstract fun buildStateMachine(): StateMachine<S, I>
}