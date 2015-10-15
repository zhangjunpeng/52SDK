package com.game.paysdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.game.gamesdk.R;
import com.game.http.GameHttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PayActivity extends FragmentActivity {

	public static Activity instance;
	private ListView listView;
	FragmentManager fragmentManager;
	String mtag = "";
	double money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pay);
		instance = this;

		getChannel();
		fragmentManager = getSupportFragmentManager();
		// initView();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		money = bundle.getDouble("paymoney");

		comitFragment("ali", money);

	}

	private void getChannel() {
		// TODO Auto-generated method stub
		ExecutorService singleThread = Executors.newSingleThreadExecutor();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					Log.i("ZJP", "channel=====" + msg.obj.toString());

					Gson gson = new Gson();
					PayCofing.list = gson.fromJson(msg.obj.toString(),
							new TypeToken<List<PayChannel>>() {
							}.getType());
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
		if (!mtag.equals(tag)) {
			AliPayFragment aliPayFragment = new AliPayFragment();
			Bundle bundle = new Bundle();
			bundle.putString("type", tag);
			bundle.putDouble("paymoney", money2);
			mtag = tag;
			aliPayFragment.setArguments(bundle);
			// fragmentManager.beginTransaction().add(R.id.contioner_pay,aliPayFragment).commit();
			fragmentManager.beginTransaction()
					.replace(R.id.contioner_pay, aliPayFragment).commit();
		} else {

		}
	}

	class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			return PayCofing.list.size();
		}

		@Override
		public Object getItem(int position) {
			return PayCofing.list.get(position).getChannel_name();
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
			viewHolder.textView.setText(PayCofing.list.get(position)
					.getChannel_name());
			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.listView_pay);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PayChannel payChannel = PayCofing.list.get(position);
				String data = payChannel.getPay_nums();
				if (TextUtils.isEmpty(data)) {
					if (payChannel.getChannel_name_en().equals("weixin")) {
						comitFragment("weixin", money);
					} else if (payChannel.getChannel_name_en().equals("alipay")) {
						comitFragment("alipay", money);
					} else if (payChannel.getChannel_name_en().equals("bank")) {
						comitFragment("bank", money);
					}

				} else {
					comitCardFragment(position + "", money);
				}

			}
		});
		if (PayCofing.list == null) {
			return;
		}
		listView.setAdapter(new Myadapter());
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

}
