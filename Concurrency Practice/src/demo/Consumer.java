package demo;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Consumer extends Thread {

	private BoundedBuffer<Double> buffer;

	public Consumer(BoundedBuffer<Double> buffer) {
		this.buffer = buffer;
	}

	public void run() {
		while (true) {
			if (buffer.isEmpty()) {
				try {
					System.out.println("buffer empty");
					Thread.sleep((long) 200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} else {
				Double x = buffer.dequeue();
				if (x == null) {
					try {
						makeSound();
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					throw new NullPointerException("Race Condition:");
				} else {
					System.out.println("recieved: " + x);
				}

			}
		}
	}
	//This part is just for testing purposes during the process of development.
	//I used this to check for race conditions.
	public void makeSound() throws LineUnavailableException {
		System.out.println("Make sound");
	    byte[] buf = new byte[2];
	    int frequency = 44100; //44100 sample points per 1 second
	    AudioFormat af = new AudioFormat((float) frequency, 16, 1, true, false);
	    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
	    sdl.open();
	    sdl.start();
	    int durationMs = 90;
	    int numberOfTimesFullSinFuncPerSec = 441; //number of times in 1sec sin function repeats
	    for (int i = 0; i < durationMs * (float) 44100 / 1000; i++) { //1000 ms in 1 second
	      float numberOfSamplesToRepresentFullSin= (float) frequency / numberOfTimesFullSinFuncPerSec;
	      double angle = i / (numberOfSamplesToRepresentFullSin/ 2.0) * Math.PI;  // /divide with 2 since sin goes 0PI to 2PI
	      short a = (short) (Math.sin(angle) * 32767);  //32767 - max value for sample to take (-32767 to 32767)
	      buf[0] = (byte) (a & 0xFF); //write 8bits ________WWWWWWWW out of 16
	      buf[1] = (byte) (a >> 8); //write 8bits WWWWWWWW________ out of 16
	      sdl.write(buf, 0, 2);
	    }
	    sdl.drain();
	    sdl.stop();
	  }
	}
	

