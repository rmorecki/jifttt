/*
 * 此接口为所有监视插件需要实现的接口，其接口面向MonitorSubTaskHandler实例
 * 接口中所有的过程均采用阻塞式调用方式，无需考虑多线程环境
 * 
 */
package org.sunicy.ift.server;

import org.json.simple.JSONObject;

public interface IMonitorSubTask {
	/*一下两个函数（setArgs(), setStatus()）互斥[结果互相覆盖]*/
	/*设置参数，调用后SubTask认为任务重启*/
	public SubTaskCallbackMessage setArgs(JSONObject args);
	
	/*设置运行状态，调用后子任务认为任务正在继续*/
	public SubTaskCallbackMessage setStatus(JSONObject status);
	
	/*监视子任务的执行过程，所有监视事件在此填充*/
	public SubTaskCallbackMessage doMonitor();
	
	/*返回MonitorSubTask的现场信息，以便进行状态备份和还原（如：pause、resume）*/
	public JSONObject getStatus();
	
	/*根据子任务优先级返回监视步长，即每多少个短时间片进行一次“doMonitor”*/
	public int getMonitorInterval(int priority);
}
