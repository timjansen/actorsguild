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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.actorsguildframework.ActorRuntimeException;
import org.actorsguildframework.Immutable;

/**
 * ImmutableMap is a special map implementation that can not be modified after its
 * construction phase. Its elements must all be either Immutable, Serializable or Actors.
 * 
 * Please note that there is a significant performance overhead for Serializable
 * keys and values. They will be copied at least once: the first time when you create the
 * ImmutableSet instance is being created, and additionally every time you read them.
 * 
 * @param <K> the type of the keys
 * @param <V> the type of the values
 * @see Immutable
 */
public final class ImmutableMap<K, V> implements Immutable {
	private static final long serialVersionUID = -5771612082237551689L;
	@SuppressWarnings("unchecked")
	private static final ImmutableMap EMPTY_MAP = new ImmutableMap(Collections.emptyMap());
	
	private final boolean keysImmutable; // true if all keys are immutable
	private final boolean valuesImmutable; // true if all values are immutable
	private final HashMap<K, V> data;
	
	/**
	 * Creates a new immutable map that copies the elements from the given map.
	 * Keys and values that are not immutable (but only Serializable) will be copied. Actors
	 * will be proxied.
	 * @param data the data to be added
	 * @throws ActorRuntimeException if a map member can not be converted
	 * @throws IllegalArgumentException if the argument was null
	 */
	public ImmutableMap(Map<K, V> data) {
		if (data == null)
			throw new IllegalArgumentException("argument was null");
		
		boolean keysImmutable = true;
		boolean valuesImmutable = true;
		this.data = new HashMap<K, V>();
		for (K key: data.keySet()) {
			V value = data.get(key);
			K newKey = key;
			V newValue = value;
			
			if (key != null ){
				if (keysImmutable && !ImmutableHelper.isImmutableType(key.getClass()))
					keysImmutable = false;
				if (!keysImmutable)
					newKey = ImmutableHelper.handleValue("map key", key);
			}

			if (value != null ){
				if (valuesImmutable && !ImmutableHelper.isImmutableType(value.getClass()))
					valuesImmutable = false;
				if (!valuesImmutable)
					newValue = ImmutableHelper.handleValue("map value", value);
			}

			this.data.put(newKey, newValue);
		}
		this.keysImmutable = keysImmutable;
		this.valuesImmutable = valuesImmutable;
	}
	
	/**
	 * Returns a set of all keys in the map. Please note that it is both easier and faster to
	 * use the iterator() method (or the {@link Iterable} interface, to use the map keys in a for loop)
	 * than calling this method only to iterate over all elements in the map.
	 * @return a set of all keys
	 */
	public ImmutableSet<K> keySet() {
		return new ImmutableSet<K>(data.keySet());
	}
	
	/**
	 * Returns the number of elements in the map.
	 * @return the number of elements
	 */
	public int size() {
		return data.size();
	}
	
	/**
	 * Checks whether the map is empty.
	 * @return true if the map is empty, false otherwise
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	/**
	 * Checks whether the set contains the given key. 
	 * @param key the key to search
	 * @return true if the key has been found, false otherwise
	 */
	public boolean containsKey(K key) {
		return data.containsKey(key);
	}

	/**
	 * Checks whether the set contains the given value. 
	 * @param value the value to search
	 * @return true if the value has been found, false otherwise
	 */
	public boolean containsValue(V value) {
		return data.containsValue(value);
	}

	/**
	 * Returns the map value with the given key.
	 * Please note that if the value is non-immutable, a copy of the value will be created and 
	 * the result can only be used in the Thread that invoked the method (unless you use proper
	 * synchronization to read it in other threads). 
	 * @param key the key to look up
	 * @return the value with the given key, or null if not found
	 * @throws ActorRuntimeException if the value is not immutable and can not be copied/serialized
	 */
	public V get(K key) {
		if (valuesImmutable)
			return data.get(key);
		else
			return ImmutableHelper.handleValue("map value", data.get(key));
	}
	
	/**
	 * Returns the content of the map as a Map. All elements that are not Immutable
	 * or actors will be copied again.
	 * @throws ActorRuntimeException if a map key or value can not be copied
	 * @return the set
	 */
	public Map<K, V> toMap() {
		HashMap<K, V> l = new HashMap<K, V>();
		if (keysImmutable && valuesImmutable)
			for (K e: data.keySet())
				l.put(e, data.get(e));
		else if (keysImmutable)
			for (K e: data.keySet())
				l.put(e, ImmutableHelper.handleValue("map value", data.get(e)));
		else if (valuesImmutable)
			for (K e: data.keySet())
				l.put(ImmutableHelper.handleValue("map key", e), data.get(e));
		else
			for (K e: data.keySet())
				l.put(ImmutableHelper.handleValue("map key", e), 
						ImmutableHelper.handleValue("map value", data.get(e)));
		return l;
	}
	
	/**
	 * Returns true if both objects are ImmutableMaps, the key set is identical
	 * and all entries are equal. Calls {@link Object#equals(Object)} on all entries.
	 * @param obj the object to compare to
	 * @return true if the objects are equal
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof ImmutableMap))
			return false;
		if (this == obj)
			return true;
		ImmutableMap o = (ImmutableMap) obj;
		if (o.data.size() != data.size())
			return false;

		for (K key: keySet()) {
			V val = data.get(key);
			Object otherVal = o.data.get(key);
			if ((otherVal == null) && !o.containsKey(key))
				return false;
			if (val == otherVal)
				continue;
			if ((val == null) || !val.equals(otherVal))
				return false;
		}
		return true;
	}
	
	/**
	 * Returns the hash code of the map. Calls {@link Object#hashCode()} on every entry.
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		return data.hashCode();
	}
	
	/**
	 * Returns an empty immutable map.
	 * @param <K> the type of the keys
	 * @param <V> the type of the values
	 * @return the immutable map
	 */
	@SuppressWarnings({ "unchecked" })
	public static <K, V> ImmutableMap<K, V> emptyMap() {
		ImmutableMap<K, V> emptyMap = (ImmutableMap<K, V>) EMPTY_MAP;
		return emptyMap;
	}
}
