package com.game.paysdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.game.callback.GamePayCallback;
import com.game.gamesdk.GameSDK;

public class PaySDK {
	public static GamePayCallback mcallback;

	public static void startPay(Context context, GamePayCallback callback,
			double money, String serverID, String productName, String productDes) {

		if (!GameSDK.isLogin) {
			Toast.makeText(context, "请重新登录。", Toast.LENGTH_LONG).show();
			return;
		}
		// money 付款的总额，单位元
		mcallback = callback;
		PayCofing.productName = productName;
		PayCofing.productDes = productDes;
		PayCofing.serverID = serverID;

		Intent intent = new Intent(context, PayActivity.class);

		Bundle bundle = new Bundle();
		bundle.putDouble("paymoney", money);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

}
