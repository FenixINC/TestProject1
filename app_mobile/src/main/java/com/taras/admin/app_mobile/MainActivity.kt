package com.taras.admin.app_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mSwitchReference: DatabaseReference? = null

    private var mAuth: FirebaseAuth? = null
    private val mAuthStateListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val redLightSwitch = findViewById<Switch>(R.id.red_switch)
        val blueLightSwitch = findViewById<Switch>(R.id.blue_switch)
        val greenLightSwitch = findViewById<Switch>(R.id.green_switch)

        val database = FirebaseDatabase.getInstance()
        mSwitchReference = database.getReference("lighting_system")
        val redReference = mSwitchReference?.child("red_on")
        val blueReference = mSwitchReference?.child("blue_on")
        val greenReference = mSwitchReference?.child("green_on")

        redReference?.addValueEventListener(SwitchValueEventListener(redLightSwitch))
        blueReference?.addValueEventListener(SwitchValueEventListener(blueLightSwitch))
        greenReference?.addValueEventListener(SwitchValueEventListener(greenLightSwitch))

        redLightSwitch.setOnCheckedChangeListener { buttonView, isChecked -> redReference?.setValue(isChecked) }
        blueLightSwitch.setOnCheckedChangeListener { buttonView, isChecked -> blueReference?.setValue(isChecked) }
        greenLightSwitch.setOnCheckedChangeListener { buttonView, isChecked -> greenReference?.setValue(isChecked) }

        val turnAllOnButton = findViewById<Button>(R.id.turn_all_on_btn)
        val turnAllOffButton = findViewById<Button>(R.id.turn_all_off_btn)

        turnAllOffButton.setOnClickListener { v -> updateAllSwitches(mSwitchReference!!, false, "") }
        turnAllOnButton.setOnClickListener { v -> updateAllSwitches(mSwitchReference!!, true, "") }

        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener { v -> onLogoutClick() }
    }

    private fun onLogoutClick() {
        mAuth = FirebaseAuth.getInstance()
        mAuth?.signOut()
        val intent = Intent(this@MainActivity, AuthorizationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun updateAllSwitches(reference: DatabaseReference, on: Boolean, switchLed: String) {
        val childUpdates = HashMap<String, Any>()
        when {
            switchLed.equals("red_on", ignoreCase = true) -> childUpdates["/red_on"] = on
            switchLed.equals("blue_on", ignoreCase = true) -> childUpdates["/blue_on"] = on
            else -> {
                childUpdates["/red_on"] = on
                childUpdates["/blue_on"] = on
                childUpdates["/green_on"] = on
            }
        }
        reference.updateChildren(childUpdates)
    }

    override fun onDestroy() {
        super.onDestroy()
        updateAllSwitches(mSwitchReference!!, false, "")
    }
}