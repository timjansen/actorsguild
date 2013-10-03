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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.actorsguildframework.ActorRuntimeException;
import org.actorsguildframework.Immutable;

/**
 * ImmutableSet is a special set implementation that can not be modified after its
 * construction phase. As in {@link java.util.Set}, every element can be contained only
 * once in the set. Equality will be checked using {@link java.lang.Object#equals(Object)} 
 * and  {@link java.lang.Object#hashCode()}. The elements do not have an order.
 *  
 * All elements of an ImmutableSet must all be either Immutable, Serializable or Actors.
 * 
 * Please note that there is a significant performance overhead for Serializable
 * elements. They will be copied at least once: the first time when you create the
 * ImmutableSet instance is being created, and additionally every time you read them.
 * 
 * @param <T> the type of the list elements.
 * @see Immutable
 */
public final class ImmutableSet<T> implements Immutable, Iterable<T> {
	private static final long serialVersionUID = 1631952082155811251L;
	@SuppressWarnings("unchecked")
	private static final ImmutableSet EMPTY_SET = new ImmutableSet(Collections.emptySet());
	
	private final boolean allImmutable; // true if all elements are immutable
	private final HashSet<T> data;
	
	/**
	 * Creates a new immutable set that copies the elements from the given collection.
	 * If two (or more) equal elements are in the collection, the last one will be kept.
	 * Elements that are not immutable (but only Serializable) will be copied. Actors
	 * will be proxied.
	 * @param initialData the data to be added
	 * @throws ActorRuntimeException if a collection member can not be converted
	 * @throws IllegalArgumentException if the argument was null
	 */
	public ImmutableSet(Collection<T> initialData) {
		if (initialData == null)
			throw new IllegalArgumentException("Argument was null");
		boolean allImmutable = true;
		this.data = new HashSet<T>();
		for (T element: initialData) {
			if (element != null ){
				if (allImmutable) {
					if (ImmutableHelper.isImmutableType(element.getClass()))
						this.data.add(element);
					else
						allImmutable = false;
				}
				if (!allImmutable)
					this.data.add(ImmutableHelper.handleValue("Array element", element));
			}
			else
				this.data.add(null);
		}
		this.allImmutable = allImmutable;
	}
	
	/**
	 * Returns a immutable set with the given arguments as content.
	 * @param <T> the type of the set elements
	 * @param elements the elements to put into the set
	 * @return a set
	 * @throws ActorRuntimeException if an argument can not be converted
	 */
	public static <T> ImmutableSet<T> create(T... elements) {
		if (elements == null)
			throw new IllegalArgumentException("Argument was null");
		if (elements.length == 0)
			return emptySet();
		else
			return new ImmutableSet<T>(Arrays.asList(elements));
	}
	
	
	/**
	 * Returns an iterator that iterates over all elements of the set.
	 * Please note that in the case of a non-immutable T the results are copies of the values 
	 * that can be only read from the thread that invoked {@link Iterator#next()}, because there 
	 * is no proper synchronization to be readable in other threads.
	 * @return the iterator for the set
	 */
	public Iterator<T> iterator() {
		final Iterator<T> internalIterator = data.iterator();
		if (allImmutable)
			return new Iterator<T>() {
				public boolean hasNext() { return internalIterator.hasNext(); }
				public T next() { return internalIterator.next(); }
	
				public void remove() {
					throw new UnsupportedOperationException("remove() not supported.");
				}
			};
		else 
			return new Iterator<T>() {
				public boolean hasNext() { 
					return internalIterator.hasNext(); 
				}
				public T next() { 
					return ImmutableHelper.handleValue("set element", internalIterator.next());
				}
				public void remove() {
					throw new UnsupportedOperationException("remove() not supported.");
				}
			};
	}

	/**
	 * Returns the number of elements in the set.
	 * @return the number of elements
	 */
	public int size() {
		return data.size();
	}
	
	/**
	 * Checks whether the set is empty.
	 * @return true if the set is empty, false otherwise
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	/**
	 * Checks whether the set contains the given object. A object
	 * is contained when either both the argument and a set element are
	 * null, or the argument is not null and {@link java.lang.Object#equals(Object)}
	 * returns true for one of the set arguments.
	 * @param o the object to search
	 * @return true if the object has been found, false otherwise
	 */
	public boolean contains(T o) {
		return data.contains(o);
	}
	
	/**
	 * Returns the content of the set as a set. All elements that are not Immutable
	 * or actors will be copied again (and thus can only be read from the invoking thread).
	 * @throws ActorRuntimeException if a set member can not be copied
	 * @return the set
	 */
	public Set<T> toSet() {
		HashSet<T> l = new HashSet<T>();
		if (allImmutable)
			for (T e: data)
				l.add(e);
		else
			for (T e: data)
				l.add(ImmutableHelper.handleValue("set element", e));
		return l;
	}

	/**
	 * Returns true if both objects are ImmutableSets and contain the same members. 
	 * Calls {@link Object#equals(Object)} on all entries.
	 * @param obj the object to compare to
	 * @return true if the objects are equal
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof ImmutableSet))
			return false;
		if (this == obj)
			return true;

		ImmutableSet<T> o = (ImmutableSet<T>) obj;
		if (o.data.size() != data.size())
			return false;

		for (T t: this)
			if (!o.contains(t))
				return false;
		return true;
	}
	
	/**
	 * Returns the hash code of the set. Calls {@link Object#hashCode()} on every member.
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		return data.hashCode();
	}
	
	/**
	 * Returns an empty immutable set.
	 * @param <T> the type of the members
	 * @return the immutable set
	 * @see #create(Object...)
	 */
	@SuppressWarnings({ "unchecked" })
	private static <T> ImmutableSet<T> emptySet() {
		ImmutableSet<T> emptySet = (ImmutableSet<T>) EMPTY_SET;
		return emptySet;
	}
}
