package com.google.android.libraries.remixer.ui.gesture;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentActivity;

import com.google.android.libraries.remixer.ui.view.RemixerFragment;

public class ShakeListener implements SensorEventListener {
    private double lastMagnitude;
    // TODO(nicksahler): Remove `lastSpike` once `show` bug is fixed
    private long lastSpike = 0;

    private double threshold;
    private final FragmentActivity activity;
    private RemixerFragment remixerFragment;

    private ShakeListener(final FragmentActivity activity, final double threshold, RemixerFragment remixerFragment) {
        this.activity = activity;
        this.threshold = threshold;
        this.remixerFragment = remixerFragment;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        double currentMagnitude = Math.sqrt(
            event.values[0] * event.values[0]
            + event.values[1] * event.values[1]
            + event.values[2] * event.values[2]);

        if ((currentMagnitude - lastMagnitude) > threshold && System.currentTimeMillis() - lastSpike > 500) {
            remixerFragment.show(activity.getSupportFragmentManager(), RemixerFragment.REMIXER_TAG);
            lastSpike = System.currentTimeMillis();
        }

        this.lastMagnitude = currentMagnitude;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public static void attach(final FragmentActivity activity, final double threshold, RemixerFragment remixerFragment) {
        SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        sensorManager.registerListener(new ShakeListener(activity, threshold, remixerFragment), sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

}
