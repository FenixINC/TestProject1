package com.taras.admin.testproject1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver;
import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class BlinkActivity extends Activity {
    private static final String TAG = BlinkActivity.class.getSimpleName();

    private static final String RED_PIN = "BCM4";
    private static final String BLUE_PIN = "BCM6";
    private static final String GREEN_PIN = "BCM16";

    private PeripheralManagerService mManager;
    private static final int ADDRESS = 0x76;

    private SimpleLed red;
    private SimpleLed blue;
    private SimpleLed green;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            red = new SimpleLed(RED_PIN, SimpleLed.InitialState.OFF);
            blue = new SimpleLed(BLUE_PIN, SimpleLed.InitialState.OFF);
            green = new SimpleLed(GREEN_PIN, SimpleLed.InitialState.OFF);
        } catch (Exception e) {
            Log.e(TAG, "Error opening LEDs", e);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("lighting_system");
        DatabaseReference redRef = ref.child("red_on");
        DatabaseReference blueRef = ref.child("blue_on");
        DatabaseReference greenRef = ref.child("green_on");

        redRef.addValueEventListener(new LedValueEventListener(red));
        blueRef.addValueEventListener(new LedValueEventListener(blue));
        greenRef.addValueEventListener(new LedValueEventListener(green));

        printDeviceId();
        readSample();

    }

    private void printDeviceId() {
        List<String> deviceList = mManager.getI2cBusList();
        if (deviceList.isEmpty()) {
            Log.i(TAG, "No I2C bus available on this device.");
        } else {
            Log.i(TAG, "List of available devices: " + deviceList);
        }
        I2cDevice device = null;
        try {
            device = mManager.openI2cDevice(deviceList.get(0), ADDRESS);
            Log.d(TAG, "Device ID byte: 0x" + Integer.toHexString(device.readRegByte(0xD0)));
        } catch (IOException |RuntimeException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                device.close();
            } catch (Exception ex) {
                Log.d(TAG, "Error closing device");
            }
        }
    }

    private void readSample() {
        try (Bme280 bmxDriver = new Bme280(mManager.openI2cDevice(mManager.getI2cBusList().get(0), ADDRESS))){
            bmxDriver.setTemperatureOversampling(Bmx280.OVERSAMPLING_1X);
            bmxDriver.setPressureOversampling(Bmx280.OVERSAMPLING_1X);

            bmxDriver.setMode(Bme280.MODE_NORMAL);
            for(int i = 0 ; i < 5 ; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                Log.d(TAG, "Temperature: " + bmxDriver.readTemperature());
                Log.d(TAG, "Pressure: " + bmxDriver.readPressure());
            }


        } catch (IOException e) {
            Log.e(TAG, "Error during IO", e);
            // error reading temperature
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            red.close();
        } catch (Exception ignored) {
            red = null;
        }

        try {
            blue.close();
        } catch (Exception ignored) {
            blue = null;
        }

        try {
            green.close();
        } catch (Exception ignored) {
            green = null;
        }
    }
}


