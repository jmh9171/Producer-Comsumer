package queueLock;

import queueLock.BoundedBuffer;

public class QueueLockProducer extends Thread {
	private BoundedBuffer<Integer> buffer;
	private Thread t;
	private int x = 1;

	public QueueLockProducer(BoundedBuffer<Integer> buffer, String name) {
		this.buffer = buffer;
		this.setName(name);
	}

	public void run() {
		System.out.println("Producer " + this.getName() + "Running!");
		while (true) { // run forever
			try {
				while (buffer.isFull()) { // while the bounded buffer is full
					buffer.enqueueFull(this);// queue this thread in the buffers producer queue
					if ((t = buffer.dequeueEmpty()) != null) {// try to dequeue the consumer queue, it it comes back
																// null then there was nothing there
						t.interrupt();// interrupt the consumer thread
					}
					this.join();// then sleep on itself forever waiting for an interrupt
				}
			} catch (InterruptedException e) {// this is said interrupt
			} finally {// need finally block so it runs every time even when the buffer is not full.
				if ((t = buffer.dequeueEmpty()) != null) {// try to dequeue the consumer queue, it it comes back null
															// then there was nothing there
					t.interrupt();// interrupt the consumer thread
				}
				sendData(x++ % 10);// send the data

			}

		}
	}

	// method to send data across to the buffer.
	public void sendData(Integer temp) {
		System.out.println("Producer " + this.getName() + ": Sent " + temp);
		buffer.enqueue(temp);
	}
}
