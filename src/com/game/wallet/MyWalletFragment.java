package com.game.wallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.game.fragment.BindPhoneNumFragment;
import com.game.fragment.UserInfoFragment;
import com.game.gamesdk.GameSDK;
import com.game.gamesdk.R;
import com.game.gamesdk.UserInfo;
import com.game.http.GameHttpClient;
import com.game.paysdk.PayActivity;
import com.game.paysdk.PayCofing;
import com.game.tools.MD5Test;
import com.game.tools.MyLog;

public class MyWalletFragment extends Fragment {

	private Button back;
	private Button pay;
	private TextView textView_wallet;
	private TextView textView_balance;
	ProgressDialog progressDialog;

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
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("提示：");
						builder.setMessage("请绑定手机号");

						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}

								});
						builder.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										getFragmentManager()
												.beginTransaction()
												.replace(
														R.id.container_userinfo,
														new BindPhoneNumFragment())
												.commit();
									}
								});

						builder.show();
					} else if ("7004".equals(errorCode)) {
						// 已绑定
						Intent intent = new Intent(getActivity(),
								PayActivity.class);
						intent.putExtra("tag", "wallet");
						startActivity(intent);
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
				progressDialog.dismiss();
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
						textView_wallet.setText(platform_money + " 元");
						textView_balance.setText(balance + " 点");
					} else if ("8003".equals(errorCode)) {
						textView_wallet.setText("0  元");
						textView_balance.setText("0 点");
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
		View view = inflater.inflate(R.layout.fragment_mywallet, null);
		back = (Button) view.findViewById(R.id.back_frag_mywallet);
		pay = (Button) view.findViewById(R.id.pay_mywallet_fg);
		textView_wallet = (TextView) view.findViewById(R.id.money_mywallet);
		textView_balance = (TextView) view.findViewById(R.id.balance_mywallet);
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("正在加载");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		checkWallet();
		initView();

		return view;
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

	private void initView() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.container_userinfo,
								new UserInfoFragment()).commit();
			}
		});
		pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Executors.newSingleThreadExecutor().execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						NameValuePair nameValuePair1 = new BasicNameValuePair(
								"type", "phone");
						NameValuePair nameValuePair2 = new BasicNameValuePair(
								"user_id", UserInfo.userID);
						nameValuePairs.add(nameValuePair1);
						nameValuePairs.add(nameValuePair2);
						GameHttpClient gameHttpClient = new GameHttpClient(
								handler);
						gameHttpClient.startClient(PayCofing.checkBind,
								nameValuePairs);

					}
				});

			}
		});

	}
}
