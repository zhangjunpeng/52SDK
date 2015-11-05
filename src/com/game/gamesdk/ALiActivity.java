package com.game.gamesdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ALiActivity extends Activity {

	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ali);

		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String tag = bundle.getString("tag");
		if ("ali".equals(tag)) {
			startAlipay(bundle);
			finish();
		} else if ("download".equals(tag)) {
			String apkurl = bundle.getString("data");
			webView.loadUrl(apkurl);
			finish();

		} else if ("bbs".equals(tag)) {
			String url = bundle.getString("data");
			webView.setWebViewClient(new WebViewClient());
			webView.loadUrl(url);
		}

	}

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void startAlipay(Bundle bundle) {
		// TODO Auto-generated method stub

		String data = bundle.getString("data");

		webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
		// webView.loadData(data, "text/html", "utf-8");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		finish();
	}

}
