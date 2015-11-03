package com.game.paysdk;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.game.callback.GamePayCallback;
import com.game.gamesdk.GameSDK;
import com.game.sdkclass.PayChannel;
import com.shengpay.smc.HybridClientActivity;
import com.shengpay.smc.enums.Stage;
import com.shengpay.smc.vo.OrderInfo;

public class PaySDK {
	public static GamePayCallback mcallback;

	public static void startPay(Context context, String orderid_cp,
			GamePayCallback callback, double money, String serverID,
			String productName, String productDes) {

		if (!GameSDK.isLogin) {
			Toast.makeText(context, "请重新登录。", Toast.LENGTH_LONG).show();
			return;
		}
		// money 付款的总额，单位元
		mcallback = callback;
		PayCofing.productName = productName;
		PayCofing.productDes = productDes;
		PayCofing.serverID = serverID;
		PayCofing.orderid_cp = orderid_cp;

		Intent intent = new Intent(context, PayActivity.class);

		Bundle bundle = new Bundle();
		bundle.putDouble("paymoney", money);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static void SFTpay(Context context, String orderNo, String money,
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
		info.setNotifyUrl(payChannel.getNotify_pay_url());

		String ip = "";
		try {
			ip = getlocalIp(context);
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

	public static String getlocalIp(Context context) throws SocketException {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface networkInterface = en.nextElement();
				for (Enumeration<InetAddress> enAddress = networkInterface
						.getInetAddresses(); enAddress.hasMoreElements();) {
					InetAddress inetAddress = enAddress.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);

		return ip;
	}

	public static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

}
