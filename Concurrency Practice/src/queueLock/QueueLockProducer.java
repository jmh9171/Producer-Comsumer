package queueLock;

import demo.BoundedBuffer;

public class QueueLockProducer extends Thread {
	private BoundedBuffer<Double> buffer;

	public QueueLockProducer(BoundedBuffer<Double> buffer) {
		this.buffer = buffer;
	}

	public void run() {
		while (true) {
			long x = (long) (Math.random() * (300 - 200 + 10) + 10);

			try {
				// Thread.sleep(x);
				// buffer.lock(); // Acquire lock for initial busy-wait check.
				while (buffer.isFull()) { // Busy-wait until the queue is non-full.
					// buffer.release();
					System.out.println("Producer: Buffer full");
					buffer.enqueueFull(this);
					System.out.println("Producer: Sleeping");
					this.join();
					// buffer.lock();
				}

			} catch (InterruptedException e) {
				System.out.println("Producer: awake");
				
				sendData();
				// buffer.release();
			}
			if (!buffer.isFull()) {
				sendData();
			}
			
		}

	}
	
	public void sendData() {
		send((Double) (Math.random() * 5));
		System.out.println("Producer: Data sent");
		// buffer.notifyLock();
		Thread t;
		if ((t = buffer.dequeueEmpty()) != null) {
			System.out.println("Producer: Interupting Consumer");
			t.interrupt();
		}
	}

	public void send(Double double1) {
		buffer.enqueue(double1);
	}
}
