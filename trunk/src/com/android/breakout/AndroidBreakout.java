package com.android.breakout;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.Window;

public class AndroidBreakout extends Activity {
    private GraphView mGraphView;
    private SensorManager mSensorManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGraphView = new GraphView(this);
        setContentView(mGraphView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mGraphView, 
                SensorManager.SENSOR_ACCELEROMETER,
//                SensorManager.SENSOR_ORIENTATION | 
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(mGraphView);
        super.onStop();
    }
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
/*
    public void onSensorChanged(int sensor, float[] values) {
        //Log.d(TAG, "sensor: " + sensor + ", x: " + values[0] + ", y: " + values[1] + ", z: " + values[2]);
        synchronized (this) {
            if (mBitmap != null) {
                final Canvas canvas = mCanvas;
                final Paint paint = mPaint;
               	if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
               		mPaddleX += (int)(values[0] * 2.0f);
               		if (mPaddleX < 0)
               			mPaddleX = 0;
               		if (mPaddleX > ((int)mWidth-mPaddleWidth))
               			mPaddleX = (int)mWidth - mPaddleWidth;
               	}
                invalidate();
            }
        }
    }
*/
    public void onAccuracyChanged(int sensor, int accuracy) {
        // TODO Auto-generated method stub
        
    }
}