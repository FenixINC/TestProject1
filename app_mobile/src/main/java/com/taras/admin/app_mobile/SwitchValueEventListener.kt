package com.taras.admin.app_mobile

import android.widget.Switch
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

internal class SwitchValueEventListener(private val switchButton: Switch) : ValueEventListener {

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val checked = dataSnapshot.value as Boolean?
        switchButton.isChecked = checked ?: false
    }

    override fun onCancelled(databaseError: DatabaseError) {}
}