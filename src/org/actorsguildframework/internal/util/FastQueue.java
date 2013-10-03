/*
 *    Copyright 2008 Tim Jansen
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.actorsguildframework.internal.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Fast FIFO queue implementation. Entries must extend FastQueue.Entry. 
 * Unlike Java implementations, it can remove entries by reference in o(1) instead
 * of o(n).
 * FastQueue is not thread-safe.
 * @param <T> the type of the entries
 */
public class FastQueue<T extends FastQueue.Entry> implements Iterable<T>{
	/**
	 * The base class of all entries of FastQueue. A object can only be in one FastQueue at a time.
	 * The result of adding an object that is already in another FastQueue is undefined (but
	 * probably not good). 
	 */
	public static class Entry {
		private Entry fqePrev, fqeNext; // prev/next element. Null if at begin/end of the queue
	}
	
	// Beginning of the queue. Null for empty lists.
	// FastQueue is actually a ring buffer: the last entry can be found by following the
	// fqePrev reference of the first entry.
	private Entry firstEntry;
	// number of real entries
	private int size; 
	
	/**
	 * Creates a new, empty queue.
	 */
	public FastQueue() {
		size = 0;
	}

	private void init(T entry) {
		firstEntry = entry;
		entry.fqeNext = entry;
		entry.fqePrev = entry;
		size = 1;
	}
	
	/**
	 * Inserts the given element in front of the other element.
	 * @param newElement the new element
	 * @param existingElement the existing element
	 */
	private void insertInFrontOf(Entry newElement, Entry existingElement)
	{
		newElement.fqePrev = existingElement.fqePrev;
		newElement.fqeNext = existingElement;
		existingElement.fqePrev.fqeNext = newElement;
		existingElement.fqePrev = newElement;
		size++;
	}
	
	/**
	 * Adds an element to the end of the queue.
	 * @param e the element to add.  
	 */
	public void add(T e) {
		if (firstEntry == null) {
			init(e);
			return;
		}
		insertInFrontOf(e, firstEntry);
	}
	
	/**
	 * Adds an element to the beginning of the queue.
	 * @param e the element to add.  
	 */
	public void addFront(T e) {
		add(e);
		firstEntry = e;
	}
	
	/**
	 * Returns the first element of the queue, or null if empty.
	 * @return the first element or null
	 */
	@SuppressWarnings("unchecked")
	public T peek() {
		return (T)firstEntry;
	}
	
	/**
	 * Returns the first element of the queue and removes it. Returns null if empty.
	 * @return the first element or null
	 */
	@SuppressWarnings("unchecked")
	public T pop() {
		if (size < 2) {
			if (firstEntry == null)
				return null;
			T r = (T)firstEntry;
			size = 0;
			firstEntry = null;
			r.fqeNext = null;
			r.fqePrev = null;
			return r;
		}
		T r = (T)firstEntry;
		firstEntry = r.fqeNext;
		removeInternalNoCheck(r);
		return r;
	}
	
	/**
	 * Returns the last element of the queue, or null if empty.
	 * @return the last element or null
	 */
	@SuppressWarnings("unchecked")
	public T getLast() {
		if (firstEntry == null)
			return null;
		return (T)firstEntry.fqePrev;
	}

	// removes the instance from the queue
	private void removeInternalNoCheck(T instance) {
		instance.fqePrev.fqeNext = instance.fqeNext;
		instance.fqeNext.fqePrev = instance.fqePrev;
		instance.fqeNext = null;
		instance.fqePrev = null;
		size--;
	}
	
	// same as remove(), but different name, so the iterator can use the impl
	private boolean removeInternal(T instance) {
		if (instance.fqeNext == null)
			return false;
		if (instance == firstEntry){
			pop();
			return true;
		}
		removeInternalNoCheck(instance);
		return true;
	}

	
	/**
	 * Removes the given entry from this FastQueue. Does nothing if the element is in no list.
	 * Please note: do not remove an instance that is in another FastQueue. The result is undefined.
	 * @param instance the instance of the list
	 * @return true if the instance was removed, false if it was not in any list
	 */
	public boolean remove(T instance) {
		return removeInternal(instance);
	}
	
	/**
	 * Checks whether the given entry is in a queue. Please note that this function will not 
	 * determine whether it is actually in this queue, or any other (that would cause a
	 * o(n) complexity instead of o(1)).
	 * @param instance the entry to check
	 * @return true if the entry is in a queue. False otherwise
	 */
	public boolean isInQueue(T instance) {
		return instance.fqeNext != null;
	}
	
	/**
	 * Returns the number of elements in the queue.
	 * @return the number of elements
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks whether the queue is empty.
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Puts the first entry to the end of the queue and returns it.
	 * @return the entry, or null if the queue was empty.
	 */
	@SuppressWarnings("unchecked")
	public T rotate() {
		if (firstEntry == null)
			return null;
		T r = (T)firstEntry;
		firstEntry = r.fqeNext;
		return r;
	}
	
	/**
	 * Returns an iterator for the queue.
	 * @return the iterator
	 */
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Entry nextElement = firstEntry;
			boolean lastElementPossible = false;

			public boolean hasNext() {
				return nextElement != null;
			}

			@SuppressWarnings("unchecked")
			public T next() {
				if (nextElement == null)
					throw new NoSuchElementException("End of queue reached.");
				T r = (T)nextElement;
				nextElement = nextElement.fqeNext;
				if (nextElement == firstEntry)
					nextElement = null;
				lastElementPossible = true;
				return r;
			}

			@SuppressWarnings("unchecked")
			public void remove() {
				if (!lastElementPossible)
					throw new IllegalStateException("Element already deleted or beginning of the list.");
				Entry lastElement;
				if (nextElement == null)
					lastElement = firstEntry.fqePrev;
				else
					lastElement = nextElement.fqePrev;
				removeInternal((T)lastElement);
				lastElementPossible = false;
			}
		};
				
	}
	
	/**
	 * Removes all elements from the queue.
	 */
	public void clear() {
		while (pop() != null)
			;
	}
}
