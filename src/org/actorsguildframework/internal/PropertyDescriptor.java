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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;

import org.actorsguildframework.ConfigurationException;
import org.actorsguildframework.annotations.Prop;
import org.actorsguildframework.annotations.Shared;

/**
 * Describes the configuration of a property.
 * Immutable thread-safe.
 * @see ActorClassDescriptor
 */
public final class PropertyDescriptor {
	/**
	 * Describes the source of the property.
	 */
	public enum PropertySource {
		/**
		 * The user wrote the property accessors.
		 */
		USER_WRITTEN,
		/**
		 * The user provided abstract accessors.
		 */
		ABSTRACT_METHOD;
		
		/**
		 * Checks whether this type of property generates accessor methods.
		 * @return true if it generates methods, false otherwise
		 */
		public boolean isGenerating() {
			return this != USER_WRITTEN;
		}
	}
	
	/**
	 * Describes the access of the property.
	 */
	public enum PropertyAccess {
		/**
		 * The property can only be read.
		 */
		READ,
		/**
		 * The property can only be written
		 */
		WRITE,
		/**
		 * The property can be written and read.
		 */
		READ_WRITE;
		
		/**
		 * Checks whether this property access is readable.
		 * @return true if readable, false otherwise
		 */
		public boolean isReadable() {
			return this != WRITE;
		}
		
		/**
		 * Checks whether this property access is writable.
		 * @return true if writable, false otherwise
		 */
		public boolean isWritable() {
			return this != READ;
		}
	}
	
	/**
	 * The name of the property.
	 */
	private final String name;
	
	/**
	 * The class of the property.
	 */
	private final Class<?> propertyClass;
	
	/**
	 * The generic type of the property.
	 */
	private final java.lang.reflect.Type propertyType;
	
	/**
	 * The type of property.
	 */
	private final PropertySource generationSource;
	
	/**
	 * The method that implements the getter, or null for setter-only.
	 */
	private final Method getter;
	
	/**
	 * The method that implements the setter, or null if no setter available.
	 */
	private final Method setter;

	/**
	 * The static final field containing the property default value, or null if no
	 * default value available.
	 */
	private final Field defaultValue;
	
	/**
	 * True if the property is a @Prop and has a {@link Shared} annotation as well.
	 */
	private final boolean sharedReference; 
	
	/**
	 * Defines whether the property can be read and/or written.
	 */
	private final PropertyAccess access;
	
