package com.game.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.game.gamesdk.R;
import com.game.sdkclass.GameOpen;
import com.game.tools.ImageDownloadHelper;
import com.game.tools.MyLog;
import com.game.tools.ImageDownloadHelper.OnImageDownloadListener;

public class KaifuFragment extends Fragment {
	private ListView listView;
	private ArrayList<GameOpen> gameList;
	private ImageDownloadHelper mDownloadHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_kaifu, null);
		listView = (ListView) view.findViewById(R.id.list_kaifu_fragment);
		gameList = (ArrayList<GameOpen>) getArguments().get("gamelist");
		initView(savedInstanceState);
		return view;
	}

	private void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (gameList == null) {
			return;
		}
		listView.setAdapter(new Myadapter(savedInstanceState));
	}

	class Myadapter extends BaseAdapter {
		Bundle bundle;

		public Myadapter(Bundle savedInstanceState) {
			// TODO Auto-generated constructor stub
			bundle = savedInstanceState;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gameList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return gameList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHloder viewHloder;
			if (convertView == null) {
				convertView = getLayoutInflater(bundle).inflate(
						R.layout.item_listview_kffg, null);
				viewHloder = new ViewHloder();
				viewHloder.imageView = (ImageView) convertView
						.findViewById(R.id.imageView_item_kaifulist);
				viewHloder.textView1 = (TextView) convertView
						.findViewById(R.id.textView1_kf);
				viewHloder.textView2 = (TextView) convertView
						.findViewById(R.id.textView2_kf);
				viewHloder.textView3 = (TextView) convertView
						.findViewById(R.id.textView3_kf);
				viewHloder.button_xz = (Button) convertView
						.findViewById(R.id.button_item_kflist);
				convertView.setTag(viewHloder);
			} else {
				viewHloder = (ViewHloder) convertView.getTag();
			}
			GameOpen gameOpen = gameList.get(position);
			String urlString = gameOpen.getSmall_img();
			viewHloder.imageView.setTag(urlString);
			viewHloder.textView2.setText(gameOpen.getGame_name());
			viewHloder.textView3.setText(gameOpen.getGame_type());
			final String apkDownload = gameOpen.getApk_url();
			viewHloder.button_xz.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// Intent intent = new Intent(getActivity(),
					// ALiActivity.class);
					// Bundle bundle1 = new Bundle();
					// bundle1.putString("tag", "download");
					// bundle1.putString("data", apkDownload);
					//
					// intent.putExtras(bundle1);
					// startActivity(intent);
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(apkDownload);
					intent.setData(content_url);
					startActivity(intent);
					MyLog.i("webview加载");

					// DownloadManager downloadManager = (DownloadManager)
					// getActivity()
					// .getSystemService(Context.DOWNLOAD_SERVICE);
					// Uri uri = Uri
					// .parse("http://d1.07073sy.com:81/app/ttgj/CY_guaji_c9995206.apk");
					// Request request = new Request(uri);
					// // 设置允许使用的网络类型，这里是移动网络和wifi都可以
					// request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
					// | DownloadManager.Request.NETWORK_WIFI);
					// //
					// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
					// // request.setShowRunningNotification(false);
					//
					// // 不显示下载界面
					// request.setVisibleInDownloadsUi(false);
					// /*
					// *
					// 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
					// * 在
					// *
					// /mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用
					// * ,设置了下面这个将报错，不设置，下载后的文件在/cache这个 目录下面
					// */
					// // request.setDestinationInExternalFilesDir(this, null,
					// // "tar.apk");
					// long id = downloadManager.enqueue(request);
					// // TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
				}
			});
			final ImageView mimageView = viewHloder.imageView;
			if (mDownloadHelper == null) {
				mDownloadHelper = new ImageDownloadHelper();
			}
			mDownloadHelper.imageDownload(getActivity(), urlString,
					viewHloder.imageView, "/img_temp",
					new OnImageDownloadListener() {

						@Override
						public void onImageDownload(Bitmap bitmap, String imgUrl) {
							// TODO Auto-generated method stub
							if (bitmap == null) {
								MyLog.i("图片加载失败");
								return;
							}
							mimageView.setImageBitmap(bitmap);
						}
					});
			return convertView;
		}

		class ViewHloder {
			ImageView imageView;
			TextView textView1;
			TextView textView2;
			TextView textView3;
			Button button_xz;
		}

	}

}
