package com.mirceabucerzan.kotlinmachineexample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mirceabucerzan.kotlinmachine.StateMachine
import kotlinx.android.synthetic.main.activity_main.*

/**
 * View which displays a [Door]'s current state and allows user inputs to be sent to it.
 */
class MainActivity : AppCompatActivity(), Door.Listener, View.OnClickListener {

    private lateinit var door: Door

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StateMachine.LOGGING_ENABLED = true

        button_open.setOnClickListener(this)
        button_close.setOnClickListener(this)
        button_lock.setOnClickListener(this)
        button_unlock.setOnClickListener(this)
        button_strike_lock.setOnClickListener(this)

        door = DefaultDoor()
    }

    override fun onStart() {
        super.onStart()
        door.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        door.unsubscribe()
    }

    override fun onStateChange(state: DoorState) {
        // display new state
        progress_indicator.visibility = View.INVISIBLE
        door_state.visibility = View.VISIBLE
        when (state) {
            DoorState.Open() -> door_state.text = getString(R.string.state_open)
            DoorState.Closed() -> door_state.text = getString(R.string.state_closed)
            DoorState.Locked() -> door_state.text = getString(R.string.state_locked)
            DoorState.Stuck() -> door_state.text = getString(R.string.state_stuck)
        }
    }

    override fun onClick(view: View) {
        // send input
        door_state.visibility = View.INVISIBLE
        progress_indicator.visibility = View.VISIBLE
        when (view.id) {
            R.id.button_open -> door.onInput(DoorInput.Open())
            R.id.button_close -> door.onInput(DoorInput.Close())
            R.id.button_lock -> door.onInput(DoorInput.Lock())
            R.id.button_unlock -> door.onInput(DoorInput.Unlock())
            R.id.button_strike_lock -> door.onInput(DoorInput.StrikeLock())
        }
    }
}
