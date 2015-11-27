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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.callback.LoginGameCallback;
import com.game.gamesdk.GameSDK;
import com.game.gamesdk.InitListView;
import com.game.gamesdk.ShowDialog;
import com.game.gamesdk.UserInfo;
import com.game.http.NameRegLogin;
import com.game.tools.MyLog;
import com.game.tools.NetWorkState;
import com.game.tools.ResourceUtil;

public class LoginFragment extends Fragment {

	static boolean pwd_isvisiable = false;
	static ProgressDialog loading;
	static Activity mContext;
	public static LoginGameCallback loginGamePayCallback;
	String name;

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {

			case 1:
				// 登录成功
				String data1 = msg.obj.toString();
				MyLog.i("登录返回：" + data1);
				try {
					JSONObject jsonObject = new JSONObject(data1);
					String errorCode = jsonObject.getString("errorCode");
					if (errorCode.equals("200")) {

						GameSDK.isLogin = true;

						UserInfo.userName = name;
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");
						String sid = jsonObject2.getString("sid");
						Toast.makeText(mContext, "登录成功", Toast.LENGTH_LONG)
								.show();
						loginGamePayCallback.loginEndCallback(UserInfo.userID,
								sid);
						mContext.finish();
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
						Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_LONG)
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
		mContext = getActivity();

		View localView = inflater.inflate(
				ResourceUtil.getLayoutId(mContext, "dialog_login"), null);

		final EditText edit_name = (EditText) localView
				.findViewById(ResourceUtil.getId(getActivity(),
						"edit_username_login"));
		final EditText edit_pwd = (EditText) localView
				.findViewById(ResourceUtil.getId(getActivity(),
						"edit_pwd_login"));
		final ImageButton button_pwd_isvisible = (ImageButton) localView
				.findViewById(ResourceUtil.getId(getActivity(),
						"pwd_isvisiable_login"));

		Button quickRegisiter = (Button) localView.findViewById(ResourceUtil
				.getId(getActivity(), "button_qiuckRegister_login"));
		Button login = (Button) localView.findViewById(ResourceUtil.getId(
				getActivity(), "login_login"));
		CheckBox checkBox = (CheckBox) localView.findViewById(ResourceUtil
				.getId(getActivity(), "checkBox_login"));
		MyLog.i((checkBox == null) + "");
		TextView findbackpwd = (TextView) localView.findViewById(ResourceUtil
				.getId(getActivity(), "find_pwd_login"));

		checkBox.setChecked(AccountWork.autoLogin);
		// 每次改变自动登录的选中状态，系统都会记录
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				AccountWork.autoLogin = isChecked;
				GameSDK.saveInfo("autoLogin", AccountWork.autoLogin);
			}

		});

		button_pwd_isvisible.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pwd_isvisiable) {
					pwd_isvisiable = false;
					button_pwd_isvisible.setImageResource(ResourceUtil
							.getDrawableId(getActivity(), "eye_close"));
					edit_pwd.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				} else {
					pwd_isvisiable = true;
					button_pwd_isvisible.setImageResource(ResourceUtil
							.getDrawableId(getActivity(), "eye_open"));
					edit_pwd.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				}

			}
		});
		quickRegisiter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到注册
				getFragmentManager()
						.beginTransaction()
						.replace(
								ResourceUtil.getId(mContext,
										"container_userinfo"),
								new NameRegisterFragment()).commit();

			}
		});
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 登录
				if (!NetWorkState.getNetState(getActivity())) {
					Toast.makeText(getActivity(), "网络连接错误，请检查网络",
							Toast.LENGTH_SHORT).show();
					return;
				}
				name = edit_name.getText().toString();

				String pwd = edit_pwd.getText().toString();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
					Toast.makeText(getActivity(), "用户名或密码不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				GameSDK.saveInfo("name", name);
				GameSDK.saveInfo("pwd", pwd);

				NameRegLogin nameRegLogin = new NameRegLogin();
				nameRegLogin.nameLogin(name, pwd, "1", handler);
				loading = ShowDialog.showLoadingDialog(getActivity(),
						"正在登录中。。。");
				loading.show();
			}
		});
		findbackpwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		// 点击向下箭头，显示输入过用户名
		ImageButton imageButton = (ImageButton) localView
				.findViewById(ResourceUtil.getId(getActivity(),
						"more_name_login"));
		final ListView listView = (ListView) localView
				.findViewById(ResourceUtil.getId(getActivity(),
						"listview_login"));

		GameSDK.getUsedName();

		if (UserInfo.Name_used.size() > 0) {

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String name = UserInfo.Name_used.get(position);
					edit_name.setText(name);
					listView.setVisibility(View.GONE);
				}
			});
			imageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listView.getVisibility() == View.GONE) {
						InitListView initListView = new InitListView();
						initListView.initloginList(getActivity(), listView);
					} else {
						listView.setVisibility(View.GONE);
					}

				}
			});

		}
		return localView;
	}
}
