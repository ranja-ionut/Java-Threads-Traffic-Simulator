package com.apd.tema2.intersections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class ComplexMaintenanceIntersection implements Intersection {
	@SuppressWarnings("unused")
	private int free, initial, capacity;
	private int[] args; // vector care pastreaza argumentele de mai sus
	private ArrayBlockingQueue<ArrayBlockingQueue<Integer>>[] newLaneQueues;
	private ArrayBlockingQueue<Integer>[] oldLaneQueues;
	private Object[] queues, atomics; // in queues se pun cele 2 de mai sus si in atomics atomicele de mai jos
	private AtomicInteger newLane;
	private AtomicInteger[] usedCars; // numarul de masini intrate in semaforul i
	
	private CyclicBarrier[] barriers;
	private Semaphore[] semaphores;
	
	public ComplexMaintenanceIntersection() {
		this(0, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public ComplexMaintenanceIntersection(int free, int initial, int capacity) {
		this.free = free;
		this.initial = initial;
		this.capacity = capacity;
		
		args = new int[3];
		newLaneQueues = new ArrayBlockingQueue[free];
		oldLaneQueues = new ArrayBlockingQueue[initial];
		barriers = new CyclicBarrier[1];
		semaphores = new Semaphore[free + 1];
		newLane = new AtomicInteger(0);
		usedCars = new AtomicInteger[free];
		queues = new Object[2];
		atomics = new Object[2];
		
		atomics[0] = newLane;
		atomics[1] = usedCars;
		barriers[0] = new CyclicBarrier(Main.carsNo);
		args[0] = free;
		args[1] = initial;
		args[2] = capacity;
		semaphores[free] = new Semaphore(Main.carsNo);
		
		for (int i = 0; i < initial; i++) {
			oldLaneQueues[i] = new ArrayBlockingQueue<Integer>(Main.carsNo);
		}
		
		// se calcuelaza impartirea lane-urilor initiale in cele noi
		for (int i = 0; i < free; i++) {
			int start = (int) (i * (double)initial / free);
			int end = (int) Math.min((i + 1) * (double)initial / free, initial);
			
			// coada i va avea end-start lane queues
			newLaneQueues[i] = new ArrayBlockingQueue<ArrayBlockingQueue<Integer>>(end - start);
			usedCars[i] = new AtomicInteger(0);
			semaphores[i] = new Semaphore(capacity);
			
			for (int j = start; j < end; j++) {
				newLaneQueues[i].add(oldLaneQueues[j]);
			}
		}
		
		queues[0] = newLaneQueues;
		queues[1] = oldLaneQueues;
	}

	@Override
	public Object getFirst() {
		return queues;
	}

	@Override
	public Object getSecond() {
		return atomics;
	}

	@Override
	public Object getThird() {
		return args;
	}

	@Override
	public Semaphore[] getSemaphores() {
		return semaphores;
	}

	@Override
	public CyclicBarrier[] getBarriers() {
		return barriers;
	}

}
