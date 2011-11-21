/**
 * 
 */
package org.sunicy.ift.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author sunicy
 *
 */
public class MessageMonitor implements Runnable {
	private ServerSocket serverSocket;
	
	public MessageMonitor() throws IOException {
		//如若不能创建，则退出！
		serverSocket = new ServerSocket(Constants.THREAD_MONITOR_PORT);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				//读取客户端请求（Client或Servlet）
				String request = new DataInputStream(socket.getInputStream()).readUTF();
				String msgType = Constants.getMessageType(request);
				if (msgType == null) {
					//消息错误
					socket.close();
					continue;
				} 
				else if (msgType.equals(Constants.MSG_FIELD_INFO)) {
					//纯粹信息获取请求，直接转交QueryCallBack
					ServerThreads.getQueryCallBack().addRequest(new RequestContent(request, socket));
				}
				else {
					//操作请求，转交TaskManagement
					//.getTaskManagementRequest().addRequest较直接获取Queue并添加更为安全
					ServerThreads.getTaskManagement().getTaskManagementRequest().addRequest(new RequestContent(request, socket));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
