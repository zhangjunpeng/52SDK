package com.game.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.game.gamesdk.GameSDK;
import com.game.gamesdk.UserInfo;
import com.game.tools.ResourceUtil;
import com.game.wallet.MyWalletFragment;

public class UserInfoFragment extends Fragment {

	public static String[] list_name = { "我的钱包", "修改密码" };

	private ListView listView;
	static Context mContext = GameSDK.mcontext;
	public int[] id_draw = { ResourceUtil.getDrawableId(mContext, "wallet"),
			ResourceUtil.getDrawableId(mContext, "changepwd") };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(
				ResourceUtil.getLayoutId(mContext, "fragment_userinfo"), null);
		TextView textView = (TextView) view.findViewById(ResourceUtil.getId(
				mContext, "zhanghao_userfrag"));
		textView.setText(UserInfo.userName);
		listView = (ListView) view.findViewById(ResourceUtil.getId(mContext,
				"list_usfrag"));
		view.findViewById(ResourceUtil.getId(mContext, "back_frag_userinfo"))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getActivity().finish();
					}
				});
		initList();

		return view;
	}

	private void initList() {
		// TODO Auto-generated method stub
		listView.setAdapter(new MyAdpter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (list_name[position].equals("修改密码")) {

					ChangePwdFragment changePwd = new ChangePwdFragment();

					UserInfoFragment.this
							.getFragmentManager()
							.beginTransaction()
							.replace(
									ResourceUtil.getId(mContext,
											"container_userinfo"), changePwd)
							.commit();

				} else if (list_name[position].equals("我的钱包")) {
					UserInfoFragment.this
							.getFragmentManager()
							.beginTransaction()
							.replace(
									ResourceUtil.getId(mContext,
											"container_userinfo"),
									new MyWalletFragment()).commit();
				}
			}
		});
	}

	class MyAdpter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_name.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list_name[position];
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
						ResourceUtil.getLayoutId(mContext, "item_list_usfrag"),
						null);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) convertView
						.findViewById(ResourceUtil.getId(mContext,
								"textView1_item_list_usfrag"));
				viewHolder.imageView = (ImageView) convertView
						.findViewById(ResourceUtil.getId(mContext,
								"imageView_item_usfg"));
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.textView.setText(list_name[position]);
			viewHolder.imageView.setImageResource(id_draw[position]);
			return convertView;
		}

		class ViewHolder {
			TextView textView;
			ImageView imageView;

		}
	}

}
