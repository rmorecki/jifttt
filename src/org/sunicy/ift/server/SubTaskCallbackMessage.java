/**
 * 此类用作封装 IMonitorSubTask、IExecuteSubTask类 的返回消息
 * 内容主要包括	int result: 表示执行成功与否（值需参照类内常量）
 * 				String comment:返回注释
 */
package org.sunicy.ift.server;


/**
 * @author sunicy
 *
 */
public class SubTaskCallbackMessage {
	public static final int MSG_SUCCESS = 0;
	public static final int MSG_RUNNING = 2;
	public static final int MSG_FAILED = -1;
	
	private int result;
	private String comment = null;
	
	public SubTaskCallbackMessage(int result) {
		this.result = result;
	}
	
	public SubTaskCallbackMessage(int result, String comment) {
		this.result = result;
		this.comment = comment;
	}
	
	public int getResult() {
		return result;
	}
	
	public String getComment() {
		return comment;
	}
}
