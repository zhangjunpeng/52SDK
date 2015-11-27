package com.game.fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.game.http.NameRegLogin;
import com.game.tools.MyLog;
import com.game.tools.ResourceUtil;
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
				MyLog.i("changepwd~~~~" + StringTools.decodeUnicode(data));

				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorcode = jsonObject.getString("errorCode");
					if (errorcode.equals("200")) {
						Toast.makeText(getActivity(), "修改密码成功",
								Toast.LENGTH_SHORT).show();
						getActivity().finish();
					} else if (errorcode.equals("5007")) {
						Toast.makeText(getActivity(), "旧密码错误",
								Toast.LENGTH_SHORT).show();
					} else if (errorcode.equals("5008")) {
						Toast.makeText(getActivity(), "新旧密码不能一致",
								Toast.LENGTH_SHORT).show();
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
	static Context mcontext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mcontext = getActivity();
		View view = inflater.inflate(
				ResourceUtil.getLayoutId(mcontext, "fragment_changepwd"), null);
		editText_old = (EditText) view.findViewById(ResourceUtil.getId(
				mcontext, "edit_oldpwd_change"));
		editText_new = (EditText) view.findViewById(ResourceUtil.getId(
				mcontext, "edit_newpwd_change"));
		editText_cofirm = (EditText) view.findViewById(ResourceUtil.getId(
				mcontext, "edit_confirmpwd_change"));
		submit = (Button) view.findViewById(ResourceUtil.getId(mcontext,
				"submit_changepwd"));
		view.findViewById(ResourceUtil.getId(mcontext, "back_frag_change"))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getActivity()
								.getSupportFragmentManager()
								.beginTransaction()
								.replace(
										ResourceUtil.getId(mcontext,
												"container_userinfo"),
										new UserInfoFragment()).commit();
					}
				});
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
				if (newpwd.length() < 4 || newpwd.length() > 20) {
					Toast.makeText(getActivity(), "新密码长度必须大于3小于21",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringTools.isHaveBlank(newpwd)) {
					Toast.makeText(getActivity(), "密码不能包含空格",
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
