/**
 * 
 */
package org.sunicy.ift.server;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author sunicy
 *
 */
public class TaskManagement implements Runnable{
	private static final int BLOCKING_QUEUE_CAPACITY = 40;
	private ArrayBlockingQueue<RequestContent> requestQueue = new ArrayBlockingQueue<RequestContent>(BLOCKING_QUEUE_CAPACITY);
	private ITaskManagementRequest taskManagementRequest;
	
	public TaskManagement() {
		taskManagementRequest = new TaskManagementRequest(this);
	}
	
	public ITaskManagementRequest getTaskManagementRequest() {
		return taskManagementRequest;
	}
	
	public ArrayBlockingQueue<RequestContent> getRequestQueue() {
		return requestQueue;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
