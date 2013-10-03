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

import org.actorsguildframework.Actor;
import org.actorsguildframework.ActorRuntimeException;
import org.actorsguildframework.Immutable;
import org.actorsguildframework.internal.ActorProxy;

/**
 * A helper for dealing with immutable values.
 */
public class ImmutableHelper {

	/**
	 * Checks whether the given value has an immutable type.
	 * Immutable are the primitive types, the number types in <code>java.lang</code>,
	 * <code>Void</code>, <code>String</code>, <code>Enum</code> and all classes that 
	 * implement {@link Immutable}.
	 * @param value the value to check. May be null (null is immutable)
	 * @return true if <code>value</code> is immutable
	 * @throws IllegalArgumentException if the argument was null
	 */
	public static boolean isImmutable(Object value) 
	{
		if (value == null)
			return true;
		return isImmutableType(value.getClass());
	}
	
	/**
	 * Checks whether the given class represents an immutable type.
	 * Immutable are the primitive types, the number types in <code>java.lang</code>,
	 * <code>Void</code>, <code>String</code>, <code>Enum</code> and all classes that 
	 * implement {@link Immutable}.
	 * @param clazz the class to check
	 * @return true if <code>clazz</code> is immutable
	 * @throws IllegalArgumentException if the argument was null
	 */
	public static boolean isImmutableType(Class<? extends Object> clazz) 
	{
		if (clazz == null)
			throw new IllegalArgumentException("argument was null");
		return clazz.isPrimitive() || 
			clazz.equals(String.class) || 
			clazz.equals(Long.class) || 
			clazz.equals(Short.class) || 
			clazz.equals(Integer.class) || 
			clazz.equals(Boolean.class) || 
			clazz.equals(Byte.class) || 
			clazz.equals(Double.class) || 
			clazz.equals(Float.class) || 
			clazz.equals(Character.class) || 
			clazz.equals(Void.class) ||
			Enum.class.isAssignableFrom(clazz) ||
			Immutable.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Handles a value which must be either {@link Immutable} or {@link Serializable}. For 
	 * immutables, the value is directly returned. If another Serializable is given,
	 * a copy of the value will be returned. 
	 * @param <T> the type of the argument
	 * @param argumentSpecifier a name or number to describe the argument. Needed 
	 * only for exceptions
	 * @param value the value to convert. Can be null (the method will then return null)
	 * @return either value or a copy of it. Is null if value was null.
	 * @throws ActorRuntimeException if the argument can not be converted
	 * @throws IllegalArgumentException if the argumentSpecifier was null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T handleValue(String argumentSpecifier, T value) {
		if (argumentSpecifier == null)
			throw new IllegalArgumentException("argumentSpecifier was null");
		if (value == null)
			return null;

		if (value instanceof Actor) {
			if (!(value instanceof ActorProxy))
				throw new ActorRuntimeException("Argument "+argumentSpecifier+" is not a valid argument: "+
							"it is an Actor without proxy. Somehow you managed to get an instance of" +
							"an Actor class without proxy. The Actor's Guild should have prevented this.");
			return value;
		}
		else if (value instanceof Serializable) {
			Class<? extends Object> c = value.getClass();
			if (isImmutableType(c))
				return value;

			Serializable sv = (Serializable) value;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(sv);
				oos.close();
				
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())); 
				T r = (T) ois.readObject();
				ois.close();
				return r;
			}
			catch (IOException e) {
				throw new ActorRuntimeException("Error while serializing or deserializing argument "+argumentSpecifier, e);
			}
			catch (ClassNotFoundException e) {
				throw new ActorRuntimeException("Unexpected error while serializing or deserializing argument "+argumentSpecifier, e);
			}
		}
		else
			throw new ActorRuntimeException("Argument "+argumentSpecifier+" is neither an Actor nor Serializable.");
	}
}
