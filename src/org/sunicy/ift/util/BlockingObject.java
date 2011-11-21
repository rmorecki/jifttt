/**
 * 阻塞型对象，保证线程安全，对象的存/取操作原子化
 */
package org.sunicy.ift.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunicy
 *
 */
public class BlockingObject<E> {
	private E o = null;
	private Lock lock = new ReentrantLock();
	
	public void setElement(E o) {
		lock.lock();
		
		try {
			this.o = o;
		}
		finally {
			lock.unlock();
		}
	}
	
	public E getElement() {
		lock.lock();
		E o = null;
		try {
			o = this.o;
		}
		finally {
			lock.unlock();
		}
		return o;
	}
}