	/**
	 * Creates a new instance.
	 * @param name the property name
	 * @param propertyClass the property class
	 * @param propertyType the property type
	 * @param generationSource true the property type
	 * @param getter the property getter
	 * @param setter the property setter, or null
	 * @param defaultValue the property default value field, or null
	 * @param sharedReference true if the property is generated using @Prop and also has a {@link Shared} annotation
	 * @param access the read/write access of the property
	 */
	private PropertyDescriptor(String name, Class<?> propertyClass, 
			java.lang.reflect.Type propertyType, PropertySource generationSource,
			Method getter, Method setter, Field defaultValue, boolean sharedReference,
			PropertyAccess access) {
		this.name = name;
		this.propertyClass = propertyClass;
		this.propertyType = propertyType;
		this.generationSource = generationSource;
		this.getter = getter;
		this.setter = setter;
		this.defaultValue = defaultValue;
		this.sharedReference = sharedReference;
		this.access = access;
	}

	
	/**
	 * Creates a new instance by inspecting the given getter method.
	 * @param propertyName the property name
	 * @param getter the getter method that defines the property
	 * @param defaultValue the field containing the default value, or null for no default value
	 * @return the new instance
	 * @throws ConfigurationException if the bean is not configured correctly
	 */
	public static PropertyDescriptor createGetterDescriptor(String propertyName, Method getter, Field defaultValue)
	{
		Class<?> beanClass = getter.getDeclaringClass();
		String methName = getter.getName();
		assert (methName.length() >= 4) && methName.startsWith("get");
			
		String getterBaseName = methName.substring(3);
		Class<?> propertyClass = getter.getReturnType();
		java.lang.reflect.Type propertyType = getter.getGenericReturnType();
		
		if ((defaultValue != null) && !propertyClass.isAssignableFrom(defaultValue.getType()))
			throw new ConfigurationException(String.format("Default value %s in %s for property with getter %s in %s has an incompatible type. The getter has %s, and the default value %s. They must be compatible.", defaultValue.getName(), defaultValue.getDeclaringClass().getName(), getter.getName(), beanClass.getName(), propertyClass.getName(), defaultValue.getType().getName()));
		
		Prop propAnnotation = getter.getAnnotation(Prop.class);
		PropertySource propSrc;
		boolean sharedReference;
		if (propAnnotation == null) {
			propSrc = PropertySource.USER_WRITTEN;
			if (Modifier.isAbstract(getter.getModifiers()))
				throw new ConfigurationException(String.format("Invalid abstract getter %s in %s. A getter can not be abstract, unless there is a @Prop annotation.", methName, beanClass.getName()));		
			sharedReference = false;
		}
		else {
			propSrc = PropertySource.ABSTRACT_METHOD;
			if (!Modifier.isAbstract(getter.getModifiers()))
				throw new ConfigurationException(String.format("Invalid getter %s in %s. Must be abstract if @Prop annotation is used.", methName, beanClass.getName()));
			sharedReference = (getter.getAnnotation(Shared.class) != null);
		}
 		
		Method setter;
		try {
			Method m = beanClass.getDeclaredMethod("set" + getterBaseName, new Class<?>[] {propertyClass});
			if (!Modifier.isPrivate(m.getModifiers()))
				setter = m;
			else
				setter = null;
		}
		catch (NoSuchMethodException e) {
			setter = null;
		}
		
		if (setter != null) {
			if (Modifier.isStatic(setter.getModifiers()))
				throw new ConfigurationException(String.format("Setter %s in %s must not be static (if there is a non-static getter).", setter.getName(), beanClass.getName()));
			if (!setter.getReturnType().equals(Void.TYPE))
				throw new ConfigurationException(String.format("Setter %s in %s cannot have a return type, but must return void (if there is a non-static getter).", setter.getName(), beanClass.getName()));
			if (setter.getExceptionTypes().length > 0) 
				throw new ConfigurationException(String.format("Invalid setter %s in %s. Setters must not throw exceptions.", setter.getName(), beanClass.getName()));
			if (propSrc.isGenerating()) {
				if (!Modifier.isAbstract(setter.getModifiers()))
					throw new ConfigurationException(String.format("Invalid setter %s in %s. Must be abstract if @Prop annotation is used on the getter.", setter.getName(), beanClass.getName()));				
			}
			else if (Modifier.isAbstract(setter.getModifiers()))
				throw new ConfigurationException(String.format("Invalid setter %s in %s. Setter is abstract, can not be handled, unless there is a @Prop annotation on the getter.", setter.getName(), beanClass.getName()));
		}
		else if ((propSrc == PropertySource.USER_WRITTEN) && (defaultValue != null))
			throw new ConfigurationException(String.format("There is a default value %s in %s for property with getter %s in %s, but I can not find a compatible setter for the getter. There is no setter, or its type is not compatible with the getter.", defaultValue.getName(), defaultValue.getDeclaringClass().getName(), getter.getName(), beanClass.getName()));
		
		return new PropertyDescriptor(propertyName, propertyClass, propertyType, propSrc, 
				getter, setter, defaultValue, sharedReference, 
				(setter!=null) ? PropertyAccess.READ_WRITE : PropertyAccess.READ);
	}

