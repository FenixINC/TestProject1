package com.taras.admin.app_mobile;

import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created on 27.02.2018
 * Author Taras
 */

public class SwitchValueEventListener implements ValueEventListener {

    private final Switch switchButton;

    SwitchValueEventListener(Switch switchButton) {
        this.switchButton = switchButton;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Boolean checked = (Boolean) dataSnapshot.getValue();
        switchButton.setChecked(checked == null ? false : checked);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}