package com.mutex.test;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalExample {
	final static private ThreadLocal<Integer> THREAD_ID = new ThreadLocal<Integer>() {
		final private AtomicInteger id = new AtomicInteger(0);

		protected Integer initialValue() {
			return id.getAndIncrement();
		}
	};
	
	static class Worker extends Thread {
		public void run() {
			System.out.println("This is :"+THREAD_ID.get());
		}
	}
	
	public static void main(String[] args) {
		int numThreads = 5;
		Thread[] t = new Thread[numThreads];
		for(int i = 0 ; i< numThreads; i++){
			t[i] = new Worker();
			t[i].start();
		}
			
	}
}
