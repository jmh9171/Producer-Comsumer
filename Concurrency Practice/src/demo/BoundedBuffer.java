package demo;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedBuffer<Item> implements Iterable<Item> {
	private Item[] buffer;
	private int head = 0;
	private int tail = 0;
	private int size = 0;
	private Semaphore mutexP;
	private Queue<Thread> queueFull;
	private Queue<Thread> queueEmpty;

	@SuppressWarnings("unchecked")
	public BoundedBuffer(int size) {
		buffer = (Item[]) new Object[size];
		mutexP = new Semaphore(1);
		queueEmpty = new LinkedList<Thread>();
		queueFull = new LinkedList<Thread>();
	}

	@SuppressWarnings("unchecked")
	public BoundedBuffer(int size, int permits) {
		buffer = (Item[]) new Object[size];
		mutexP = new Semaphore(permits);

	}

	public void notifyLock() {
		mutexP.notify();
	}

	public void enqueueEmpty(Thread thread) {
		queueEmpty.add(thread);
		System.out.println("Buffer: Queued: "+queueEmpty.element().getName());
	}

	public Thread dequeueFull() {
		if (!queueFull.isEmpty()) {
			System.out.println("Dequeued: "+queueFull.element().getName());
			return queueFull.remove();
		} else {
			System.out.println("Buffer: Nothing Dequeued in full queue");
			return null;
		}
	}

	public void enqueueFull(Thread thread) {
		queueFull.add(thread);
		System.out.println("Buffer: Queued: "+queueFull.element().getName());
	}

	public Thread dequeueEmpty() {
		if (!queueEmpty.isEmpty()) {
			System.out.println("Buffer: Dequeued: "+queueEmpty.element().getName());
			return queueEmpty.remove();
		} else {
			System.out.println("Buffer: Nothing Dequeued in Empty Queue");
			return null;
		}

	}

	public void hasLock() {
		System.out.println(mutexP.toString());
	}

	public synchronized void enqueue(Item item) {
		if (size == buffer.length) {
			throw new BufferOverflowException();
		}
		buffer[tail++] = item;
		tail %= buffer.length;
		size++;
	}

	public synchronized Item dequeue() {
		if (size == 0) {
			throw new BufferUnderflowException();
		}
		Item temp = buffer[head];
		buffer[head++] = null;
		head %= buffer.length;
		size--;
		return temp;
	}

	public int length() {
		return buffer.length;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean isFull() {
		return (this.length() - this.size == 0);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < buffer.length; i++) {
			s.append(buffer[i] + ",");
		}
		return s.toString();
	}

	@Override
	public Iterator<Item> iterator() {
		Iterator<Item> iter = new Iterator<Item>() {
			int pointer = head;
			int count = size;
			int capacity = size;

			@Override
			public boolean hasNext() {
				return (count > 0);
			}

			@Override
			public Item next() {
				System.out.println("Count: " + count);
				if (count < 0) {
					throw new NoSuchElementException("Iterator out of bounds:");
				}

				if (capacity != size) {
					throw new ConcurrentModificationException();
				}
				Item n = buffer[pointer++];
				pointer = pointer % buffer.length;
				count--;
				return n;
			}
		};
		return iter;
	}

	public void lock() {
		try {
			mutexP.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void release() {
		mutexP.release();
	}
}
