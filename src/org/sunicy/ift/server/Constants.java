package org.sunicy.ift.server;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Constants {
	//监视、消息收取进程（ServerSocket获取Socket，其后将Socket转发至QueryCallback线程中）
	public static final int THREAD_MONITOR_PORT = 54321;
	
	//以下为消息原语。MSG_FIELD_XXX：XXX的字段名；MSG_XXX_???: XXX含义为???的取值
	public static final String MSG_FIELD_TYPE = "type";
		//添加一项任务（或子任务）
		public static final String MSG_TYPE_ADD = "add";
		//中断一项任务（或子任务）
		public static final String MSG_TYPE_ABORT = "abort";
		//暂停一项任务（或子任务）
		public static final String MSG_TYPE_PAUSE = "pause";
		//Model向TaskManagement回传任务完成情况
		public static final String MSG_TYPE_CALLBACK = "callback";
	
	//客户端的信息获取请求
	public static final String MSG_FIELD_INFO = "info";
	//任务字段
	public static final String MSG_FIELD_TASK = "task";
	
	//子任务字段
	public static final String MSG_FIELD_SUBTASK = "subtask";
	
	//反馈注释（任务、子任务均可，适用于任何操作）
	public static final String MSG_FIELD_COMMENT = "comment";
	
	//任务（子任务）完成状态
	public static final String MSG_FIELD_STATUS = "status";
		public static final String MSG_STATUS_SUCCESS = "success";
		public static final String MSG_STATUS_FAIL = "fail";
		public static final String MSG_STATUS_RUNNING = "running";
		
	//模块编号，用于各类原语
	public static final String MSG_FIELD_MODEL_ID = "modelId";
	//监视原语（用于添加新任务，e.g.，“(A&B)|C”代表“当且仅当C发生或A、B同时发生时，激活Executor们”）
	public static final String MSG_FIELD_MONITOR = "monitor";
	//动作原语（用于添加新任务，e.g.,“DE”代表条件成熟时，同时执行DE）
	public static final String MSG_FIELD_ACTION = "action";
	//监视、执行模块参数原语(用于设置任务模块详细安排，如：{"A":{"modelId": "xxx", "args": {具体参数}}, "B": {"modelId": "xxxx", "args": {xx}}})
	public static final String MSG_FIELD_MODEL_SET = "modelSet";
		//在参数原语中表示各模块的参数
		public static final String MSG_FIELD_ARGUMENT = "args";
	
	public static String getRandomString(int length) {
		String s = "";
		for (int i = 0; i < length; i++)
			s += (Math.random() >= 0.5) ? (char)((int)(Math.random() * 26) + 'A') : (char)((int)(Math.random() * 26) + 'a');
		return s;
	}
	
	public static String getRandomId() {
		//始终8位
		return getRandomString(8);
	}
	
	//消息正确且有类型，则返回类型，否则置空
	public static String getMessageType(String msg) {
		try {
			String type = (String)((JSONObject)JSONValue.parse(msg)).get(MSG_FIELD_TYPE);
			return type;
		}
		catch (Exception e) {
			return null;
		}
	}
}
