package com.taras.admin.testproject1;

/**
 * Created on 27.02.2018
 * Author Taras
 */

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LedValueEventListener implements ValueEventListener {

    private final SimpleLed led;

    LedValueEventListener(SimpleLed led) {
        this.led = led;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Boolean on = (Boolean) dataSnapshot.getValue();
        led.turnOnOff(on == null ? false : on);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}