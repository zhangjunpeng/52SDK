package com.game.gamesdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.game.callback.LoginGameCallback;
import com.game.http.GameHttpClient;
import com.game.tools.MD5Test;
import com.game.tools.MyLog;

/**
 * Created by Administrator on 2015/9/15.
 */
public class ShowDialog {

	private static View localView;
	static boolean autoLogin = false;// 是否自动登录
	private static ExecutorService singleThreadExecutors = Executors
			.newSingleThreadExecutor();

	// 密码可见的属性为 pwd_isvisiable 默认为false
	static boolean pwd_isvisiable = false;

	public static LoginGameCallback loginGamePayCallback;

	public static Context mcontext;

	static Dialog dialog;
	static Button getproving;
	static String phoneNum;

	static ProgressDialog progressDialog;
	static String token;

	static ProgressDialog loading = null;
	final static Handler handler = new Handler() {
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
						Toast.makeText(mcontext, "注册成功", Toast.LENGTH_LONG)
								.show();
						loginGamePayCallback.registerEndCallback(data);
						GameSDK.isLogin = true;
						dialog.dismiss();
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
						Toast.makeText(mcontext, "登录成功", Toast.LENGTH_LONG)
								.show();
						loginGamePayCallback.loginEndCallback(msg.obj
								.toString());
						dialog.dismiss();
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
	static Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("验证码返回：" + data);

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
						Log.i("ZJP", token);

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("ZJP", "解析错误");
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		}

	};
	static Handler handler3 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog.dismiss();
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
						Toast.makeText(mcontext, "注册成功", Toast.LENGTH_LONG)
								.show();
						loginGamePayCallback.registerEndCallback(data);
						dialog.dismiss();
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

	private static void setButton(final Button button) {
		// TODO Auto-generated method stub

		final Handler handler3 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					button.setClickable(false);
					button.setText(msg.obj.toString() + "秒");
					break;

				case 1:
					button.setClickable(true);
					button.setText(R.string.getproving);
					break;
				}
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (int i = 60; i > 0; i--) {
					handler3.obtainMessage(0, i).sendToTarget();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				handler3.sendEmptyMessage(1);
			}
		}).start();

	}

	public static void showPhoneRegisterDialog(final Context paramContext) {

		localView = LayoutInflater.from(paramContext).inflate(
				R.layout.dialog_showmobleregist, null);
		// localView.setPadding(30, 30, 30, 30);
		dialog = new Dialog(paramContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(localView);

		// WindowManager.LayoutParams params =
		// dialog.getWindow().getAttributes();
		//
		// WindowManager wm = (WindowManager) paramContext
		// .getSystemService(Context.WINDOW_SERVICE);
		//
		// int width = wm.getDefaultDisplay().getWidth();
		// int height = wm.getDefaultDisplay().getHeight();
		// if (width > height) {
		// params.width = (int) (width * 0.65);
		// params.height = (int) (height * 0.8);
		// } else {
		// params.width = (int) (height * 0.5);
		// params.height = (int) (width * 0.7);
		// }
		// dialog.getWindow().setAttributes(params);

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

						String pwd = edit_pwd.getText().toString();
						String prov = edit_prov.getText().toString();
						if (TextUtils.isEmpty(phoneNum)) {
							Toast.makeText(paramContext, "请输入手机号码",
									Toast.LENGTH_LONG).show();
							return;
						} else if (TextUtils.isEmpty(prov)) {
							Toast.makeText(paramContext, "请输入验证码",
									Toast.LENGTH_LONG).show();
							return;
						} else if (TextUtils.isEmpty(pwd)) {
							Toast.makeText(paramContext, "请输入密码",
									Toast.LENGTH_LONG).show();
							return;
						} else if (pwd.length() < 4 || pwd.length() > 20) {
							Toast.makeText(paramContext, "密码长度必须4-20位",
									Toast.LENGTH_LONG).show();
							return;
						}
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
					Toast.makeText(paramContext, "手机号格式不正确", Toast.LENGTH_LONG)
							.show();
					return;
				}
				singleThreadExecutors.execute(new Runnable() {

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
				setButton(getproving);
			}
		});
		localView.findViewById(R.id.login_phoneregister).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						showLoginDialog(paramContext);

					}
				});

		localView.findViewById(R.id.namereg_phoneregister).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						showNameRegisterDialog(paramContext);
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
		dialog.show();

	}

	public static void showLoginDialog(final Context paramContext) {
		localView = LayoutInflater.from(paramContext).inflate(
				R.layout.dialog_login, null);
		dialog = new Dialog(paramContext);
		// localView.setPadding(30, 30, 30, 30);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(localView);
		// WindowManager.LayoutParams params =
		// dialog.getWindow().getAttributes();
		//
		// WindowManager wm = (WindowManager) paramContext
		// .getSystemService(Context.WINDOW_SERVICE);
		//
		// int width = wm.getDefaultDisplay().getWidth();
		// int height = wm.getDefaultDisplay().getHeight();
		// if (width > height) {
		// params.width = (int) (width * 0.65);
		// params.height = (int) (height * 0.65);
		// } else {
		// params.width = (int) (height * 0.5);
		// params.height = (int) (width * 0.7);
		// }

		// dialog.getWindow().setAttributes(params);
		initLoginDialogClickListener(paramContext, localView, dialog);

		dialog.show();

	}

	public static void showNameRegisterDialog(final Context paramContext) {
		localView = LayoutInflater.from(paramContext).inflate(
				R.layout.dialog_nameregister, null);
		dialog = new Dialog(paramContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(localView);

		// WindowManager.LayoutParams params =
		// dialog.getWindow().getAttributes();
		//
		// WindowManager wm = (WindowManager) paramContext
		// .getSystemService(Context.WINDOW_SERVICE);
		//
		// int width = wm.getDefaultDisplay().getWidth();
		// int height = wm.getDefaultDisplay().getHeight();
		// if (width > height) {
		// params.width = (int) (width * 0.65);
		// params.height = (int) (height * 0.65);
		// } else {
		// params.width = (int) (height * 0.5);
		// params.height = (int) (width * 0.7);
		// }

		// dialog.getWindow().setAttributes(params);
		initNameRegisterDialogClickListener(paramContext, localView, dialog);
		dialog.show();
	}

	public static void initLoginDialogClickListener(final Context paramContext,
			View localView, final Dialog dialog) {
		Log.i("52Game", localView.toString());
		Log.i("52Game", (localView == null) + "");
		final EditText edit_name = (EditText) localView
				.findViewById(R.id.edit_username_login);
		final EditText edit_pwd = (EditText) localView
				.findViewById(R.id.edit_pwd_login);
		final ImageButton button_pwd_isvisible = (ImageButton) localView
				.findViewById(R.id.pwd_isvisiable_login);
		Button quickRegisiter = (Button) localView
				.findViewById(R.id.button_qiuckRegister_login);
		Button login = (Button) localView.findViewById(R.id.login_login);
		CheckBox checkBox = (CheckBox) localView
				.findViewById(R.id.checkBox_login);
		Log.i("52Game", (checkBox == null) + "");
		TextView findbackpwd = (TextView) localView
				.findViewById(R.id.find_pwd_login);

		// 每次改变自动登录的选中状态，系统都会记录
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				autoLogin = isChecked;
				GameSDK.saveInfo("autoLogin", autoLogin);
			}

		});

		button_pwd_isvisible.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pwd_isvisiable) {
					pwd_isvisiable = false;
					button_pwd_isvisible.setImageResource(R.drawable.eye_close);
					edit_pwd.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				} else {
					pwd_isvisiable = true;
					button_pwd_isvisible.setImageResource(R.drawable.eye_open);
					edit_pwd.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				}

			}
		});
		quickRegisiter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showNameRegisterDialog(paramContext);

			}
		});
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 登录
				String name = edit_name.getText().toString();
				UserInfo.userName = name;
				String pwd = edit_pwd.getText().toString();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
					Toast.makeText(paramContext, "用户名或密码不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				GameSDK.saveInfo("name", name);
				GameSDK.saveInfo("pwd", pwd);

				NameRegLogin nameRegLogin = new NameRegLogin();
				nameRegLogin.nameLogin(name, pwd, "1", handler);
				loading = showLoadingDialog(paramContext, "正在登录中。。。");
			}
		});
		findbackpwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

	}

	public static void initNameRegisterDialogClickListener(
			final Context paramContext, View localView, final Dialog dialog) {
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
						// TODO Auto-generated method stub
						dialog.dismiss();
						showPhoneRegisterDialog(paramContext);
					}
				});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = edit_name.getText().toString();
				String pwd = edit_pwd.getText().toString();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
					Toast.makeText(paramContext, "用户名或密码不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (name.length() > 20 || name.length() < 6) {
					Toast.makeText(paramContext, "用户名长度不能小于6大于20",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (pwd.length() < 4 || pwd.length() > 20) {
					Toast.makeText(paramContext, "密码长度必须4-20位",
							Toast.LENGTH_LONG).show();
					return;
				}

				GameSDK.saveInfo("name", name);
				GameSDK.saveInfo("pwd", pwd);
				NameRegLogin nameRegister = new NameRegLogin();
				nameRegister.nameRegister(name, "1", pwd, handler);
				loading = showLoadingDialog(paramContext, "正在登录");
			}
		});
		haveAcount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showLoginDialog(paramContext);

			}
		});
	}

	public static ProgressDialog showLoadingDialog(Context context, String mes) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("提示：");
		progressDialog.setMessage(mes);
		progressDialog.setCancelable(false);

		return progressDialog;
	}

}
