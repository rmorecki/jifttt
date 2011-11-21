package org.sunicy.ift.server;

public interface IExecutor {
	//用于向Executor递交申请
	public boolean setRequest(RequestContent request);
	
	//设置Executor可以访问到的ManagementAdapter
	//（用于将Callback、请求等信息加入主处理线程（TaskManagement）的阻塞队列中）
	public void setTaskManagementAdapter(ITaskManagementRequest taskManagementRequest);
}
