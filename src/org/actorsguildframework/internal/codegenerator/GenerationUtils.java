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

import java.lang.reflect.Modifier;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


/**
 * Helper methods for ASM code generation.
 */
public class GenerationUtils {

	/**
	 * Returns the internal name of the wrapper class for the given primitive.
	 * @param clazz the primitive class (isPrimitive() true)
	 * @return the internal name
	 */
	public static String getWrapperInternalName(Class<?> clazz) {
		assert clazz.isPrimitive();
		if (clazz.equals(Integer.TYPE))
			return "java/lang/Integer";
		else if (clazz.equals(Long.TYPE))
			return "java/lang/Long";
		else if (clazz.equals(Character.TYPE))
			return "java/lang/Character";
		else if (clazz.equals(Boolean.TYPE))
			return "java/lang/Boolean";
		else if (clazz.equals(Double.TYPE))
			return "java/lang/Double";
		else if (clazz.equals(Float.TYPE))
			return "java/lang/Float";
		else if (clazz.equals(Short.TYPE))
			return "java/lang/Short";
		else if (clazz.equals(Byte.TYPE))
			return "java/lang/Byte";
		else 
			throw new RuntimeException("Oops, is "+clazz+" a primitive?");
	}

	/**
	 * Writes an instructions to the given MethodVisitor which will load the default
	 * value for the given class. For primitives, it will load 0 or false in the
	 * correct type onto the stack. For references, the generated code will put a null.
	 * @param mv the MethodVisitor to write to
	 * @param clazz the class to write the default for.
	 */
	public static void generateLoadDefault(MethodVisitor mv, Class<?> clazz) {
		if (!clazz.isPrimitive())
			mv.visitInsn(Opcodes.ACONST_NULL);
		else if (clazz.equals(Integer.TYPE) || clazz.equals(Character.TYPE) || clazz.equals(Short.TYPE) ||
				clazz.equals(Byte.TYPE) || clazz.equals(Boolean.TYPE))
			mv.visitInsn(Opcodes.ICONST_0);
		else if (clazz.equals(Long.TYPE))
			mv.visitInsn(Opcodes.LCONST_0);
		else if (clazz.equals(Double.TYPE))
			mv.visitInsn(Opcodes.DCONST_0);
		else if (clazz.equals(Float.TYPE))
			mv.visitInsn(Opcodes.FCONST_0);
		else 
			throw new RuntimeException("Oops, forgot the primitive "+clazz+"?");
	}
	
	/**
	 * Loads the class given as byte code. Code from ASM FAQ.
	 * @param className the name of the class (using '.' as package separator)
	 * @param classByteCode the byte code
	 * @return the new class
	 */
	public static Class<?> loadClass (String className, byte[] classByteCode) {
		//override classDefine (as it is protected) and define the class.
		Class<?> clazz = null;
		try {
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Class<?> cls = Class.forName("java.lang.ClassLoader");
			java.lang.reflect.Method method =
				cls.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
	
			// protected method invocation
			method.setAccessible(true);
			try {
				Object[] args = new Object[] { className, classByteCode, new Integer(0), new Integer(classByteCode.length)};
				clazz = (Class<?>) method.invoke(loader, args);
			} finally {
				method.setAccessible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return clazz;
	}

	/**
	 * Converts the given reflection modifiers (e.g. Method.getModifier()) and translates them
	 * into ASM modifiers ({@link Opcodes#ACC_PUBLIC} etc).
	 * @param reflectModifiers the modifiers of the reflection API
	 * @return the modifiers for ASM ({@link Opcodes#ACC_PUBLIC} etc)
	 */
	public static int convertAccessModifiers(int reflectModifiers) {
		return reflectModifiers & (Modifier.PUBLIC + Modifier.PROTECTED + Modifier.PRIVATE);
	}
	
	/**
	 * Returns the descriptor type for the given class to put it into a frame descriptor
	 * for MethodVisitor.visitFrame().
	 * Does not support uninitialized types.
	 * @param clazz the class of the type. May be null for the null descriptor.
	 * @return the descriptor
	 */
	public static Object getFrameType(Class<?> clazz) {
		if (clazz == null)
			return Opcodes.NULL;
		if (!clazz.isPrimitive())
			return Type.getInternalName(clazz);
		else if (clazz.equals(Integer.TYPE) || clazz.equals(Character.TYPE) || clazz.equals(Short.TYPE) ||
				clazz.equals(Byte.TYPE) || clazz.equals(Boolean.TYPE))
			return Opcodes.INTEGER;
		else if (clazz.equals(Long.TYPE))
			return Opcodes.LONG;
		else if (clazz.equals(Double.TYPE))
			return Opcodes.DOUBLE;
		else if (clazz.equals(Float.TYPE))
			return Opcodes.FLOAT;
		else 
			throw new RuntimeException("Oops, forgot the primitive "+clazz+"?");
	}
}
