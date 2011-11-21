package org.sunicy.ift.server;

public interface IMonitor {
	//向Monitor递交申请（放入阻塞队列中）
	public boolean addRequest(RequestContent request);
	
	//设置Monitor可以访问到的ManagementAdapter
	//（用于将Callback、请求等信息加入主处理线程（TaskManagement）的阻塞队列中）
	public void setTaskManagementAdapter(ITaskManagementRequest taskManagementRequest);
}
