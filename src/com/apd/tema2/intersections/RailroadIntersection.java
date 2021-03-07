package com.apd.tema2.intersections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class RailroadIntersection implements Intersection {
	private CyclicBarrier[] barriers;
	private ArrayBlockingQueue<Integer> queue;
	
	public RailroadIntersection() {
		barriers = new CyclicBarrier[1];
		barriers[0] = new CyclicBarrier(Main.carsNo);
		queue = new ArrayBlockingQueue<>(Main.carsNo);
	}
	
	@Override
	public Object getFirst() {
		return queue;
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
		return null;
	}

	@Override
	public CyclicBarrier[] getBarriers() {
		return barriers;
	}

}
