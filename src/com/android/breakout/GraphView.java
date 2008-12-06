/**
 * 
 */
package com.android.breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.*;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.view.View;

/**
 * @author lithium
 *
 */
public class GraphView extends View implements SensorListener
{
    private Bitmap  mBitmap;
    private Paint   mPaint = new Paint();
    private Canvas  mCanvas = new Canvas();
    private Path    mPath = new Path();
    private RectF   mRect = new RectF();
    private int     mColors[] = new int[3*2];
    private float   mWidth;
    private float   mHeight;
    private int		mPaddleX = 0;
    private int		mPaddleY = 0;
    private int		mPaddleWidth = 80;
    private int		mPaddleHeight = 15;
    private final int mAccelMultiplier = 3;
    private final int mNudgeValue = 8;
    private GradientDrawable mPaddle;
    private GradientDrawable mBackground;
    
    public GraphView(Context context) {
        super(context);
        mColors[0] = Color.argb(192, 255, 64, 64);
        mColors[1] = Color.argb(192, 64, 128, 64);
        mColors[2] = Color.argb(192, 64, 64, 255);
        mColors[3] = Color.argb(192, 64, 255, 255);
        mColors[4] = Color.argb(192, 128, 64, 128);
        mColors[5] = Color.argb(192, 255, 255, 64);
        
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mRect.set(-0.5f, -0.5f, 0.5f, 0.5f);
        mPath.arcTo(mRect, 0, 180);
        
        mPaddle = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                	new int[] { 0xFF0040FF, 0xFFFFFFFF, 0xFF002080 });
        mPaddle.setShape(GradientDrawable.LINEAR_GRADIENT);
        mPaddle.setCornerRadius(3);
        mPaddle.setStroke(2, 0xFF000000);
        
        mBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        				new int[] { 0xFF000040, 0xFFFF8060, 0xFFFFFFFF });
        mBackground.setShape(GradientDrawable.LINEAR_GRADIENT);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(0xFFFFFFFF);
        mWidth = w;
        mHeight = h;
        mPaddleY = h - mPaddleHeight - 2;
        mPaddleX = (w >> 1) - (mPaddleWidth >> 1);
        mBackground.setBounds(0, 0, (int)mWidth, (int)mHeight);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            if (mBitmap != null) {
                final Paint paint = mPaint;
                final Path path = mPath;

//                canvas.drawBitmap(mBitmap, 0, 0, null);
                mBackground.draw(canvas);
                drawPaddle(canvas, paint);
            } 
        }
    }
    
    private void drawPaddle(Canvas canvas, Paint paint) {
//        paint.setColor(mPaddleColor);
    	mPaddle.setBounds(mPaddleX, mPaddleY, mPaddleX + mPaddleWidth, mPaddleY + mPaddleHeight);
    	mPaddle.draw(canvas);
    }

    public void onSensorChanged(int sensor, float[] values) {
        //Log.d(TAG, "sensor: " + sensor + ", x: " + values[0] + ", y: " + values[1] + ", z: " + values[2]);
        synchronized (this) {
            if (mBitmap != null) {
                final Canvas canvas = mCanvas;
                final Paint paint = mPaint;
               	if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
               		int accel = (int)(values[SensorManager.RAW_DATA_Y]);
               		
               		if (accel >= mNudgeValue) {
               			mPaddleX = 0;
               		} else if (accel <= -mNudgeValue) {
               			mPaddleX = (int)mWidth-mPaddleWidth;
               		} else
               			mPaddleX -= accel * mAccelMultiplier;
               		if (mPaddleX < 0)
               			mPaddleX = 0;
               		if (mPaddleX > ((int)mWidth-mPaddleWidth))
               			mPaddleX = (int)mWidth - mPaddleWidth;
               	}
                invalidate();
            }
        }
    }

    public void onAccuracyChanged(int sensor, int accuracy) {
        // TODO Auto-generated method stub
        
    }
}
