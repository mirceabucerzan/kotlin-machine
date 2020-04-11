package com.mirceabucerzan.kotlinmachine.internal

import com.mirceabucerzan.kotlinmachine.Transition

internal class DefaultTransition<S : Any, I : Any>(
    override val state: S,
    override val nextState: S,
    override val input: I,
    override val output: (@Throws(Exception::class) (input: I) -> Unit)?
) : Transition<S, I> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DefaultTransition<*, *>) return false
        if (state != other.state) return false
        if (nextState != other.nextState) return false
        if (input != other.input) return false
        return true
    }

    override fun hashCode(): Int {
        var result = state.hashCode()
        result = 31 * result + nextState.hashCode()
        result = 31 * result + input.hashCode()
        return result
    }

    override fun toString(): String {
        return "DefaultTransition(state=$state, nextState=$nextState, input=$input)"
    }
}