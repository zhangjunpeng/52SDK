package com.game.gamesdk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

public class UserInfoActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_info);
		comitFragment();
	}

	private void comitFragment() {
		// TODO Auto-generated method stub
		UserInfoFragment userInfoFragment = new UserInfoFragment();

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container_userinfo, userInfoFragment).commit();

	}

}
