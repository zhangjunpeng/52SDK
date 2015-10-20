package com.game.paysdk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.gamesdk.ALiActivity;
import com.game.gamesdk.R;
import com.game.gamesdk.UserInfo;

/**
 * Created by Administrator on 2015/9/17.
 */
public class AliPayFragment extends Fragment {
	private double money;
	boolean isAli;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Log.i("ZJP", "ali~~~getorder==" + msg.obj.toString());
				String data = msg.obj.toString();
				Intent intent1 = new Intent(getActivity(), ALiActivity.class);
				Bundle bundle1 = new Bundle();
				bundle1.putString("data", data);
				intent1.putExtras(bundle1);
				startActivity(intent1);
				break;
			case 1:

				Intent intent = new Intent(AliPayFragment.this.getActivity(),
						ALiActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("data", msg.obj.toString());
				intent.putExtras(bundle);
				startActivity(intent);

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
			isAli = true;
		} else if ("weixin".equals(mes)) {
			textView1.setText("微信支付");
			isAli = false;

		} else if ("bank".equals(mes)) {
			isAli = true;
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

						if (isAli) {
							// 支付宝快捷支付
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

						} else {
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

						}

					}

				});

		return view;
	}
}
