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
package org.actorsguildframework.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.actorsguildframework.ConfigurationException;
import org.actorsguildframework.annotations.Bean;
import org.actorsguildframework.annotations.DefaultValue;
import org.actorsguildframework.annotations.Initializer;
import org.actorsguildframework.annotations.Prop;

/**
 * Describes the configuration of {@link Bean}.
 * Immutable thread-safe.
 */
public final class BeanClassDescriptor {
	/**
	 * The properties of the bean class.
	 */
	private final PropertyDescriptor[] properties;
	
	/**
	 * The @Initializer methods of the bean class, ordered base class methods first
	 * (invocation order).
	 */
	private final Method[] initializers;
	
	/**
	 * True if the bean is thread-safe (synchronized), false otherwise.
	 */
	private final boolean threadSafe;
	
	/**
	 * Creates a new instance.
	 * @param properties the properties of the bean. Will be copied
	 * @param initializers the initializers of the bean, ordered base class methods first. Will be copied
	 * @param threadSafe true if the bean is thread-safe, false otherwise
	 */
	private BeanClassDescriptor(Collection<PropertyDescriptor> properties, List<Method> initializers, boolean threadSafe ) {
		this.properties = properties.toArray(new PropertyDescriptor[properties.size()]);
		this.initializers = initializers.toArray(new Method[initializers.size()]);
		this.threadSafe = threadSafe;
	}
	
