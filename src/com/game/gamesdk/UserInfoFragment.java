package com.game.gamesdk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserInfoFragment extends Fragment {

	public static String[] list_name = { "修改密码" };
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_userinfo, null);
		TextView textView = (TextView) view
				.findViewById(R.id.zhanghao_userfrag);
		textView.setText(UserInfo.userName);
		listView = (ListView) view.findViewById(R.id.list_usfrag);
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
					getActivity().getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.container_userinfo, changePwd)
							.commit();
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
						R.layout.item_list_usfrag, null);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textView1_item_list_usfrag);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.textView.setText(list_name[position]);
			return convertView;
		}

		class ViewHolder {
			TextView textView;

		}
	}

}
