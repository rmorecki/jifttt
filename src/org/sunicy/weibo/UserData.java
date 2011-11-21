package org.sunicy.weibo;

public class UserData {
	private String uid;		
	private String username;//u
	private String password;//p
	private String gsid;	//gsid
	private String userAgent;//ua
	private String from;	//from
	private String wm;		//wm
	private String lang;	//lang
	private String client;	//c
	private String sessionId;//sid	
	private String nickName; //nick
	
	public UserData() {
		client = "android";
		userAgent = "Sunicy_Web_Browser";
		from = "10255010";
		wm = "3333";
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGsid() {
		return gsid;
	}
	public void setGsid(String gsid) {
		this.gsid = gsid;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getWm() {
		return wm;
	}
	public void setWm(String wm) {
		this.wm = wm;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
