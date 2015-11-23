package com.game.sdkclass;

import java.io.Serializable;

public class GameOpen implements Serializable {
	// "id": "222",
	// "game_name": "少年三国志",
	// "apk_url": "http://dwz.cn/Gp8eV",
	// "sid": "257",
	// "gid": "222",
	// "open_time": "1429869600",
	// "wap_img": "./assets/uploads/images/2015/04/10/552780523f689.jpg",
	// "game_desc": "",
	// "game_type": "战争策略"
	private String id;
	private String game_name;
	private String apk_url;
	private String sid;
	private String gid;
	private String open_time;
	private String wap_img;
	private String game_desc;
	private String game_type;
	private String small_img;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public String getApk_url() {
		return apk_url;
	}

	public void setApk_url(String apk_url) {
		this.apk_url = apk_url;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getOpen_time() {
		return open_time;
	}

	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}

	public String getWap_img() {
		return wap_img;
	}

	public void setWap_img(String wap_img) {
		this.wap_img = wap_img;
	}

	public String getGame_desc() {
		return game_desc;
	}

	public void setGame_desc(String game_desc) {
		this.game_desc = game_desc;
	}

	public String getGame_type() {
		return game_type;
	}

	public void setGame_type(String game_type) {
		this.game_type = game_type;
	}

	public String getSmall_img() {
		return small_img;
	}

	public void setSmall_img(String small_img) {
		this.small_img = small_img;
	}

}
