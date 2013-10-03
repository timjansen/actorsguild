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
package org.actorsguildframework.immutable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.actorsguildframework.ActorRuntimeException;
import org.actorsguildframework.Immutable;

/**
 * FreezableList is an implementation of the {@link List} interface that allows freezing the list. 
 * Once the list has been frozen (using the method {@link #freeze()}, no modification is 
 * allowed. However, the list can then accessed using an {@link ImmutableList} instance 
 * that is multi-threading safe. 
 * 
 * FreezableList is only safe for use in a single thread. It has performance characteristics
 * similar to {@link ArrayList}.
 * @param <T> the type of the list elements
 * @see ImmutableList
 */
public class FreezableList<T> extends AbstractList<T> {
	private final static int INITIAL_CAPACITY = 16;
	private final static int MAX_POS = 12;
	
	private ImmutableList<T> immutableVersion;
	private Object[] data;
	private int pos, size;
	final private boolean immutableContent;
	
	/**
	 * Creates a new, empty FreezableList instance. 
	 * @param contentType the super-class of the class members
	 */
	public FreezableList(Class<T> contentType) {
		this(contentType, INITIAL_CAPACITY);
	}

	/**
	 * Creates a new, empty FreezableList instance with the given initial capacity. 
	 * Specifying the capacity is useful for performance only, as every increase
	 * of capacity is relatively costly. It does not have any functional difference,
	 * as  <code>FreezableList</code> will increase its capacity automatically.
	 * @param contentType the super-class of the class members
	 * @param capacity the capacity of the argument
	 */
	public FreezableList(Class<T> contentType, int capacity) {
		if (contentType == null)
			throw new IllegalArgumentException("Arguments must not be null");
		if (capacity < 0)
			throw new IllegalArgumentException("Argument must not be negative.");
		this.data = new Object[capacity];
		this.immutableContent = ImmutableHelper.isImmutableType(contentType);
	}

	/**
	 * Creates a new FreezableList instance with the given initial content. 
	 * The initial data collection will be copied (shallowly).
	 * @param contentType the super-class of the class members
	 * @param initialData the initial content of the list
	 */
	public FreezableList(Class<T> contentType, Collection<T> initialData) {
		if (contentType == null)
			throw new IllegalArgumentException("Arguments must not be null");
		if (initialData == null)
			throw new IllegalArgumentException("Arguments must not be null");
		
		this.data = new Object[computeNewCapacity(0, initialData.size())];
		this.immutableContent = ImmutableHelper.isImmutableType(contentType);
		addAll(initialData);
	}

	/**
	 * Creates a new FreezableList instance with the given initial content. 
	 * The initial data collection will be copied (shallowly).
	 * @param contentType the super-class of the class members
	 * @param initialData the initial content of the list
	 */
	public FreezableList(Class<T> contentType, ImmutableList<T> initialData) {
		if (contentType == null)
			throw new IllegalArgumentException("Arguments must not be null");
		if (initialData == null)
			throw new IllegalArgumentException("Arguments must not be null");
		
		this.data = new Object[computeNewCapacity(0, initialData.size())];
		this.immutableContent = ImmutableHelper.isImmutableType(contentType);
		addAll(initialData);
	}
	
	/**
	 * Private constructor that gives the list's array
	 * @param dataToKeep the array (will be kept, don't modify it)
	 * @param pos the position of the first element in the array
	 * @param size the number of used elements in the array
	 * @param immutableContent if true, all elements of the 
	 *           array are guaranteed to be immutable. If false,
	 *           some or all may be not
	 */
	private FreezableList(T[] dataToKeep, int pos, int size, boolean immutableContent) {
		this.data = dataToKeep;
		this.pos = pos;
		this.size = size;	
		this.immutableContent = immutableContent;
	}
	
	/**
	 * Creates a new <code>FreezableList</code> with the given elements
	 * @param <T> the type of the list
	 * @param data the initial members
	 * @return the new instance of <code>FreezableList</code>
	 */
	public static <T >FreezableList<T> create(T... data) {
		if (data == null)
			throw new IllegalArgumentException("Argument must not be null");
		return new FreezableList<T>(data, 0, data.length, 
				ImmutableHelper.isImmutable(data.getClass().getComponentType())); 
	}
	
