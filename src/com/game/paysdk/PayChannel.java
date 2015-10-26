package com.game.paysdk;

import android.text.TextUtils;

public class PayChannel {
	private String id;
	private String channel_name;
	private String notify_pay_url;
	private String channel_pay_rate;
	private String pay_nums;
	private String channel_name_en;
	private String return_pay_url;

	public String getReturn_pay_url() {
		return return_pay_url;
	}

	public void setReturn_pay_url(String return_pay_url) {
		this.return_pay_url = return_pay_url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getNotify_pay_url() {
		return notify_pay_url;
	}

	public void setNotify_pay_url(String notify_pay_url) {
		this.notify_pay_url = notify_pay_url;
	}

	public String getChannel_pay_rate() {
		return channel_pay_rate;
	}

	public void setChannel_pay_rate(String channel_pay_rate) {
		this.channel_pay_rate = channel_pay_rate;
	}

	public String getPay_nums() {
		return pay_nums;
	}

	public void setPay_nums(String pay_nums) {
		this.pay_nums = pay_nums;
	}

	public String[] setPayNum() {
		if (!TextUtils.isEmpty(pay_nums)) {
			String end = pay_nums.replace(" ", "");
			String[] data = end.split(",");
			return data;
		}
		return null;

	}

	public String getChannel_name_en() {
		return channel_name_en;
	}

	public void setChannel_name_en(String channel_name_en) {
		this.channel_name_en = channel_name_en;
	}
}
