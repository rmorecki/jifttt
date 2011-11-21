/**
 * 
 */
package org.sunicy.ift.server;

/**
 * @author sunicy
 *
 */
public class TaskManagementRequest implements ITaskManagementRequest {

	private TaskManagement taskManagement = null;
	
	public TaskManagementRequest() {
		
	}
	
	public TaskManagementRequest(TaskManagement taskManagement) {
		setTaskManagement(taskManagement);
	}
	
	public void setTaskManagement(TaskManagement taskManagement) {
		this.taskManagement = taskManagement;
	}
	
	/* (non-Javadoc)
	 * @see org.sunicy.ift.server.ITaskManagementRequest#addRequest(org.sunicy.ift.server.RequestContent)
	 */
	@Override
	public boolean addRequest(RequestContent request) {
		taskManagement.getRequestQueue().add(new RequestContent(request)); //保证是全新的对象!
		return true;
		//TODO 不一定都是true！
	}

}
