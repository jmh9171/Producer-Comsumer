package queueLock;

import demo.BoundedBuffer;

public class Main {

	public static void main(String[] args) {
		BoundedBuffer<Double> buffer = new BoundedBuffer<Double>(20);
		(new QueueLockProducer(buffer)).start();
		(new QueueLockConsumer(buffer)).start();
		
		
		
		
	}

}
