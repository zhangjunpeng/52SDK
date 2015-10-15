package com.game.paysdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;
import android.util.Log;

import com.game.gamesdk.GameSDK;
import com.game.gamesdk.UserInfo;
import com.game.http.GameHttpClient;
import com.game.tools.MD5Test;

public class MyXQTPay {

	public static final String consumerId = "154093";
	public static final String WXPAYkey = "cbc96c344c325b7791f2a37ef49e7087";

	public static String _money = "";
	public static PayChannel payChannel_weixin;

	public static void XQTWXPay(final String money, final String serverId,
			final String productName, final String productDes,
			final PayChannel payChannel, final Handler handler) {

		ExecutorService singleThread = Executors.newSingleThreadExecutor();
		singleThread.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getOrderId(money, Double.parseDouble(money) + "", serverId,
						productName, productDes, payChannel, handler);
			}
		});

	}

	private static void getOrderId(String money, String gameMoney,
			String serverID, String productName, String productDes,
			PayChannel payChannel, Handler handler) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair("appid",
				GameSDK.AppID);
		NameValuePair nameValuePair2 = new BasicNameValuePair("user_id",
				UserInfo.userID);
		NameValuePair nameValuePair3 = new BasicNameValuePair("pay_amount",
				money);
		NameValuePair nameValuePair4 = new BasicNameValuePair("game_amount",
				gameMoney);
		NameValuePair nameValuePair5 = new BasicNameValuePair("channel_id",
				payChannel.getId());
		NameValuePair nameValuePair6 = new BasicNameValuePair("server_id",
				serverID);
		NameValuePair nameValuePair7 = new BasicNameValuePair("game_pay_url",
				"");
		NameValuePair nameValuePair8 = new BasicNameValuePair("goods_name",
				productName);
		NameValuePair nameValuePair9 = new BasicNameValuePair("goods_des",
				productDes);
		Date date = new Date();
		long time = date.getTime();
		NameValuePair nameValuePair10 = new BasicNameValuePair("time", time
				+ "");
		String unsign = "appid=" + GameSDK.AppID + "&user_id="
				+ UserInfo.userID + "&pay_amount=" + money + "&channel_id="
				+ payChannel.getId() + "&server_id=" + serverID + "&time="
				+ time + "|" + GameSDK.AppKey;
		String sign = MD5Test.getMD5(unsign);
		NameValuePair nameValuePair11 = new BasicNameValuePair("sign", sign);

		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);
		nameValuePairs.add(nameValuePair6);
		nameValuePairs.add(nameValuePair7);
		nameValuePairs.add(nameValuePair8);
		nameValuePairs.add(nameValuePair9);
		nameValuePairs.add(nameValuePair10);
		nameValuePairs.add(nameValuePair11);
		Log.i("namevalue", nameValuePairs.toString());
		GameHttpClient gameHttpClient = new GameHttpClient(handler);
		gameHttpClient.startClient(PayCofing.getOrderIdUrl, nameValuePairs);

	}

	// // 通过md5摘要算法，得出sign值同
	// private static String Sign() {
	//
	// String str = "customerid=" + XqtPay.consumerId + "&sdcustomno="
	// + XqtPay.mhtOrderNo + "&orderAmount=" + XqtPay.mhtOrderAmt
	// + key;
	// return getMD5(str).toUpperCase();
	// }
	//
	// public static String getMD5(String content) {
	// try {
	// MessageDigest digest = MessageDigest.getInstance("MD5");
	// digest.update(content.getBytes());
	// return getHashString(digest);
	//
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// private static String getHashString(MessageDigest digest) {
	// StringBuilder builder = new StringBuilder();
	// for (byte b : digest.digest()) {
	// builder.append(Integer.toHexString((b >> 4) & 0xf));
	// builder.append(Integer.toHexString(b & 0xf));
	// }
	// return builder.toString();
	// }

	public static void cardPayGetOrder(final String money,
			final String serverId, final String productName,
			final String productDes, final PayChannel payChannel,
			String card_no, String card_key, final Handler handler) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair("appid",
				GameSDK.AppID);
		NameValuePair nameValuePair2 = new BasicNameValuePair("user_id",
				UserInfo.userID);
		NameValuePair nameValuePair3 = new BasicNameValuePair("pay_amount",
				money);
		NameValuePair nameValuePair4 = new BasicNameValuePair("game_amount",
				money);
		NameValuePair nameValuePair5 = new BasicNameValuePair("channel_id",
				payChannel.getId());
		NameValuePair nameValuePair6 = new BasicNameValuePair("server_id",
				serverId);
		NameValuePair nameValuePair7 = new BasicNameValuePair("game_pay_url",
				"");
		NameValuePair nameValuePair8 = new BasicNameValuePair("goods_name",
				productName);
		NameValuePair nameValuePair9 = new BasicNameValuePair("goods_des",
				productDes);
		Date date = new Date();
		long time = date.getTime();
		NameValuePair nameValuePair10 = new BasicNameValuePair("time", time
				+ "");
		String unsign = "appid=" + GameSDK.AppID + "&user_id="
				+ UserInfo.userID + "&pay_amount=" + money + "&channel_id="
				+ payChannel.getId() + "&server_id=" + serverId + "&time="
				+ time + "|" + GameSDK.AppKey;
		String sign = MD5Test.getMD5(unsign);
		NameValuePair nameValuePair11 = new BasicNameValuePair("sign", sign);
		NameValuePair nameValuePair12 = new BasicNameValuePair("card_no",
				card_no);
		NameValuePair nameValuePair13 = new BasicNameValuePair("card_key",
				card_key);

		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);
		nameValuePairs.add(nameValuePair6);
		nameValuePairs.add(nameValuePair7);
		nameValuePairs.add(nameValuePair8);
		nameValuePairs.add(nameValuePair9);
		nameValuePairs.add(nameValuePair10);
		nameValuePairs.add(nameValuePair11);
		nameValuePairs.add(nameValuePair12);
		nameValuePairs.add(nameValuePair13);
		Log.i("namevalue", nameValuePairs.toString());
		GameHttpClient gameHttpClient = new GameHttpClient(handler);
		gameHttpClient.startClient(PayCofing.getOrderIdUrl, nameValuePairs);
	}

	public static void cardPay(String orderId, float money, String cardno,
			String faceno, String cardNum, String cardpwd, String noticeUrl,
			final Handler handler) {
		String customerid = consumerId;// 商户ID
		String sdcustomno = orderId;// 商户订单的流水号
		Float ordermoney = money;

		// String cardno ; 此次订单的支付方式
		// String faceno ; 此次订单的卡面值编号
		// String cardnum = "";// 商户系统提供的充值卡号
		// String cardpass = "";// 商户系统提交的充值卡密码
		// String noticeurl = "";// 商户接受51支付网关异步通知订单结果地址
		String remarks = "";// 商户备注信息
		String mark = "";// 商户自定义信息,不能包含中文
		String remoteip = "";// 用户在商户系统提交的订单的实际ip，可为空

		// Md5签名,把支付的部分参数与key连接在一起,支付订单请求例子为：customerid=100501&sdcustomno=20090101123036&noticeurl=http:/
		// yoursite/noticeurl.asp&mark=1&key=e10adc3949ba59abbe56e057f20f883e
		// 支付订单查询：customerid=100501&sdcustomno=20090101123036&mark=1&key=e10ad
		// c3949ba59abbe56e057f20f883e
		// 支付订单异步通知：customerid=100501&sd51no=51cps000000100001&sdcustomno=200901
		// 01123036& mark=1&key=e10adc3949ba59abbe56e057f20f883e
		String unsign = "customerid=" + customerid + "&sdcustomno="
				+ sdcustomno + "&noticeurl=" + noticeUrl + "&mark=" + mark
				+ "&key=" + WXPAYkey;
		String sign = MD5Test.getMD5(unsign).toUpperCase();

		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair1 = new BasicNameValuePair("customerid",
				customerid);
		NameValuePair nameValuePair2 = new BasicNameValuePair("sdcustomno",
				sdcustomno);
		NameValuePair nameValuePair3 = new BasicNameValuePair("ordermoney",
				ordermoney + "");
		NameValuePair nameValuePair4 = new BasicNameValuePair("cardno", cardno);
		NameValuePair nameValuePair5 = new BasicNameValuePair("faceno", faceno);
		NameValuePair nameValuePair6 = new BasicNameValuePair("cardnum",
				cardNum);
		NameValuePair nameValuePair7 = new BasicNameValuePair("cardpass",
				cardpwd);
		NameValuePair nameValuePair8 = new BasicNameValuePair("noticeurl",
				noticeUrl);
		NameValuePair nameValuePair9 = new BasicNameValuePair("remarks",
				remarks);
		NameValuePair nameValuePair10 = new BasicNameValuePair("mark", mark);
		NameValuePair nameValuePair11 = new BasicNameValuePair("sign", sign);
		nameValuePairs.add(nameValuePair1);
		nameValuePairs.add(nameValuePair2);
		nameValuePairs.add(nameValuePair3);
		nameValuePairs.add(nameValuePair4);
		nameValuePairs.add(nameValuePair5);
		nameValuePairs.add(nameValuePair6);
		nameValuePairs.add(nameValuePair7);
		nameValuePairs.add(nameValuePair8);
		nameValuePairs.add(nameValuePair9);
		nameValuePairs.add(nameValuePair10);
		nameValuePairs.add(nameValuePair11);

		// 线程池

		ExecutorService singleThread = Executors.newSingleThreadExecutor();
		singleThread.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GameHttpClient gameHttpClient = new GameHttpClient(handler);
				gameHttpClient
						.startClient(PayCofing.cardPayUrl, nameValuePairs);
			}
		});

	}
}
