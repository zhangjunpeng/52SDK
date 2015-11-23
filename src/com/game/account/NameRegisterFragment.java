package com.game.account;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.gamesdk.GameSDK;
import com.game.gamesdk.R;
import com.game.gamesdk.ShowDialog;
import com.game.gamesdk.UserInfo;
import com.game.http.NameRegLogin;
import com.game.tools.MyLog;
import com.game.tools.NetWorkState;
import com.game.tools.StringTools;

public class NameRegisterFragment extends Fragment {
	static ProgressDialog loading;
	static Activity mcontext;

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 0:
				// 注册成功

				if (loading != null) {
					loading.dismiss();
				}
				String data = msg.obj.toString();
				MyLog.i("注册返回：" + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorcode = jsonObject.getString("errorCode");
					if (errorcode.equals("200")) {
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");
						String sid = jsonObject2.getString("sid");
						Toast.makeText(mcontext, "注册成功", Toast.LENGTH_LONG)
								.show();
						LoginFragment.loginGamePayCallback.registerEndCallback(
								UserInfo.userID, sid);
						GameSDK.isLogin = true;
						mcontext.finish();
						// 保存登陆过的用户
						String nameUsed = GameSDK.getNameUsedSP();
						if (!nameUsed.contains(UserInfo.userName)) {
							nameUsed = nameUsed + UserInfo.userName + ";";
							GameSDK.saveNameUsedSP(nameUsed);
						}

					} else if (errorcode.equals("1008")) {
						Toast.makeText(mcontext, "用户名已存在,请重新注册",
								Toast.LENGTH_LONG).show();

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 1:
				// 登录成功
				String data1 = msg.obj.toString();
				MyLog.i("登录返回：" + data1);
				try {
					JSONObject jsonObject = new JSONObject(data1);
					String errorCode = jsonObject.getString("errorCode");
					if (errorCode.equals("200")) {
						GameSDK.isLogin = true;

						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");
						String sid = jsonObject2.getString("sid");
						Toast.makeText(mcontext, "登录成功", Toast.LENGTH_LONG)
								.show();
						LoginFragment.loginGamePayCallback.loginEndCallback(
								UserInfo.userID, sid);
						mcontext.finish();
						// 保存登陆过的用户
						String nameUsed = GameSDK.getNameUsedSP();

						if (!nameUsed.contains(UserInfo.userName)) {
							nameUsed = nameUsed + UserInfo.userName + ";";
							GameSDK.saveNameUsedSP(nameUsed);
							MyLog.i("保存用户：" + nameUsed);
							return;
						}

					} else if (errorCode.equals("2004")
							|| errorCode.equals("2005")) {
						Toast.makeText(mcontext, "用户名或密码错误", Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (loading != null) {
					loading.dismiss();
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
		View localView = inflater.inflate(R.layout.dialog_nameregister, null);
		final EditText edit_name = (EditText) localView
				.findViewById(R.id.edit_username_nameregister);
		final EditText edit_pwd = (EditText) localView
				.findViewById(R.id.edit_pwd_nameregister);

		Button login = (Button) localView
				.findViewById(R.id.register_nameregister);

		TextView haveAcount = (TextView) localView
				.findViewById(R.id.haveAcount_nameregister);

		localView.findViewById(R.id.phone_nameregister).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 手机注册
						getFragmentManager()
								.beginTransaction()
								.replace(R.id.container_userinfo,
										new PhoneRegisterFragment()).commit();
					}
				});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 检查网络
				if (!NetWorkState.getNetState(mcontext)) {
					Toast.makeText(mcontext, "网络连接错误，请检查网络", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String name = edit_name.getText().toString();
				String pwd = edit_pwd.getText().toString();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
					Toast.makeText(mcontext, "用户名或密码不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (StringTools.isHaveChinese(name)
						|| StringTools.isHaveChinese(pwd)) {
					Toast.makeText(mcontext, "用户名或密码不能包含中文", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (name.length() > 20 || name.length() < 6) {
					Toast.makeText(mcontext, "用户名长度不能小于6大于20",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (pwd.length() < 4 || pwd.length() > 20) {
					Toast.makeText(mcontext, "密码长度必须4-20位", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (StringTools.isHaveBlank(name)
						|| StringTools.isHaveBlank(pwd)) {
					Toast.makeText(mcontext, "用户名或密码不能包含空格", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				UserInfo.userName = name;
				GameSDK.saveInfo("name", name);
				GameSDK.saveInfo("pwd", pwd);
				NameRegLogin nameRegister = new NameRegLogin();
				nameRegister.nameRegister(name, "1", pwd, handler);
				loading = ShowDialog.showLoadingDialog(mcontext, "正在注册");
				loading.show();
			}
		});
		haveAcount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回登录
				getFragmentManager().beginTransaction()
						.replace(R.id.container_userinfo, new LoginFragment())
						.commit();

			}
		});
		return localView;
	}
}
