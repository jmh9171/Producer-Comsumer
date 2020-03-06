package queueLock;

import demo.BoundedBuffer;

public class QueueLockConsumer extends Thread {
	private BoundedBuffer<Double> buffer;

	public QueueLockConsumer(BoundedBuffer<Double> buffer) {
		this.buffer = buffer;
	}

	public void run() {
		while (true) {
			//buffer.lock();

			while (buffer.isEmpty()) {
				System.out.println("Consumer: buffer empty");
				//buffer.release();
				buffer.enqueueEmpty(this);
				System.out.println("Consumer: asleep");
				try {
					this.join();
					//buffer.lock();
				} catch (InterruptedException e) {
					System.out.println("Consumer: awake");
					Double x = buffer.dequeue();
					System.out.println("Consumer: recieved: " + x);
					Thread t;
					if ((t = buffer.dequeueFull()) != null) {
						System.out.println("Consumer: Interupting Producer");
						t.interrupt();
					}
					
				}finally {
					
				}
			}
		
			
			
			//buffer.notifyLock();
			//buffer.release();
		}
	}
}
