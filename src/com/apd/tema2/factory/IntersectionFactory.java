package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

public class IntersectionFactory {
	/**
	 * Intoarce pentru handlerType-urile care au nevoie de un obiect Intersection, obiectul
	 * de tip specific pentru rezolvarea task-ului necesar.
	 */
    public static Intersection getIntersection(String handlerType, String[] args) {
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
    	switch(handlerType) {
    		case "simple_n_roundabout":
    			return new SimpleIntersection(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    		case "simple_strict_1_car_roundabout":
    			return new SimpleStrictIntersection(1, Integer.parseInt(args[1]), Integer.parseInt(args[0]));
    		case "simple_strict_x_car_roundabout":
    			return new SimpleStrictIntersection(Integer.parseInt(args[2]), Integer.parseInt(args[1]), Integer.parseInt(args[0]));
    		case "simple_max_x_car_roundabout":
    			return new SimpleStrictIntersection(Integer.parseInt(args[2]), Integer.parseInt(args[1]), Integer.parseInt(args[0]));
    		case "priority_intersection":
    			return new PriorityIntersection(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    		case "simple_maintenance":
    			return new SimpleMaintenanceIntersection(Integer.parseInt(args[0]));
    		case "complex_maintenance":
    			return new ComplexMaintenanceIntersection(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    		case "railroad":
    			return new RailroadIntersection();
    		default:
    			return null;
	     }
    }

}
