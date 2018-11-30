package com.taras.admin.app_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mSwitchReference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch redLightSwitch = findViewById(R.id.red_switch);
        Switch blueLightSwitch = findViewById(R.id.blue_switch);
        Switch greenLightSwitch = findViewById(R.id.green_switch);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mSwitchReference = database.getReference("lighting_system");
        final DatabaseReference redReference = mSwitchReference.child("red_on");
        final DatabaseReference blueReference = mSwitchReference.child("blue_on");
        final DatabaseReference greenReference = mSwitchReference.child("green_on");

        redReference.addValueEventListener(new SwitchValueEventListener(redLightSwitch));
        blueReference.addValueEventListener(new SwitchValueEventListener(blueLightSwitch));
        greenReference.addValueEventListener(new SwitchValueEventListener(greenLightSwitch));

        redLightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> redReference.setValue(isChecked));
        blueLightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> blueReference.setValue(isChecked));
        greenLightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> greenReference.setValue(isChecked));

        Button turnAllOnButton = findViewById(R.id.turn_all_on_btn);
        Button turnAllOffButton = findViewById(R.id.turn_all_off_btn);

        turnAllOffButton.setOnClickListener(v -> updateAllSwitches(mSwitchReference, false, ""));
        turnAllOnButton.setOnClickListener(v -> updateAllSwitches(mSwitchReference, true, ""));


        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> onLogoutClick());

    }

    private void onLogoutClick() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void updateAllSwitches(final DatabaseReference reference, final boolean on, String switchLed) {
        Map<String, Object> childUpdates = new HashMap<>();
        if (switchLed.equalsIgnoreCase("red_on")) {
            childUpdates.put("/red_on", on);
        } else if (switchLed.equalsIgnoreCase("blue_on")) {
            childUpdates.put("/blue_on", on);
        } else {
            childUpdates.put("/red_on", on);
            childUpdates.put("/blue_on", on);
            childUpdates.put("/green_on", on);
        }
        reference.updateChildren(childUpdates);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateAllSwitches(mSwitchReference, false, "");
    }
}
