package com.apd.tema2.intersections;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class SimpleStrictIntersection implements Intersection {
	private int capacity, time, lanes;
	private Semaphore[] laneSemaphores;
	private CyclicBarrier[] barriers;
	
	public SimpleStrictIntersection() {
		this(0, 0, 0);
	}
	
	// simple_strict_1
	public SimpleStrictIntersection(int time, int lanes) {
		this(1, time, lanes);
	}
	
	// simpel_strict_x
	public SimpleStrictIntersection(int capacity, int time, int lanes) {
		this.capacity = capacity;
		this.time = time;
		this.lanes = lanes;
		
		laneSemaphores = new Semaphore[lanes];
		barriers = new CyclicBarrier[2];
		
		for (int i = 0; i < lanes; i++) {
			laneSemaphores[i] = new Semaphore(capacity);
		}
		
		barriers[0] = new CyclicBarrier(Main.carsNo);
		barriers[1] = new CyclicBarrier(lanes * capacity);
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
		return lanes;
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
