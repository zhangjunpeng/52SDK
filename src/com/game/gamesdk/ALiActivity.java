package com.game.gamesdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ALiActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ali);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String data = bundle.getString("data");
		WebView webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
		webView.loadData(data, "text/html", "utf-8");
		finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		finish();
	}

}
