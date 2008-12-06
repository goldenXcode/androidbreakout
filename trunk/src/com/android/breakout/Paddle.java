/**
 * 
 */
package com.android.breakout;

import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.*;

/**
 * @author lithium
 *
 */
public class Paddle {
	private int mX = 0;
	private int mY = 0;
	private int mVx = 0;
	private int mVy = 0;
	private int mWidth = 80;
	private int mHeight = 15;
	private Rect mBounds = new Rect();
    private GradientDrawable mDrawable;

	public void Paddle() {
		mX = 0;
		mY = 0;
		
		mVx = 0;
		mVy = 0;
		
		mBounds.set(0, 0, 0, 0);
		
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            	new int[] { 0xFF0040FF, 0xFFFFFFFF, 0xFF002080 });
		mDrawable.setShape(GradientDrawable.LINEAR_GRADIENT);
		mDrawable.setCornerRadius(3);
		mDrawable.setStroke(1, 0xFF000000);
	
	}
	
	public void Paddle(int x, int y) {
		mX = x;
		mY = y;

		mVx = 0;
		mVy = 0;
		
		mBounds.set(0, 0, 0, 0);
	}

	public void Paddle(int x, int y, int vx, int vy, int w, int h, Rect bounds) {
		mX = x;
		mY = y;
		mVx = vx;
		mVy = vy;
		mWidth = w;
		mHeight = h;
		mBounds = bounds;
	}
	
	public void setPosition(int x, int y) {
		mX = x;
		mY = y;
		
		if (mX < mBounds.left)
			mX = mBounds.left;
		if ((mX + mWidth) > mBounds.right)
			mX = mBounds.right - mWidth;
	}
	
	public void setVelocity(int vx, int vy) {
		mVx = vx;
		mVy = vy;
	}
	
	public void setDimensions(int w, int h) {
		mWidth = w;
		mHeight = h;
	}
	
	public void setBounds(Rect bounds) {
		mBounds = bounds;
	}
	
	public Point getPosition() {
		Point p = new Point();
		p.x = mX;
		p.y = mY;
		return p;
	}
	
	public Point getVelocity() {
		Point p = new Point();
		p.x = mVx;
		p.y = mVy;
		return p;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		mDrawable.setBounds(mX, mY, 
				mX + mWidth, mY + mHeight);
		mDrawable.draw(canvas);
		
	}
}
