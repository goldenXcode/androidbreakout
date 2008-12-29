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

import android.graphics.Rect;
import android.graphics.Canvas;
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
	private int mHeight = 12;
	private Rect mBounds = new Rect();
    private GradientDrawable mDrawable;

	public Paddle() {
		mX = 0;
		mY = 0;
		
		mVx = 0;
		mVy = 0;
		
		mBounds.set(0, 0, 0, 0);
		
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            	new int[] { 0xFF0040FF, 0xFFFFFFFF, 0xFF400080 });
		mDrawable.setShape(GradientDrawable.LINEAR_GRADIENT);
		mDrawable.setCornerRadius(5);
		mDrawable.setStroke(2, 0xFF000000);
	
	}
	
	public Paddle(final int x, final int y) {
		mX = x;
		mY = y;

		mVx = 0;
		mVy = 0;
		
		mBounds.set(0, 0, 0, 0);
	}

	public Paddle(int x, int y, int vx, int vy, int w, int h, Rect bounds) {
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
	
	public void draw(Canvas canvas) {
		mDrawable.setBounds(mX, mY, 
				mX + mWidth, mY + mHeight);
		mDrawable.draw(canvas);
	}
	
	public void update() {
		mX += mVx;

		if (mX < mBounds.left)
			mX = mBounds.left;
		if ((mX + mWidth) > mBounds.right)
			mX = mBounds.right - mWidth;
	}
}
