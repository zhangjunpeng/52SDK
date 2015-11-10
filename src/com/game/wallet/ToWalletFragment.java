package com.game.wallet;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.game.gamesdk.R;
import com.game.paysdk.PayActivity;
import com.game.paysdk.PayCofing;
import com.game.sdkclass.PayChannel;
import com.game.tools.MyLog;

public class ToWalletFragment extends Fragment {
	private int buton_id[] = { R.id.walletpay10, R.id.walletpay30,
			R.id.walletpay50, R.id.walletpay100, R.id.walletpay300,
			R.id.walletpay500, R.id.walletpay1000, R.id.walletpay2000 };
	private int[] money_selected = { 10, 30, 50, 100, 300, 500, 1000, 2000 };

	private Button confirm_towallet;
	private EditText editText_money;
	private int money;
	private List<Button> buttons;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_walletpay, null);

		editText_money = (EditText) view.findViewById(R.id.editText_towfg);
		buttons = new ArrayList<Button>();

		for (int i = 0; i < buton_id.length; i++) {
			Button button = (Button) view.findViewById(buton_id[i]);

			buttons.add(button);
		}
		confirm_towallet = (Button) view
				.findViewById(R.id.button_confirm_mywfg);
		initView();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		for (int i = 0; i < buttons.size(); i++) {
			final int position = i;
			buttons.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					initButtonbg();
					v.setBackgroundResource(R.drawable.button_framework_orange);
					money = money_selected[position];
					editText_money.setText(money + "");
				}
			});
		}
		confirm_towallet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mon = editText_money.getText().toString();
				money = Integer.parseInt(mon);
				if (PayActivity.selected_po != -1 && money > 0) {
					PayChannel payChannel = PayCofing.list
							.get(PayActivity.selected_po);

					if (payChannel.getChannel_name_en().equals("weixin")) {
						MyLog.i("支付" + payChannel.getChannel_name());
						comitFragment("weixin", money);
					} else if (payChannel.getChannel_name_en().equals("alipay")) {

						comitFragment("alipay", money);
					} else if (payChannel.getChannel_name_en().equals("bank")) {
						MyLog.i("支付" + payChannel.getChannel_name());
						comitFragment("bank", money);
					} else if (payChannel.getChannel_name_en().equals("wallet")) {

					} else {
						MyLog.i("支付" + payChannel.getChannel_name());
						comitCardFragment(PayActivity.selected_po + "", money);
					}
				}
				if (PayActivity.selected_po == -1) {
					Toast.makeText(getActivity(), "请选择左边支付方式",
							Toast.LENGTH_SHORT).show();
				}
				if (money <= 0 || money > 100000) {
					Toast.makeText(getActivity(), "请输入1-100000的整数",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	public void comitFragment(String tag, double money2) {

		ConfirmAliTWFragment aliPayFragment = new ConfirmAliTWFragment();
		Bundle bundle = new Bundle();
		bundle.putString("type", tag);
		bundle.putDouble("paymoney", money2);

		aliPayFragment.setArguments(bundle);
		// fragmentManager.beginTransaction().add(R.id.contioner_pay,aliPayFragment).commit();
		getFragmentManager().beginTransaction()
				.replace(R.id.contioner_pay, aliPayFragment).commit();

	}

	public void comitCardFragment(String tag, double money2) {
		Fragment fragment = new ConfirmCardTWFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		bundle.putDouble("paymoney", money2);
		fragment.setArguments(bundle);
		getFragmentManager().beginTransaction()
				.replace(R.id.contioner_pay, fragment).commit();

	}

	private void initButtonbg() {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setBackgroundResource(
					R.drawable.button_framework_gra);
		}
	}
}
