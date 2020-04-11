package com.mirceabucerzan.kotlinmachine.internal

sealed class State(val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is State) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "State(name='$name')"
    }

    class S0 : State("S0")
    class S1 : State("S1")
    class S2 : State("S2")
    class S3 : State("S3")
}


sealed class Input(val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Input) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Input(name='$name')"
    }

    class I0 : Input("I0")
    class I1 : Input("I1")
    class I2 : Input("I2")
    class I3 : Input("I3")
    class I4 : Input("I4")
    class I5 : Input("I5")
    class I6 : Input("I6")
}