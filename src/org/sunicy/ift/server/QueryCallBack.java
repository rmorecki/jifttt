/**
 * 
 */
package org.sunicy.ift.server;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author sunicy
 *
 */
public class QueryCallBack implements Runnable{
	private static final int BLOCKING_QUEUE_CAPACITY = 30;
	private ArrayBlockingQueue<RequestContent> requestQueue = new ArrayBlockingQueue<RequestContent>(BLOCKING_QUEUE_CAPACITY);
	
	public boolean addRequest(RequestContent request) {
		try {
			requestQueue.add(new RequestContent(request));
			return false;
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	@Override
	public void run() {
		
		while (true) {
			// TODO 保持读取队列，空则等待;非空则获取信息并通过Socket发送，出现错误则丢弃Socket	
		}
	}

}
