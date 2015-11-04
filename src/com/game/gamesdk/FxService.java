package com.game.gamesdk;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FxService extends Service {

	// 定义浮动窗口布局
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	// 创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;
	LinearLayout linearLayout;

	ImageButton mFloatView;
	boolean isshow = false;

	private static final String TAG = "FxService";
	private static boolean isClick = false;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				if (isshow) {
					return;
				}
				mFloatView.setBackgroundResource(R.drawable.fx_mini);
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Log.i(TAG, "oncreat");
		createFloatView();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void createFloatView() {
		wmParams = new WindowManager.LayoutParams();
		// 获取的是WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager) getApplication().getSystemService(
				getApplication().WINDOW_SERVICE);
		Log.i(TAG, "mWindowManager--->" + mWindowManager);
		// 设置window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		wmParams.x = 0;
		wmParams.y = 0;

		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		/*
		 * // 设置悬浮窗口长宽数据 wmParams.width = 200; wmParams.height = 80;
		 */

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout,
				null);
		linearLayout = (LinearLayout) mFloatLayout.findViewById(R.id.show_fx);
		initLinearout();
		// 添加mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);
		// 浮动窗口按钮
		mFloatView = (ImageButton) mFloatLayout.findViewById(R.id.float_id);
		mFloatView.setBackgroundResource(R.drawable.fx_mini);
		mFloatView.setScaleX(0.5f);
		mFloatView.setScaleY(0.5f);
		// mFloatView.setScaleType(ScaleType.FIT_START);
		mFloatView.setPivotX(0);
		mFloatView.setPivotY(0);

		// mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
		// .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		// Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth() / 2);
		// Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);

		// 设置监听浮动窗口的触摸移动

		mFloatView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isClick = true;
					mFloatView.setFocusable(true);
					mFloatView.setBackgroundResource(R.drawable.fx);
					break;

				case MotionEvent.ACTION_MOVE:
					isClick = false;
					// getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
					wmParams.x = 0 - mFloatView.getMeasuredWidth();

					// Log.i(TAG, "RawX" + event.getRawX());
					// Log.i(TAG, "X" + event.getX());
					// 减25为状态栏的高度
					wmParams.y = (int) event.getRawY()
							- mFloatView.getMeasuredHeight() - 25;
					// Log.i(TAG, "RawY" + event.getRawY());
					// Log.i(TAG, "Y" + event.getY());
					// 刷新
					// RelativeLayout.LayoutParams layoutParams =
					// (android.widget.RelativeLayout.LayoutParams) mFloatView
					// .getLayoutParams();
					// layoutParams.setMargins(0, 0, 1, 1);
					// mFloatView.setLayoutParams(layoutParams);
					mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					showView();
					break;

				case MotionEvent.ACTION_UP:

					wmParams.x = 0;
					mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					if (isClick) {
						isshow = !isshow;
						showView();
					}
					mFloatView.setFocusable(false);

					break;
				}

				return true; // 此处必须返回false，否则OnClickListener获取不到监听
			}

		});

	}

	private void initLinearout() {
		// TODO Auto-generated method stub
		mFloatLayout.findViewById(R.id.geren_show).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (!GameSDK.isLogin) {
							Toast.makeText(getBaseContext(), "请先登录",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Intent intent1 = new Intent(FxService.this
								.getBaseContext(), UserInfoActivity.class);
						intent1.putExtra("tag", 1);
						intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						FxService.this.getBaseContext().startActivity(intent1);
						isshow = !isshow;
						showView();
						stopSelf();
					}
				});
		mFloatLayout.findViewById(R.id.gamebbs_show).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent2 = new Intent(FxService.this
								.getBaseContext(), ALiActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("tag", "bbs");
						bundle.putString("data", "http://bbs.m.52game.com");
						intent2.putExtras(bundle);
						intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getBaseContext().startActivity(intent2);

					}
				});
		mFloatLayout.findViewById(R.id.connect_show).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent1 = new Intent(FxService.this
								.getBaseContext(), UserInfoActivity.class);
						intent1.putExtra("tag", 2);
						intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						FxService.this.getBaseContext().startActivity(intent1);

						isshow = !isshow;
						showView();
						stopSelf();
					}
				});
		mFloatLayout.findViewById(R.id.switch_show).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// if (!GameSDK.isLogin) {
						// return;
						// }
						// GameSDK.isLogin = false;
						ShowDialog.showLoginDialog(GameSDK.mcontext);
						isshow = !isshow;
						showView();
						// stopSelf();
					}
				});

	}

	public void showView() {

		if (isshow) {

			linearLayout.setVisibility(View.VISIBLE);
			mFloatView.bringToFront();
		} else {
			linearLayout.setVisibility(View.GONE);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(1000);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(1);

				}

			}).start();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mFloatLayout != null) {
			// 移除悬浮窗口
			mWindowManager.removeView(mFloatLayout);
		}
	}

}