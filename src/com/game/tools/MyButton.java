package com.game.tools;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.Button;

import com.game.gamesdk.R;

public class MyButton extends Button {

	public MyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		if (focused) {
			setBackgroundResource(R.drawable.fx);
		} else {
			// setBackgroundResource(R.drawable)
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
}
