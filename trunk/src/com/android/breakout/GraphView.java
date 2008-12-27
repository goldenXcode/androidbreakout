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
import java.util.Random;

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
    private int		mPaddleWidth = 80;
    private int		mPaddleHeight = 16;
    private final int mAccelMultiplier = 5;
    private final int mNudgeValue = 8;
    private Paddle 	mPaddle;
    private GradientDrawable mBackground;
    private GradientDrawable mBall;
    private Point	mBallPos = new Point();
    private Point	mBallVel = new Point();
    private float	mSin[]  = {-0.5f, -0.707106781f, -0.866025404f, -0.965925826f, -1.0f,
    						   -1.0f, -0.965925826f, -0.866025404f, -0.707106781f, -0.5f};
    private float 	mCos[]	= {-0.866025404f, -0.707106781f, -0.5f, -0.258819045f, 0.0f,
    						   0.0f, 0.258819045f, 0.5f, 0.707106781f, 0.866025404f};
    private float	mVelMag = 4.0f;
    
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
        
        mBallPos.x = 240;
        mBallPos.y = 120;
        
        Random rand = new Random();
        int i = rand.nextInt(10);
        mBallVel.x = mVelMag * mCos[i];
        mBallVel.y = mVelMag * mSin[i];
        
        mPaddle = new Paddle();
        
        mBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        				new int[] { 0xFF0094BF, 0xFFF0F0F0 });
        mBackground.setShape(GradientDrawable.LINEAR_GRADIENT);
        
        mBall = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        				new int[] { 0xFFFFFF00, 0xFFFF7200 });
        mBall.setShape(GradientDrawable.OVAL);
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
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setColor(0xFFFF7200);
                paint.setStrokeWidth(1);
                mBall.setBounds((int)mBallPos.x-8, (int)mBallPos.y-8, (int)mBallPos.x+8, (int)mBallPos.y+8);
                mBall.setStroke(1, 0xFF000000);
                mBall.draw(canvas);
//                canvas.drawCircle(mBallPos.x, mBallPos.y, 8, paint);
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
               		{
               			mPaddle.setVelocity((int)(values[SensorManager.RAW_DATA_Y] * -mAccelMultiplier), 0);
               		}
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
               	        mBallPos.x = 240;
               	        mBallPos.y = 120;
               	        
               	        mVelMag = 4.0f;
               	        
               	        Random rand = new Random();
               	        int i = rand.nextInt(10);
               	        mBallVel.x = mVelMag * mCos[i];
               	        mBallVel.y = mVelMag * mSin[i];
               		}
               		
               		if(mBallVel.y > 0 && (mBallPos.y < mHeight - 5) &&
               				mBallPos.y > p.y && mBallPos.x > p.x &&
               				mBallPos.x < p.x + mPaddleWidth) {
               			float divider = (float)mPaddleWidth/8.0f;
               			float sector = (float)p.x+divider;
               			boolean found = false;
               			for(int i = 0; !found && i < 8; i++) {
               				if(mBallPos.x <= sector){
               					mBallVel.y = mVelMag * mSin[i+1];
               					mBallVel.x = mVelMag * mCos[i+1];
               					found = true;
               				}
               				sector += divider;
               			}
               			if(mVelMag < 10.0f)
               				mVelMag += 1.0f;
               		}
               	}
            }
        }
    }

    private boolean testBallPaddleCollision(float bx, float by, float px, float py) {
    	
    	return false;
    }
    
    public void onAccuracyChanged(int sensor, int accuracy) {
        // TODO Auto-generated method stub
        
    }
}
