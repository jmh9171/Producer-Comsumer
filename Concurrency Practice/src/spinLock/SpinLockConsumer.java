package spinLock;

import java.util.concurrent.Semaphore;

import demo.BoundedBuffer;

public class SpinLockConsumer extends Thread {
	private BoundedBuffer<Double> buffer;

	public SpinLockConsumer(BoundedBuffer<Double> buffer) {
		this.buffer = buffer;
	}

	public void run() {
		while (true) {
				buffer.lock(); // Acquire lock for initial busy-wait check.
				while (buffer.isEmpty()) { // Busy-wait until the queue is non-empty.
					buffer.release();
					Thread.yield();
					// Drop the lock temporarily to allow a chance for other threads
					// needing queueLock to run so that a producer might add a task.
					buffer.lock(); // Re-acquire the lock for the next call to "queue.isEmpty()".
				}
				Double x = buffer.dequeue();
				System.out.println("recieved: " + x); // Take a task off of the queue.
				buffer.release(); // Drop the queue lock until we need itdoStuff(myTask); // Go off and do
										// something with the task.
		}
	}
}