package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.utils.Constants;

import static java.lang.Thread.sleep;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	int id = car.getId();
                	
                	// afiseaza mesaj de sosire
                	System.out.println("Car " + id + " has reached the semaphore, now waiting...");
                	
                	// asteapta car waitingTime
                	try {
						sleep(car.getWaitingTime());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// afiseaza mesaj de plecare
                	System.out.println("Car " + id + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	Intersection in = Main.intersection;
                	int id = car.getId();
                	int time = (int)in.getSecond();
                	
                	// afiseaza mesaj de sosire
                	System.out.println("Car " + id + " has reached the roundabout, now waiting...");
                	
                	Semaphore canEnter = in.getSemaphores()[0];
                	
                	// incearca sa intri in intersectie daca nu s-a atins limita
                	try {
						canEnter.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// afiseaza mesaj de intrare
                	System.out.println("Car " + id + " has entered the roundabout");
                	
                	// asteapta timpul necesar parcurgerii intersectiei
                	try {
						sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// afiseaza mesaj de iesire
                	System.out.println("Car " + id + " has exited the roundabout after " + time / 1000 + " seconds");
                	
                	// marcheaza iesirea din intersectie si permite altei masini sa intre
                	canEnter.release();
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	Intersection in = Main.intersection;
                	int id = car.getId();
                	int time = (int)in.getSecond();
                	int start = car.getStartDirection();
                	
                	// afiseaza mesaj de sosire
                	System.out.println("Car " + id + " has reached the roundabout");
                	
                	CyclicBarrier barrier = in.getBarriers()[1];
                	Semaphore canEnter = in.getSemaphores()[start];
                	
                	// incearca sa intri in intersectie daca lane-ul tau mai permite acest lucru
                	try {
						canEnter.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// asteapta sa intre fix CAPACITY * LANES (in acest caz LANES) masini in intersectie
                	try {
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e1) {
						e1.printStackTrace();
					}
                	
                	// afiseaza mesaj de intrare
                	System.out.println("Car " + id + " has entered the roundabout from lane " + start);
                	
                	// asteapta timpul necesar parcurgerii intersectiei
                	try {
						sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// afiseaza mesaj de iesire
                	System.out.println("Car " + id + " has exited the roundabout after " + time / 1000 + " seconds");
                	
                	// marcheaza iesirea acestei masini si permite intrarea altei masini de pe acelasi lane
                	canEnter.release();
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	Intersection in = Main.intersection;
                	int id = car.getId();
                	int time = (int)in.getSecond();
                	int start = car.getStartDirection();
                	
                	// afiseaza mesaj de sosire
                	System.out.println("Car " + id + " has reached the roundabout, now waiting...");
                	
                	CyclicBarrier allBarrier = in.getBarriers()[0];
                	CyclicBarrier roundaboutBarrier = in.getBarriers()[1];
                	Semaphore canEnter = in.getSemaphores()[start];
                	
                	// asteapta la bariera pana cand ajung absolut toate masinile
                	try {
                		allBarrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
                	
                	// incearca sa intri in intersectie daca lane-ul tau mai permite acest lucru
                	try {
						canEnter.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// afiseaza mesaj de selectie
                	System.out.println("Car " + id + " was selected to enter the roundabout from lane " + start);
                	
                	// asteapta sa intre fix CAPACITY * LANES masini in intersectie
                	try {
						roundaboutBarrier.await();
					} catch (InterruptedException | BrokenBarrierException e1) {
						e1.printStackTrace();
					}
                	
                	// afiseaza mesaj de intrare
                	System.out.println("Car " + id + " has entered the roundabout from lane " + start);
                	
                	// asteapta timpul necesar parcurgerii intersectiei
                	try {
						sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// afiseaza mesaj de iesire
                	System.out.println("Car " + id + " has exited the roundabout after " + time / 1000 + " seconds");
                	
                	// marcheaza iesirea acestei masini si permite intrarea altei masini de pe acelasi lane
                	canEnter.release();
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	Intersection in = Main.intersection;
                	int id = car.getId();
                	int time = (int)in.getSecond(); // time
                	int start = car.getStartDirection();

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                	Semaphore canEnter = in.getSemaphores()[start];
                	
                	// afiseaza mesaj de sosire
                	System.out.println("Car " + id + " has reached the roundabout from lane " + start);
                	
                	// incearca sa intri in intersectie daca lane-ul tau mai permite acest lucru
                	try {
						canEnter.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// afiseaza mesaj de intrare
                	System.out.println("Car " + id + " has entered the roundabout from lane " + start);
                	
                	// asteapta timpul necesar parcurgerii intersectiei
                	try {
						sleep(time);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	// afiseaza mesaj de iesire
                	System.out.println("Car " + id + " has exited the roundabout after " + time / 1000 + " seconds");
                	
                	// marcheaza iesirea acestei masini si permite intrarea altei masini de pe acelasi lane
                	canEnter.release();
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	Intersection in = Main.intersection;
                	int id = car.getId();
                	int priority = car.getPriority();
                	int lows = (int)in.getSecond();
                	Semaphore prioritySem = in.getSemaphores()[0];
                	AtomicInteger totalHigh = (AtomicInteger)in.getThird();
                	@SuppressWarnings("unchecked")
					ArrayBlockingQueue<Integer> queue = (ArrayBlockingQueue<Integer>)in.getFirst();
                	
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI
                    
                    // masina cu prioritate scazuta
                    if (priority == 1) {
                    	// zona critica in care ne asiguram ca daca masina X vrea sa intre in acelasi timp cu masina Y
                    	// nu este posibil ca in cazul in care X afiseaza primul mesajul, el sa fie adaugat in coada dupa Y
                    	synchronized(in) {
	                    	// afisare mesaj de incercare intrare
	                    	System.out.println("Car " + id + " with low priority is trying to enter the intersection...");
	                    	
	                    	// adauga-l in coada
	                    	queue.add(id);
                    	}
                    	
                    	// se incearca trecerea de semafor 
                    	// se va bloca doar cand exista o masina cu prioritate ridicate in intersectie
                    	try {
							prioritySem.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                    	
                    	// cat timp mai sunt masini cu prioritate scazuta ce pot intra
                    	while (!queue.isEmpty()) {
                        	if (id == queue.peek()) { // daca este randul meu sa intru
                        		// afiseaza mesaj intrare
                        		System.out.println("Car " + id + " with low priority has entered the intersection");
                        		
                        		// scoate masina din coada
                        		try {
    								queue.take();
    							} catch (InterruptedException e) {
    								e.printStackTrace();
    							}
                        		
                        		// am terminat cu aceasta masina
                        		return;
                        	}
                        }
                    } else if (priority > 1) { // masina cu prioritate ridicata
                    	// numarul total de masini cu prioritate ridicata din intersectie creste
                    	totalHigh.incrementAndGet();
                    	
                    	// blocheaza toate masinile cu prioritate scazuta ce vor sa intre in intersectie
                    	prioritySem.drainPermits();
                    	
                    	// afiseaza mesaj de intrare
                    	System.out.println("Car " + id + " with high priority has entered the intersection");
                    	
                    	// asteapta cele 2s necesare trecerii prin intersectie
                    	try {
							sleep(Constants.PRIORITY_INTERSECTION_PASSING);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                    	
                    	// afiseaza mesaj iesire
                    	System.out.println("Car " + id + " with high priority has exited the intersection");
                    	
                    	// asigurare ca nu se va da release de 2 ori intrand peste o alta care va da drainPermits
                    	synchronized(in) {
                    		// decrementez numarul total de masini cu prioritate ridicata
                    		totalHigh.decrementAndGet();
                    	
	                    	// daca am fost ultima masina
	                    	if (totalHigh.get() == 0) {
	                    		// dau drumul masinilor cu prioritate scazuta
	                    		prioritySem.release(lows);
	                    		// chiar daca s-ar face release de mai multe ori, acest lucru nu conteaza
	                    		// cand apare o noua masina cu prioritate ridicata se va face drainPermits
	                    	}
                    	}
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	Pedestrians pedestrians = Main.pedestrians;
                    int id = car.getId();
                    // old si current reprezinta tipurile de mesaje care ar trebui sa fie afisate
                    // un mesaj nou se va afisa doar cand old != current
                    int old = -2;
                    int current = -1;
                    
                    // cat timp thread-ul pedestrians nu si-a incheiat rularea
                    while (!pedestrians.isFinished()) {
                    	// asteapta o notificare noua de la pietoni (green light sau red light)
                    	synchronized(Main.pedestrians) {
                    		try {
                    			Main.pedestrians.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
                    	}
                    	
                    	// daca pietonii nu trec atunci se afiseaza un mesaj nou de tip 1 - green light
                    	if (!pedestrians.isPass()) {
                    		current = 1;
                    	} else { // in caz contrar un mesaj nou de tip 0 - red light
                    		current = 0;
                    	}
                    	
                    	if (old != current) { // daca mesajul curent este diferit de cel anterior
                    		if (current == 1) { // si este de tip 1
                    			// afiseaza mesaj green light
                    			System.out.println("Car " + id + " has now green light");
                    		} else { // si este de tip 0
                    			// afiseaza mesaj red light
                    			System.out.println("Car " + id + " has now red light");
                    		}
                    	}
                    	
                    	// mesajul curent devine cel vechi
                    	old = current;
                    }
                    
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection in = Main.intersection;
                    CyclicBarrier barrier = in.getBarriers()[0];
                    Semaphore zero = in.getSemaphores()[0];
                    Semaphore one = in.getSemaphores()[1];
                    int id = car.getId();
                    int start = car.getStartDirection();
                    
                    // afiseaza mesaj de sosire
                    System.out.println("Car " + id + " from side number " + start + " has reached the bottleneck");
                    
                    // daca sunt o masina de pe lane-ul 0
                    if (start == 0) {
                    	try { // incearca sa intri in intersectie daca ti se permite acest lucru
							zero.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                    	
                    	try { // asteapta sa se stranga exact CAPACITY masini
							barrier.await();
						} catch (InterruptedException | BrokenBarrierException e) {
							e.printStackTrace();
						}
                    	
                    	// afiseaza mesaj de iesire
                    	System.out.println("Car " + id + " from side number " + start + " has passed the bottleneck");
                    	
                    	// da drumul masinilor din lane-ul 1 pentru a putea intra
                    	one.release();
                    } else { // daca sunt o masina de pe lane-ul 1
                    	try { // incearca sa intri in intersectie daca ti se permite acest lucru
							one.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                    	
                    	try { // asteapta sa se stranga exact CAPACITY masini
							barrier.await();
						} catch (InterruptedException | BrokenBarrierException e) {
							e.printStackTrace();
						}
                    	
                    	// afiseaza mesaj de iesire
                    	System.out.println("Car " + id + " from side number " + start + " has passed the bottleneck");
                    	
                    	// da drumul masinilor din lane-ul 0 pentru a putea intra
                    	zero.release();
                    }
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @SuppressWarnings("unchecked")
				@Override
                public void handle(Car car) {
                	Intersection in = Main.intersection;
                	int id = car.getId();
                	int oldLaneID = car.getStartDirection();
                	int[] args = (int[]) in.getThird();
                	int free = args[0];
                	Semaphore[] laneSemaphores = in.getSemaphores();
                	Semaphore lock = laneSemaphores[free];
                	CyclicBarrier barrier = in.getBarriers()[0];
                	Object[] atomics = (Object[]) in.getSecond();
                	AtomicInteger newLane = (AtomicInteger) atomics[0];
                	AtomicInteger[] usedCars = (AtomicInteger[]) atomics[1];
                	Object[] queues = (Object[]) in.getFirst();
                	ArrayBlockingQueue<ArrayBlockingQueue<Integer>>[] newLaneQueues = (ArrayBlockingQueue<ArrayBlockingQueue<Integer>>[]) queues[0];
                	
                	// zona critica in care ne asiguram ca masina care intra este si cea bagata in coada
                	synchronized(in) {
                		// afiseaza mesaj de sosire
	                	System.out.println("Car " + id + " has come from the lane number " + oldLaneID);
	                	
	                	// obtine coada lane-ului masinii din queues[1] (adica din oldLaneQueues)
	                	ArrayBlockingQueue<Integer> oldLaneQueue = ((ArrayBlockingQueue<Integer>[]) queues[1])[oldLaneID];
	                	// adauga masina la codul lane-ului de pe care a venit
	                	oldLaneQueue.add(id);
                	}
                	
                	// asteapta ca toate masinile sa fi ajuns in intersectie
                	try {
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
                	
                	// cat timp mai exista lane-uri initiale ce mai contin masini
                	while (((int[])in.getThird())[1] > 0) { // verifica daca initial > 0
                		
                		// dupa ce trec de acest semafor, toate masinile, mai putin cea care trebuie evaluata, vor fi
                		// blocate aici pana cand acea masina isi termina treaba
                		try {
							lock.acquire();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
                		
                		ArrayBlockingQueue<ArrayBlockingQueue<Integer>> currentNewLane = newLaneQueues[newLane.get()];
                		
                		if (currentNewLane.isEmpty()) { // daca am terminat cu o banda noua trecem mai departe
                			newLane.set((newLane.get() + 1) % free);
                			lock.release(); // dam drumul masinilor care asteapta (sau propriei masini in viitor)
                			
                			continue;
                		}
                		
                		Semaphore currentNewLaneSemaphore = laneSemaphores[newLane.get()];
                		
                		ArrayBlockingQueue<Integer> currentOldLane = currentNewLane.peek();
                		
                		int currentCar = currentOldLane.peek();
                		
                		// asteptam pana cand toate thread-urile au obtinut informatiile de mai sus
                		try {
							barrier.await();
						} catch (InterruptedException | BrokenBarrierException e) {
							e.printStackTrace();
						}
                		
                		// Daca sunt masina care astepta in coada
                		if (id == currentCar) {
                			
                			// Daca mai pot intra in intersectie
                			if (currentNewLaneSemaphore.availablePermits() > 0) {
                				// ia un permis
                				try {
									currentNewLaneSemaphore.acquire();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
                				
                				// afiseaza mesaj de intrare
                				System.out.println("Car " + id + " from the lane " + oldLaneID + " has entered lane number " + newLane.get());
                				// creste numarul de masini care au trecut de semafor
                				usedCars[newLane.get()].incrementAndGet();
                				
                				// scoatem masina din coada benzii
                				try {
									currentOldLane.take();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
                				
                				if (currentOldLane.isEmpty()) { // daca am reusit sa golim banda
                					// afiseaza mesaj de terminare a benzii
                					System.out.println("The initial lane " + oldLaneID + " has been emptied and removed from the new lane queue");
                					
                					// decrementeaza numarul de benzi initiale
                					((int[])in.getThird())[1]--;
                					
                					// elimina banda veche din coada benzii noi
                					try {
										currentNewLane.take();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
                					
                					// intoarce permisuri pentru masinile care deja si-au terminat treaba
                					currentNewLaneSemaphore.release(usedCars[newLane.get()].get());
                					
                					// reseteaza numarul de masini la 0
                					usedCars[newLane.get()].set(0);
                				}
                				
                				// trecem la urmatoare banda noua
                				newLane.set((newLane.get() + 1) % free);
                			} else if (!currentOldLane.isEmpty()){ // banda a ramas fara locuri libere si mai are masini
                				// afiseaza mesaj de trimitere la finalul cozii a benzii
                				System.out.println("The initial lane " + oldLaneID + " has no permits and is moved to the back of the new lane queue");
                				
                				// scoate banda veche din coada benzii noi
                				try {
									currentOldLane = currentNewLane.take();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
                				
                				// si adaug-o la finalul cozii
                				currentNewLane.add(currentOldLane);
                				
                				// intoarce permisuri pentru masinile care deja si-au terminat treaba
                				currentNewLaneSemaphore.release(usedCars[newLane.get()].get());
                				// reseteaza numarul de masini la 0
                				usedCars[newLane.get()].set(0);
                				
                				// trecem la urmatoare banda noua
                				newLane.set((newLane.get() + 1) % free);
                			}
                			
                			// da drumul masinilor care asteapta ca aceasta masina sa isi termine treaba
                			lock.release(Main.carsNo);
                		}
                	}
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection in = Main.intersection;
                    int id = car.getId();
                    int start = car.getStartDirection();
                    CyclicBarrier barrier = in.getBarriers()[0];
                    @SuppressWarnings("unchecked")
					ArrayBlockingQueue<Integer> queue = (ArrayBlockingQueue<Integer>) in.getFirst();
                    
                    // zona critica care asigura ordinea de sosire a masinilor in intersecite
                    synchronized(queue) {
                    	// se afiseaza mesajul de oprire
	                    System.out.println("Car " + id + " from side number " + start + " has stopped by the railroad");
	                    
	                    // se adauga masina in coada
	                    queue.add(id);
                    }
                    
                    // se asteapta toate thread-urile sa ajunga la bariera
                    try {
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
                    
                    // un singur thread va afisa mesajul (thread-ul 0 mereu exista)
                    if (id == 0) {
                    	System.out.println("The train has passed, cars can now proceed");
                    }
                    
                    // se asteapta la bariera pentru a asigura faptul ca mesajul de mai sus
                    // nu apare dupa unul din mesajele de mai jos, adica sa nu existe
                    // masini care incep sa conduca inainte ca trenul sa fi trecut
                    try {
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
                    
                    // cat timp coada de masini nu este goala
                    while (!queue.isEmpty()) {
                    	// daca e randul meu sa trec
                    	if (id == queue.peek()) {
                    		// afiseaza mesaj de trecere
                    		System.out.println("Car " + id + " from side number " + start + " has started driving");
                    		
                    		// scoate masina din coada
                    		try {
								queue.take();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
                    		
                    		// am terminat cu aceasta masina
                    		return;
                    	}
                    }
                }
            };
            default -> null;
        };
    }
}
