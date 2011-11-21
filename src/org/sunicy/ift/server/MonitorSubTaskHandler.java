/**
 * 此类用于直接维护和控制 IMonitorSubTask实例
 * 内置Timer，进行“守护”级调度
 * 由于类内需要维护Timer，因此涉及主线程与TimerTask的消息调度
 */
package org.sunicy.ift.server;

import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONObject;
import org.sunicy.ift.util.BlockingObject;

/**
 * @author sunicy
 *
 */
public class MonitorSubTaskHandler {
	//public static final String MSG_FIELD_CALLBACK_REQUEST
	public static final String MSG_FIELD_PRIORITY = "priority";
	public static final String MSG_FIELD_MONITOR_SUBTASK_ARGS = "args";
	//后面args的含义
	public static final String MSG_FIELD_ARGS_MEANING = "argsMeaning";
		public static final String MSG_ARGS_MEANING_STATUS = "status";
	public static final String MSG_FIELD_SUBTASKID = "subTaskId";
	public static final int PRIORITY_DEFAULT = 50;
	
	//具体状态编号
	public static final int STATUS_STOP = 1;
	public static final int STATUS_MONITOR = 20;
	public static final int STATUS_PAUSE = 30;
	public static final int STATUS_FAIL = 4;
	public static final int STATUS_ABORT = 5;
	
	//请求类型（pause、modify），便于向主线程回传结果的时候知道自己在做什么- -#
	public static final String MSG_FIELD_REQUEST_TYPE = "requestType";
		public static final String MSG_REQUEST_TYPE_PAUSE = "pause";
		public static final String MSG_REQUEST_TYPE_MODIFY = "modify";
	
	private ITaskManagementRequest taskManagementRequest;
	//子任务状态
	private int subTaskStatus = STATUS_STOP;
	//子任务优先级
	private int priority;
	//子任务的subTaskId
	private String subTaskId;
	//守护周期（基本time span长度）
	private int subTaskTimeSpan;
	//SubTask传递而来的time span （getMonitorInterval()）
	private int subTaskMonitorInterval;
	//用以记录IMonitorSubTask反馈的运行现场
	private JSONObject monitorSubTaskStatus;	
	
	//记录来自TaskManagement的请求（status、args互斥），格式{"requestType": "modify", "args":"..."}(TODO容器)
	private BlockingObject<JSONObject> request = null;
	
	//IMonitorSubTask实例
	private IMonitorSubTask monitorSubTask;
	
	//实际运行的守护、处理线程
	private Timer timer;
	
	private void setSubTaskTimeSpan() { 
		subTaskTimeSpan = 5000;
	}
	
	//subTaskArgs指针对“Handler”的参数，其格式为{"priority":5, "args":"IMonitorSubTask的参数"}
	private void setMonitorSubTaskHandler(JSONObject subTaskArgs) {
		//初始化Handler
		resolvePriority((Number)subTaskArgs.get(MSG_FIELD_PRIORITY));
		//顺便设置一下守护时间长度:)
		setSubTaskTimeSpan();
		//设置subTaskId
		subTaskId = (String)subTaskArgs.get(MSG_FIELD_SUBTASKID);
		//获取请求类型（重新启动或resume）
		subTaskStatus = (((String)subTaskArgs.get(MSG_FIELD_ARGS_MEANING)).equals(MSG_ARGS_MEANING_STATUS)) ? STATUS_PAUSE : STATUS_STOP;
		//将subTaskArgs压入TODO容器中
		request.setElement(subTaskArgs);
	}
	
	//解析优先级
	private void resolvePriority(Number priority) {
		if (priority == null)
			this.priority = PRIORITY_DEFAULT;
		else
			this.priority = priority.intValue();
	}
	
	//仅提供1个构造函数，原因在于Handler与IMonitorSubTask共生
	public MonitorSubTaskHandler(IMonitorSubTask monitorSubTask, JSONObject subTaskArgs, ITaskManagementRequest taskManagementRequest) {
		if (monitorSubTask == null)
			throw new NullPointerException("IMonitorSubTask is null.");
		this.taskManagementRequest = taskManagementRequest; 
		
		this.monitorSubTask = monitorSubTask;
		setMonitorSubTaskHandler(subTaskArgs);
		timer = new Timer();
	}
	
	//获取MonitorSubTask的状态（由MonitorSubTask自行维护）
	public JSONObject getMonitorSubTaskStatus() {
		return monitorSubTaskStatus;
	}
	
	//获取子任务状态，结果为状态常量
	public int getSubTaskStatus() {
		return subTaskStatus;
	}
	
	//重新启动Timer线程，前提为
	public boolean start() {
		//若正在监视或暂停，则返回false
		if (getSubTaskStatus() >= STATUS_MONITOR)
			return false;
		
		doSchedule();
		return true;
	}
	
	//modify 与 pause 同时仅能有一个生效
	//modify中的args为MonitorSubTask的参数！
	public boolean modify(JSONObject args) {
		if (args == null)
			return false;
		//加入“修改”标识符
		JSONObject o = new JSONObject();
		o.put(MSG_FIELD_MONITOR_SUBTASK_ARGS, args);
		o.put(MSG_FIELD_REQUEST_TYPE, MSG_REQUEST_TYPE_MODIFY);
		//阻塞加入
		request.setElement(o);
		return true;
	}
	
	public boolean pause() {
		//当且仅当正在运行时可以pause
		if (getSubTaskStatus() != STATUS_MONITOR)
			return false;
		//阻塞加入
		request.setElement((JSONObject)(new JSONObject()).put(MSG_FIELD_REQUEST_TYPE, MSG_REQUEST_TYPE_PAUSE));
		return true;
	}
	
	public boolean resume() {
		if (getSubTaskStatus() != STATUS_PAUSE)
			return false;
		doSchedule();
		return true;
	}
	
	private void sendBackSubTaskCallbackMessage(SubTaskCallbackMessage msg) {
		JSONObject o = new JSONObject();
		//o.put(key, value)
	}
	
	private void doSchedule() {
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//检查request，若有则直接调用
				if (request != null) {
					JSONObject req = request.getElement();
					String requestType = (String)req.get(MSG_FIELD_REQUEST_TYPE);
					if (requestType.equals(MSG_REQUEST_TYPE_MODIFY)) {
						SubTaskCallbackMessage result = monitorSubTask.setStatus((JSONObject)req.get(MSG_FIELD_MONITOR_SUBTASK_ARGS));
						if (((Number)result.getResult()).intValue() == SubTaskCallbackMessage.MSG_FAILED)
							
					}
					else if (requestType.equals(MSG_REQUEST_TYPE_PAUSE)) {
						
					}
				}
				
			}
		}, 0, subTaskTimeSpan);
	}
	
}
