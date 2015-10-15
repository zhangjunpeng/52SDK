package com.game.gamesdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.game.callback.LoginGameCallback;

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

	static ProgressDialog loading = null;
	final static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 0:
				// 注册成功
				dialog.dismiss();
				Toast.makeText(mcontext, "注册成功", Toast.LENGTH_LONG).show();
				loginGamePayCallback.registerEndCallback(msg.obj.toString());
				if (loading != null) {
					loading.dismiss();
				}
				String data = msg.obj.toString();
				if (data.contains("200")) {
					GameSDK.isLogin = true;
					try {
						JSONObject jsonObject = new JSONObject(data);
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 1:
				// 登录成功
				String data1 = msg.obj.toString();
				if (data1.contains("200")) {
					GameSDK.isLogin = true;
					try {
						JSONObject jsonObject = new JSONObject(data1);
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						UserInfo.userID = jsonObject2.getString("uid");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				dialog.dismiss();
				Log.i("ZJP", "loginEND====" + msg.obj.toString());
				Toast.makeText(mcontext, "登录成功", Toast.LENGTH_LONG).show();
				loginGamePayCallback.loginEndCallback(msg.obj.toString());
				if (loading != null) {
					loading.dismiss();
				}
				break;

			default:
				break;
			}
		}
	};

	public static void showPhoneRegisterDialog(final Context paramContext) {

		localView = LayoutInflater.from(paramContext).inflate(
				R.layout.dialog_showmobleregist, null);
		// localView.setPadding(30, 30, 30, 30);
		Dialog dialog = new Dialog(paramContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(localView);

		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

		WindowManager wm = (WindowManager) paramContext
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		if (width > height) {
			params.width = (int) (width * 0.65);
			params.height = (int) (height * 0.8);
		} else {
			params.width = (int) (height * 0.5);
			params.height = (int) (width * 0.7);
		}
		dialog.getWindow().setAttributes(params);

		final EditText edit_phonenum = (EditText) localView
				.findViewById(R.id.edit_phonenum_phoneregister);
		final EditText edit_pwd = (EditText) localView
				.findViewById(R.id.edit_pwd_phoneregister);
		final EditText edit_prov = (EditText) localView
				.findViewById(R.id.edit_pronum_phoneregister);

		CheckBox checkbox = (CheckBox) localView
				.findViewById(R.id.checkBox_phoneregister);
		final boolean isagree = checkbox.isChecked();

		localView.findViewById(R.id.register_register).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isagree) {
							String phonenum = edit_phonenum.getText()
									.toString();
							String pwd = edit_pwd.getText().toString();
							String prov = edit_prov.getText().toString();
							if (!"".equals(phonenum) && !"".equals(pwd)
									&& !"".equals(prov)) {

							}

						} else {
							Toast.makeText(paramContext, "需要同意《52游戏条款》",
									Toast.LENGTH_LONG).show();
						}
					}
				});
		localView.findViewById(R.id.button_getproving_phoneRegister)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});

		initNameRegisterDialogClickListener(paramContext, localView, dialog);
		dialog.show();
	}

	public static void showLoginDialog(final Context paramContext) {
		localView = LayoutInflater.from(paramContext).inflate(
				R.layout.dialog_login, null);
		dialog = new Dialog(paramContext);
		// localView.setPadding(30, 30, 30, 30);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(localView);
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

		WindowManager wm = (WindowManager) paramContext
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		if (width > height) {
			params.width = (int) (width * 0.65);
			params.height = (int) (height * 0.65);
		} else {
			params.width = (int) (height * 0.5);
			params.height = (int) (width * 0.7);
		}

		dialog.getWindow().setAttributes(params);
		initLoginDialogClickListener(paramContext, localView, dialog);

		dialog.show();

	}

	public static void showNameRegisterDialog(final Context paramContext) {
		localView = LayoutInflater.from(paramContext).inflate(
				R.layout.dialog_nameregister, null);
		dialog = new Dialog(paramContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(localView);

		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

		WindowManager wm = (WindowManager) paramContext
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		if (width > height) {
			params.width = (int) (width * 0.65);
			params.height = (int) (height * 0.65);
		} else {
			params.width = (int) (height * 0.5);
			params.height = (int) (width * 0.7);
		}

		dialog.getWindow().setAttributes(params);
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
					button_pwd_isvisible.setImageResource(R.drawable.fy_pwd);
					edit_pwd.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				} else {
					pwd_isvisiable = true;
					button_pwd_isvisible.setImageResource(R.drawable.fy_d_pwd);
					edit_pwd.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				}

			}
		});
		quickRegisiter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showNameRegisterDialog(paramContext);
				dialog.dismiss();
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
		final ImageButton button_pwd_isvisible = (ImageButton) localView
				.findViewById(R.id.pwd_isvisiable_nameregister);
		Button login = (Button) localView
				.findViewById(R.id.register_nameregister);
		CheckBox checkBox = (CheckBox) localView
				.findViewById(R.id.checkBox_nameregister);
		TextView findbackpwd = (TextView) localView
				.findViewById(R.id.haveAcount_nameregister);

		// 每次改变自动登录的选中状态，系统都会记录
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
					// 不显示密码
					button_pwd_isvisible.setImageResource(R.drawable.fy_pwd);
					edit_pwd.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				} else {
					pwd_isvisiable = true;
					button_pwd_isvisible.setImageResource(R.drawable.fy_d_pwd);
					edit_pwd.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				}
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

				GameSDK.saveInfo("name", name);
				GameSDK.saveInfo("pwd", pwd);
				NameRegLogin nameRegister = new NameRegLogin();
				nameRegister.nameRegister("1", name, "1", pwd, handler);
				loading = showLoadingDialog(paramContext, "正在登录");
			}
		});
		findbackpwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static ProgressDialog showLoadingDialog(Context context, String mes) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("提示：");
		progressDialog.setMessage(mes);
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
		return progressDialog;
	}
}
