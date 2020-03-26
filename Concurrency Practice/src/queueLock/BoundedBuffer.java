package queueLock;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.LinkedList;
import java.util.Queue;


public class BoundedBuffer<Item> {
	private Item[] buffer;
	private int head = 0;
	private int tail = 0;
	private int entries = 0;
	private int size = 0;
	private int queueCount = 0;
	private Queue<Thread> queueFullCV;
	private Queue<Thread> queueEmptyCV;
	private Item temp;
	private int ratio = 0;

	@SuppressWarnings("unchecked")
	public BoundedBuffer(int size) {
		buffer = (Item[]) new Object[size];
		this.size = size;
		queueEmptyCV = new LinkedList<Thread>();
		queueFullCV = new LinkedList<Thread>();
	}

	public synchronized Thread dequeueFull() {
		if (!queueFullCV.isEmpty()) {
			return queueFullCV.remove();
		} else {
			return null;
		}
	}

	public synchronized void enqueueFull(Thread thread) {
		queueFullCV.add(thread);
	}

	public synchronized void enqueueEmpty(Thread thread) {
		queueEmptyCV.add(thread);
	}

	public synchronized Thread dequeueEmpty() {
		if (!queueEmptyCV.isEmpty()) {
			return queueEmptyCV.remove();
		} else {
			return null;
		}
	}

	public synchronized void enqueue(Item item) {
		if (entries == buffer.length) {
			throw new BufferOverflowException();
		}
		buffer[tail++] = item;
		tail %= buffer.length;
		entries++;
		System.out.println("\t\t\t\tQueue Count: " + queueCount++);
	}

	public synchronized Item dequeue() {
		if (entries == 0) {
			throw new BufferUnderflowException();
		}
		temp = buffer[head];
		if (temp == null) {
			System.out.println("NULLS " + ratio++);
		}
		buffer[head++] = null;
		head %= buffer.length;
		entries--;
		return temp;
	}

	public synchronized boolean isEmpty() {
		return entries == 0;
	}

	public synchronized boolean isFull() {
		return ((this.size - this.entries) == 0);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < buffer.length; i++) {
			s.append(buffer[i] + ",");
		}
		return s.toString();
	}
}
