package com.mutex.resource;

public class SharedResource {
	
	public volatile int sharedCounter;
	
	public void increment(){
		sharedCounter += 1;
	}
	
	public int get() {
		return sharedCounter;
	}
}
