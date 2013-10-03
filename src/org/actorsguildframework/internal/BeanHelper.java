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
package org.actorsguildframework.internal;

/**
 * Static helpers for generated beans.
 */
public class BeanHelper {
	private BeanHelper() {
	}
	
	/**
	 * Extracts an int value from the given Object, if possible.
	 * @param propName the name of the property to extract (needed for exceptions)
	 * @param value the value to convert. Can be either a {@link Number} or a {@link Character}
	 * @return the resulting integer
	 * @throws IllegalArgumentException if the value was null or an unsupported type
	 */
	public static int getIntFromPropValue(String propName, Object value) {
		if (value == null)
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not null, which is not allowed for primitives.", propName));
		else if (value instanceof Number)
			return ((Number)value).intValue();
		else if (value instanceof Character)
			return ((Character)value).charValue();
		else
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not compatible with int primitives. Given class: %s", propName, value.getClass().getName()));
	}
	
	/**
	 * Extracts a char value from the given Object, if possible.
	 * @param propName the name of the property to extract (needed for exceptions)
	 * @param value the value to convert. Can be either a {@link Number} or a {@link Character}
	 * @return the resulting char
	 * @throws IllegalArgumentException if the value was null or an unsupported type
	 */
	public static char getCharFromPropValue(String propName, Object value) {
		if (value == null)
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not null, which is not allowed for primitives.", propName));
		else if (value instanceof Integer)
			return (char)((Integer)value).intValue();
		else if (value instanceof Character)
			return ((Character)value).charValue();
		else
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not compatible with char primitives. Given class: %s", propName, value.getClass().getName()));
	}
	
	/**
	 * Extracts a boolean value from the given Object, if possible.
	 * @param propName the name of the property to extract (needed for exceptions)
	 * @param value the value to convert. Can be either a {@link Number} or a {@link Character}
	 * @return the resulting boolean
	 * @throws IllegalArgumentException if the value was null or an unsupported type
	 */
	public static boolean getBooleanFromPropValue(String propName, Object value) {
		if (value == null)
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not null, which is not allowed for primitives.", propName));
		else if (value instanceof Boolean)
			return ((Boolean)value).booleanValue();
		else
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not compatible with boolean primitives. Given class: %s", propName, value.getClass().getName()));
	}
	
	/**
	 * Extracts a byte value from the given Object, if possible.
	 * @param propName the name of the property to extract (needed for exceptions)
	 * @param value the value to convert. Can be either a {@link Number} or a {@link Character}
	 * @return the resulting byte
	 * @throws IllegalArgumentException if the value was null or an unsupported type
	 */
	public static byte getByteFromPropValue(String propName, Object value) {
		if (value == null)
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not null, which is not allowed for primitives.", propName));
		else if (value instanceof Number)
			return ((Number)value).byteValue();
		else
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not compatible with byte primitives. Given class: %s", propName, value.getClass().getName()));
	}
	
	/**
	 * Extracts a short value from the given Object, if possible.
	 * @param propName the name of the property to extract (needed for exceptions)
	 * @param value the value to convert. Can be either a {@link Number} or a {@link Character}
	 * @return the resulting short
	 * @throws IllegalArgumentException if the value was null or an unsupported type
	 */
	public static short getShortFromPropValue(String propName, Object value) {
		if (value == null)
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not null, which is not allowed for primitives.", propName));
		else if (value instanceof Number)
			return ((Number)value).shortValue();
		else
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not compatible with short primitives. Given class: %s", propName, value.getClass().getName()));
	}
	
	/**
	 * Extracts a long value from the given Object, if possible.
	 * @param propName the name of the property to extract (needed for exceptions)
	 * @param value the value to convert. Can be either a {@link Number} or a {@link Character}
	 * @return the resulting long
	 * @throws IllegalArgumentException if the value was null or an unsupported type
	 */
	public static long getLongFromPropValue(String propName, Object value) {
		if (value == null)
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not null, which is not allowed for primitives.", propName));
		else if (value instanceof Number)
			return ((Number)value).longValue();
		else
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not compatible with long primitives. Given class: %s", propName, value.getClass().getName()));
	}
	
	/**
	 * Extracts a float value from the given Object, if possible.
	 * @param propName the name of the property to extract (needed for exceptions)
	 * @param value the value to convert. Can be either a {@link Number} or a {@link Character}
	 * @return the resulting float
	 * @throws IllegalArgumentException if the value was null or an unsupported type
	 */
	public static float getFloatFromPropValue(String propName, Object value) {
		if (value == null)
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not null, which is not allowed for primitives.", propName));
		else if (value instanceof Number)
			return ((Number)value).floatValue();
		else
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not compatible with float primitives. Given class: %s", propName, value.getClass().getName()));
	}
	
	/**
	 * Extracts a double value from the given Object, if possible.
	 * @param propName the name of the property to extract (needed for exceptions)
	 * @param value the value to convert. Can be either a {@link Number} or a {@link Character}
	 * @return the resulting double
	 * @throws IllegalArgumentException if the value was null or an unsupported type
	 */
	public static double getDoubleFromPropValue(String propName, Object value) {
		if (value == null)
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not null, which is not allowed for primitives.", propName));
		else if (value instanceof Number)
			return ((Number)value).doubleValue();
		else
			throw new IllegalArgumentException(String.format("Value given for property \"%s\" is not compatible with double primitives. Given class: %s", propName, value.getClass().getName()));
	}
}
