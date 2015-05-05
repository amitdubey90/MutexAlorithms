package com.mutex.algo;

import java.util.concurrent.atomic.AtomicInteger;

import com.mutex.ifc.Lock;

public class Filter implements Lock {

	private AtomicInteger[] level = null;
	private AtomicInteger[] waiting = null;
	private int numThreads;

	public Filter(int numThreads) {
		this.numThreads = numThreads;
		level = new AtomicInteger[numThreads];
		waiting = new AtomicInteger[numThreads];

		for (int i = 0; i < numThreads; i++) {
			level[i] = new AtomicInteger(-1);
			waiting[i] = new AtomicInteger(-1);
		}
	}

	public void lock(int threadId) {
		for (int i = 1; i < this.numThreads; i++) { // attempt level 1
			// System.out.println("Attempting Level " + i +" for "+me);
			level[threadId].set(i);
			waiting[i].set(threadId);
			// spin while conflicts exist
			while (cantLock(threadId, i))
				;

			// System.out.println(threadId +" got lock for level "+ i);
		}
	}

	public boolean cantLock(int threadId, int threadLevel) {
		for (int i = 0; i < numThreads; i++) {
			if (i == threadId)
				continue;

			if (level[i].get() >= threadLevel
					&& waiting[threadLevel].get() == threadId)
				return true;
		}
		return false;
	}

	public void unlock(int threadId) {
		level[threadId].set(-1);
	}
}
