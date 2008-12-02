/**
 * 
 */
package com.android.breakout;

/**
 * @author lithium
 *
 */
public class Paddle {
	private Point mPosition;
	private Point mVelocity;
	private int mWidth = 80;
	private int mHeight = 15;

	
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
	
	public Point getPosition() {
		return mPosition;
	}
	
	public Point getVelocity() {
		return mVelocity;
	}
}
