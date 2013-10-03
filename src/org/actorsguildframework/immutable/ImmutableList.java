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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.actorsguildframework.ActorRuntimeException;
import org.actorsguildframework.Immutable;

/**
 * ImmutableList is a special list implementation that can not be modified after its
 * construction phase. Its elements must all be either Immutable, Serializable or Actors.
 *
 * The fastest way to create a ImmutableList from a dynamic list is to 
 * create the list as a {@link FreezableList} first and then 
 * {@link FreezableList#freeze()} it.
 * 
 * Please note that there is a significant performance overhead for non-immutable 
 * Serializable elements. They will be copied at least once: the first time when you create the
 * ImmutableList instance is being created, and additionally every time you read them.
 * 
 * @param <T> the type of the list elements.
 * @see FreezableList
 * @see Immutable
 */
public class ImmutableList<T> implements Immutable, Iterable<T> {
	private static final long serialVersionUID = 8631766476252446155L;
	@SuppressWarnings("unchecked")
	private static final ImmutableList EMPTY_LIST = new ImmutableList(new Object[] {});
	
	private final Object[] data; 
	private final int firstIndex; // first element (for sublists)
	private final int size;       // size (for sublists)
	private final boolean allImmutable; // true if all elements are immutable
	
	/**
	 * Creates a new immutable list that contains the content of the given array. 
	 * @param elements the array of elements
	 * @see #create(Object...)
	 * @throws ActorRuntimeException if an array member can not be converted
	 * @throws IllegalArgumentException if the argument was null
	 */
	public ImmutableList(T[] elements) {
		if (elements == null)
			throw new IllegalArgumentException("argument was null");
		boolean allImmutable = true;
		this.data = new Object[elements.length];
		if (ImmutableHelper.isImmutableType(elements.getClass().getComponentType()))
			System.arraycopy(elements, 0, data, 0, elements.length);
		else
			for (int i = 0; i < elements.length; i++) {
				T element = elements[i];
				if (element != null ){
					if (allImmutable) {
						if (ImmutableHelper.isImmutableType(element.getClass()))
							this.data[i] = element;
						else
							allImmutable = false;
					}
					if (!allImmutable)
						this.data[i] = ImmutableHelper.handleValue("Array element", element);
				}
			}
		this.allImmutable = allImmutable;
		firstIndex = 0;
		size = elements.length;
	}
	
	/**
	 * Returns a immutable list with the given arguments as content.
	 * @param <T> the type of the list elements
	 * @param elements the elements to put into the list
	 * @return a list
	 * @throws ActorRuntimeException if an argument can not be converted
	 */
	public static <T> ImmutableList<T> create(T... elements) {
		if (elements == null)
			throw new IllegalArgumentException("argument was null");
		if (elements.length > 0)
			return new ImmutableList<T>(elements);
		else
			return emptyList();
	}
	
	/**
	 * Creates a new immutable list that contains the content of the Collection. 
	 * @param elements the collection of elements to copy
	 * @see #create(Object...)
	 * @throws ActorRuntimeException if an array member can not be converted
	 * @throws IllegalArgumentException if the argument was null
	 */
	public ImmutableList(Collection<T> elements) {
		if (elements == null)
			throw new IllegalArgumentException("argument was null");
		boolean allImmutable = true;
		int i = 0;
		this.data = new Object[elements.size()];
		for (T element: elements) {
			if (element != null ) {
				if (allImmutable) {
					if (ImmutableHelper.isImmutableType(element.getClass()))
						this.data[i] = element;
					else
						allImmutable = false;
				}
				if (!allImmutable)
					this.data[i] = ImmutableHelper.handleValue("Array element", element);
			}
			i++;
		}
		firstIndex = 0;
		size = elements.size();
		this.allImmutable = allImmutable;
	}