	/**
	 * Computes the recommended capacity of the internal array if actual length of the list
	 * increases or decreases to the given length.
	 * @param oldCapacity the original capacity of the array
	 * @param newLength the new length of the array
	 * @return the recommended new capacity
	 */
	private static int computeNewCapacity(int oldCapacity, int newLength) {
		if (oldCapacity < newLength) { // capacity not sufficient
			return newLength + 32 + (newLength >> 3);
		}
		else { // capacity may be too big
			int d = oldCapacity - newLength;
			if ((d > 1024) || 
				((oldCapacity > (newLength+newLength)) && (d > 64))) 
				return computeNewCapacity(0, newLength);
			return oldCapacity;
		}
	}
	
	/**
	 * Makes sure that this list has at least the given capacity. If not, the size of
	 * the list will be increased.
	 * Please note that this method is only useful for performance reasons. FreezableList
	 * will always automatically increase its capacity if needed. The advantage
	 * of <code>ensureCapacity</code> is that is can minimize the number of costly
	 * resizing operations.
	 * @param newCapacity the minimum capacity
	 */
	public void ensureCapacity(int newCapacity) {
		if (newCapacity < data.length)
			return;
		increaseCapacityTo(newCapacity);
	}

	/**
	 * Adjusts the capacity to the given new size, if it is not large enough yet.
	 * @param newCapacity the new size
	 */
	private void increaseCapacityTo(int newCapacity) {
		int l = data.length-pos;
		if (l >= newCapacity)
			return;
		int n = computeNewCapacity(l, newCapacity);
		if (n != l) {
			Object[] d2 = new Object[n];
			System.arraycopy(data, pos, d2, 0, size);
			data = d2;
			pos = 0;
		}
	}
	
	/**
	 * Freezes the list. After the first invocation of <code>freeze</code>, the list can not
	 * be modified anymore. All calls that would modify the list will throw 
	 * a {@link UnsupportedOperationException}. You can call <code>freeze</code> any 
	 * number of times.
	 * <p>
	 * The method returns an {@link ImmutableList}, which is a thread-safe representation of 
	 * the <code>FreezableList</code> at the time of freezing. The <code>FreezableList</code>
	 * itself is never thread-safe, not even after freezing.
	 * <p>
	 * Freezing can only work if all members of the list are {@link Serializable} (or,
	 * even better, {@link Immutable}).
	 * 
	 * @return the current content as <code>ImmutableList</code>
	 * @throws ActorRuntimeException if one of the values was not {@link Serializable}
	 */
	public ImmutableList<T> freeze() {
		if (immutableVersion != null)
			return immutableVersion;
		if (immutableContent) {
			immutableVersion = new ImmutableList<T>(data, false, pos, size, true);
			return immutableVersion;
		}
		
		int firstMutable = size;
		for (int i = 0; i < size; i++)
			if (!ImmutableHelper.isImmutable(data[i+pos])) {
				firstMutable = i;
				break;
			}
		
		if (firstMutable == size) {
			immutableVersion = new ImmutableList<T>(data, false, pos, size, true);
			return immutableVersion;
		}

		Object[] d2 = new Object[size];
		if (firstMutable > 0)
			System.arraycopy(data, pos, d2, 0, firstMutable);
		for (int i = firstMutable; i < size; i++)
			d2[i] = ImmutableHelper.handleValue("list element", data[i+pos]);
		
		immutableVersion = new ImmutableList<T>(data, false, pos, size, false);
		return immutableVersion;
	}
	
