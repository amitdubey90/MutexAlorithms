package com.mutex.algo;

import com.mutex.ifc.Lock;

public class Peterson implements Lock {

	boolean[] flag = new boolean[2];

	int turn;

	public Peterson() {
		flag[0] = false;
		flag[1] = false;

		turn = 1;
	}

	public void lock(int threadId) {
		flag[threadId] = true;

		int otherProcessID = (threadId == 0 ? 1 : 0);

		turn = otherProcessID;

		while (flag[otherProcessID] == true && turn == otherProcessID)
			; // wait

	}

	public void unlock(int threadId) {
		flag[threadId] = false;

	}

}
