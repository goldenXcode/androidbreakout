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
import android.graphics.Rect;
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
    private int		mWidth;
    private float   mHeight;
    private int		mPaddleWidth = 70;
    private int		mPaddleHeight = 12;
    private final int mAccelMultiplier = 4;
    private final int mNudgeValue = 8;
//    private GradientDrawable mPaddle;
    private Paddle 	mPaddle;
    private GradientDrawable mBackground;
    private Point	mBallPos = new Point();
    private Point	mBallVel = new Point();
    
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
        
        mBallPos.x = 160;
        mBallPos.y = 120;
        
        mBallVel.x = 3;
        mBallVel.y = 2;
        
        mPaddle = new Paddle();
        
        mBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        				new int[] { 0xFF000040, 0xFF000000, 0xFF400000 });
        mBackground.setShape(GradientDrawable.LINEAR_GRADIENT);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(0xFFFFFFFF);
        mWidth = w;
        mHeight = h;
        mPaddle.setPosition((w >> 1) - (mPaddleWidth >> 1), h - mPaddleHeight - 2);
        Rect bounds = new Rect(0,0,(int)mWidth,(int)mHeight);
        mPaddle.setBounds(bounds);
        mPaddle.setDimensions(mPaddleWidth, mPaddleHeight);
        mBackground.setBounds(0, 0, (int)mWidth, (int)mHeight);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            if (mBitmap != null) {
                final Paint paint = mPaint;
                mBackground.draw(canvas);
                mPaddle.draw(canvas);
                paint.setColor(0xFF00FF00);
                canvas.drawCircle(mBallPos.x, mBallPos.y, 8, paint);
            } 
        }
    }
    
    public void onSensorChanged(int sensor, float[] values) {
        //Log.d(TAG, "sensor: " + sensor + ", x: " + values[0] + ", y: " + values[1] + ", z: " + values[2]);
        synchronized (this) {
            if (mBitmap != null) {
               	if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
               		int accel = (int)(values[SensorManager.RAW_DATA_Y]);
               		
               		if (accel >= mNudgeValue) {
               			mPaddle.setVelocity(-mWidth, 0);
               		} else if (accel <= -mNudgeValue) {
               			mPaddle.setVelocity(mWidth, 0);
               		} else
               			mPaddle.setVelocity(accel * -mAccelMultiplier, 0);
               		mPaddle.update();
               		
               		mBallPos.x += mBallVel.x;
               		mBallPos.y += mBallVel.y;
               		
               		Point p = mPaddle.getPosition();
               		if(mBallPos.x < 0 || mBallPos.x > mWidth) {
               			mBallVel.x = -mBallVel.x;
               			mBallPos.x += mBallVel.x;
               		}
               		
               		if(mBallPos.y < 0) {
               			mBallVel.y = -mBallVel.y;
               			mBallPos.y += mBallVel.y;
               		} else if (mBallPos.y >= mHeight) {
               	        mBallPos.x = 160;
               	        mBallPos.y = 120;
               	        
               	        mBallVel.x = 3;
               	        mBallVel.y = 2;
               		}
               		
               		if(mBallVel.y > 0 && (mBallPos.y < mHeight - 5) &&
               				mBallPos.y > p.y && mBallPos.x > p.x &&
               				mBallPos.x < p.x + mPaddleWidth) {
               			
               			mBallVel.y = -(mBallVel.y + 1);
               			mBallPos.y = (p.y - 8);
               			mBallVel.x++;
               		}
//               		mPaddle.setPosition(mPaddleX, mPaddleY);
               	}
                invalidate();
            }
        }
    }

    public void onAccuracyChanged(int sensor, int accuracy) {
        // TODO Auto-generated method stub
        
    }
}