	/**
	 * Adds the given element to the end of the list, increasing the list's size
	 * by one and possibly increasing the lists capacity, if needed.
	 * @param element the element to add. May be null
	 * @return always true
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	public boolean add(Object element) {
		checkFrozen();
		modCount++;

		increaseCapacityTo(size+1);
		data[pos+size] = element;
		size++;
		return true;
	}
	
	/**
	 * Adds the given element into the list at the given position. All 
	 * existing elements from the index to the end will be moved to the back.
	 * The list's size increases by one and, if needed, the lists capacity will
	 * be increased. By specifying the first index after the list's last element,
	 * the new element can be placed into the list's back.  
	 * @param index the index to add the element to. Must not be negative,
	 *    and not larger than the list's current size
	 * @param element the element to add
	 * @throws IndexOutOfBoundsException if the index is not in the valid range
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	public void add(int index, T element) {
		checkFrozen();

		if (index == size)
			add(element);
		else if ((index == 0) && (pos > 0)) {
			modCount++;
			data[--pos] = element;
			size++;
		}
		else {
			checkBounds(index, size+1);
			modCount++;
			increaseCapacityTo(size+1);
			System.arraycopy(data, pos+index, data, pos+index+1, size - index);
			data[pos+index] = element;
			size++;
		}
	}

	/**
	 * Adds the content of the given collection into the list at the given position. All 
	 * existing elements from the index to the end will be moved to the back.
	 * The list's size increases by the collection's size and, if needed, the lists 
	 * capacity will also be increased. By specifying the first index after the list's 
	 * last element, the new elements can be placed into the list's back.  
	 * @param index the index to add the new elements to. Must not be negative,
	 *    and not larger than the list's current size
	 * @param c the collection of elements to add
	 * @return true if the collection has at least one element, false for empty collections
	 * @throws IndexOutOfBoundsException if the index is not in the valid range
	 * @throws IllegalArgumentException if the collection argument is null
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	public boolean addAll(int index, Collection<? extends T> c) {
		if (c == null)
			throw new IllegalArgumentException("Arguments must not be null");
		checkFrozen();
		checkBounds(index, size+1);
		modCount++;
		
		int s = c.size();
		if (s == 0)
			return false;
		
		increaseCapacityTo(size+s);
		if (index == size) {
			for (Object o: c) {
				data[pos+size] = o;
				size++;
			}
		}
		else if ((index == 0) && (pos > 0) && (pos <= s)) {
			int i = pos - s;
			for (Object o: c)
				data[i++] = o;
			pos -= s;
			size += s;
		}
		else {
			System.arraycopy(data, pos+index, data, pos+index+s, size - index);
			int i = pos + index;
			for (Object o: c)
				data[i++] = o;
			size += s;
		}
		
		return true;
	}

	/**
	 * Adds all elements of the given immutable list to the end of the list.
	 * @param list the list to add to this list
	 * @throws IllegalArgumentException if the collection argument is null
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	public void addAll(ImmutableList<? extends T> list) {
		addAll(size, list);
	}
	
	/**
	 * Adds the content of the given immutable list into this list at the given position. All 
	 * existing elements from the index to the end will be moved to the back.
	 * This list's size increases by the collection's size and, if needed, the lists 
	 * capacity will also be increased. By specifying the first index after this list's 
	 * last element, the new elements can be placed into the list's back.  
	 * @param index the index to add the new elements to. Must not be negative,
	 *    and not larger than the list's current size
	 * @param list the immutable list whose elements will be added
	 * @throws IndexOutOfBoundsException if the index is not in the valid range
	 * @throws IllegalArgumentException if the list argument is null
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	public void addAll(int index, ImmutableList<? extends T> list) {
		if (list == null)
			throw new IllegalArgumentException("Arguments must not be null");
		checkFrozen();
		checkBounds(index, size+1);
		modCount++;
		
		int s = list.size();
		if (s == 0)
			return;
		
		increaseCapacityTo(size+s);
		if (index == size) {
			for (Object o: list) {
				data[pos+size] = o;
				size++;
			}
		}
		else if ((index == 0) && (pos > 0) && (pos <= s)) {
			int i = pos - s;
			for (Object o: list)
				data[i++] = o;
			pos -= s;
			size += s;
		}
		else {
			System.arraycopy(data, pos+index, data, pos+index+s, size - index);
			int i = pos + index;
			for (Object o: list)
				data[i++] = o;
			size += s;
		}
	}
	
	/**
	 * Removes all elements from the list and set the list's size to 0.
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	public void clear() {
		checkFrozen();
		modCount++;
		int n = computeNewCapacity(data.length, 0);
		if (n != data.length)
			data = new Object[n];
		else
			Arrays.fill(data, null);
		pos = 0;
		size = 0;
	}

	/**
	 * Returns the element at the given index.
	 * @param index the index of the element to get. Must not be negative,
	 *   and smaller than the list's current size
	 * @return the element at the given index (may be null)
	 * @throws IndexOutOfBoundsException if the index is negative or too large
	 */
	@SuppressWarnings("unchecked")
	public T get(int index) {
		checkBounds(index, size);
		return (T)data[pos+index];
	}

