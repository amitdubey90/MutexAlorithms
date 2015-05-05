package com.mutex.algo;

import java.util.concurrent.atomic.AtomicInteger;

public class FilterExample {
	
	final private ThreadLocal<Integer> THREAD_ID = new ThreadLocal<Integer>() {
		final private AtomicInteger id = new AtomicInteger(0);

		protected Integer initialValue() {
			return id.getAndIncrement();
		}
	};

	// We use AtomicInteger here.
	// Using int would not work because of memory model issues ---
	// Java memory model is not sequentially consistent.
	// Using volatile int would not work, because there is no way of declaring
	// an array element volatile.
	final private AtomicInteger[] level;
	final private AtomicInteger[] victim;
	final private int n;

	public FilterExample(int n) {
		this.n = n;
		level = new AtomicInteger[n];
		victim = new AtomicInteger[n]; // use 1.. n-1
		for (int i = 0; i < n; i++) {
			level[i] = new AtomicInteger();
			victim[i] = new AtomicInteger();
		}
		//System.out.println(THREAD_ID.get() +"<>"+ THREAD_ID.get() );
	}

	public void lock() {
		int me = THREAD_ID.get();
		for (int i = 1; i < this.n; i++) { // attempt level 1
			//System.out.println("Attempting Level " + i +" for "+me);
			level[me].set(i);
			victim[i].set(me);
			// spin while conflicts exist
			while (exists(me, i)) {
			}
			;
			System.out.println(THREAD_ID.get() +" got lock for level "+ i);
		}
	}

	private boolean exists(int me, int i) {
		for (int k = 0; k < this.n; k++) {
			if (k == me)
				continue;

			if (level[k].get() >= i && victim[i].get() == me)
				return true;
		}
		return false;
	}

	public void unlock() {
		int me = THREAD_ID.get();
		level[me].set(0);
	}

	private final static int THREADS = 3;
	private final static int COUNT = 3 * 1;
	private final static int PER_THREAD = COUNT / THREADS;
	Thread[] thread = new Thread[THREADS];
	volatile int counter = 0;
	FilterExample instance = this;
	
	public void test() throws InterruptedException {
		System.out.println("test parallel");
		// ThreadID.reset();
		for (int i = 0; i < THREADS; i++) {
			thread[i] = new MyThread();
			
		}
		for (int i = 0; i < THREADS; i++) {
			thread[i].start();
		}
		for (int i = 0; i < THREADS; i++) {
			thread[i].join();
		}
		if (counter != COUNT) {
			System.out.println("Wrong! " + counter + " " + COUNT);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new FilterExample(THREADS).test();
	}

	class MyThread extends Thread {
		public void run() {
			//System.out.println("I am thread  :"+THREAD_ID.get());
			for (int i = 0; i < PER_THREAD; i++) {
				instance.lock();
				try {
					System.out.println(THREAD_ID.get() +" got lock");
					counter = counter + 1;
				} finally {
					instance.unlock();
				}
			}
			// System.out.println("ThreadID: "+ThreadID.get());
		}
	}
}
