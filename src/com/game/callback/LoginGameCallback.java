package com.game.callback;

/**
 * Created by Administrator on 2015/9/18.
 * 
 * 登录接口的回调
 */
public interface LoginGameCallback {

	// 登录成功后的回调
	public void loginEndCallback(String msg);

	// 注册后的回调接口
	public void registerEndCallback(String msg);

}
