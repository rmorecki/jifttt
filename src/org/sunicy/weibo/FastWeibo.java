package org.sunicy.weibo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class FastWeibo {
	public static final String HOST_NAME = "api.weibo.cn"; 
	public static final String TAG_SESSION_ID = "sid";
	public static final String TAG_GSID = "gsid";
	public static final String TAG_OAUTH_TOKEN = "oauth_token";
	public static final String TAG_OAUTH_TOKEN_SECRET = "oauth_token_secret";
	public static final String TAG_UID = "uid";
	public static final String TAG_NICK_NAME = "nick";
	public static final String TAG_STATUS = "status";
	public static final String TAG_RESULT = "result";
	public static final String TAG_MBLOGID = "mblogid";
	public static final String TAG_ERRNO = "errno";
	
	
	private HttpClient client = new HttpClient();
	private UserData userData = new UserData();
	private boolean loginStatus = false;
	
	public FastWeibo() {
		client.getHostConfiguration().setHost(HOST_NAME);
	}

	/* 获得baseString的MD5码 */
	public String hexDigest(String baseString) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(baseString.getBytes());
			byte[] md5Bytes = md5.digest();
			String hex = "";
			for (int i = 0; i < md5Bytes.length; i++) {
				String doubleBit = Integer.toHexString(md5Bytes[i] & 0xff);
				hex += (doubleBit.length() == 1) ? "0" + doubleBit : doubleBit;
			}
			return hex;

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/* 获得uid的MD5码，并生成CheckSum */
	private String calcChecksum(String uid) {
		char[] md5Bytes = hexDigest(uid + "5l0WXnhiY4pJ794KIJ7Rw5F45VXg9sjo")
				.toCharArray();
		return String.format("%c%c%c%c%c%c%c%c", md5Bytes[1], md5Bytes[5],
				md5Bytes[2], md5Bytes[10], md5Bytes[17], md5Bytes[9],
				md5Bytes[25], md5Bytes[27]);
	}

	public InputStream doGet(String url, String queryString) {
		GetMethod method = new GetMethod(url);
		method.setQueryString(queryString);
		byte[] responseBytes = null;
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				responseBytes = method.getResponseBody();
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ByteArrayInputStream(responseBytes);
	}

	public String doPost(String url, String queryString, Map<String, String> params) {
		PostMethod method = new PostMethod(url);
		method.setQueryString(queryString);
		Part[] parts = new Part[params.size()];
		int partsI = 0;
		for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
			String paramKey = (String)it.next();
			StringPart sp = new StringPart(paramKey, params.get(paramKey));
			sp.setCharSet("UTF-8");
			parts[partsI++] = sp;
		}
		method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
		
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				return method.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setUserAgent(String userAgent) {
		userData.setUserAgent(userAgent);
	}
	
	public String getUserAgent() {
		return userData.getUserAgent();
	}
	
	public void setClient(String client) {
		userData.setClient(client);
	}
	
	public String getClient() {
		return userData.getClient();
	}
	
	public boolean isLogin() {
		return loginStatus;
	}
	
	public void setUsername(String username) {
		userData.setUsername(username);
	}
	
	public void setPassword(String password) {
		userData.setPassword(password);
	}
	
	/* 简易获取器 */
	private String getItemContent(String src, String tagName) {
		int start = src.indexOf("<" + tagName + ">") + tagName.length() + 2;
		if (start <= 0)
			return null;
		int end = src.indexOf("</" + tagName + ">");
		if (end <= 0)
			return null;
		return src.substring(start, end);
	}
	
	/* 登录， 成功返回true */
	public boolean login() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ua", userData.getUserAgent());
		map.put("u", userData.getUsername());
		map.put("s",  calcChecksum(userData.getUsername() + userData.getPassword()));
		map.put("c", userData.getClient());
		map.put("p", userData.getPassword());	
		String loginResponse = doPost("/interface/f/ttt/v3/login.php", "wm="+ userData.getWm() +"&from="
				+ userData.getFrom() +"&c=" + userData.getClient() + "&ua="+ userData.getUserAgent() +"&lang=1", map);
		
		System.out.println("Login Response: " + loginResponse);
		loginStatus = (!loginResponse.matches("<" + TAG_ERRNO + ">")) && (getItemContent(loginResponse, TAG_SESSION_ID) != null);
		if (loginStatus) {
			userData.setSessionId(getItemContent(loginResponse, TAG_SESSION_ID));
			userData.setGsid(getItemContent(loginResponse, TAG_GSID));
			userData.setNickName(getItemContent(loginResponse, TAG_NICK_NAME));
			userData.setUid(getItemContent(loginResponse, TAG_UID));
		}
		return loginStatus;
	}
	
	/* 更新状态 , 成功返回微博id， 失败返回null*/
	public String updateStatus(String status) {
		if (loginStatus) {
			Map<String, String> map = new HashMap<String, String>();

			map.put("ua", userData.getUserAgent());
			map.put("content", status);
			map.put("s",  calcChecksum(userData.getUid()));
			map.put("c", userData.getClient());
			map.put("act", "add");
			map.put("wm", userData.getWm());
			map.put("from", userData.getFrom());
			
			String updateStatusResponse = doPost("/interface/f/ttt/v3/dealmblog.php", "gsid="+ userData.getGsid() 
					+"&wm=" + userData.getWm() + "&from="+ userData.getFrom() +"&c="+ userData.getClient() 
					+"&ua=" + userData.getUserAgent() + "&lang=1", map);
			if (updateStatusResponse == null)
				return null;
			else if (getItemContent(updateStatusResponse, TAG_ERRNO) != null)
				return null;
			if (getItemContent(updateStatusResponse, TAG_RESULT).equals("1"))
				return getItemContent(updateStatusResponse, TAG_MBLOGID);
		}
		return null;
	}
}
