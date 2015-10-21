package com.game.gamesdk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class UserInfoActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_info);
	}

}
