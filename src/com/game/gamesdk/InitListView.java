package com.game.gamesdk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class InitListView {
	MySimpleAdapter mySimpleAdapter;

	public void initloginList(Context context, final ListView listView) {
		mySimpleAdapter = new MySimpleAdapter(context);
		listView.setAdapter(mySimpleAdapter);
		listView.setVisibility(View.VISIBLE);
		listView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					listView.setVisibility(View.GONE);
				}
			}
		});
	}

	private void upData() {
		// TODO Auto-generated method stub
		mySimpleAdapter.notifyDataSetChanged();
	}

	class MySimpleAdapter extends BaseAdapter {
		Context mContext;

		public MySimpleAdapter(Context content) {
			mContext = content;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final int id = position;
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_listview_login, null);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textView_item_list_login);
				viewHolder.imageView = (ImageView) convertView
						.findViewById(R.id.imageView_item_list_login);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.textView.setText(UserInfo.Name_used.get(position));
			viewHolder.imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UserInfo.Name_used.remove(id);

					String mes = "";
					for (int i = 0; i < UserInfo.Name_used.size(); i++) {
						mes = mes + UserInfo.Name_used.get(i) + ";";
						GameSDK.saveNameUsedSP(mes);
					}
					upData();
				}

			});
			return convertView;
		}

		class ViewHolder {
			TextView textView;
			ImageView imageView;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return UserInfo.Name_used.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return UserInfo.Name_used.size();
		}
	}
}
