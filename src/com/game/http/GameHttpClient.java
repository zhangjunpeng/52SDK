package com.game.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.util.Log;

public class GameHttpClient {
	private Handler handler;

	public GameHttpClient(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;

	}

	final static String TAG = "GAMEHTTPCLIENT";

	public void startClient(String uri, List<NameValuePair> nameValuePairs) {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpPost httpPost = new HttpPost(uri);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();// 获取响应里面的内容
				// InputStream inputStream = entity.getContent();
				// 得到服务气端发回的响应的内容（都在一个流里面）

				String strResult = EntityUtils.toString(entity);
				// 得到服务气端发回的响应的内容（都在一个字符串里面）
				handler.obtainMessage(0, strResult).sendToTarget();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loginClient(String uri, List<NameValuePair> nameValuePairs) {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpPost httpPost = new HttpPost(uri);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();// 获取响应里面的内容
				// InputStream inputStream = entity.getContent();
				// 得到服务气端发回的响应的内容（都在一个流里面）

				String strResult = EntityUtils.toString(entity);
				// 得到服务气端发回的响应的内容（都在一个字符串里面）
				Log.i("ZJP", "请求成功");
				handler.obtainMessage(1, strResult).sendToTarget();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
