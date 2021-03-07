package com.apd.tema2.intersections;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import com.apd.tema2.entities.Intersection;

public class SimpleIntersection implements Intersection {
	private int capacity, time;
	private Semaphore[] canEnter;
	
	public SimpleIntersection() {
		this(0, 0);
	}
	
	public SimpleIntersection(int capacity, int time) {
		this.capacity = capacity;
		this.time = time;
		canEnter = new Semaphore[1];
		canEnter[0] = new Semaphore(capacity);
	}
	
	@Override
	public Object getFirst() {
		return capacity;
	}

	@Override
	public Object getSecond() {
		return time;
	}

	@Override
	public Object getThird() {
		return 0;
	}

	@Override
	public Semaphore[] getSemaphores() {
		return canEnter;
	}

	@Override
	public CyclicBarrier[] getBarriers() {
		return null;
	}
}
