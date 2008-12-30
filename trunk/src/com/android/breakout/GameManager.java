/****************************************************************************
    Copyright 2008 Clark Scheff
    
    This file is part of AndroidBreakout.

    AndroidBreakout is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation version 3 of the License.

    AndroidBreakout is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AndroidBreakout.  If not, see <http://www.gnu.org/licenses/>.
****************************************************************************/

package com.android.breakout;

import android.graphics.drawable.GradientDrawable;

/**
 * @author lithium
 *
 */
public class GameManager {
    private int		mPaddleWidth = 80;
    private int		mPaddleHeight = 16;
    private final float mAccelMultiplier = 5.0f;
    private int mNudgeValue = 8;
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
    
    public final int	INITIAL_PADDLE_HEIGHT = 16;
    public final int	INITIAL_PADDLE_WIDTH = 80;
    public final float	INITIAL_BALL_VELOCITY = 6.0f;
    public final float	ACCELEROMETER_MULTIPLIER = 5.0f;
    
}