	/**
	 * Checks whether the list is empty (size 0).
	 * @return true if the list is empty, false otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes the element at the given index. All following elements will be moved forward
	 * by one position, and the list's size decreased by one.
	 * @param index the index of the element to remove. Must not be negative,
	 *   and smaller than the list's current size
	 * @throws IndexOutOfBoundsException if the index is negative or too large
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	@SuppressWarnings("unchecked")
	public T remove(int index) {
		checkFrozen();
		checkBounds(index, size);
		modCount++;
		T r = (T)data[pos+index];
		
		int l = data.length - pos;
		int n = computeNewCapacity(l, size-1);
		if (n != l) {
			Object[] d2 = new Object[n];
			if (index > 0)
				System.arraycopy(data, pos, d2, 0, index);
			if (index < size-1)
				System.arraycopy(data, pos+index+1, d2, index, size - index - 1);
			data = d2;
			pos = 0;
			size--;
		}
		else if (index == 0) {
			if (pos >= MAX_POS) {
				System.arraycopy(data, pos+1, data, 0, size-1);
				for (int i = 0; i < pos+1; i++)
					data[size-1+i] = null; // help GC
				pos = 0;
				size--;
			}
			else {
				data[pos] = null; // help GC
				pos++;
				size--;
			}
		}
		else  {
			if (index < size-1)
				System.arraycopy(data, pos+index+1, data, pos+index, size-index-1);
			data[pos+size-1] = null; // help GC
			size--;
		}
		return r;
	}

	/**
	 * Checks whether the instance has been frozen.
	 * @throws UnsupportedOperationException if the instance was frozen
	 */
	private void checkFrozen() {
		if (immutableVersion != null)
			throw new UnsupportedOperationException("FreezableList has been frozen. No modifications allowed");
	}

	/**
	 * Checks whether the index is inside the list's bounds.
	 * @param index the index to check
	 * @param upperLimit the size to check against
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 */
	private void checkBounds(int index, int upperLimit) {
		if (index < 0)
			throw new IndexOutOfBoundsException("Index must not be negative (is "+index+")");
		if (index >= upperLimit)
			throw new IndexOutOfBoundsException("Index too large (is "+index+", list size "+size+")");
	}

	/**
	 * Sets the element at the given index, overwriting the existing element.
	 * @param index the index of the element to set. Must not be negative,
	 *   and smaller than the list's current size
	 * @param element the new element
	 * @throws IndexOutOfBoundsException if the index is negative or too large
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	@SuppressWarnings("unchecked")
	public T set(int index, T element) {
		checkFrozen();
		checkBounds(index, size+1);
		T r = (T)data[pos+index];
		data[pos+index] = element;
		return r;
	}

	/**
	 * Returns the number of elements of the list.
	 * @return the current number of elements
	 */
	public int size() {
		return size;
	}

	/**
	 * Creates an array of this list's size and copies this list's content into
	 * it (shallow copies, references only).
	 * @return the copied array
	 */
	public Object[] toArray() {
		Object[] d2 = new Object[size];
		System.arraycopy(data, pos, d2, 0, size);
		return d2;
	}
	
	/**
	 * Returns an array that contains a copy of this list (shallow copies, references only).
	 * If the given array has at least the length of this list, the elements will
	 * be copied into it. Otherwise a new array will be created whose length is the
	 * same as this list's.
	 * @param a the array to copy the data into, provided that it is large enough
	 * @return the copied array
	 */
	@SuppressWarnings("unchecked")
	public <U extends Object> U[] toArray(U[] a) {
		if ((a != null) && (a.length >= size)) {
			System.arraycopy(data, pos, a, 0, size);
			if (a.length > size)
				a[size] = null;
			return a;
		}
		
		U[] b = (U[])Array.newInstance(a.getClass().getComponentType(), size);
		System.arraycopy(data, pos, b, 0, size);
		return b;
	}
	
	/**
	 * Removes the elements from the given start index (inclusive) to the given end
	 * index (exclusive). If they are the same, no element will be removed. 
	 * All elements starting at the end index will be moved forward.
	 * @param fromIndex the first index of the elements to remove. Must not be negative,
	 *   and smaller than the list's current size
	 * @param toIndex the first index that must not be removed. Must not be negative,
	 *   not larger than the list's current size, and not smaller than the start index
	 * @throws IndexOutOfBoundsException if the indexes are too small or large
	 * @throws UnsupportedOperationException if the list is already frozen
	 */
	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		checkFrozen();
		if (toIndex < fromIndex)
			throw new IndexOutOfBoundsException("toIndex is smaller than fromIndex");
		else if (toIndex == fromIndex)
			return;
		checkBounds(fromIndex, size);
		checkBounds(toIndex, size+1);
		modCount++;
				
		int s = toIndex - fromIndex;
		int l = data.length - pos;
		int n = computeNewCapacity(l, size-s);
		if (n != l) {
			Object[] d2 = new Object[n];
			if (fromIndex > 0)
				System.arraycopy(data, pos, d2, 0, fromIndex);
			if (toIndex < size)
				System.arraycopy(data, pos+toIndex+s, d2, toIndex, size - toIndex);
			data = d2;
			pos = 0;
			size--;
		}
		else  {
			if (toIndex < size)
				System.arraycopy(data, pos+toIndex, data, pos+fromIndex, size-toIndex);
			for (int i = pos+size; i < pos+size+s; i++)
				data[i] = null; // help GC
			size -= s;
		}
	}

}
