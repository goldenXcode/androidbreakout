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
	private Point mPosition = new Point();
	private Point mVelocity = new Point();
	private int mWidth = 80;
	private int mHeight = 15;
	private Rect mBounds = new Rect();
    private GradientDrawable mDrawable;

	public void Paddle() {
		mPosition.x = 0;
		mPosition.y = 0;
		
		mVelocity.x = 0;
		mVelocity.y = 0;
		
		mBounds.set(0, 0, 0, 0);
		
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            	new int[] { 0xFF0040FF, 0xFFFFFFFF, 0xFF002080 });
		mDrawable.setShape(GradientDrawable.LINEAR_GRADIENT);
		mDrawable.setCornerRadius(3);
		mDrawable.setStroke(1, 0xFF000000);
	
	}
	
	public void Paddle(Point pos) {
		mPosition = pos;

		mVelocity.x = 0;
		mVelocity.y = 0;
		
		mBounds.set(0, 0, 0, 0);
	}

	public void Paddle(Point pos, Point vel, int w, int h, Rect bounds) {
		mPosition = pos;
		mVelocity = vel;
		mWidth = w;
		mHeight = h;
		mBounds = bounds;
	}
	
	public void setPosition(int x, int y) {
		mPosition.x = x;
		mPosition.y = y;
	}
	
	public void setVelocity(int vx, int vy) {
		mVelocity.x = vx;
		mVelocity.y = vy;
	}
	
	public void setDimensions(int w, int h) {
		mWidth = w;
		mHeight = h;
	}
	
	public void setBounds(Rect bounds) {
		mBounds = bounds;
	}
	
	public Point getPosition() {
		return mPosition;
	}
	
	public Point getVelocity() {
		return mVelocity;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		mDrawable.setBounds(mPosition.x, mPosition.y, 
				mPosition.x + mWidth, mPosition.y + mHeight);
		mDrawable.draw(canvas);
		
	}
}
