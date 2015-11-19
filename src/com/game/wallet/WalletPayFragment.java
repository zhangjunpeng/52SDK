package com.game.wallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.gamesdk.GameSDK;
import com.game.gamesdk.R;
import com.game.gamesdk.RegisterConfig;
import com.game.gamesdk.ShowDialog;
import com.game.gamesdk.UserInfo;
import com.game.gamesdk.UserInfoActivity;
import com.game.http.GameHttpClient;
import com.game.paysdk.PayCofing;
import com.game.tools.MD5Test;
import com.game.tools.MyLog;

public class WalletPayFragment extends Fragment {
	private TextView username_wt_fragment;
	private TextView paymoney_wt_fragment;
	private TextView mywallet_wt_fragment;
	private TextView payname_wt_fg;

	private Button pay_button;
	private LinearLayout linearLayout;
	private TextView phonenum_textView;
	private EditText proving_edit;
	private Button getproving;
	boolean isBind = false;

	double money = 0;
	double yue = 0;
	String phonenum;
	String token;
	String mobile_code;

	ProgressDialog progressDialog;
	Handler provHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("获取短信验证返回：" + data);
				progressDialog.dismiss();
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

			default:
				break;
			}

		}
	};

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				MyLog.i(msg.obj.toString() + "");
				String data = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if ("7005".equals(errorCode)) {
						// 未绑定
						mywallet_wt_fragment.setText("该账号未开通钱包功能");

						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						isBind = false;
						setKTButton();
					} else if ("7004".equals(errorCode)) {
						// 已绑定
						isBind = true;
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						phonenum = jsonObject2.getString("phone");
						checkWallet();

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
	Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("获取平台币返回：" + data);

				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if ("200".equals(errorCode)) {
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						String platform_money = jsonObject2
								.getString("platform_money");
						String balance = jsonObject2.getString("balance");
						// double money = Double.parseDouble(platform_money);
						mywallet_wt_fragment.setText(platform_money + " 元");
						yue = Double.parseDouble(balance);
					} else if ("8003".equals(errorCode)) {
						yue = 0;
					}
					setPayButton();
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
	Handler payHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				progressDialog.dismiss();
				String data = msg.obj.toString();
				MyLog.i("钱包支付返回：===" + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if ("200".equals(errorCode)) {
						Toast.makeText(getActivity(), "支付成功",
								Toast.LENGTH_SHORT).show();
						getActivity().finish();

					} else if ("1211".equals(errorCode)) {
						Toast.makeText(getActivity(), "验证码不正确",
								Toast.LENGTH_SHORT).show();

					} else if ("1212".equals(errorCode)) {
						Toast.makeText(getActivity(), "钱包余额不足，请选择其他支付方式或充值钱包",
								Toast.LENGTH_SHORT).show();
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
		String mon = getArguments().getString("money", "");
		if (!TextUtils.isEmpty(mon)) {
			money = Double.parseDouble(mon);
		}
		View view = inflater.inflate(R.layout.fragment_wallettopay, null);
		username_wt_fragment = (TextView) view
				.findViewById(R.id.username_wt_fragment);
		paymoney_wt_fragment = (TextView) view
				.findViewById(R.id.paymoney_wt_fragment);
		mywallet_wt_fragment = (TextView) view
				.findViewById(R.id.mywallet_wt_fragment);
		payname_wt_fg = (TextView) view.findViewById(R.id.payname_wt_fg);

		linearLayout = (LinearLayout) view
				.findViewById(R.id.yanzheng_linearlayout_wtfg);
		phonenum_textView = (TextView) view.findViewById(R.id.phonenum_wtfg);
		proving_edit = (EditText) view.findViewById(R.id.edit_proving);
		getproving = (Button) view.findViewById(R.id.getproving_wtfg);

		pay_button = (Button) view.findViewById(R.id.pay_wt_fg);

		initView();
		getIsbind();
		return view;
	}

	protected void setPayButton() {
		// TODO Auto-generated method stub

		linearLayout.setVisibility(View.VISIBLE);
		MyLog.i("phonenum=====" + phonenum);
		phonenum_textView.setText(phonenum);
		getproving.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getProving();
				ShowDialog.setButton(getproving);
			}
		});

	}

	private void getProving() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				NameValuePair nameValuePair1 = new BasicNameValuePair("phone",
						phonenum);
				long time = new Date().getTime();
				NameValuePair nameValuePair2 = new BasicNameValuePair("time",
						time + "");
				String unsign = "phone=" + phonenum + "&time=" + time + "|"
						+ GameSDK.Key;

				String sign = MD5Test.getMD5(unsign);
				NameValuePair nameValuePair3 = new BasicNameValuePair(
						"user_id", UserInfo.userID);
				NameValuePair nameValuePair4 = new BasicNameValuePair("sign",
						sign);
				nameValuePairs.add(nameValuePair1);
				nameValuePairs.add(nameValuePair2);
				nameValuePairs.add(nameValuePair3);
				nameValuePairs.add(nameValuePair4);
				GameHttpClient gameHttpClient = new GameHttpClient(provHandler);
				gameHttpClient.startClient(RegisterConfig.smsUrl,
						nameValuePairs);
			}
		});
	}

	protected void setKTButton() {
		// TODO Auto-generated method stub
		pay_button.setText("开通钱包");

		linearLayout.setVisibility(View.GONE);
	}

	private void getIsbind() {
		// TODO Auto-generated method stub
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("获取钱包信息。。");
		progressDialog.show();

		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				NameValuePair nameValuePair1 = new BasicNameValuePair("type",
						"phone");
				NameValuePair nameValuePair2 = new BasicNameValuePair(
						"user_id", UserInfo.userID);
				nameValuePairs.add(nameValuePair1);
				nameValuePairs.add(nameValuePair2);
				GameHttpClient gameHttpClient = new GameHttpClient(handler);
				gameHttpClient.startClient(PayCofing.checkBind, nameValuePairs);

			}
		});
	}

	private void checkWallet() {
		// TODO Auto-generated method stub
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				NameValuePair nameValuePair1 = new BasicNameValuePair(
						"user_id", UserInfo.userID);
				long time = new Date().getTime();
				NameValuePair nameValuePair2 = new BasicNameValuePair("time",
						time + "");
				String unsign = "user_id=" + UserInfo.userID + "&time=" + time
						+ "|" + GameSDK.Key;
				MyLog.i("unsign:::" + unsign);
				String sign = MD5Test.getMD5(unsign);
				NameValuePair nameValuePair3 = new BasicNameValuePair("sign",
						sign);
				nameValuePairs.add(nameValuePair1);
				nameValuePairs.add(nameValuePair2);
				nameValuePairs.add(nameValuePair3);
				MyLog.i("获取平台币：" + nameValuePairs.toString());
				GameHttpClient gameHttpClient = new GameHttpClient(handler2);
				gameHttpClient.startClient(PayCofing.wallet, nameValuePairs);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		MyLog.i("绑定手机后执行1");
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			MyLog.i("绑定手机后执行2");
			WalletPayFragment walletPayFragment = new WalletPayFragment();
			Bundle bundle = new Bundle();
			bundle.putString("money", money + "");
			walletPayFragment.setArguments(bundle);
			getFragmentManager().beginTransaction()
					.replace(R.id.contioner_pay, walletPayFragment).commit();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		username_wt_fragment.setText(UserInfo.userName);
		paymoney_wt_fragment.setText(money + "");
		payname_wt_fg.setText("钱包余额支付");
		pay_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isBind) {
					Intent intent = new Intent(getActivity(),
							UserInfoActivity.class);
					intent.putExtra("tag", 4);
					startActivityForResult(intent, Activity.RESULT_FIRST_USER);
					// getActivity().finish();
				} else {
					// 钱包支付

					mobile_code = proving_edit.getText().toString();
					if (money > yue) {
						Toast.makeText(getActivity(), "钱包余额不足，请选择其他支付方式或充值钱包",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (TextUtils.isEmpty(mobile_code)) {
						Toast.makeText(getActivity(), "请输入验证码",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (TextUtils.isEmpty(token)) {
						Toast.makeText(getActivity(), "验证码不正确",
								Toast.LENGTH_SHORT).show();
						return;
					}

					Executors.newSingleThreadExecutor().execute(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							WalletPay.walletGamePay(money, token, mobile_code,
									payHandler);
						}
					});

					progressDialog.setMessage("正在支付。");
					progressDialog.show();

				}
			}

		});
	}
}
