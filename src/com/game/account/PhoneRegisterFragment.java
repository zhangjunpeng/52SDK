package com.game.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.game.gamesdk.GameSDK;
import com.game.gamesdk.R;
import com.game.gamesdk.RegisterConfig;
import com.game.gamesdk.ShowDialog;
import com.game.gamesdk.UserInfo;
import com.game.http.GameHttpClient;
import com.game.http.NameRegLogin;
import com.game.tools.MD5Test;
import com.game.tools.MyLog;
import com.game.tools.NetWorkState;
import com.game.tools.StringTools;

public class PhoneRegisterFragment extends Fragment {

	static Activity mcontext;
	static Button getproving;
	static String phoneNum;
	static String token;
	static boolean pwd_isvisiable = false;

	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("验证码返回：" + data);
				// json格式转
				if (data.startsWith("\ufeff")) {
					data = data.substring(1);
				}
				try {
					JSONObject jsonObject = new JSONObject(data);

					String errorcode = jsonObject.getString("errorCode");
					Log.i("ZJP", errorcode + "");
					if (errorcode.equals("200")) {
						Toast.makeText(mcontext, "验证码已发送", Toast.LENGTH_SHORT)
								.show();
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						token = jsonObject2.getString("token");
						MyLog.i("token::" + token);

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
	Handler handler3 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				String data = msg.obj.toString();
				MyLog.i("手机注册返回：" + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorcode = jsonObject.getString("errorCode");
					if (errorcode.equals("1008")) {
						Toast.makeText(mcontext, "手机号已注册", Toast.LENGTH_LONG)
								.show();
						return;
					} else if (errorcode.equals("1011")) {
						Toast.makeText(mcontext, "验证码错误", Toast.LENGTH_LONG)
								.show();
						return;
					} else if (errorcode.equals("200")) {
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");
						String sid = jsonObject2.getString("sid");
						Toast.makeText(mcontext, "注册成功", Toast.LENGTH_LONG)
								.show();
						LoginFragment.loginGamePayCallback.registerEndCallback(
								UserInfo.userID, sid);
						mcontext.finish();
					} else {
						Toast.makeText(mcontext, "注册错误", Toast.LENGTH_LONG)
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mcontext = getActivity();
		View localView = inflater
				.inflate(R.layout.dialog_showmobleregist, null);
		final EditText edit_phonenum = (EditText) localView
				.findViewById(R.id.edit_phonenum_phoneregister);
		final EditText edit_pwd = (EditText) localView
				.findViewById(R.id.edit_pwd_phoneregister);
		final EditText edit_prov = (EditText) localView
				.findViewById(R.id.edit_pronum_phoneregister);

		localView.findViewById(R.id.register_register).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!NetWorkState.getNetState(mcontext)) {
							Toast.makeText(mcontext, "网络连接错误，请检查网络",
									Toast.LENGTH_SHORT).show();
							return;
						}

						String pwd = edit_pwd.getText().toString();
						String prov = edit_prov.getText().toString();
						String phone = edit_phonenum.getEditableText()
								.toString();
						if (TextUtils.isEmpty(phone)) {
							Toast.makeText(mcontext, "请输入手机号码",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (phone.length() != 11) {
							Toast.makeText(mcontext, "请输入正确格式的手机号码",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (StringTools.isHaveChinese(pwd)) {
							Toast.makeText(mcontext, "密码不能包含中文",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (TextUtils.isEmpty(prov)) {
							Toast.makeText(mcontext, "请输入验证码",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (TextUtils.isEmpty(pwd)) {
							Toast.makeText(mcontext, "请输入密码",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (pwd.length() < 4 || pwd.length() > 20) {
							Toast.makeText(mcontext, "密码长度必须4-20位",
									Toast.LENGTH_SHORT).show();
							return;
						}
						UserInfo.userName = phoneNum;
						NameRegLogin nameRegLogin = new NameRegLogin();
						nameRegLogin.phoneRegister(phoneNum, pwd, prov,
								handler3);
						// Toast.makeText(paramContext, "开始注册",
						// Toast.LENGTH_SHORT)
						// .show();

					}
				});
		getproving = (Button) localView
				.findViewById(R.id.button_getproving_phoneRegister);
		getproving.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("ZJP", "验证码开始");
				phoneNum = edit_phonenum.getEditableText().toString();
				if (phoneNum.length() != 11) {
					Toast.makeText(mcontext, "手机号格式不正确", Toast.LENGTH_LONG)
							.show();
					return;
				}
				Executors.newSingleThreadExecutor().execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						NameValuePair nameValuePair = new BasicNameValuePair(
								"phone", phoneNum);
						String time = new Date().getTime() + "";
						NameValuePair nameValuePair2 = new BasicNameValuePair(
								"time", time);
						String unsign = "phone=" + phoneNum + "&time=" + time
								+ "|" + GameSDK.Key;
						Log.i("ZJP", unsign);
						String sign = MD5Test.getMD5(unsign);
						NameValuePair nameValuePair3 = new BasicNameValuePair(
								"sign", sign);
						nameValuePairs.add(nameValuePair);
						nameValuePairs.add(nameValuePair2);
						nameValuePairs.add(nameValuePair3);
						GameHttpClient gameHttpClient = new GameHttpClient(
								handler2);

						gameHttpClient.startClient(RegisterConfig.smsUrl,
								nameValuePairs);

						Log.i("ZJP", nameValuePairs.toString());
						Log.i("ZJP", "验证码开始2");

					}
				});
				ShowDialog.setButton(getproving);
			}
		});
		localView.findViewById(R.id.login_phoneregister).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getFragmentManager()
								.beginTransaction()
								.replace(R.id.container_userinfo,
										new LoginFragment()).commit();

					}
				});

		localView.findViewById(R.id.namereg_phoneregister).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getFragmentManager()
								.beginTransaction()
								.replace(R.id.container_userinfo,
										new NameRegisterFragment()).commit();
					}
				});
		final ImageButton eye = (ImageButton) localView
				.findViewById(R.id.eye_phoneregister);
		eye.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pwd_isvisiable) {
					pwd_isvisiable = false;
					eye.setImageResource(R.drawable.eye_close);
					edit_pwd.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				} else {
					pwd_isvisiable = true;
					eye.setImageResource(R.drawable.eye_open);
					edit_pwd.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				}
			}
		});

		return localView;
	}
}
