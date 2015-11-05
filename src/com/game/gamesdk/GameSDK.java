package com.game.gamesdk;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import com.game.account.AccountWork;
import com.game.account.LoginFragment;
import com.game.callback.LoginGameCallback;
import com.game.http.NameRegLogin;
import com.game.tools.MyLog;
import com.game.tools.NetWorkState;
import com.game.tools.StringTools;

/**
 * Created by Administrator on 2015/9/16.
 */
public class GameSDK {

	// APP参数

	public static String AppID = "52452712";
	public static String Key = "";
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
				JSONObject jsonObject3;
				try {
					jsonObject3 = new JSONObject(data1);
					String errorCode = jsonObject3.getString("errorCode");
					if (!"200".equals(errorCode)) {
						Toast.makeText(mcontext, "自动登录失败", Toast.LENGTH_LONG)
								.show();
						return;
					}
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
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
								// Intent intent = new Intent(mcontext,
								// FxService.class);
								// mcontext.startService(intent);
							}
						});

					}
				}).start();

				if (data1.contains("200")) {
					isLogin = true;
					MyLog.i("登录返回==" + StringTools.decodeUnicode(data1));

					try {
						JSONObject jsonObject = new JSONObject(data1);
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");
						String sid = jsonObject2.getString("sid");
						loginGCallback.loginEndCallback(UserInfo.userID, sid);
						UserInfo.userName = getInfoFromSP("name");
						// 保存登陆过的用户
						String nameUsed = GameSDK.getNameUsedSP();
						if (nameUsed.contains(UserInfo.userName)) {
							return;
						}
						nameUsed = nameUsed + UserInfo.userName + ";";
						GameSDK.saveNameUsedSP(nameUsed);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			}
		}
	};
	static Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("getkey返回：：" + data);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if ("200".equals(errorCode)) {
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						GameSDK.Key = jsonObject2.getString("key");
						Toast.makeText(mcontext, "初始化完成", Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		}
	};

	public static void init(Context context, final String appid,
			String App_key, String channelId) {
		AppID = appid;

		mcontext = context;
		Activity activity = (Activity) context;
		// View view = LayoutInflater.from(context)
		// .inflate(R.layout.welcome, null);
		// activity.setContentView(view);
		activity.startActivity(new Intent(context, WelcomeActivity.class));
		MyLog.i("设置ContentView");

		if (!NetWorkState.getNetState(mcontext)) {
			Toast.makeText(mcontext, "网络连接错误，请检查网络", Toast.LENGTH_SHORT).show();
			return;
		}

		ExecutorService single = Executors.newSingleThreadExecutor();
		single.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				NameRegLogin.getkey(appid, handler2);
			}
		});

		UserInfo.channel = channelId;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"gameInfo", Context.MODE_PRIVATE);

		ShowDialog.autoLogin = sharedPreferences.getBoolean("autoLogin", true);

		// 获取imei
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		UserInfo.imei = telephonyManager.getDeviceId();
		if (TextUtils.isEmpty(UserInfo.imei)) {
			String serialnum = null;
			try {
				Class<?> c = Class.forName("android.os.SystemProperties");
				Method get = c.getMethod("get", String.class, String.class);
				serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
			} catch (Exception ignored) {
			}
			UserInfo.imei = serialnum;
		}
		// 获取输入过的用户名
		getUsedName();

	}

	public static void getUsedName() {
		String namestring = getNameUsedSP();
		MyLog.i("UsedName===" + namestring);
		if (TextUtils.isEmpty(namestring)) {
			return;
		}
		String[] named = namestring.split(";");
		if (named != null && named.length > 0) {
			UserInfo.Name_used = new ArrayList<String>();
			for (int i = 0; i < named.length; i++) {
				UserInfo.Name_used.add(named[i]);
			}
		}
	}

	public static String getNameUsedSP() {
		sharedPreferences = mcontext.getSharedPreferences("gameInfo",
				Context.MODE_PRIVATE);
		String data = sharedPreferences.getString("NameUsed", "");
		return data;
	}

	public static void saveNameUsedSP(String named) {
		sharedPreferences = mcontext.getSharedPreferences("gameInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("NameUsed", named);
		editor.commit();
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
		LoginFragment.loginGamePayCallback = loginGameCallback;
		loginGCallback = loginGameCallback;

		ShowDialog.mcontext = scontext;
		MyLog.i("autoLogin===" + ShowDialog.autoLogin);
		if (!NetWorkState.getNetState(mcontext)) {
			Toast.makeText(mcontext, "网络连接错误，请检查网络", Toast.LENGTH_SHORT).show();
			ShowDialog.showLoginDialog(mcontext);
			return;
		}

		if (AccountWork.autoLogin) {
			// 自动登录

			final String name = getInfoFromSP("name");
			final String pwd = getInfoFromSP("pwd");

			MyLog.i("sp~~~name====" + name);
			MyLog.i("sp~~~pwd=====" + pwd);
			if (TextUtils.isEmpty(name)) {
				// ShowDialog.showLoginDialog(mcontext);
				AccountWork.showLogin(mcontext);
				return;
			}

			progressDialog = ShowDialog.showLoadingDialog(mcontext, "正在自动登录。。");
			progressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "切换帐号",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							isLogin = false;
							isshow = false;
							dialog.dismiss();
							// ShowDialog.showLoginDialog(mcontext);
							AccountWork.showLogin(mcontext);
						}
					});
			progressDialog.show();

			NameRegLogin nameRegister = new NameRegLogin();
			nameRegister.nameLogin(name, pwd, "1", handler);

		} else {
			// ShowDialog.showLoginDialog(mcontext);
			AccountWork.showLogin(mcontext);

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

	public static void stopSDK() {
		GameSDK.isLogin = false;
	}
}
