package com.mutex.test;

import com.mutex.algo.Filter;
import com.mutex.resource.SharedResource;
import com.mutex.worker.WorkerThread;

public class PetersonTest {
	private static int numThreads;
	private static int perThread;
	private static int count;
	Thread[] thread;
	Filter instance;
	SharedResource resource;
	
	public PetersonTest(int numThreads, int perThread) {
		PetersonTest.numThreads = numThreads;
		PetersonTest.perThread = perThread;
		PetersonTest.count = numThreads * perThread;
		instance = new Filter(numThreads);
		thread = new Thread[numThreads];
		resource = new SharedResource();
	}
	
	public void test() throws InterruptedException {

		 long now = System.currentTimeMillis();
		for (int i = 0; i < numThreads; i++) {
			thread[i] = new WorkerThread(i, perThread, instance, resource);
		}
		for (int i = 0; i < numThreads; i++) {
			thread[i].start();
		}
		for (int i = 0; i < numThreads; i++) {
			thread[i].join();
		}

		System.out.println("Expected output: " + count + "\t Final output: "
				+ resource.get());

		 System.out.println("Total time taken :"
		 + (System.currentTimeMillis() - now));
	}
	
	public static void main(String[] args) throws InterruptedException {
		int numThreads = 2; // Peterson assumes only 2 threads are working
		int perThread = 100;
		new PetersonTest(numThreads, perThread).test();
	}
}
