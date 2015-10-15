package com.game.paysdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.gamesdk.R;
import com.game.gamesdk.UserInfo;

/**
 * Created by Administrator on 2015/9/17.
 */
public class AliPayFragment extends Fragment {
	private double money;
	boolean isAli;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_alipay, null);

		Bundle bundle = getArguments();
		String mes = bundle.getString("type");
		money = bundle.getDouble("paymoney");
		TextView textView1 = (TextView) view
				.findViewById(R.id.payname_table_fragment);
		if ("alipay".equals(mes)) {
			textView1.setText("支付宝快捷支付");
			isAli = true;
		} else {
			textView1.setText("微信支付");
			isAli = false;

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
