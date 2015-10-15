package com.game.paysdk;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.game.gamesdk.R;
import com.game.tools.MD5Test;
import com.game.tools.StringTools;
import com.ipaynow.plugin.api.IpaynowPlugin;
import com.xqt.now.paysdk.XqtPay;
import com.xqt.now.paysdk.XqtPay.XqtPayListener;

public class TestActivity extends Activity {

	public static TestActivity instance;

	public ProgressDialog progressDialog;
	int position;
	static PayChannel payChannel;
	static int num = 0;
	static double money;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String data = StringTools.decodeUnicode(msg.obj.toString());
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject jsonObject2 = jsonObject.getJSONObject("data");
					String orderID = jsonObject2.getString("order_id");
					if (data.contains("200")) {
						prePayMessage();
						// 支付订单号
						XqtPay.mhtOrderNo = orderID;
						Log.i("orderID", orderID);
						// 支付类型，13为微信支付
						XqtPay.payChannelType = "13";
						XqtPay.sign = MD5Test.getMD5(
								"customerid=" + XqtPay.consumerId
										+ "&sdcustomno=" + XqtPay.mhtOrderNo
										+ "&orderAmount=" + XqtPay.mhtOrderAmt
										+ MyXQTPay.WXPAYkey).toUpperCase();

						Log.i("ZJP", "获取微信支付参数");
						XqtPay.Transit(TestActivity.this, new XqtPayListener() {

							@Override
							public void success(String arg0) {
								// TODO Auto-generated method stub
								progressDialog.dismiss();
								Log.i("ZJP", "开启微信支付");
								IpaynowPlugin.pay(TestActivity.this, arg0);
							}

							@Override
							public void error(String arg0) {
								// TODO Auto-generated method stub
								Log.i("ZJP", arg0);
							}
						});
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.i("ZJP", data);

				break;

			default:
				break;
			}
		}
	};

	// 设置支付参数
	private static void prePayMessage() {
		XqtPay.consumerId = MyXQTPay.consumerId;
		XqtPay.mhtOrderName = PayCofing.productName;
		XqtPay.mhtOrderAmt = (money * 100) + "";
		XqtPay.mhtOrderDetail = PayCofing.productDes;
		XqtPay.notifyUrl = payChannel.getNotify_pay_url();
		XqtPay.superid = "100000";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_test);

		instance = this;
		show();

		if (PayCofing.list != null) {
			if (num != 0) {
				return;
			}
			Bundle bundle = getIntent().getExtras();
			position = bundle.getInt("position_list");
			payChannel = PayCofing.list.get(position);
			money = bundle.getDouble("money");

			MyXQTPay.XQTWXPay(money + "", "1", PayCofing.productName,
					PayCofing.productDes, payChannel, handler);
			num++;

		}
	}

	private void show() {
		// 得到网络连接信息
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info != null && info.isConnected()) {
			// 网络连接正常
			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("提示：");
			progressDialog.setMessage("正在进行安全支付扫描");
			progressDialog.setCancelable(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();

		} else {
			Log.i("52Game", "网络异常");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data == null) {
			return;
		}
		String respCode = data.getExtras().getString("respCode");
		String respMsg = data.getExtras().getString("respMsg");
		PaySDK.mcallback.wxPayCallback(respCode, respMsg);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("支付结果通知");
		StringBuilder temp = new StringBuilder();
		if (respCode.equals("00")) {
			temp.append("交易状态:成功");
		}

		if (respCode.equals("02")) {
			temp.append("交易状态:取消");
		}

		if (respCode.equals("01")) {
			temp.append("交易状态:失败").append("\n").append("原因:" + respMsg);
		}

		if (respCode.equals("03")) {
			temp.append("交易状态:未知").append("\n").append("原因:" + respMsg);
		}
		builder.setMessage(temp.toString());
		builder.setInverseBackgroundForced(true);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				TestActivity.this.finish();
			}
		});
		builder.create().show();
	}
}
