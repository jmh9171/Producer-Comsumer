package queueLock;

import queueLock.BoundedBuffer;

public class QueueLockConsumer extends Thread {
	private BoundedBuffer<Integer> buffer;
	private Thread t;

	public QueueLockConsumer(BoundedBuffer<Integer> buffer, String name) {
		this.buffer = buffer;
		this.setName(name);
	}

	public void run() {
		System.out.println("Consumer " + this.getName() + "Running!");
		while (true) {
			try {
				while (buffer.isEmpty()) {
					buffer.enqueueEmpty(this);
					if ((t = buffer.dequeueFull()) != null) {
						t.interrupt();
					}
					this.join();
				}
			} catch (InterruptedException e) {
			} finally {

				if (!buffer.isEmpty()) {
					Integer x = buffer.dequeue();
					System.out.println("\t\tConsumer " + this.getName() + ": recieved: " + x);
				}
			}
		}

	}
}
