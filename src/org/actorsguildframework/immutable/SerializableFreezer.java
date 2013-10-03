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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.actorsguildframework.ActorRuntimeException;
import org.actorsguildframework.Immutable;

/**
 * A class to transport Serializable objects between threads. It freezes the
 * object (by serializing it) on construction, and can later return copies 
 * of the object (by deserializing it).
 * Frozen can be classes that extend Serializable as well as primitives and arrays.
 * @param <T> the type of the frozen object
 * @see #freeze(Serializable)
 */
public final class SerializableFreezer<T> implements Immutable {
	private static final long serialVersionUID = 34470868131720775L;
	private final byte[] frozenObject;
	
	/**
	 * Creates a new SerializableFreezer that contains the given object.
	 * @param object the object. Will be copied immediately.
	 */
	private SerializableFreezer(T object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.close();
			
			frozenObject = baos.toByteArray();
		}
		catch (IOException e) {
			throw new ActorRuntimeException("Error while serializing", e);
		}
	}
	
	/**
	 * Creates a new SerializableFreezer that contains the given object.
	 * The returned freezer can return any number of copies.
	 * @param <T> the type to freeze
	 * @param object the object. Will be copied immediately.
	 * @return the new, frozen object
	 */
	public static <T> SerializableFreezer<T> freeze(T object) {
		return new SerializableFreezer<T>(object);
	}
	
	/**
	 * Returns a deep copy of the frozen object.
	 * @return the frozen object
	 */
	@SuppressWarnings("unchecked")
	public T get() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(frozenObject)); 
			T r = (T) ois.readObject();
			ois.close();
			return r;
		}
		catch (IOException e) {
			throw new ActorRuntimeException("Error while deserializing", e);
		}
		catch (ClassNotFoundException e) {
			throw new ActorRuntimeException("Unexpected error while deserializing", e);
		}
	}
}
