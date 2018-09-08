/*
 * MIT License
 *
 * Copyright (c) 2015 Douglas Nassif Roma Junior
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.douglasjunior.bluetoothsample;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothWriter;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Created by douglas on 10/04/2017.
 */

public class DeviceActivity extends AppCompatActivity implements BluetoothService.OnBluetoothEventCallback, SensorEventListener {
    SensorManager sm;
    Sensor sr;
    TextView[] txv = new TextView[5];
    SeekBar sb_normal ;
    int Throttle ;
    private BluetoothService mService;
    private BluetoothWriter mWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        mService = BluetoothService.getDefaultInstance();
        mWriter = new BluetoothWriter(mService);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sr = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        txv[0] = (TextView) findViewById(R.id.textView);
        txv[1] = (TextView) findViewById(R.id.textView2);
        txv[0].setTextSize(25);
        txv[1].setTextSize(25);
        txv[1].setText(String.format("油門:%4d / 255", 0));
        bindViews() ;
    }

    private void bindViews()
    {
        sb_normal = (SeekBar) findViewById(R.id.seekBar) ;
        sb_normal.setMax(255);
        sb_normal.setRotation(-90);

        sb_normal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Throttle = progress ;
                txv[1].setText(String.format("油門:%4d / 255",progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }




    @Override
    public void onDataRead(byte[] buffer, int length) {
    }

    @Override
    public void onStatusChange(BluetoothStatus status) {
    }

    @Override
    public void onDeviceName(String deviceName) {
    }

    @Override
    public void onToast(String message) {
    }

    @Override
    public void onDataWrite(byte[] buffer) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService.disconnect();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int pitch = (int) event.values[1] ;
        int yaw   = (int) event.values[2] ;
        int roll  = (int) event.values[0] ;

        txv[0].setText(String.format("Pitch: %d\n\nYaw: %d\n\nRoll: %d",pitch, yaw, roll));

        mWriter.writeln("P"+(pitch+360)+"_Y"+(yaw+360)+"_R"+(roll+360)+"_T"+Throttle+"_");
//        mWriter.writeln(Integer.toString(yaw)+"_");
//        mWriter.writeln(Integer.toString(roll)+"_");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, sr, SensorManager.SENSOR_DELAY_GAME);
        mService.setOnEventCallback(this);
    }
}
