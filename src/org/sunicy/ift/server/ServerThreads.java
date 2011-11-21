/**
 * 
 */
package org.sunicy.ift.server;

/**
 * @author sunicy
 *
 */
class ServerThreads {
	private static MessageMonitor messageMonitor;
	private static QueryCallBack queryCallBack;
	private static TaskManagement taskManagement;
	private static ExecutionThreadPool executionThreadPool;
	private static DataStorage dataStorage;
	
	public static MessageMonitor getMessageMonitor() {
		return messageMonitor;
	}
	
	public static QueryCallBack getQueryCallBack() {
		return queryCallBack;
	}
	
	public static TaskManagement getTaskManagement() {
		return taskManagement;
	}
	
	public static ExecutionThreadPool getExecutionThreadPool() {
		return executionThreadPool;
	}
	
	public static DataStorage getDataStorage() {
		return dataStorage;
	}
	
	public static void main(String[] args) {
		//TODO: new 各种线程
	}
}
