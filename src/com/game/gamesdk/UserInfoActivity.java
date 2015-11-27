package com.game.gamesdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.game.account.LoginFragment;
import com.game.fragment.BindPhoneNumFragment;
import com.game.fragment.ConnectFragment;
import com.game.fragment.KaifuFragment;
import com.game.fragment.UserInfoFragment;
import com.game.http.GameHttpClient;
import com.game.sdkclass.GameOpen;
import com.game.tools.MyLog;
import com.game.tools.ResourceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserInfoActivity extends FragmentActivity {

	private ArrayList<GameOpen> gamelist;
	Context mContext = GameSDK.mcontext;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				MyLog.i("开服列表返回:::" + data);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if ("200".equals(errorCode)) {
						String mes = jsonObject.getString("data");
						Gson gson = new Gson();
						gamelist = gson.fromJson(mes,
								new TypeToken<List<GameOpen>>() {
								}.getType());
						comitKFFragment(gamelist);
					}
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

	private void comitKFFragment(ArrayList<GameOpen> gamelist) {
		// TODO Auto-generated method stub
		if (gamelist == null) {
			return;
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("gamelist", gamelist);
		Fragment kaifuFragment = new KaifuFragment();
		kaifuFragment.setArguments(bundle);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(ResourceUtil.getId(this, "container_userinfo"),
						kaifuFragment).commit();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(ResourceUtil.getLayoutId(mContext, "activity_user_info"));
		int tag = getIntent().getIntExtra("tag", 1);
		comitFragment(tag);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		findViewById(ResourceUtil.getId(mContext, "ImageView1_acinfo"))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (GameSDK.isLogin) {
							comitFragment(1);
						} else {
							comitFragment(3);
						}

					}
				});
		final ImageView im = (ImageView) findViewById(ResourceUtil.getId(
				mContext, "textView4_uiac"));
		im.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				im.setFocusable(true);
				ExecutorService single = Executors.newSingleThreadExecutor();
				single.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						NameValuePair nameValuePair1 = new BasicNameValuePair(
								"appid", GameSDK.AppID);
						nameValuePairs.add(nameValuePair1);
						GameHttpClient gameHttpClient = new GameHttpClient(
								handler);
						gameHttpClient.startClient(RegisterConfig.getFu,
								nameValuePairs);
					}
				});
			}
		});
		findViewById(ResourceUtil.getId(mContext, "textView3"))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent2 = new Intent(UserInfoActivity.this,
								ALiActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("tag", "bbs");
						bundle.putString("data", "http://bbs.m.52game.com");
						intent2.putExtras(bundle);
						intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getBaseContext().startActivity(intent2);
					}
				});
		findViewById(ResourceUtil.getId(mContext, "textView2"))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent2 = new Intent(UserInfoActivity.this,
								ALiActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("tag", "bbs");
						bundle.putString("data", "http://m.52game.com");
						intent2.putExtras(bundle);
						intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getBaseContext().startActivity(intent2);
					}
				});
	}

	private void comitFragment(int tag) {
		// TODO Auto-generated method stub
		switch (tag) {
		case 1:
			UserInfoFragment userInfoFragment = new UserInfoFragment();

			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(
							ResourceUtil.getId(mContext, "container_userinfo"),
							userInfoFragment).commit();
			break;

		case 2:
			ConnectFragment connectFragment = new ConnectFragment();
			getSupportFragmentManager()
					.beginTransaction()
					.replace(
							ResourceUtil.getId(mContext, "container_userinfo"),
							connectFragment).commit();
			break;

		case 3:
			getSupportFragmentManager()
					.beginTransaction()
					.replace(
							ResourceUtil.getId(mContext, "container_userinfo"),
							new LoginFragment()).commit();
			break;
		case 4:
			BindPhoneNumFragment bindPhoneNumFragment = new BindPhoneNumFragment();
			Bundle bundle = new Bundle();
			bundle.putString("tag", "walletpay");
			bindPhoneNumFragment.setArguments(bundle);
			getSupportFragmentManager()
					.beginTransaction()
					.replace(
							ResourceUtil.getId(mContext, "container_userinfo"),
							bindPhoneNumFragment).commit();
			break;

		default:
			break;
		}

	}

}
