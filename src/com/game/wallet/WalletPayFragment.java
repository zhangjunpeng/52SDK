package com.game.wallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.gamesdk.R;

public class WalletPayFragment extends Fragment {
	private int buton_id[] = { R.id.walletpay10, R.id.walletpay30,
			R.id.walletpay50, R.id.walletpay100, R.id.walletpay300,
			R.id.walletpay500, R.id.walletpay1000, R.id.walletpay2000 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_walletpay, null);

		return view;
	}

}
