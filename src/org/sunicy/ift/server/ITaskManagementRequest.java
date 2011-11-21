/**
 * 
 */
package org.sunicy.ift.server;

/**
 * @author sunicy
 *
 */
public interface ITaskManagementRequest {
	//向TaskManager递交申请(用于Monitor和Executor们)
	public boolean addRequest(RequestContent request);
}