	/**
	 * Creates a new BeanClassDescriptor for the given bean class.
	 * @param beanClass the bean's class
	 * @return the new instance
	 * @throws ConfigurationException if the agent is not configured correctly
	 */
	public static BeanClassDescriptor create(Class<?> beanClass) {
		// evaluate @Bean
		boolean threadSafe;
		Bean ba = beanClass.getAnnotation(Bean.class);
		if (ba == null)
			threadSafe = false;
		else
			threadSafe = true;

		// check modifiers
		if (Modifier.isInterface(beanClass.getModifiers()))
			throw new ConfigurationException(String.format("@Bean class %s is an interface. You can not create an instance of an interface.", beanClass));
		if (Modifier.isFinal(beanClass.getModifiers()))
			throw new ConfigurationException(String.format("@Bean class %s is final. You must not define a @Bean as final.", beanClass));
		if ((beanClass.getDeclaringClass() != null) && !Modifier.isStatic(beanClass.getModifiers()))
			throw new ConfigurationException(String.format("@Bean class %s is a non-static inner class. If you declare a @Bean as inner class, you must use the 'static' modifier.", beanClass));
		
		// check that there is a default constructor
		Constructor<?> ctor;
		try {
			ctor = beanClass.getDeclaredConstructor();
		}
		catch (NoSuchMethodException e)
		{
			throw new ConfigurationException(String.format("@Bean class %s does not have a default constructor (without arguments). "+
					"A default constructor is required for every Bean.", beanClass), 
					e);
		}
		
		if (Modifier.isPrivate(ctor.getModifiers()))
			throw new ConfigurationException(String.format("Default constructor of class %s must not be private. Every bean needs a non-private default constructor.", beanClass));

		//  collect all fields in the class (including sub-classes)
		ArrayList<Field> fields = getAllFields(beanClass);
		
		// find all fields that have a @DefaultValue annotation
		HashMap<String, Field> defaultValueFields = new HashMap<String, Field>(); 
		for (Field f: fields) {
			DefaultValue dv = f.getAnnotation(DefaultValue.class);
			if (dv == null)
				continue;
			if (Modifier.isPrivate(f.getModifiers()))
					throw new ConfigurationException(String.format("Field %s in %s has a @DefaultValue annotation, but is private. Default values can not be private (all other modes are ok).", f.getName(), beanClass.getName()));
			if ((dv.value() == null) || (dv.value().length() == 0))
				throw new ConfigurationException(String.format("Field %s in %s has a @DefaultValue annotation, but no property name set.", f.getName(), beanClass.getName()));
			if (defaultValueFields.containsKey(dv.value()))
				throw new ConfigurationException(String.format("Fields %s and %s in %s both have @DefaultValue annotation for the property %s. You can have only one default value.", f.getName(), defaultValueFields.get(dv.value()).getName(), beanClass.getName(), dv.value()));				
				
			if ((!Modifier.isStatic(f.getModifiers())) || (!Modifier.isFinal(f.getModifiers())))
				throw new ConfigurationException(String.format("Field %s in %s has a @DefaultValue annotation, but is not 'static final'. Default values must be static final.", f.getName(), beanClass.getName()));
			defaultValueFields.put(dv.value(), f);
		}

		//  collect all methods in the class (including sub-classes)
		ArrayList<Method> methods = getAllMethods(beanClass, Object.class);
		
		
		HashMap<String, PropertyDescriptor> properties = new HashMap<String, PropertyDescriptor>();
		// find getters
		for (Method m: methods) {
			String methodName = m.getName();
			Prop propAn = m.getAnnotation(Prop.class);
			boolean isGetter = (methodName.length() >= 4) && methodName.startsWith("get") && methodName.substring(3, 4).toUpperCase().equals(methodName.substring(3, 4));
			if (!isGetter) {
				if (propAn != null)
					throw new ConfigurationException(String.format("Method %s in %s has a @Prop annotation, but has no getter name. Getters must start with \"get\", followed by a capital letter. Setters do not need @Prop, only their getter.", methodName, beanClass.getName()));
				continue;
			}
			if (Modifier.isStatic(m.getModifiers())) {
				if (propAn != null)
					throw new ConfigurationException(String.format("Method %s in %s is static and has a @Prop annotation. To use @Prop, the method must not be static.", methodName, beanClass.getName()));		
				continue;
			}
			if (Modifier.isPrivate(m.getModifiers())) {
				if (propAn != null) 
					throw new ConfigurationException(String.format("Method %s in %s is private and has a @Prop annotation. To use @Prop, the method must not be private.", methodName, beanClass.getName()));
				continue;
			}
			if (m.getParameterTypes().length > 0) {
				if (propAn != null) 
					throw new ConfigurationException(String.format("Method %s in %s has a @Prop annotation, but requires arguments. A getter can not require arguments.", methodName, beanClass.getName()));
				continue;
			}
			if ((m.getExceptionTypes().length > 0) && (propAn != null)) 
				throw new ConfigurationException(String.format("Method %s in %s has a @Prop annotation, but throws exceptions. A @Prop getter can not throw exceptions.", methodName, beanClass.getName()));

			String name = getPropertyNameFromAccessor(methodName);
			PropertyDescriptor pd = PropertyDescriptor.createGetterDescriptor(name, m, defaultValueFields.get(name));

			if (pd.getPropertySource().isGenerating() || (pd.getSetter() != null))
				properties.put(name, pd); // only add properties that can be written to!
		}

		// find setters without getters
		for (Method m: methods) {
			String methodName = m.getName();
			boolean isSetter = (methodName.length() >= 4) && methodName.startsWith("set") && methodName.substring(3, 4).toUpperCase().equals(methodName.substring(3, 4));
			if (!isSetter)
				continue;
			if (Modifier.isPrivate(m.getModifiers()))
				continue;
			if (Modifier.isStatic(m.getModifiers()))
				continue;
			if (m.getParameterTypes().length != 1)
				continue;
			if (!m.getReturnType().equals(Void.TYPE))
				continue;
			String name = getPropertyNameFromAccessor(methodName);
			if (properties.containsKey(name)) {
				if (properties.get(name).getSetter() != null)
					continue;
				else
					throw new ConfigurationException(String.format("Setter %s in %s seems to have a signature that conflicts with the corresponding getter.", methodName, beanClass.getName()));
			}
			properties.put(name, PropertyDescriptor.createSetterOnlyDescriptor(name, m, defaultValueFields.get(name)));
		}

		// check that no @DefaultValue without existing property is set
		for (String dv: defaultValueFields.keySet()) {
			if (!properties.containsKey(dv))
				throw new ConfigurationException(String.format("Field %s in %s has a @DefaultValue annotation for a property %s, but that property does not exist in the class (or, possibly, it has only a private setter).", defaultValueFields.get(dv).getName(), beanClass.getName(), dv));
			else {
				PropertyDescriptor pd = properties.get(dv);
				if ((!pd.getPropertySource().isGenerating()) && (pd.getSetter() == null))
					throw new ConfigurationException(String.format("Field %s in %s has a @DefaultValue annotation for a property %s, but that property does not have a accessible setter (maybe the setter is private).", defaultValueFields.get(dv).getName(), beanClass.getName(), dv));			
			}
		}
		
		// Find initializers
		// check that there are no abstract methods in the class, unless they are a @Prop
		for (Method m: methods)
			if (Modifier.isAbstract(m.getModifiers())) {
				String name = m.getName();
				if ((name.length() < 4) || !(name.startsWith("get") || name.startsWith("set")))
					throw new ConfigurationException(String.format("Method %s in %s is abstract. A @Bean can not be instantiated with abstract methods (except @Prop accessors).", name, beanClass.getName()));
				String propName = getPropertyNameFromAccessor(name);
				if ((!properties.containsKey(propName)) || !((m.equals(properties.get(propName).getGetter())) || (m.equals(properties.get(propName).getSetter()))))
					throw new ConfigurationException(String.format("Method %s in %s is abstract and has no @Prop annotation. Maybe you wanted to declare a @Prop getter? A bean with abstract methods can not be instantiated, unless they are property accessors with @Prop.", name, beanClass.getName()));
			}
	
		// find the initializers of the bean
		ArrayList<Method> initializers = new ArrayList<Method>();
		for (Method m: methods)
			if (m.getAnnotation(Initializer.class) != null) {
				if ((!m.getReturnType().equals(Void.TYPE)) || (m.getParameterTypes().length > 0))
					throw new ConfigurationException(String.format("Method %s in %s has a @Initializer annotation, but has arguments or a non-void return type. Initializers must not take any arguments or return a value.", m.getName(), beanClass.getName()));
				initializers.add(m);
			}
		
		Collections.reverse(initializers); // list must be base-class first
		
		return new BeanClassDescriptor(properties.values(), initializers, threadSafe);
	}

