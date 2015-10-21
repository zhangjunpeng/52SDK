package com.game.gamesdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;
import android.util.Log;

import com.game.http.GameHttpClient;
import com.game.tools.MD5Test;

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
				+ "&password=" + password + "&time=" + time + "|"
				+ GameSDK.AppKey;
		String sign = MD5Test.getMD5(unsign);

		NameValuePair nameValuePair9 = new BasicNameValuePair(
				RegisterConfig.sign, sign);
		Log.i("ZJP", unsign);
		Log.i("ZJP", sign);
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

		String unsign = "login_from=1&login_name=" + name + "&password=" + pwd
				+ "|" + GameSDK.AppKey;
		Log.i("ZJP", "unsign=====" + unsign);
		String sign = MD5Test.getMD5(unsign);
		Log.i("ZJP", "sign=====" + sign);
		NameValuePair nameValuePair5 = new BasicNameValuePair(
				RegisterConfig.sign, sign);
		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);
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
				+ password + "&time=" + time + "|" + GameSDK.AppKey;
		String sign = MD5Test.getMD5(unsign);

		NameValuePair nameValuePair9 = new BasicNameValuePair(
				RegisterConfig.sign, sign);
		Log.i("ZJP", unsign);
		Log.i("ZJP", sign);
		NameValuePair nameValuePair11 = new BasicNameValuePair("appid",
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
		nameValuePairs.add(nameValuePair11);

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
}
