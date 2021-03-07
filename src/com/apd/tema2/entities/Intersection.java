package com.apd.tema2.entities;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Utilizata pentru a uniformiza tipul de date ce ajuta la definirea unei intersectii / a unui task.
 * Implementarile acesteia vor contine variabile specifice task-ului, respectiv mecanisme de sincronizare.
 */
public interface Intersection {
	/**
	 * Majoritatea intersectiilor vor avea intre 1 si 3 informatii despre propria structura, cum ar fi:
	 * 			* numar maxim de masini ce pot intra in intersectie;
	 * 			* timpul necesar unei masini sa iasa din interesectie;
	 * 			* numarul de lane-uri, etc.
	 * 
	 * Folosind getX se poate obtine acea informatie, iar pentru cazurile in care nu exista informatie
	 * utila pentru a putea fi folosita in `handle` sau nu exista informatie in plus despre propria
	 * structura, atunci se pot folosi aceste metode pentru a obtine un element de sincronizare:
	 * 			* avem nevoie de capacitatea intersectiei si de un ArrayBlockingQueue 
	 * 			  asa ca vom pune in getFirst un `return capacity;` si in getSecond
	 *			  un `return queue;`, iar `handle` vom face cast int si la ArrayBlockingQueue
	 * 
	 * Astfel, se asigura o implementare cat mai generala ce permite o implementare particulara
	 * a unui anumit tip de intersectie intr-un mod facil.
	 */
	public Object getFirst();
	public Object getSecond();
	public Object getThird();
	
	/**
	 * In cazul in care avem nevoie de unul sau mai multe SEMAFOARE sau BARIERE atunci putem
	 * obtine un vector de astfel de obiecte.
	 * 
	 * Pentru a generaliza numarul de SEMAFOARE/BARIERE aceste metode vor intoarce un vector
	 * a carui semnificatie este cunoscuta in `handle` pentru fiecare tip de clasa Intersection:
	 * 			* de exemplu semaphores[i] ar putea reprezenta semaforul pentru lane-ul `i`
	 */
	public Semaphore[] getSemaphores();
	public CyclicBarrier[] getBarriers();
}
