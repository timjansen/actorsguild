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
package org.actorsguildframework;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Props is an immutable set of properties (key/value pairs) for Actors. Each time 
 * a new property element is added, it creates a new version. The older versions can
 * be retrieved though.<p>
 * Props is used to create actors with initial properties.<p>
 * Example of creating a Props list:
 * <pre>Props props = new Props("a", 1).add("b", 2).add("c", 3);
 * </pre>
 */
public final class Props implements Iterable<Props> {
	private final Props prevProps; // null for end-of-list
	private final String key;
	private final Object value;
	
	/**
	 * Creates a new instance.
	 * @param prevProps the previous Props object. Null for end.
	 * @param key the key of the last property in the list. Must be non-null and non-empty.
	 * @param value the value of the last property in the list. May be null.
	 * @exception IllegalArgumentException if the key is not set
	 */
	private Props(Props prevProps, String key, Object value) {
		if ((key == null) || (key.length() == 0))
			throw new IllegalArgumentException("Key must be set to a non-empty string.");
		this.prevProps = prevProps;
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Creates a new Props list with a single element.
	 * @param key the key of the first element. Must be non-null and non-empty.
	 * @param value the value of the first element
	 * @exception IllegalArgumentException if the key is not set
	 */
	public Props(String key, Object value) {
		this(null, key, value);
	}
	
	/**
	 * Adds a new element to the beginning of this list and returns this new list (does
	 * not modify the existing list!). The new element has the given key and value.
	 * @param key the key of the additional element. Must be non-null and non-empty.
	 * @param value the value of the additional element
	 * @return the new list
	 */
	public Props add(String key, Object value) {
		return new Props(this, key, value);
	}
	
	/**
	 * Returns the last key that has been added.
	 * @return the list head's key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Returns the last value that has been added.
	 * @return the last value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Checks whether the given key is at least once in the list.
	 * @param key the key to check
	 * @return true if the key is in the list, false otherwise
	 */
	public boolean hasKey(String key) {
		Props p = this; 
		while (p != null)
			if (p.key.equals(key))
				return true;
		return false;
	}
	
	/**
	 * Returns the value of the given key, if it exists.
	 * (If it has been inserted more than once, the most recent value is returned)
	 * @param key the key to check
	 * @return the value (may be null), or null if the value does not exist
	 */
	public Object get(String key) {
		Props p = this; 
		while (p != null)
			if (p.key.equals(key))
				return p.value;
		return null;
	}
	
	/**
	 * Gets the previous version of this list, thus without the last element.
	 * @return this list without the last element. Null if empty
	 */
	public Props tail() {
		return prevProps;
	}
	
	/**
	 * Iterates of all versions of the Props list, starting with the newest one. In other
	 * words, it first includes the whole list, then the list without the last element, then 
	 * the list with the two last elements and so on. It ends with the Props list with only a
	 * single element. Use {@link #getKey()} and {@link #getValue()} to get the keys
	 * and values of all elements.
	 * <p>
	 * Example that creates a list and iterates over all elements.
	 * <pre>
	 * Props props = new Props("a", 1).add("b", 2).add("c", 3);
	 * for (Props p: props)
	 *    System.out.println(p.getKey() + " = " + p.getValue());
	 * </pre>
	 */
	public Iterator<Props> iterator() {
		final Props self = this;
		return new Iterator<Props>() {
			Props p = self;
			
			public boolean hasNext() {
				return p != null;
			}
			
			public void remove() {
				throw new UnsupportedOperationException("Rmeove not supported");
			}
			
			public Props next() {
				if (p == null)
					throw new NoSuchElementException("End of props reached.");
				Props r = p;
				p = p.prevProps;
				return r;
			}
		};
	}
}
