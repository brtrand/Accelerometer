package com.example.accelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private long lastUpdate;
    private View v;
    private boolean color = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = findViewById(R.id.v);
        v.setBackgroundColor(Color.BLACK);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(sensorEvent);
        }
    }

    private void getAccelerometer(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelSqr = (x*x+y*y+z*z)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH);
        long actualTime = sensorEvent.timestamp;
        if (accelSqr>=2){
            if((actualTime - lastUpdate)< 200){
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(this, "Shaken", Toast.LENGTH_SHORT).show();
            if (color){
                v.setBackgroundColor(Color.GREEN);
            }else {
                v.setBackgroundColor(Color.RED);
            }
            color =!color;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}