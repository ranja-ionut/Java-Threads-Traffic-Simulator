package com.apd.tema2.intersections;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import com.apd.tema2.entities.Intersection;

public class SimpleMaintenanceIntersection implements Intersection {
	private int capacity;
	private Semaphore[] laneSemaphores;
	private CyclicBarrier[] barriers;
	
	public SimpleMaintenanceIntersection() {
		this(0);
	}
	
	public SimpleMaintenanceIntersection(int capacity) {
		this.capacity = capacity;
		laneSemaphores = new Semaphore[2];
		barriers = new CyclicBarrier[1];
		
		barriers[0] = new CyclicBarrier(capacity);
		laneSemaphores[0] = new Semaphore(capacity);
		laneSemaphores[1] = new Semaphore(0);
	}

	@Override
	public Object getFirst() {
		return capacity;
	}

	@Override
	public Object getSecond() {
		return null;
	}

	@Override
	public Object getThird() {
		return null;
	}

	@Override
	public Semaphore[] getSemaphores() {
		return laneSemaphores;
	}

	@Override
	public CyclicBarrier[] getBarriers() {
		return barriers;
	}

}
