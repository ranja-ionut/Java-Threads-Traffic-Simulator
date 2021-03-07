package com.apd.tema2.intersections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import com.apd.tema2.entities.Intersection;

public class PriorityIntersection implements Intersection {
	@SuppressWarnings("unused")
	private int highCars, lowCars;
	private AtomicInteger totalHigh;
	private Semaphore[] prioritySemaphore;
	private ArrayBlockingQueue<Integer> queue;
	
	public PriorityIntersection() {
		this(0, 0);
		totalHigh = new AtomicInteger(0);
	}
	
	public PriorityIntersection(int high_cars, int low_cars) {
		this.highCars = high_cars;
		this.lowCars = low_cars;
		totalHigh = new AtomicInteger(0);
		queue = new ArrayBlockingQueue<>(low_cars);
		
		prioritySemaphore = new Semaphore[1];
		
		prioritySemaphore[0] = new Semaphore(low_cars);
	}
	
	@Override
	public Object getFirst() {
		return queue;
	}

	@Override
	public Object getSecond() {
		return lowCars;
	}

	@Override
	public Object getThird() {
		return totalHigh;
	}

	@Override
	public Semaphore[] getSemaphores() {
		return prioritySemaphore;
	}

	@Override
	public CyclicBarrier[] getBarriers() {
		return null;
	}

}