	/**
	 * Returns the property name, given an accessor method's name.
	 * @param methodName the name of the accessor method
	 * @return the property name
	 */
	private static String getPropertyNameFromAccessor(String methodName) {
		assert methodName.length() > 3;
		return (methodName.length() == 4) ? methodName.substring(3).toLowerCase() : methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
	}

	/**
	 * Returns all fields of the given class, including super-classes except Object.
	 * @param beanClass the class to search
	 * @return the list of fields
	 */
	private static ArrayList<Field> getAllFields(Class<?> beanClass) {
		ArrayList<Field> fields = new ArrayList<Field>();
		{
			Class<?> c = beanClass; 
			while (!c.equals(Object.class)) {
				fields.addAll(Arrays.asList(c.getDeclaredFields()));
				c = c.getSuperclass();
			}
		}
		return fields;
	}

	/**
	 * Returns all methods of the given class, including super-classes, 
	 * except those of the given stop class and private methods.
	 * If a method is overridden by a sub-class, it is not returned.
	 * @param beanClass the class to search
	 * @param stopClass the class to stiop at. Its methods will not be returned.
	 * @return the list of methods
	 */
	public static ArrayList<Method> getAllMethods(Class<?> beanClass, Class<?> stopClass) {
		ArrayList<Method> methods = new ArrayList<Method>();
		Method[] declaredMethods = beanClass.getDeclaredMethods();
		methods.addAll(Arrays.asList(declaredMethods));
		Class<?> superClass = beanClass.getSuperclass();
		if (!superClass.equals(stopClass)) {
			ArrayList<Method> superMethods = getAllMethods(superClass, stopClass);
			for (Method s: superMethods) {
				boolean found = false;
				for (Method m: declaredMethods)
					if (s.getName().equals(m.getName()) && !Modifier.isPrivate(m.getModifiers())) {
						Class<?>[] sArgs = s.getParameterTypes();
						Class<?>[] mArgs = m.getParameterTypes();
						if (sArgs.length == mArgs.length) {
							boolean sameArgs = true;
							for (int i = 0; i < sArgs.length; i++)
								if (!sArgs[i].equals(mArgs[i])) {
									sameArgs = false;
									break;
								}
							if (sameArgs) {
								found = true;
								break;
							}
						}
					}
				if (!found)
					methods.add(s);
			}
		}
		return methods;
	}

	/**
	 * Returns the property at the given index.
	 * @param index the index of the property
	 * @return the descriptor
	 * @throws IndexOutOfBoundsException if there is no property at the given index 
	 * @see #getPropertyCount()
	 */
	public PropertyDescriptor getProperty(int index) {
		return properties[index];
	}

	/**
	 * Returns the number of properties.
	 * @return the number of properties
	 */
	public int getPropertyCount() {
		return properties.length;
	}

	/**
	 * Returns the @Initializer methods of the bean class, ordered base class methods first
	 * (invocation order).
	 * @return the initializers
	 */
	public Method[] getInitializers() {
		return initializers;
	}

	/**
	 * Returns the @Initializer at the given index, from the list of initializers in 
	 * invocation order.
	 * @param index the index of the initializer
	 * @return the initializer
	 * @throws IndexOutOfBoundsException if there is no initializer at the given index 
	 * @see #getInitializerCount()
	 */
	public Method getInitializers(int index) {
		return initializers[index];
	}

	/**
	 * Returns the number of initializers.
	 * @return the number of initializers
	 */
	public int getInitializerCount() {
		return initializers.length;
	}

	/**
	 * ThreadSafe is true if the property synchronizes constructor, initializer
	 * and property accessor methods. False for no synchronization.
	 * @return true if the bean is thread-safe (synchronized), false otherwise.
	 */
	public boolean isThreadSafe() {
		return threadSafe;
	}
}
