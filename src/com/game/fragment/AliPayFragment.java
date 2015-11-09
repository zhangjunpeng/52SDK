package com.game.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.gamesdk.ALiActivity;
import com.game.gamesdk.R;
import com.game.gamesdk.UserInfo;
import com.game.paysdk.MyXQTPay;
import com.game.paysdk.PayCofing;
import com.game.paysdk.PaySDK;
import com.game.paysdk.TestActivity;
import com.game.sdkclass.PayChannel;
import com.game.tools.MyLog;

/**
 * Created by Administrator on 2015/9/17.
 */
public class AliPayFragment extends Fragment {
	private double money;

	int tag = 0;

	int position3 = -1;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				String data = msg.obj.toString();
				MyLog.i("ali~~~getorder==" + data);
				Intent intent1 = new Intent(getActivity(), ALiActivity.class);
				Bundle bundle1 = new Bundle();
				bundle1.putString("tag", "ali");
				bundle1.putString("data", data);
				intent1.putExtras(bundle1);
				startActivity(intent1);
				getActivity().finish();
				break;
			case 1:

				break;

			default:
				break;
			}
		}
	};
	public Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("getorderid返回：" + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if (errorCode.equals("200")) {
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						String orderid = jsonObject2.getString("order_id");
						PaySDK.SFTpay((Context) getActivity(), orderid, money
								+ "", PayCofing.list.get(position3));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			case 1:

				break;

			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_alipay, null);

		Bundle bundle = getArguments();
		String mes = bundle.getString("type");
		money = bundle.getDouble("paymoney");
		TextView textView1 = (TextView) view
				.findViewById(R.id.payname_table_fragment);
		textView1.setTextColor(Color.rgb(254, 146, 38));
		if ("alipay".equals(mes)) {
			textView1.setText("支付宝快捷支付");

			tag = 1;
		} else if ("weixin".equals(mes)) {
			textView1.setText("微信支付");

			tag = 2;

		} else if ("bank".equals(mes)) {

			tag = 3;
			textView1.setText("网银支付");
		}
		TextView money_text = (TextView) view
				.findViewById(R.id.paymoney_fragment);
		money_text.setText(money + " 元");

		// 显示用户名
		TextView name_text = (TextView) view
				.findViewById(R.id.username_fragment);

		name_text.setText(UserInfo.userName);

		view.findViewById(R.id.pay_fragment_ali).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						switch (tag) {
						case 1:// 支付宝
							int position2 = -1;
							for (int i = 0; i < PayCofing.list.size(); i++) {
								PayChannel payChannel = PayCofing.list.get(i);
								if (payChannel.getChannel_name()
										.equals("支付宝支付")) {
									position2 = i;
								}
							}
							MyXQTPay.XQTWXPay(PayCofing.orderid_cp, money + "",
									PayCofing.serverID, PayCofing.productName,
									PayCofing.productDes,
									PayCofing.list.get(position2), handler);
							break;
						case 2:// 微信
							Intent intent = new Intent(AliPayFragment.this
									.getActivity(), TestActivity.class);
							int position = -1;
							for (int i = 0; i < PayCofing.list.size(); i++) {
								PayChannel payChannel = PayCofing.list.get(i);
								if (payChannel.getChannel_name().equals("微信支付")) {
									position = i;
								}
							}
							Bundle bundle = new Bundle();
							bundle.putInt("position_list", position);
							bundle.putDouble("money", money);
							intent.putExtras(bundle);
							startActivity(intent);
							getActivity().finish();
							break;

						case 3:// 网银

							for (int i = 0; i < PayCofing.list.size(); i++) {
								PayChannel payChannel = PayCofing.list.get(i);
								if (payChannel.getChannel_name_en().equals(
										"bank")) {
									position3 = i;
								}
							}
							MyXQTPay.XQTWXPay(PayCofing.orderid_cp, money + "",
									PayCofing.serverID, PayCofing.productName,
									PayCofing.productDes,
									PayCofing.list.get(position3), handler2);

							break;
						}

					}

				});

		return view;
	}
}
