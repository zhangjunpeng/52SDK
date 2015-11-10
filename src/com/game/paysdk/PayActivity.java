package com.game.paysdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.fragment.AliPayFragment;
import com.game.fragment.CardPayFragment;
import com.game.gamesdk.ALiActivity;
import com.game.gamesdk.R;
import com.game.http.GameHttpClient;
import com.game.sdkclass.PayChannel;
import com.game.tools.MyLog;
import com.game.wallet.ToWalletFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shengpay.smc.HybridClientActivity;
import com.shengpay.smc.enums.TransStatus;

public class PayActivity extends FragmentActivity {

	public static Activity instance;
	private ListView listView;
	FragmentManager fragmentManager;
	String mtag = "";
	String tag;
	public static int selected_po = -1;

	double money;
	static List<PayChannel> channellist;
	private ToWalletFragment toWalletFragment = new ToWalletFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pay);
		instance = this;
		tag = getIntent().getStringExtra("tag");
		fragmentManager = getSupportFragmentManager();
		getSDKChannel();
		if ("wallet".equals(tag)) {
			comitFragment("wallet");

		} else if ("sdk".equals(tag)) {

			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			money = bundle.getDouble("paymoney");

			comitFragment("weixin", money);
		}

		findViewById(R.id.back_pay).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		findViewById(R.id.kefu_pay).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(PayActivity.this, ALiActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tag", "bbs");
				bundle.putString("data", "http://m.52game.com/wap/service");
				intent2.putExtras(bundle);
				intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getBaseContext().startActivity(intent2);
			}
		});

	}

	private void initWalletView() {
		// TODO Auto-generated method stub

	}

	private void comitFragment(String string) {
		// TODO Auto-generated method stub
		if (string.equals("wallet")) {
			fragmentManager.beginTransaction()
					.replace(R.id.contioner_pay, toWalletFragment).commit();
		}

	}

	private void getSDKChannel() {
		// TODO Auto-generated method stub
		ExecutorService singleThread = Executors.newSingleThreadExecutor();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:

					MyLog.i("channel=====" + msg.obj.toString());

					Gson gson = new Gson();

					PayCofing.list = gson.fromJson(msg.obj.toString(),
							new TypeToken<List<PayChannel>>() {
							}.getType());
					for (PayChannel payChannel : PayCofing.list) {
						if (payChannel.getChannel_name_en().equals("wallet")) {
							PayCofing.list.remove(payChannel);
						}
					}
					if ("wallet".equals(tag)) {
						channellist = PayCofing.list;
						for (PayChannel payChannel : channellist) {
							if (payChannel.getChannel_name_en()
									.equals("wallet")) {
								channellist.remove(payChannel);
							}
						}
					}

					initView();

					break;

				default:
					break;
				}
			}
		};
		singleThread.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				GameHttpClient gameHttpClient = new GameHttpClient(handler);
				gameHttpClient.startClient(PayCofing.payChannelUrl,
						nameValuePairs);
			}
		});

	}

	public void comitFragment(String tag, double money2) {

		AliPayFragment aliPayFragment = new AliPayFragment();
		Bundle bundle = new Bundle();
		bundle.putString("type", tag);
		bundle.putDouble("paymoney", money2);
		mtag = tag;
		aliPayFragment.setArguments(bundle);
		// fragmentManager.beginTransaction().add(R.id.contioner_pay,aliPayFragment).commit();
		fragmentManager.beginTransaction()
				.replace(R.id.contioner_pay, aliPayFragment).commit();

	}

	class Myadapter extends BaseAdapter {
		List<PayChannel> mylist;

		public Myadapter(List<PayChannel> list) {
			// TODO Auto-generated constructor stub
			mylist = list;
		}

		@Override
		public int getCount() {
			return mylist.size();
		}

		@Override
		public Object getItem(int position) {
			return mylist.get(position).getChannel_name();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.item_listview_pay, null);
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textView_itemList_pay);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			PayChannel payChannel = mylist.get(position);

			viewHolder.textView.setText(payChannel.getChannel_name());
			convertView.setBackgroundColor(Color.rgb(254, 146, 38));
			if (position == selected_po) {

				convertView.setBackgroundColor(Color.rgb(255, 186, 117));
			}

			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.listView_pay);
		if (tag.equals("wallet")) {
			listView.setAdapter(new Myadapter(channellist));
		} else if (tag.equals("sdk")) {
			if (PayCofing.list == null) {
				return;
			}
			listView.setAdapter(new Myadapter(PayCofing.list));
		}
		listView.setAdapter(new Myadapter(PayCofing.list));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// setexBackground(ex_selected);
				for (int i = 0; i < listView.getChildCount(); i++) {
					listView.getChildAt(i).setBackgroundColor(
							Color.rgb(254, 146, 38));
				}
				selected_po = position;
				view.setBackgroundColor(Color.rgb(255, 186, 117));
				// setBackground(position);
				// ex_selected = selected_po;
				if ("wallet".equals(tag)) {
					comitFragment("wallet");
				}

				if (tag.equals("sdk")) {

					PayChannel payChannel = PayCofing.list.get(position);

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
						comitCardFragment(position + "", money);
					}
				}

			}
		});

		listView.setItemChecked(0, false);

	}

	protected void setBackground(int position) {
		// TODO Auto-generated method stub
		if (listView == null) {
			return;
		}
		View view = listView.getChildAt(position);
		if (view == null) {
			return;
		}
		view.setBackgroundColor(Color.rgb(255, 186, 117));

	}

	protected void setexBackground(int position) {
		// TODO Auto-generated method stub
		if (listView == null) {
			return;
		}
		View view = listView.getChildAt(position);
		if (view == null) {
			return;
		}
		view.setBackgroundColor(Color.rgb(254, 146, 38));

	}

	public void comitCardFragment(String tag, double money2) {
		Fragment fragment = new CardPayFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		bundle.putDouble("paymoney", money2);
		fragment.setArguments(bundle);
		fragmentManager.beginTransaction()
				.replace(R.id.contioner_pay, fragment).commit();

	}

	@Override
	// 重写Acitivity的onActivityResult方法
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == HybridClientActivity.SMC_REQUEST_CODE
				&& resultCode == HybridClientActivity.SMC_RESULT_CODE) {
			String returnValue = data
					.getStringExtra(HybridClientActivity.SMC_RETURN_VALUE);
			// 是否支付成功，请以notifyUrl接收到的信息为准.因为有些支付是异步的，这里的通知不一定能实时告知支付结果
			if (TransStatus.isSuccess(returnValue)) {// 支付成功
				Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT)
						.show();
				PaySDK.mcallback.wxPayCallback("00", "支付成功");
				finish();
			} else if (TransStatus.isFailed(returnValue)) {
				Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT)
						.show();
				PaySDK.mcallback.wxPayCallback("01", "支付失败");
			} else {
				Toast.makeText(PayActivity.this, "支付未完成", Toast.LENGTH_SHORT)
						.show();
				PaySDK.mcallback.wxPayCallback("02", "支付取消");
			}
		}
	}

}
