package com.game.account;

import android.content.Context;
import android.content.Intent;

import com.game.gamesdk.UserInfoActivity;

public class AccountWork {

	public static boolean autoLogin = true;

	public static void showLogin(Context context) {
		Intent intent = new Intent(context, UserInfoActivity.class);
		intent.putExtra("tag", 3);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(intent);
	}
}
