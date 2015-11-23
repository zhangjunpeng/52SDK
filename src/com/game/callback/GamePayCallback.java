package com.game.callback;

public interface GamePayCallback {

	public void wxPayCallback(String resCode, String msg);

	public void cardPayCallback(String resCode);
}
