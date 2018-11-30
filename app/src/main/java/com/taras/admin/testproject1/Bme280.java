package com.taras.admin.testproject1;

import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.android.things.pio.I2cDevice;

import java.io.IOException;

/**
 * Created on 09.04.2018
 * Author Taras
 */

public class Bme280 extends Bmx280 {

    public Bme280(I2cDevice device) throws IOException {
        super(device.toString());
//        super(device)
    }
}