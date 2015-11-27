package com.game.fragment;

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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.game.gamesdk.GameSDK;
import com.game.gamesdk.RegisterConfig;
import com.game.gamesdk.ShowDialog;
import com.game.gamesdk.UserInfo;
import com.game.http.GameHttpClient;
import com.game.tools.MD5Test;
import com.game.tools.MyLog;
import com.game.tools.ResourceUtil;
import com.game.wallet.MyWalletFragment;

public class BindPhoneNumFragment extends Fragment implements OnClickListener {

	private EditText editText_phone;
	private EditText editText_proving;
	private Button getProving;
	private Button bind;
	private Button back;
	String phonenum;
	String prove;
	private String token;
	String tag = "";

	Handler provHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("获取短信验证返回：" + data);
				// json格式转
				if (data.startsWith("\ufeff")) {
					data = data.substring(1);
				}
				try {
					JSONObject jsonObject = new JSONObject(data);

					String errorcode = jsonObject.getString("errorCode");
					MyLog.i("errorCode::" + errorcode);
					if (errorcode.equals("200")) {

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
			}

		}
	};
	Handler bindHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("绑定手机返回：" + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if ("200".equals(errorCode)) {
						Toast.makeText(getActivity(), "绑定手机成功",
								Toast.LENGTH_SHORT).show();

						// Intent intent = new Intent(getActivity(),
						// PayActivity.class);
						// intent.putExtra("tag", "wallet");
						// startActivity(intent);
						if ("walletpay".equals(tag)) {
							getActivity().setResult(Activity.RESULT_OK);
							getActivity().finish();
						} else if ("mywallet".equals(tag)) {
							getFragmentManager()
									.beginTransaction()
									.replace(
											ResourceUtil.getId(getActivity(),
													"container_userinfo"),
											new MyWalletFragment()).commit();
						}

					} else if ("9006".equals(errorCode)) {
						Toast.makeText(getActivity(), "验证码错误",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "验证码错误",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(ResourceUtil.getLayoutId(getActivity(),
				"fragment_bindphonenum"), null);
		editText_phone = (EditText) view.findViewById(ResourceUtil.getId(
				getActivity(), "edit_phonenum_bindphone"));
		editText_proving = (EditText) view.findViewById(ResourceUtil.getId(
				getActivity(), "edit_proving_bindphone"));
		getProving = (Button) view.findViewById(ResourceUtil.getId(
				getActivity(), "button_getproving_bindphone"));
		bind = (Button) view.findViewById(ResourceUtil.getId(getActivity(),
				"button_bind_bindphone"));
		back = (Button) view.findViewById(ResourceUtil.getId(getActivity(),
				"back_frag_bindphone"));
		tag = getArguments().getString("tag", "tag");
		initView();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		editText_phone.setOnClickListener(this);
		editText_proving.setOnClickListener(this);
		getProving.setOnClickListener(this);
		bind.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		phonenum = editText_phone.getEditableText().toString();
		prove = editText_proving.getEditableText().toString();
		if (v == getProving) {
			if (TextUtils.isEmpty(phonenum)) {
				Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (phonenum.length() != 11) {
				Toast.makeText(getActivity(), "请输入正确格式手机", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Executors.newSingleThreadExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					NameValuePair nameValuePair1 = new BasicNameValuePair(
							"phone", phonenum);
					long time = new Date().getTime();
					NameValuePair nameValuePair2 = new BasicNameValuePair(
							"time", time + "");
					String unsign = "phone=" + phonenum + "&time=" + time + "|"
							+ GameSDK.Key;

					String sign = MD5Test.getMD5(unsign);
					NameValuePair nameValuePair3 = new BasicNameValuePair(
							"sign", sign);
					nameValuePairs.add(nameValuePair1);
					nameValuePairs.add(nameValuePair2);
					nameValuePairs.add(nameValuePair3);
					MyLog.i("getKey参数==" + nameValuePairs);
					GameHttpClient gameHttpClient = new GameHttpClient(
							provHandler);
					gameHttpClient.startClient(RegisterConfig.smsUrl,
							nameValuePairs);
				}
			});
			ShowDialog.setButton(getProving);
		} else if (v == bind) {
			if (TextUtils.isEmpty(phonenum)) {
				Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (phonenum.length() != 11) {
				Toast.makeText(getActivity(), "请输入正确格式手机", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (TextUtils.isEmpty(prove)) {
				Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Executors.newSingleThreadExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					NameValuePair nameValuePair1 = new BasicNameValuePair(
							"token", token);
					NameValuePair nameValuePair2 = new BasicNameValuePair(
							"user_id", UserInfo.userID);
					NameValuePair nameValuePair3 = new BasicNameValuePair(
							"mobile_code", prove);
					NameValuePair nameValuePair4 = new BasicNameValuePair(
							"phone", phonenum);
					String unsign = "user_id=" + UserInfo.userID
							+ "&mobile_code=" + prove + "&phone=" + phonenum
							+ "|" + GameSDK.Key;

					String sign = MD5Test.getMD5(unsign);
					NameValuePair nameValuePair5 = new BasicNameValuePair(
							"sign", sign);
					nameValuePairs.add(nameValuePair1);
					nameValuePairs.add(nameValuePair2);
					nameValuePairs.add(nameValuePair3);
					nameValuePairs.add(nameValuePair4);
					nameValuePairs.add(nameValuePair5);
					MyLog.i("绑定手机参数：：：" + nameValuePairs.toString());
					GameHttpClient gameHttpClient = new GameHttpClient(
							bindHandler);
					gameHttpClient.startClient(RegisterConfig.bindPhone,
							nameValuePairs);
				}
			});

		} else if (v == back) {
			getFragmentManager()
					.beginTransaction()
					.replace(
							ResourceUtil.getId(getActivity(),
									"container_userinfo"),
							new MyWalletFragment()).commit();
		}
	}
}
