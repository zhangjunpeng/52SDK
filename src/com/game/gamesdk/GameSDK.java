package com.game.gamesdk;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.game.callback.LoginGameCallback;
import com.game.tools.MyLog;
import com.game.tools.StringTools;

/**
 * Created by Administrator on 2015/9/16.
 */
public class GameSDK {

	// APP参数

	public static String AppID = "52452712";
	public static String Key = "52game20153965981616";
	public static Context mcontext;

	private static SharedPreferences sharedPreferences;
	private static ProgressDialog progressDialog;

	private static LoginGameCallback loginGCallback;
	public static boolean isLogin = false;
	static boolean isshow = true;
	// debug模式。出版本之前改为false
	public static boolean isDebug = false;

	static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// 登录成功：

				progressDialog.dismiss();
				String data = msg.obj.toString();
				if (data.contains("200")) {
					isLogin = true;
					try {
						JSONObject jsonObject = new JSONObject(data);
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");
						GameSDK.isLogin = true;

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				loginGCallback.loginEndCallback(data);

				break;

			case 1:
				String data1 = msg.obj.toString();
				if (data1.contains("2004") || data1.contains("2005")) {
					Toast.makeText(mcontext, "用户名或密码错误，请重新登录",
							Toast.LENGTH_LONG).show();
					progressDialog.dismiss();
					ShowDialog.autoLogin = false;
					ShowDialog.showLoginDialog(mcontext);
					return;
				}

				// 自动登录成功：

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {

							Thread.sleep(1500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						progressDialog.dismiss();
						Activity activity = (Activity) mcontext;
						if (!isshow) {
							return;
						}
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(mcontext, "自动登录成功",
										Toast.LENGTH_LONG).show();
								Intent intent = new Intent(mcontext,
										FxService.class);
								// mcontext.startService(intent);
							}
						});

					}
				}).start();

				if (data1.contains("200")) {
					isLogin = true;
					MyLog.i("登录返回==" + StringTools.decodeUnicode(data1));

					loginGCallback.loginEndCallback(data1);

					try {
						JSONObject jsonObject = new JSONObject(data1);
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");
						UserInfo.userName = getInfoFromSP("name");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			}
		}
	};

	public static void init(Context context, String appid, String App_key,
			String channelId) {
		AppID = appid;
		Key = App_key;

		UserInfo.channel = channelId;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"gameInfo", Context.MODE_PRIVATE);

		ShowDialog.autoLogin = sharedPreferences.getBoolean("autoLogin", true);

		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		UserInfo.imei = telephonyManager.getDeviceId();

		mcontext = context;
	}

	public static void saveInfo(String name, String value) {

		sharedPreferences = mcontext.getSharedPreferences("gameInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(name, value);
		editor.commit();
	}

	public static void saveInfo(String name, boolean value) {

		sharedPreferences = mcontext.getSharedPreferences("gameInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}

	public static String getInfoFromSP(String name) {
		sharedPreferences = mcontext.getSharedPreferences("gameInfo",
				Context.MODE_PRIVATE);
		String value = sharedPreferences.getString(name, "");
		return value;

	}

	public static void login(final Context scontext,
			final LoginGameCallback loginGameCallback) {

		// 在登录成功和失败后分别调用loginGameCallback的两个方法

		ShowDialog.loginGamePayCallback = loginGameCallback;
		loginGCallback = loginGameCallback;

		ShowDialog.mcontext = scontext;
		MyLog.i("autoLogin===" + ShowDialog.autoLogin);

		if (ShowDialog.autoLogin) {
			// 自动登录

			final String name = getInfoFromSP("name");
			final String pwd = getInfoFromSP("pwd");

			MyLog.i("sp~~~name====" + name);
			MyLog.i("sp~~~pwd=====" + pwd);
			if (TextUtils.isEmpty(name)) {
				ShowDialog.showLoginDialog(mcontext);
				return;
			}

			progressDialog = ShowDialog.showLoadingDialog(mcontext, "正在自动登录。。");
			progressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "切换帐号",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							isshow = false;
							dialog.dismiss();
							ShowDialog.showLoginDialog(mcontext);
						}
					});
			progressDialog.show();

			NameRegLogin nameRegister = new NameRegLogin();
			nameRegister.nameLogin(name, pwd, "1", handler);

		} else {
			ShowDialog.showLoginDialog(mcontext);

		}
	}

	// 显示悬浮按钮
	public static void startFB(Context context) {
		context.startService(new Intent(context, FxService.class));
	}

	public static void loginOut() {
		isLogin = false;
		saveInfo("name", "");
		saveInfo("pwd", "");
	}

	public static void stop(Context context) {
		context.stopService(new Intent(context, FxService.class));
	}
}
