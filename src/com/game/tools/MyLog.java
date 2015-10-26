package com.game.tools;

import android.util.Log;

import com.game.gamesdk.GameSDK;

public class MyLog {

	public static void i(String mes) {
		if (!GameSDK.isDebug) {
			return;
		}
		Log.i("52Game", mes);
	}
}
