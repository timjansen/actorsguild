/*
 *    Copyright 2008,2009 Tim Jansen
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
package org.actorsguildframework.internal.codegenerator;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureWriter;

/**
 * Helpers for extracting signatures from generic types.
 */
public class GenericTypeHelper {

	/**
	 * Writes the signature of the given type to the SignatureWriter.
	 * @param sw the signature writer to use
	 * @param type the Type to analyze
	 */
	@SuppressWarnings("unchecked")
	public static void writeSignature(SignatureWriter sw, java.lang.reflect.Type type) {
		if (type instanceof Class) {
			Class c = (Class) type;
			if (c.isPrimitive())
				sw.visitBaseType(Type.getDescriptor(c).charAt(0));
			else {
				if (c.getDeclaringClass() != null)
					sw.visitInnerClassType(Type.getInternalName(c.getDeclaringClass()));
				sw.visitClassType(Type.getInternalName(c));
			}
		}
		else if (type instanceof GenericArrayType) {
			GenericArrayType gat = (GenericArrayType) type;
			sw.visitArrayType();
			writeSignature(sw, gat.getGenericComponentType());
		}
		else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			writeSignature(sw, pt.getRawType());
			for (java.lang.reflect.Type t: pt.getActualTypeArguments()) {
				sw.visitTypeArgument();
				writeSignature(sw, t);
			}
		}
		else if (type instanceof TypeVariable) {
			TypeVariable<?> tv = (TypeVariable<?>) type;
			sw.visitTypeVariable(tv.getName());
		}
		else if (type instanceof WildcardType) {
			if (((WildcardType) type).getLowerBounds().length + ((WildcardType) type).getUpperBounds().length == 0)
				sw.visitTypeArgument();
			else {
				for (java.lang.reflect.Type t: ((WildcardType) type).getLowerBounds()) {
					sw.visitTypeArgument('+');
					writeSignature(sw, t);
				}
				for (java.lang.reflect.Type t: ((WildcardType) type).getUpperBounds()) {
					sw.visitTypeArgument('-');
					writeSignature(sw, t);
				}
			}
		}
		else
			throw new RuntimeException("Unsupported type "+type);
		sw.visitEnd();
	}

	/**
	 * Converts the given Type into a JVM signature. 
	 * @param type the Type to analyze
	 * @return the resulting signature
	 */
	public static String getSignature(java.lang.reflect.Type type) {
		SignatureWriter sw = new SignatureWriter();
		writeSignature(sw, type);
		return sw.toString();
	}
	

	/**
	 * Converts the given Type into a JVM signature if it is a generic type. Otherwise it returns null.
	 * @param type the Type to analyze
	 * @return the resulting signature, or null if not generic
	 */
	public static String getSignatureIfGeneric(java.lang.reflect.Type type) {
		if (type instanceof Class)
			return null;
		return getSignature(type);
	}
	
	/**
	 * Converts the given Method into a JVM signature. 
	 * @param method the method to analyze
	 * @return the resulting signature
	 */
	public static String getSignature(Method method) {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for (java.lang.reflect.Type t: method.getGenericParameterTypes())
			sb.append(getSignature(t));
		sb.append(')');
		sb.append(getSignature(method.getGenericReturnType()));
		return sb.toString();
	}
	
}
