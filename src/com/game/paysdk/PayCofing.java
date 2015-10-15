package com.game.paysdk;

import java.util.ArrayList;

public class PayCofing {
	// 获取充值渠道参数
	public static String payChannelUrl = "http://m.52game.com/sdkpay/paychannel";
	public static ArrayList<PayChannel> list;

	// 支付参数名称
	public static String getOrderIdUrl = "http://m.52game.com/sdkpay/genorder";
	public static String serverID = "1";

	// 卡类充值
	public static String cardPayUrl = "http://www.zhifuka.net/gateway/zfgateway.asp";

	public static String channelPayRate = "";
	public static String productName = "";
	public static String productDes = "";

	// 卡类参数名称
	public static String cardNo = "Card_no";
	public static String cardKey = "Card_key";

	public static int[] zfk = { 10, 30, 50, 53, 100, 105, 200, 210 };
	public static int[] szk = { 10, 20, 30, 50, 100, 300, 500 };
	public static int[] qqb = { 5, 10, 15, 30, 60, 100 };
	public static int[] sdk = { 5, 10, 15, 25, 30, 35, 45, 50, 100, 300, 350,
			1000 };
	public static int[] ztk = { 10, 15, 20, 25, 30, 50, 60, 100, 300, 468 };
	public static int[] shk = { 5, 10, 15, 30, 40, 100 };
	public static int[] jyk = { 5, 10, 15, 20, 30, 50, 100, 500, 1000 };
	public static int[] jwk = { 5, 6, 10, 15, 20, 30, 50, 100, 500, 1000 };
	public static int[] wmk = { 15, 30, 50, 100 };
	public static int[] ltk = { 20, 30, 50, 100, 300, 500 };
	public static int[] wyk = { 5, 10, 15, 20, 30, 50 };
	public static int[] thk = { 5, 10, 15, 30, 50, 100 };
	public static int[] txk = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
	public static int[] gyk = { 5, 10, 20, 30, 50, 100 };
	public static int[] zyk = { 10, 15, 30, 50, 100 };
	public static int[] dxk = { 50, 100 };
	public static int[] syk = { 10, 30, 50, 100 };
	public static String[] cardName = { "zfk", "szk", "qqb", "sdk", "ztk",
			"shk", "jyk", "jwk", "wmk", "ltk", "wyk", "thk", "txk", "gyk",
			"zyk", "dxk", "syk" };
	public static int[][] cardnum = { { 10, 30, 50, 53, 100, 105, 200, 210 },
			{ 10, 20, 30, 50, 100, 300, 500 }, { 5, 10, 15, 30, 60, 100 },
			{ 5, 10, 15, 25, 30, 35, 45, 50, 100, 300, 350, 1000 },
			{ 10, 15, 20, 25, 30, 50, 60, 100, 300, 468 },
			{ 5, 10, 15, 30, 40, 100 },
			{ 5, 10, 15, 20, 30, 50, 100, 500, 1000 },
			{ 5, 6, 10, 15, 20, 30, 50, 100, 500, 1000 }, { 15, 30, 50, 100 },
			{ 20, 30, 50, 100, 300, 500 }, { 5, 10, 15, 20, 30, 50 },
			{ 5, 10, 15, 30, 50, 100 },
			{ 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 },
			{ 5, 10, 20, 30, 50, 100 }, { 10, 15, 30, 50, 100 }, { 50, 100 },
			{ 10, 30, 50, 100 } };

}
