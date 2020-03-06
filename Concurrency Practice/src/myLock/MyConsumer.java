package myLock;

import java.util.concurrent.Semaphore;

import demo.BoundedBuffer;

public class MyConsumer extends Thread {
	private BoundedBuffer<Double> buffer;
	private Semaphore lock;

	public MyConsumer(BoundedBuffer<Double> buffer) {
		this.buffer = buffer;
	
	}

	public void run() {
		lock.drainPermits();
		while (true) {
			while(buffer.isEmpty()){
				
			}
				

			
				Double x = buffer.dequeue();
				System.out.println(x);
			
		}
	}
}
