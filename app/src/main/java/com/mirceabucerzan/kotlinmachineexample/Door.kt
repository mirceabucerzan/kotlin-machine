package com.mirceabucerzan.kotlinmachineexample

/**
 * Interface definition for a door. It can be in any of [DoorState]
 * states and can accept any of [DoorInput] inputs.
 */
interface Door {
    /**
     * Interface definition for a callback invoked when the [Door] changes state.
     */
    interface Listener {
        fun onStateChange(state: DoorState)
    }

    /**
     * Subscribes the [listener] for state changes.
     */
    fun subscribe(listener: Listener)

    /**
     * Un-subscribes the previously set [Listener] from state changes.
     */
    fun unsubscribe()

    /**
     * Send a [DoorInput] which might trigger a state change.
     */
    fun onInput(input: DoorInput)
}

/**
 * All states a [Door] can be in.
 */
sealed class DoorState(private val id: Int) {
    class Open : DoorState(0)
    class Closed : DoorState(1)
    class Locked : DoorState(2)
    class Stuck : DoorState(3)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DoorState) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int = id
}

/**
 * All inputs a [Door] responds to.
 */
sealed class DoorInput(private val id: Int) {
    class Close : DoorInput(0)
    class Open : DoorInput(1)
    class Lock : DoorInput(2)
    class Unlock : DoorInput(3)
    class StrikeLock : DoorInput(4)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DoorInput) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int = id
}