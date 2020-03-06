package spinLock;

import java.util.concurrent.Semaphore;

import demo.BoundedBuffer;

public class SpinLockProducer extends Thread {
	private BoundedBuffer<Double> buffer;

	public SpinLockProducer(BoundedBuffer<Double> buffer) {
		this.buffer = buffer;
	}

	public void run() {
		while (true) {
			long x = (long) (Math.random() * (300 - 200 + 10) + 10);

			try {

				Thread.sleep(x);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			buffer.lock(); // Acquire lock for initial busy-wait check.
			while (buffer.isFull()) { // Busy-wait until the queue is non-full.
				buffer.release();
				Thread.yield();
				// Drop the lock temporarily to allow a chance for other threads
				// needing queueLock to run so that a consumer might take a task.
				buffer.lock(); // Re-acquire the lock for the next call to "queue.isFull()".
			}

			this.send((Double) (Math.random() * 5));
			buffer.release(); // Drop the queue lock until we need it again to add the next task.

		}

	}

	public void send(Double double1) {
		buffer.enqueue(double1);
	}
}
