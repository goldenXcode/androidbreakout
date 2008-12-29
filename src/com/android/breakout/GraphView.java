/****************************************************************************
    Copyright 2008 Clark Scheff
    
    This file is part of AndroidBreakout.

    AndroidBreakout is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AndroidBreakout is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AndroidBreakout.  If not, see <http://www.gnu.org/licenses/>.
****************************************************************************/

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
import android.os.Handler;
import android.os.Message;
import android.view.View;
import java.util.Random;

/**
 * @author lithium
 *
 */
public class GraphView extends View implements SensorListener, Runnable
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
    private final float mAccelMultiplier = 5.0f;
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
    private float	mVelMag = 6.0f;
    
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

        Thread thread = new Thread(this);
        thread.start();
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
            } 
        }
    }
    
    public void onSensorChanged(int sensor, float[] values) {
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
               	}
            }
        }
    }

    private boolean testBallPaddleCollision(float bx, float by, float px, float py) {
    	
    	return false;
    }
    
    // this method will execute once thread.start is called.  This method
    // will notify the handler that it is time to update the game
    public void run() {
    	while(true) {
   			handler.sendEmptyMessage(0);
   			try {
				// allow the thread to sleep a bit and allow other threads to run
   				// 17 milliseconds will allow for a frame rate of about 60 FPS.
   				Thread.sleep(17);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    private Handler handler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
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
       	        
       	        mVelMag = 6.0f;
       	        
       	        Random rand = new Random();
       	        int i = rand.nextInt(10);
       	        mBallVel.x = mVelMag * mCos[i];
       	        mBallVel.y = mVelMag * mSin[i];
       		}
       		
       		if(mBallVel.y > 0 && (mBallPos.y < mHeight - 5) &&
       				mBallPos.y > p.y && mBallPos.x > p.x &&
       				mBallPos.x < p.x + mPaddleWidth) {
       			float divider = (float)mPaddleWidth/10.0f;
       			float sector = (float)p.x+divider;
       			boolean found = false;
       			for(int i = 0; !found && i < 10; i++) {
       				if(mBallPos.x <= sector){
       					mBallVel.y = mVelMag * mSin[i];
       					mBallVel.x = mVelMag * mCos[i];
       					found = true;
       				}
       				sector += divider;
       			}
//       			if(mVelMag < 10.0f)
//       				mVelMag += 1.0f;
       		}
    		invalidate();
    	}
    };

	@Override
	public void onAccuracyChanged(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}

