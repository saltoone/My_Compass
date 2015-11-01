package com.example.sultan.mycompass;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener,SurfaceHolder.Callback {
    private ImageView CompassImage;
    private float currentDegree = 0.0f;
    TextView HeadingDgree;
    private SensorManager mSensorManager;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CompassImage = (ImageView) findViewById(R.id.imageView);
        HeadingDgree = (TextView) findViewById(R.id.textView);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        HeadingDgree.setText("Heading: " + Float.toString(degree) + " degrees");
        RotateAnimation rate = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rate.setDuration(100);
        rate.setFillAfter(true);
        CompassImage.startAnimation(rate);
        currentDegree = -degree;

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        } catch (RuntimeException ex) {
            System.err.println(ex);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        param.setPreviewSize(500, 400);
       camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
    int e = 0;

    public void ImageVisble(View view) {
        if (e == 0) {
            CompassImage.setVisibility(view.INVISIBLE);
            e++;
            return;
        }
        if (e == 1) {
            CompassImage.setVisibility(view.VISIBLE);
            e = 0;
        }
    }
}
