package com.game.wallet;

import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.game.gamesdk.GameSDK;
import com.game.gamesdk.UserInfo;
import com.game.http.GameHttpClient;
import com.game.paysdk.PaySDK;
import com.game.paysdk.SFTPayConfig;
import com.game.sdkclass.PayChannel;
import com.game.tools.MD5Test;
import com.game.tools.MyLog;
import com.shengpay.smc.HybridClientActivity;
import com.shengpay.smc.enums.Stage;
import com.shengpay.smc.vo.OrderInfo;

public class WalletPay {

	final static String getWalletOrder = "http://m.52game.com/sdkpay/genWalletOrder";

	public static void getOrderIdPayToWallet(String money, String card_no,
			String card_key, PayChannel payChannel, Handler handler) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair("pay_location",
				"1");
		NameValuePair nameValuePair2 = new BasicNameValuePair("user_id",
				UserInfo.userID);
		NameValuePair nameValuePair3 = new BasicNameValuePair("pay_amount",
				money);
		NameValuePair nameValuePair4 = new BasicNameValuePair("platform_money",
				money);
		NameValuePair nameValuePair5 = new BasicNameValuePair("channel_id",
				payChannel.getId());
		NameValuePair nameValuePair6 = new BasicNameValuePair("card_no",
				card_no);
		NameValuePair nameValuePair7 = new BasicNameValuePair("card_key",
				card_key);

		Date date = new Date();
		long time = date.getTime();
		NameValuePair nameValuePair10 = new BasicNameValuePair("time", time
				+ "");
		String unsign = "pay_location=1&user_id=" + UserInfo.userID
				+ "&pay_amount=" + money + "&channel_id=" + payChannel.getId()
				+ "&platform_money=" + money + "&time=" + time + "|"
				+ GameSDK.Key;
		String sign = MD5Test.getMD5(unsign);
		NameValuePair nameValuePair11 = new BasicNameValuePair("sign", sign);

		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);
		nameValuePairs.add(nameValuePair6);
		nameValuePairs.add(nameValuePair7);

		nameValuePairs.add(nameValuePair10);
		nameValuePairs.add(nameValuePair11);

		MyLog.i("getWalletorderID~~namevalue===" + nameValuePairs.toString());
		GameHttpClient gameHttpClient = new GameHttpClient(handler);
		gameHttpClient.startClient(getWalletOrder, nameValuePairs);

	}

	public static void SFTPay(Context context, String orderNo, String money,
			PayChannel payChannel) {
		OrderInfo info = new OrderInfo();
		info.setSenderId(SFTPayConfig.senderId);
		info.setOrderNo(orderNo);
		info.setOrderAmount(money);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		Date date = new Date();
		String time = simpleDateFormat.format(date);
		info.setOrderTime(time);
		info.setPageUrl(payChannel.getReturn_pay_url());
		info.setNotifyUrl(payChannel.getNotify_wallet_url());

		String ip = "";
		try {
			ip = PaySDK.getlocalIp(context);
			if (TextUtils.isEmpty(ip)) {
				ip = "127.0.0.1";
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		info.setBuyerIp(ip);
		info.setSignType("MD5");
		info.setSenderKey(SFTPayConfig.senderKey);
		info.setSignFromClient(true);

		JSONObject orderJson = info.getOrderInfoJson();

		String orderInfo = orderJson.toString();

		// TODO 调用MAS收单 or 支付
		Log.d("ZJP", orderInfo);
		Intent intent = new Intent();
		intent.putExtra("orderInfo", orderInfo);
		intent.putExtra("stage", Stage.PROD.toString());// stage有两种模式(TEST走集测，Product走生产)
		intent.putExtra("isDebug", true);// isDebug是否打印日志(true是调试模式，false非调试模式（默认）)
		intent.setClass(context, HybridClientActivity.class);
		((Activity) context).startActivityForResult(intent,
				HybridClientActivity.SMC_REQUEST_CODE);
	}
}
