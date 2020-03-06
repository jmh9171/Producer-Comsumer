package demo;

public class Producer extends Thread {
	private BoundedBuffer<Double> buffer;

	public Producer(BoundedBuffer<Double> buffer) {
		this.buffer = buffer;
	}

	public void run() {
		while(true) {
			long x = (long)(Math.random() * (300 - 200 + 10) + 10);
			
			try {
				
				Thread.sleep(x);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.send((Double)(Math.random() * 5));
		}

	}

	public void send(Double double1) {
		buffer.enqueue(double1);
	}
}
