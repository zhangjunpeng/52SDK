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
import android.widget.ProgressBar;

import com.game.gamesdk.R;
import com.game.tools.MD5Test;
import com.game.tools.MyLog;
import com.game.tools.StringTools;
import com.ipaynow.plugin.api.IpaynowPlugin;
import com.xqt.now.paysdk.XqtPay;
import com.xqt.now.paysdk.XqtPay.XqtPayListener;

public class TestActivity extends Activity {

	public static TestActivity instance;

	public ProgressDialog progressDialog;
	int position;
	static PayChannel payChannel;

	static double money;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String data = StringTools.decodeUnicode(msg.obj.toString());

				try {

					if (data.contains("200")) {
						JSONObject jsonObject = new JSONObject(data);
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						String orderID = jsonObject2.getString("order_id");
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

						MyLog.i("获取微信支付参数");
						XqtPay.Transit(TestActivity.this, new XqtPayListener() {

							@Override
							public void success(String arg0) {
								// TODO Auto-generated method stub
								progressDialog.dismiss();
								MyLog.i("开启微信支付");
								IpaynowPlugin.pay(TestActivity.this, arg0);
							}

							@Override
							public void error(String arg0) {
								// TODO Auto-generated method stub
								MyLog.i("微信支付异常：==" + arg0);
							}
						});
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				MyLog.i(data);

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
		int count = (int) (money * 100);
		XqtPay.mhtOrderAmt = count + "";
		Log.i("ZJP", "XqtPay.mhtOrderAmt===" + XqtPay.mhtOrderAmt);
		XqtPay.mhtOrderDetail = PayCofing.productDes;
		XqtPay.notifyUrl = payChannel.getNotify_pay_url();
		XqtPay.superid = "100000";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.activity_test);

		instance = this;
		show();

		Bundle bundle = getIntent().getExtras();
		position = bundle.getInt("position_list");
		payChannel = PayCofing.list.get(position);
		money = bundle.getDouble("money");
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		MyXQTPay.XQTWXPay(PayCofing.orderid_cp, money + "", PayCofing.serverID,
				PayCofing.productName, PayCofing.productDes, payChannel,
				handler);

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
			MyLog.i("网络异常");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data == null) {
			return;
		}
		MyLog.i("微信支付结果：" + data);
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
