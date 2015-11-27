package com.game.wallet;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
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
import com.game.paysdk.PayActivity;
import com.game.paysdk.PayCofing;
import com.game.sdkclass.PayChannel;
import com.game.tools.MyLog;
import com.game.tools.ResourceUtil;

public class ToWalletFragment extends Fragment {

	Context mContext = GameSDK.mcontext;
	private int buton_id[] = { ResourceUtil.getId(mContext, "walletpay10"),
			ResourceUtil.getId(mContext, "walletpay30"),
			ResourceUtil.getId(mContext, "walletpay50"),
			ResourceUtil.getId(mContext, "walletpay100"),
			ResourceUtil.getId(mContext, "walletpay300"),
			ResourceUtil.getId(mContext, "walletpay500"),
			ResourceUtil.getId(mContext, "walletpay1000"),
			ResourceUtil.getId(mContext, "walletpay2000") };
	private int[] money_selected = { 10, 30, 50, 100, 300, 500, 1000, 2000 };

	private Button confirm_towallet;
	private EditText editText_money;
	private double money;
	private List<Button> buttons;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(
				ResourceUtil.getLayoutId(mContext, "fragment_walletpay"), null);

		editText_money = (EditText) view.findViewById(ResourceUtil.getId(
				mContext, "editText_towfg"));
		buttons = new ArrayList<Button>();

		for (int i = 0; i < buton_id.length; i++) {
			Button button = (Button) view.findViewById(buton_id[i]);

			buttons.add(button);
		}
		confirm_towallet = (Button) view.findViewById(ResourceUtil.getId(
				mContext, "button_confirm_mywfg"));
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
					v.setBackgroundResource(ResourceUtil.getDrawableId(
							mContext, "button_framework_orange"));

					editText_money.setText(money_selected[position] + "");
				}
			});
		}
		confirm_towallet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mon = editText_money.getText().toString();
				if (TextUtils.isEmpty(mon)) {
					Toast.makeText(getActivity(), "请选择或输入金额",
							Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					money = Double.parseDouble(mon);
				} catch (Exception e) {
					Toast.makeText(getActivity(), "输入金额错误", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (PayActivity.selected_po < 0) {
					Toast.makeText(getActivity(), "请选择左边支付方式",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (money <= 0 || money > 100000) {
					Toast.makeText(getActivity(), "请输入1-100000的整数",
							Toast.LENGTH_SHORT).show();
					return;
				}

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
		});
	}

	public void comitFragment(String tag, double money2) {

		ConfirmAliTWFragment aliPayFragment = new ConfirmAliTWFragment();
		Bundle bundle = new Bundle();
		bundle.putString("type", tag);
		bundle.putDouble("paymoney", money2);

		aliPayFragment.setArguments(bundle);
		// fragmentManager.beginTransaction().add(R.id.contioner_pay,aliPayFragment).commit();
		getFragmentManager()
				.beginTransaction()
				.replace(ResourceUtil.getId(mContext, "contioner_pay"),
						aliPayFragment).commit();

	}

	public void comitCardFragment(String tag, double money2) {
		Fragment fragment = new ConfirmCardTWFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		bundle.putDouble("paymoney", money2);
		fragment.setArguments(bundle);
		getFragmentManager()
				.beginTransaction()
				.replace(ResourceUtil.getId(mContext, "contioner_pay"),
						fragment).commit();

	}

	private void initButtonbg() {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setBackgroundResource(
					ResourceUtil
							.getDrawableId(mContext, "button_framework_gra"));
		}
	}
}
