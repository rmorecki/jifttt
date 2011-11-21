package org.sunicy.ift.server;

public class RequestContent {
	public String request;
	public Object obj; //一般没用- -#，主要用作传递Socket
	
	public RequestContent(String request, Object obj) {
		this.request = request;
		this.obj = obj; 
	}
	
	//拷贝构造函数
	public RequestContent(RequestContent rc) {
		this.request = rc.request;
		this.obj = rc.obj;
	}
}