	/**
	 * Special internal ctor that allows avoiding the complicated construction phase
	 * of immutable list. 
	 * @param data the data. Must not be used by anything else!!
	 * @param copyList true if the array needs to be copied (shallow copy)
	 * @param firstIndex the index of the first element to be used. If the list is copied,
	 * only this part is copied. If the list is taken, the object is configured to use
	 * only this part
	 * @param size the number of elements to use from the given data array
	 * @param isAllImmutable true if all elements are immutable
	 */
	ImmutableList(Object[] data, boolean copyList, int firstIndex,
			int size, boolean isAllImmutable) 
	{
		if (copyList) {
			this.data = new Object[data.length];
			System.arraycopy(data, firstIndex, this.data, 0, size);
		}
		else
			this.data = data;
		this.firstIndex = copyList ? 0 : firstIndex;
		this.size = size;
		this.allImmutable = isAllImmutable;
	}
	
	/**
	 * Returns an iterator that iterates over all elements of the list.
	 * Please note that in the case of a non-immutable T the results are copies of the values.
	 * Thus they can be only read from the thread that invoked {@link Iterator#next()}, unless you add
	 * additional synchronization.
	 * @return the iterator for the list
	 */
	public Iterator<T> iterator() {
		if (allImmutable)
			return new Iterator<T>() {
				int pos = 0;	
				public boolean hasNext() {
					return pos < size;
				}
	
				@SuppressWarnings("unchecked")
				public T next() {
					if (pos >= size)
						throw new NoSuchElementException("End of list reached");
					return (T) data[firstIndex + pos++];
				}
	
				public void remove() {
					throw new UnsupportedOperationException("remove() not supported.");
				}
			};
		else 
			return new Iterator<T>() {
				int pos = 0;	
				public boolean hasNext() {
					return pos < size;
				}
		
				@SuppressWarnings("unchecked")
				public T next() {
					if (pos >= size)
						throw new NoSuchElementException("End of list reached");
					return ImmutableHelper.handleValue("Immutable list element", (T) data[firstIndex + pos++]);
				}
		
				public void remove() {
					throw new UnsupportedOperationException("remove() not supported.");
				}
			};
	}

	/**
	 * Returns the number of elements in the list.
	 * @return the number of elements
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Checks whether the list is empty.
	 * @return true if the list is empty, false otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns the element at the given index, never copies it.
	 * @param index the index of the element to retrieve (0 based)
	 * @return the element at the given index
	 * @throws IndexOutOfBoundsException if the index is negative or too high
	 * @throws ActorRuntimeException if an array member is not immutable and can not be copied/serialized
	 */
	@SuppressWarnings("unchecked")
	private T getNoCopy(int index) {
		return (T)data[firstIndex+index];
	}
	
	/**
	 * Returns the element at the given index. Please note that in the case of a non-immutable T the
	 * result is a copy of the value. Without proper synchronization is can be only read from 
	 * the thread that invoked {@link #get(int)}.
	 * @param index the index of the element to retrieve (0 based)
	 * @return the element at the given index
	 * @throws IndexOutOfBoundsException if the index is negative or too high
	 * @throws ActorRuntimeException if an array member is not immutable and can not be copied/serialized
	 */
	public T get(int index) {
		if (allImmutable)
			return getNoCopy(index);
		else
			return ImmutableHelper.handleValue("list element", getNoCopy(index));
	}
	
	/**
	 * Checks whether the list contains the given object. A object
	 * is contained when either both the argument and a list element are
	 * null, or the argument is not null and {@link java.lang.Object#equals(Object)}
	 * returns true for one of the list arguments.
	 * @param o the object to search
	 * @return true if the object has been found, false otherwise
	 */
	public boolean contains(T o) {
		if (o == null) {
			for (int i = firstIndex; i < firstIndex+size; i++)
				if (data[i] == null)
					return true;
		}
		else
			for (int i = firstIndex; i < firstIndex+size; i++)
				if (o.equals(data[i]))
					return true;
		return false;
	}
	
	// copies the list elements into the given array
	private void copyIntoArray(Object[] dest) 
	{
		if (allImmutable)
			System.arraycopy(data, firstIndex, dest, 0, size);
		else
			for (int i = firstIndex; i < firstIndex+size; i++)
				dest[i] = ImmutableHelper.handleValue("list element", data[i]);
	}
	
