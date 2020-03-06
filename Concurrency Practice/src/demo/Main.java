/**
 * 
 */
package demo;

import java.util.Iterator;
import java.util.Scanner;

import spinLock.SpinLockConsumer;
import spinLock.SpinLockProducer;

/**
 * @author jacobheylek
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BoundedBuffer<Double> buffer = new BoundedBuffer<Double>(10);
		// Regular producer/consumer without the semaphore
		(new Producer(buffer)).start();
		(new Consumer(buffer)).start();

	}

	public void stringTest(BoundedBuffer<String> buffer) {
		Scanner kbd = new Scanner(System.in);
		String resp = "";
		while (!(resp = kbd.next()).equals(".")) {
			if (resp.equals("-")) {
				System.out.println(buffer.dequeue());
			} else {
				System.out.println("Putting Message: " + resp);
				buffer.enqueue(resp);
			}
		}

		System.out.println(buffer);
		System.out.println("Exiting loop");

		Iterator<String> iter = buffer.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
		iter.next();
	}

}
