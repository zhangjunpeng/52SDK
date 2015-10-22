package com.game.gamesdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.game.tools.StringTools;

public class ChangePwdFragment extends Fragment {

	private EditText editText_old;
	private EditText editText_new;
	private EditText editText_cofirm;
	private Button submit;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String data = msg.obj.toString();
				Log.i("ZJP", "changepwd~~~~" + StringTools.decodeUnicode(data));

				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorcode = jsonObject.getString("errorCode");
					if (errorcode.equals("200")) {
						Toast.makeText(getActivity(), "修改密码成功",
								Toast.LENGTH_SHORT).show();
						getActivity().finish();
					} else {
						Toast.makeText(getActivity(), "修改密码失败",
								Toast.LENGTH_SHORT).show();
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_changepwd, null);
		editText_old = (EditText) view.findViewById(R.id.edit_oldpwd_change);
		editText_new = (EditText) view.findViewById(R.id.edit_newpwd_change);
		editText_cofirm = (EditText) view
				.findViewById(R.id.edit_confirmpwd_change);
		submit = (Button) view.findViewById(R.id.submit_changepwd);
		initView();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String old = editText_old.getEditableText().toString();
				final String newpwd = editText_new.getEditableText().toString();
				final String confirm = editText_cofirm.getEditableText()
						.toString();
				if (TextUtils.isEmpty(old)) {
					Toast.makeText(getActivity(), "旧密码不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				} else if (TextUtils.isEmpty(newpwd)) {
					Toast.makeText(getActivity(), "新密码不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				} else if (TextUtils.isEmpty(confirm)) {
					Toast.makeText(getActivity(), "确认密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!newpwd.equals(confirm)) {
					Toast.makeText(getActivity(), "两次输入的密码不相同",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (old.equals(newpwd)) {
					Toast.makeText(getActivity(), "新密码与旧密码相同",
							Toast.LENGTH_SHORT).show();
					return;
				}
				ExecutorService single = Executors.newSingleThreadExecutor();
				single.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						NameRegLogin.changepwd(old, newpwd, confirm, handler);
					}
				});

			}
		});
	}
}
