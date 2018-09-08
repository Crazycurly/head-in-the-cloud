package com.example.nike.headinthecloud;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    SensorManager sm;
    Sensor sr;
    TextView [] txv = new TextView[5];
    SeekBar sb_normal ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sr = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        txv[0] = (TextView) findViewById(R.id.textView);
        txv[1] = (TextView) findViewById(R.id.textView2);
        txv[1].setText(String.format("油門:%4d / 1000",0));
        bindViews() ;
    }
    private void bindViews()
    {
        sb_normal = (SeekBar) findViewById(R.id.seekBar) ;
        sb_normal.setMax(1000);

        sb_normal.setRotation(-90);
        sb_normal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txv[1].setText(String.format("油門:%4d / 1000",progress));
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
    public void onSensorChanged(SensorEvent event) {
        int roll = (int)event.values[0] ;
        roll = roll > 180 ? roll-360 : roll ;
        txv[0].setText(String.format("Pitch: %d\n\nYaw: %d\n\nRoll: %d",(int) event.values[1],(int) event.values[2], roll));
//        double a = SensorManager.getOrientation() ;
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, sr, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }
}
