package org.sunicy.ift.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataStorage implements IDataStorage {
	
	//根据subTaskId寻找TaskDetail
	private Map<String, TaskDetail> subTaskIdToTask = new HashMap<String, TaskDetail>();
	//根据taskId寻找TaskDetail
	private Map<String, TaskDetail> tasks = new HashMap<String, TaskDetail>();

	@Override
	public boolean addTask(String taskId, String expression, String comment, 
			SubTaskDetail[] subTaskDetail) {
		if (tasks.get(taskId) == null)
			return false;
		TaskDetail task = new TaskDetail(taskId, expression, comment, subTaskDetail);
		tasks.put(taskId, task);
		for (int i = 0; i < subTaskDetail.length; i++)
			subTaskIdToTask.put(subTaskDetail[i].getSubTaskId(), task);
		return true;
	}

	@Override
	public boolean modifyComment(String taskId, String comment) {
		if (tasks.get(taskId) == null)
			return false;
		tasks.get(taskId).setComment(comment);
		return true;
	}

	@Override
	public boolean modifySubTaskArgs(String subTaskId, JSONObject newArgs) {
		if (subTaskIdToTask.get(subTaskId) == null)
			return false;
		
		subTaskIdToTask.get(subTaskId).getSubTask(subTaskId).setArgs(newArgs);
		return true;
	}

	@Override
	public boolean modifySubTaskStatus(String subTaskId, JSONObject newStatus) {
		if (subTaskIdToTask.get(subTaskId) == null)
			return false;
		
		subTaskIdToTask.get(subTaskId).getSubTask(subTaskId).setStatus(newStatus);
		return true;
	}

	@Override
	public boolean modifySubTaskCondition(String subTaskId, int conditionCode) {
		if (subTaskIdToTask.get(subTaskId) == null)
			return false;
		subTaskIdToTask.get(subTaskId).getSubTask(subTaskId).setCondition(conditionCode);
		return true;
	}

	@Override
	public boolean modifyTaskCondition(String taskId, int conditionCode) {
		if (tasks.get(taskId) == null)
			return false;
		
		tasks.get(taskId).setCondition(conditionCode);
		return true;
	}

	@Override
	public JSONObject getTaskInfoDetail(String taskId) {
		TaskDetail task = tasks.get(taskId);
		JSONObject info = new JSONObject();
		info.put(Constants.MSG_FIELD_TASKID, task.getTaskId());
		info.put(Constants.MSG_FIELD_CONDITION, task.getCondition());
		info.put(Constants.MSG_FIELD_EXPRESSION, task.getExpression());
		info.put(Constants.MSG_FIELD_COMMENT, task.getComment());
		JSONArray subTaskArray = new JSONArray();
		SubTaskDetail[] subTaskList = task.getAllSubTaskDetail();
		for (int i = 0; i < subTaskList.length; i++) {
			JSONObject subTask = new JSONObject();
			subTask.put(Constants.MSG_FIELD_SUBTASKID, subTaskList[i].getSubTaskId());
			subTask.put(Constants.MSG_FIELD_MODEL_ID, subTaskList[i].getModelId());
			subTask.put(Constants.MSG_FIELD_ARGUMENT, subTaskList[i].getArgs());
			subTask.put(Constants.MSG_FIELD_CONDITION, subTaskList[i].getCondition());
			
			subTaskArray.add(subTask);
		}
		info.put(Constants.MSG_FIELD_SUBTASK, subTaskArray);
		
		return info;
	}

	@Override
	public JSONArray getAllTaskInfoDetail() {
		JSONArray list = new JSONArray();
		String[] taskIdList = getAllTaskId();
		for (int i = 0; i < taskIdList.length; i++) {
			list.add(getTaskInfoDetail(taskIdList[i]));
		}
		return list;
	}

	@Override
	public int getTaskCondition(String taskId) {
		if (tasks.get(taskId) == null)
			return TaskDetail.TASK_COND_ERR;
		else
			return tasks.get(taskId).getCondition();
	}

	@Override
	public int getSubTaskCondition(String subTaskId) {
		if (subTaskIdToTask.get(subTaskId) == null)
			return SubTaskDetail.SUBTASK_COND_ERR;
		else
			return subTaskIdToTask.get(subTaskId).getCondition();
	}

	@Override
	public String[] getSubTasksOfTask(String taskId) {
		if (tasks.get(taskId) == null)
			return null;
		
		return tasks.get(taskId).getAllSubTaskId();
	}

	@Override
	public JSONObject getSubTaskArgs(String subTaskId) {
		if (subTaskIdToTask.get(subTaskId) == null)
			return null;
		
		return subTaskIdToTask.get(subTaskId).getSubTask(subTaskId).getArgs();
	}

	@Override
	public JSONObject getSubTaskStatus(String subTaskId) {
		if (subTaskIdToTask.get(subTaskId) == null)
			return null;
		
		return subTaskIdToTask.get(subTaskId).getSubTask(subTaskId).getStatus();		
	}

	@Override
	public String getParentTaskId(String subTaskId) {
		if (subTaskIdToTask.get(subTaskId) == null)
			return null;
		else
			return subTaskIdToTask.get(subTaskId).getTaskId();
	}

	@Override
	public String getTaskExpression(String taskId) {
		if (tasks.get(taskId) == null)
			return null;
		
		return tasks.get(taskId).getExpression();
	}

	@Override
	public String[] getAllTaskId() {
		Iterator it = tasks.entrySet().iterator();
		String[] taskIdList = new String[tasks.size()];
		int i = 0;
		while (it.hasNext())
			taskIdList[i++] = (String)((Entry)it.next()).getKey();
		return taskIdList;
	}
	
}
