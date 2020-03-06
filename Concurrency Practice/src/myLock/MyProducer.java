package myLock;

import java.util.concurrent.Semaphore;

import demo.BoundedBuffer;

public class MyProducer extends Thread {
	private BoundedBuffer<Double> buffer;
	private Semaphore lock;
	private Semaphore unlock;

	public MyProducer(BoundedBuffer<Double> buffer) {
		this.buffer = buffer;
	}

	public void run() {
		
		lock.drainPermits();
		
		while (true) {
			long x = (long) (Math.random() * (300 - 200 + 10) + 10);
			
			try {
				Thread.sleep(x);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.send((Double) (Math.random() * 5));
		}

	}

	public void send(Double double1) {
		buffer.enqueue(double1);
	}
}
