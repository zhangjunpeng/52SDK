package com.game.gamesdk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.game.fragment.ConnectFragment;
import com.game.fragment.UserInfoFragment;

public class UserInfoActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.activity_user_info);
		int tag = getIntent().getIntExtra("tag", 1);
		comitFragment(tag);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.ImageView1_acinfo).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						comitFragment(1);
					}
				});
	}

	private void comitFragment(int tag) {
		// TODO Auto-generated method stub
		switch (tag) {
		case 1:
			UserInfoFragment userInfoFragment = new UserInfoFragment();

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container_userinfo, userInfoFragment)
					.commit();
			break;

		case 2:
			ConnectFragment connectFragment = new ConnectFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container_userinfo, connectFragment).commit();
			break;
		default:
			break;
		}

	}

}
