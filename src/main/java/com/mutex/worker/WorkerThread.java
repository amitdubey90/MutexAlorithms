package com.mutex.worker;

import com.mutex.ifc.Lock;
import com.mutex.resource.SharedResource;

public class WorkerThread extends Thread {
	int threadId;
	int count;
	Lock instance;
	SharedResource resource;

	public WorkerThread(int threadId, int count, Lock instance, SharedResource resource) {
		this.threadId = threadId;
		this.count = count;
		this.instance = instance;
		this.resource = resource;
	}

	@Override
	public void run() {
		for (int i = 0; i < count; i++) {
			instance.lock(threadId);
			try {
				//System.out.println(threadId + " got lock. Enterign CS");
				resource.increment();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				instance.unlock(threadId);
			}
		}
	}
}