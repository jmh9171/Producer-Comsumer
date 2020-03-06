package spinLock;

import demo.BoundedBuffer;

public class Main {

	public static void main(String[] args) {
		// Consumer/Producer with Semaphore
		BoundedBuffer<Double> buffer = new BoundedBuffer<Double>(10);
		(new SpinLockProducer(buffer)).start();
		(new SpinLockConsumer(buffer)).start();
	}
}