	/**
	 * Creates a new instance by inspecting the given setter method. Only for setter that
	 * do not have a getter. Never uses @Prop.
	 * @param propertyName the property name
	 * @param setter the setter method that defines the property
	 * @param defaultValue the field containing the default value, or null for no default value
	 * @return the new instance
	 */
	public static PropertyDescriptor createSetterOnlyDescriptor(String propertyName, Method setter, Field defaultValue)
	{
		assert setter.getName().startsWith("set");
		assert setter.getParameterTypes().length == 1;
		Class<?> propertyClass = setter.getParameterTypes()[0];
		java.lang.reflect.Type propertyType = setter.getGenericParameterTypes()[0];
		
		if (defaultValue != null) {
			if (!propertyClass.isAssignableFrom(defaultValue.getType()))
				throw new ConfigurationException(String.format("Default value %s in %s for property with setter %s in %s has an incompatible type. The setter has %s, and the default value %s. They must be compatible.", defaultValue.getName(), defaultValue.getDeclaringClass().getName(), setter.getName(), setter.getDeclaringClass().getName(), propertyClass.getName(), defaultValue.getType().getName()));
		}
		if (Modifier.isAbstract(setter.getModifiers()))  
			throw new ConfigurationException(String.format("Invalid setter %s in %s. Setter must not be abstract, unless there is a @Prop-annotated getter for it.", setter.getName(), setter.getDeclaringClass().getName()));
		if (setter.getExceptionTypes().length > 0) 
			throw new ConfigurationException(String.format("Invalid setter %s in %s. Setter must not throw exceptions.", setter.getName(), setter.getDeclaringClass().getName()));

		return new PropertyDescriptor(propertyName, propertyClass, propertyType, PropertySource.USER_WRITTEN, null, 
				setter, defaultValue, false, PropertyAccess.WRITE);
	}
	
	/**
	 * Returns the getter method of the property, or null for write-only.
	 * @return the getter method, or null
	 */
	public Method getGetter() {
		return getter;
	}


	/**
	 * Returns the setter method of the property, or null for read-only.
	 * @return the setter method, or null
	 */
	public Method getSetter() {
		return setter;
	}


	/**
	 * Returns the name of the property.
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Returns the property's class.
	 * @return the property's class
	 */
	public Class<?> getPropertyClass() {
		return propertyClass;
	}

	/**
	 * Returns the property's generic type.
	 * @return the property's generic type
	 */
	public java.lang.reflect.Type getPropertyType() {
		return propertyType;
	}


	/**
	 * Returns the property source.
	 * @return the property source
	 */
	public PropertySource getPropertySource() {
		return generationSource;
	}

	/**
	 * Returns the Field that stores the default value of the property (@DefaultValue annotation). 
	 * Null for no default value.
	 * @return the field containing the default value
	 */
	public Field getDefaultValue() {
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("PD name=%s class=%s generated=%s getter=%s setter=%s defaultField=%s.%s", name, propertyClass.getName(),
					generationSource, getter, setter, 
					(defaultValue!=null)? defaultValue.getDeclaringClass().getName() : null, 
							(defaultValue!=null)? defaultValue.getName():"");
	}


	/**
	 * Returns true if this property is @Prop generated and has a @Shared annotation.
	 * @return true if @Shared annotated, false otherwise.
	 */
	public boolean isSharedReference() {
		return sharedReference;
	}


	/**
	 * Returns the properties access (read/write)
	 * @return the access modes
	 */
	public PropertyAccess getAccess() {
		return access;
	}
	
	/**
	 * Returns the name of the getter method for the given property name
	 * @param propertyName the property name
	 * @return the getter method name
	 */
	public static String getGetterName(String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase(Locale.US) + propertyName.substring(1);
	}
	
	/**
	 * Returns the name of the setter method for the given property name
	 * @param propertyName the property name
	 * @return the setter method name
	 */
	public static String getSetterName(String propertyName) {
		return "set" + propertyName.substring(0, 1).toUpperCase(Locale.US) + propertyName.substring(1);
	}
}
