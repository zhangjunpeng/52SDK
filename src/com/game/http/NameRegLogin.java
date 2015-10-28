package com.game.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;

import com.game.gamesdk.GameSDK;
import com.game.gamesdk.RegisterConfig;
import com.game.gamesdk.ShowDialog;
import com.game.gamesdk.UserInfo;
import com.game.tools.MD5Test;
import com.game.tools.MyLog;

public class NameRegLogin {

	public String nameRegister(String name, String type, String password,
			final Handler handler) {

		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair(
				RegisterConfig.imei, UserInfo.imei);

		NameValuePair nameValuePair2 = new BasicNameValuePair(
				RegisterConfig.channel, UserInfo.channel);
		NameValuePair nameValuePair3 = new BasicNameValuePair(
				RegisterConfig.platform, "1");
		NameValuePair nameValuePair4 = new BasicNameValuePair(
				RegisterConfig.phonetype, "1");
		NameValuePair nameValuePair5 = new BasicNameValuePair(
				RegisterConfig.name, name);

		NameValuePair nameValuePair6 = new BasicNameValuePair(
				RegisterConfig.type, type);
		NameValuePair nameValuePair7 = new BasicNameValuePair(
				RegisterConfig.password, password);

		Date d = new Date();

		long time = d.getTime();

		NameValuePair nameValuePair8 = new BasicNameValuePair(
				RegisterConfig.time, time + "");
		String unsign = "login_name=" + name + "&reg_type=" + type
				+ "&password=" + password + "&time=" + time + "|" + GameSDK.Key;
		String sign = MD5Test.getMD5(unsign);

		NameValuePair nameValuePair9 = new BasicNameValuePair(
				RegisterConfig.sign, sign);

		NameValuePair nameValuePair10 = new BasicNameValuePair("appid",
				GameSDK.AppID);
		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);
		nameValuePairs.add(nameValuePair6);
		nameValuePairs.add(nameValuePair7);
		nameValuePairs.add(nameValuePair8);
		nameValuePairs.add(nameValuePair9);
		nameValuePairs.add(nameValuePair10);

		MyLog.i(nameValuePairs.toString());
		ExecutorService single = Executors.newSingleThreadExecutor();
		single.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GameHttpClient httpClient = new GameHttpClient(handler);
				httpClient.startClient(RegisterConfig.registerURL,
						nameValuePairs);
			}
		});
		return null;
	}

	public void nameLogin(String name, String pwd, String regtype,
			final Handler handler) {
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair(
				RegisterConfig.form, "1");

		NameValuePair nameValuePair2 = new BasicNameValuePair(
				RegisterConfig.name, name);
		NameValuePair nameValuePair3 = new BasicNameValuePair(
				RegisterConfig.password, pwd);
		long time = new Date().getTime();
		NameValuePair nameValuePair4 = new BasicNameValuePair(
				RegisterConfig.time, time + "");

		NameValuePair nameValuePair6 = new BasicNameValuePair("appid",
				GameSDK.AppID);
		String unsign = "login_from=1&login_name=" + name + "&password=" + pwd
				+ "|" + GameSDK.Key;

		String sign = MD5Test.getMD5(unsign);

		NameValuePair nameValuePair5 = new BasicNameValuePair(
				RegisterConfig.sign, sign);
		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);
		nameValuePairs.add(nameValuePair6);
		MyLog.i("nameLogin==" + nameValuePairs);
		ExecutorService single = Executors.newSingleThreadExecutor();
		single.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GameHttpClient httpClient = new GameHttpClient(handler);
				httpClient.loginClient(RegisterConfig.loginURL, nameValuePairs);
			}
		});
	}

	public void phoneRegister(String name, String password, String moblecode,
			final Handler handler) {
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair(
				RegisterConfig.imei, UserInfo.imei);

		NameValuePair nameValuePair2 = new BasicNameValuePair(
				RegisterConfig.channel, UserInfo.channel);
		NameValuePair nameValuePair3 = new BasicNameValuePair(
				RegisterConfig.platform, "1");
		NameValuePair nameValuePair4 = new BasicNameValuePair(
				RegisterConfig.phonetype, "1");
		NameValuePair nameValuePair5 = new BasicNameValuePair(
				RegisterConfig.name, name);

		NameValuePair nameValuePair6 = new BasicNameValuePair(
				RegisterConfig.type, "2");
		NameValuePair nameValuePair7 = new BasicNameValuePair(
				RegisterConfig.password, password);
		NameValuePair nameValuePair10 = new BasicNameValuePair("mobile_code",
				moblecode);

		Date d = new Date();

		long time = d.getTime();

		NameValuePair nameValuePair8 = new BasicNameValuePair(
				RegisterConfig.time, time + "");
		String unsign = "login_name=" + name + "&reg_type=2" + "&password="
				+ password + "&time=" + time + "|" + GameSDK.Key;
		String sign = MD5Test.getMD5(unsign);

		NameValuePair nameValuePair9 = new BasicNameValuePair(
				RegisterConfig.sign, sign);

		NameValuePair nameValuePair11 = new BasicNameValuePair("appid",
				GameSDK.AppID);
		NameValuePair nameValuePair12 = new BasicNameValuePair("token",
				ShowDialog.token);

		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);
		nameValuePairs.add(nameValuePair6);
		nameValuePairs.add(nameValuePair7);
		nameValuePairs.add(nameValuePair8);
		nameValuePairs.add(nameValuePair9);
		nameValuePairs.add(nameValuePair10);
		nameValuePairs.add(nameValuePair11);
		nameValuePairs.add(nameValuePair12);
		MyLog.i("phoneRe==" + nameValuePairs.toString());
		ExecutorService single = Executors.newSingleThreadExecutor();
		single.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GameHttpClient httpClient = new GameHttpClient(handler);
				httpClient.startClient(RegisterConfig.registerURL,
						nameValuePairs);
			}
		});
	}

	public static void changepwd(String old, String newpwd, String re_new,
			Handler handler) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair("user_id",
				UserInfo.userID);
		NameValuePair nameValuePair2 = new BasicNameValuePair("old_password",
				old);
		NameValuePair nameValuePair3 = new BasicNameValuePair("new_pasword",
				newpwd);
		NameValuePair nameValuePair4 = new BasicNameValuePair(
				"re_new_password", re_new);
		String unsign = "user_id=" + UserInfo.userID + "&old_password=" + old
				+ "&new_pasword=" + newpwd + "&re_new_password=" + re_new + "|"
				+ GameSDK.Key;
		String sign = MD5Test.getMD5(unsign);
		NameValuePair nameValuePair5 = new BasicNameValuePair("sign", sign);

		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);

		MyLog.i("changepwd.namevalue==" + nameValuePairs.toString());
		GameHttpClient gameHttpClient = new GameHttpClient(handler);
		gameHttpClient.startClient(RegisterConfig.changepwd, nameValuePairs);

	}

	public static void getkey(String appid, Handler handler) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair("appid", appid);
		nameValuePairs.add(nameValuePair1);
		GameHttpClient gameHttpClient = new GameHttpClient(handler);
		gameHttpClient.startClient(RegisterConfig.getKey, nameValuePairs);

	}
}