	/**
	 * Returns the content of the list as an array. All elements that are not Immutable
	 * or actors will be copied again (and thus can only be read from the invoking thread).
	 * @throws ActorRuntimeException if an array member can not be copied
	 * @return the array containing the list's members
	 * @see #toArray(Object[])
	 */
	public Object[] toArray() {
		Object[] data = new Object[size];
		copyIntoArray(data);
		return data;
	}
	
	/**
	 * Returns the content of the list as an array. All elements that are not Immutable
	 * or actors will be copied again (and thus can only be read from the invoking thread). 
	 * If the given array is large enough, the elements will be copied into it. Otherwise 
	 * a new array will be created and returned.
	 * @param array an array to copy the data into, if large enough
	 * @throws ActorRuntimeException if an array member can not be copied
	 * @return the array containing the list's members
	 * @throws IllegalArgumentException if the array was null
	 */
	@SuppressWarnings("unchecked")
	public T[] toArray(T[] array) {
		if (array == null)
			throw new IllegalArgumentException("array was null");
		T[] dest = array.length >= size ? array : (T[])Array.newInstance(array.getClass().getComponentType(), size);
		copyIntoArray(dest);
		return (T[]) dest;

	}
	
	/**
	 * Returns the content of the list as a List. All elements that are not Immutable
	 * or actors will be copied again (and thus can only be read from the invoking thread).
	 * @throws ActorRuntimeException if an array member can not be copied
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<T> toList() {
		ArrayList<T> l = new ArrayList<T>(size);
		if (allImmutable)
			for (int i = 0; i < size; i++)
				l.add((T)data[firstIndex+i]);
		else
			for (int i = 0; i < size; i++)
				l.add(ImmutableHelper.handleValue("list element", (T)data[firstIndex+i]));
		return l;
	}
	
	/**
	 * Creates a sub-list view for the current list and returns it. It behaves like a list that
	 * contains only the specified elements, but accesses the same elements as the original
	 * list. 
	 * Creating sub-list is very fast, but please note that it will not reduce the memory
	 * use of the original list, even if the original ImmutableList object is being garbage collected.
	 * All references to the original list will still be kept.
	 * @param fromIndex the first index of the list (inclusive)
	 * @param toIndex the last index of the list (exclusive)
	 * @return the sub-list view
	 * @throws IndexOutOfBoundsException if an index is negative, the first index is smaller than
	 * the second, of the second is larger that the list's size 
	 */
	public ImmutableList<T> subList(int fromIndex, int toIndex) {
		if (fromIndex < 0)
			throw new IndexOutOfBoundsException(String.format("First index must be positive, but is %d.", fromIndex));
		if (toIndex > size)
				throw new IndexOutOfBoundsException(String.format("Second index must not be larger than the list size %d, but is %d.", size, toIndex));
		if (toIndex < fromIndex)
			throw new IndexOutOfBoundsException(String.format("Second index must not be smaller than the first index %d, but is %d.", fromIndex, toIndex));
		
		return new ImmutableList<T>(data, false, this.firstIndex + fromIndex, toIndex - fromIndex, allImmutable);
	}

	/**
	 * Returns true if both objects are ImmutableLists and all members are equal.
	 * @param obj the object to compare to
	 * @return true if the objects are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof ImmutableList))
			return false;
		if (this == obj)
			return true;
		ImmutableList<?> o = (ImmutableList<?>) obj;
		if (o.size() != size())
			return false;
		for (int i = 0; i < size(); i++) {
			Object a = getNoCopy(i);
			Object b = o.getNoCopy(i);
			if (a == null)
				return b == null;
			if (!a.equals(b))
				return false;
		}
		return true;
	}

	/**
	 * Returns the hash code of the list. Calls {@link Object#hashCode()} on every member.
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		int h = size();
		for (T o: this)
			if (o != null)
				h = h ^ o.hashCode();
		return h;
	}
	
	/**
	 * Returns an empty immutable list.
	 * @param <T> the type of the members
	 * @return the immutable list
	 * @see #create(Object...)
	 */
	@SuppressWarnings({ "unchecked" })
	private static <T> ImmutableList<T> emptyList() {
		ImmutableList<T> emptyList = (ImmutableList<T>) EMPTY_LIST;
		return emptyList;
	}
}
