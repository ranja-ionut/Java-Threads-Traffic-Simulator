package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {
	/**
	 * Pentru fiecare handlerType pentru care exista informatii in plus despre intersectie sau
	 * in cazul CROSSWALK pentru `pedestrians` se va face apela la IntersectionFactory pentru
	 * a instantia Main.intersection cu intersectia corespunzatoare din factory, iar pentru
	 * cazul CROSSWALK se va instantia si Main.pedestrians cu un apel la constructorul clasei
	 * Pedestrians.
	 */
    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                	
                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection("simple_n_roundabout", line);
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection("simple_strict_1_car_roundabout", line);
                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection("simple_strict_x_car_roundabout", line);
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection("simple_max_x_car_roundabout", line);
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection("priority_intersection", line);
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection("crosswalk", line);
                    Main.pedestrians = new Pedestrians(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection("simple_maintenance", line);
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                    Main.intersection = IntersectionFactory.getIntersection("complex_maintenance", line);
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = IntersectionFactory.getIntersection("railroad", null);
                }
            };
            default -> null;
        };
    }

}
