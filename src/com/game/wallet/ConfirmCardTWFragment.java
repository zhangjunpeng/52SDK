package com.game.wallet;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.game.gamesdk.R;
import com.game.gamesdk.UserInfo;
import com.game.paysdk.MyXQTPay;
import com.game.paysdk.PayCofing;
import com.game.sdkclass.PayChannel;
import com.game.tools.MyLog;
import com.game.tools.StringTools;

public class ConfirmCardTWFragment extends Fragment {
	Spinner spinner;
	String num[];
	int position;
	private double money;
	public int cardValue;
	String faceno;
	String cardName;

	PayChannel payChannel;

	String card_no;
	String card_key;

	Button zhifu;

	private EditText edit_cardno;
	private EditText edit_cardkey;
	String state;

	ExecutorService singleThrad = Executors.newSingleThreadExecutor();

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = StringTools.decodeUnicode(msg.obj.toString());
				MyLog.i("cardPayGetOrder返回：==" + data);
				if (!data.contains("200")) {
					return;
				}
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(data);
					JSONObject jsonObject2 = jsonObject.getJSONObject("data");
					String orderID = jsonObject2.getString("order_id");
					MyXQTPay.cardPay(orderID, 100f, cardName, faceno, card_no,
							card_key, payChannel.getNotify_wallet_url(),
							handler2);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		}
	};
	public Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("CardPay结果返回==" + data);
				StringReader stringReader = new StringReader(data);

				try {
					XmlPullParser pullParser = Xml.newPullParser();
					pullParser.setInput(stringReader);

					int eventType = pullParser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {

						switch (eventType) {
						case XmlPullParser.START_TAG:
							if (pullParser.getName().equals("item")) {
								// Log.i("ZJP", "eventType==" + eventType);
								if ("state".equals(pullParser
										.getAttributeValue(0))) {

									state = pullParser.getAttributeValue(1);
									MyLog.i("state==" + state);

								}

							}
							eventType = pullParser.next();
							break;

						default:
							break;
						}
						eventType = pullParser.next();
					}

				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Log.i("ZJP", "state==2" + state);
				if (state.equals("0")) {
					Toast.makeText(getActivity(), "订单错误，请重新输入账号密码",
							Toast.LENGTH_SHORT).show();
				} else if (state.equals("1")) {
					Toast.makeText(getActivity(), "订单提交成功", Toast.LENGTH_SHORT)
							.show();
					getActivity().finish();
				}

				break;

			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_cardpay, null);
		Bundle bundle = getArguments();
		String tag = bundle.getString("tag");
		money = bundle.getDouble("paymoney");
		position = Integer.parseInt(tag);
		payChannel = PayCofing.list.get(position);
		MyLog.i(payChannel.getChannel_name() + ":::"
				+ payChannel.getChannel_name_en());
		num = payChannel.setPayNum();

		cardValue = Integer.parseInt(num[0]);

		initView(view);

		cardName = payChannel.getChannel_name_en();
		faceno = cardName + getFaceno(cardValue + "");

		return view;
	}

	private String getFaceno(String cardMon2) {
		// TODO Auto-generated method stub
		String facen = "000" + cardMon2;
		String face = facen.substring(facen.length() - 3, facen.length());
		// Log.i("FACENO", face);
		return face;

	}

	private void initView(View view) {
		// TODO Auto-generated method stub

		edit_cardno = (EditText) view.findViewById(R.id.edit_cardnum_fragment);
		edit_cardkey = (EditText) view
				.findViewById(R.id.edit_cardpass_fragment);

		zhifu = (Button) view.findViewById(R.id.pay_fragment_ali_card);

		zhifu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// cardValue = money;// 测试
				if (cardValue == money) {
					// 面值相等才能充值
					card_no = edit_cardno.getEditableText().toString();
					card_key = edit_cardkey.getEditableText().toString();
					if (TextUtils.isEmpty(card_no)
							|| TextUtils.isEmpty(card_key)) {
						Toast.makeText(getActivity(), "卡号或密码不能为空",
								Toast.LENGTH_LONG).show();
						return;
					}
					singleThrad.execute(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							WalletPay.getOrderIdPayToWallet(money + "",
									card_no, card_key,
									PayCofing.list.get(position), handler);
						}
					});

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle("提示：")
							.setMessage("选择面值和充值金额不匹配，请重新选择或者更换充值方式！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}

									});
					builder.show();
				}

			}
		});
		spinner = (Spinner) view.findViewById(R.id.spinner_cardpay_fragment);
		spinner.setAdapter(new MyAdapter());
		spinner.setSelection(0);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position_2, long id) {
				// TODO Auto-generated method stub
				cardValue = Integer.parseInt(num[position_2]);

				faceno = cardName + getFaceno(num[position_2]);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		TextView textView = (TextView) view
				.findViewById(R.id.payname_table_fragment_card);
		textView.setTextColor(Color.rgb(254, 146, 38));

		textView.setText(PayCofing.list.get(position).getChannel_name());
		MyLog.i(PayCofing.list.get(position).getChannel_name());
		TextView money_text = (TextView) view
				.findViewById(R.id.paymoney_fragment_card);
		money_text.setText(money + " 元");

		// 显示用户名
		TextView name_text = (TextView) view
				.findViewById(R.id.username_fragment_card);

		name_text.setText(UserInfo.userName);

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return num.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return num[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_spinner, null);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textView_item_spinner);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.textView.setText(num[position] + " 元");
			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}

	}
}
