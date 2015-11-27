package com.game.gamesdk;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.game.tools.ResourceUtil;

public class WelcomeActivity extends Activity {
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				finish();
				break;

			default:
				break;
			}
		}
	};
	Context mContext = GameSDK.mcontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Configuration mConfiguration = getResources().getConfiguration();
		int ori = mConfiguration.orientation;
		if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
			// 横屏
			setContentView(ResourceUtil.getLayoutId(mContext,
					"activity_welcone"));
		} else {
			setContentView(ResourceUtil.getLayoutId(mContext, "welcome"));
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

}
