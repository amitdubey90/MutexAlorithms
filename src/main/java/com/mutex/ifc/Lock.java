package com.mutex.ifc;

public interface Lock {
	
	public void lock(int threadId);

	public void unlock(int threadId);
}
