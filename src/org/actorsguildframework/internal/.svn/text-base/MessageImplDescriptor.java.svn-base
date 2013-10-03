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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.ConfigurationException;
import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.Shared;
import org.actorsguildframework.annotations.ThreadUsage;
import org.actorsguildframework.annotations.Usage;
import org.actorsguildframework.immutable.ImmutableHelper;

/**
 * Describes the configuration of a message or initializer implementation.
 * Immutable thread-safe.
 * @see ActorClassDescriptor
 */
public final class MessageImplDescriptor {
	/**
	 * Bitset of all modifiers that are not allowed in message methods.
	 */
	private final static int FORBIDDEN_METHOD_MODIFIERS = 
		Modifier.ABSTRACT | Modifier.FINAL | Modifier.STATIC | Modifier.SYNCHRONIZED;

	/**
	 * The class that owns the message (not the class that implements it, it may be
	 * inherited!!). This is not always the same class as {@link Method#getDeclaringClass()}
	 * returns.
	 */
	private final Class<?> ownerClass;

	/**
	 * The ThreadUsage of the message.
	 */
	private final ThreadUsage threadUsage;

	/**
	 * The method that implements the message.
	 */
	private final Method method;
	
	
	/**
	 * Creates a new instance.
	 * @param ownerClass
	 * @param threadUsage the ThreadUsage model
	 * @param method the method of the message
	 */
	private MessageImplDescriptor(Class<?> ownerClass, ThreadUsage threadUsage, Method method) {
		this.ownerClass = ownerClass;
		this.threadUsage = threadUsage;
		this.method = method;
	}
	
	/**
	 * Checks whether the method has valid arguments (either immutable or interfaces).
	 * Throws exceptions on error.
	 * @param method the method that implements the message
	 * @throws ConfigurationException if the method arguments are invalid
	 */
	public static void checkMethodArguments(Method method) {
		Annotation[][] annotations = method.getParameterAnnotations();
		Class<?>[] argClasses = method.getParameterTypes();
		for (int i = 0; i < argClasses.length; i++) {
			Class<?> argCls = argClasses[i];
			if (Serializable.class.isAssignableFrom(argCls))
				continue;
			else if (Actor.class.isAssignableFrom(argCls))
				continue;
			else if (Modifier.isInterface(argCls.getModifiers()))
				continue;
			else if (annotations[i].length > 0) {
				for (Annotation a: annotations[i])
					if (a.annotationType().equals(Shared.class))
						continue;
			}
			else if (!ImmutableHelper.isImmutableType(argCls))
				throw new ConfigurationException("Method "+ method + " argument "+(i+1)+" is neither Serializable nor an actor or interface, and it lacks a @Shared annotations. All arguments of actor messages and initializers must at least be Serializable or Actors (or primitives). If not, they need a @Shared annotation.");
		}
	}

	
	/**
	 * Creates a new instance by inspecting the given method.
	 * @param ownerClass the class that owns the message
	 * @param defaultConcurrencyModel the actor's default concurrency model
	 * @param method the method that implements the message
	 * @return the new instance
	 * @throws ConfigurationException if the agent is not configured correctly
	 */
	public static MessageImplDescriptor createMessageDescriptor(Class<?> ownerClass, ConcurrencyModel defaultConcurrencyModel, Method method)
	{
		if (!method.getReturnType().equals(AsyncResult.class))
			throw new ConfigurationException("Method "+ method + " declared as message, but does not return AsyncResult.");
		if ((method.getModifiers() & FORBIDDEN_METHOD_MODIFIERS) != 0)
			throw new ConfigurationException("Method "+ method + ", declared as message, has illegal modifiers. "
					+"Not allowed are 'final', 'static', 'abstract' and 'synchronized'.");
		if (!Modifier.isPublic(method.getModifiers()))
			throw new ConfigurationException("Method "+ method + " is declared as message but not public. "
					+"All messages must always be 'public'.");
		checkMethodArguments(method);
		
		Usage usageAnn = method.getAnnotation(Usage.class);
		ThreadUsage threadUsage = (usageAnn != null) ? usageAnn.value() : ThreadUsage.CpuBound;

		return new MessageImplDescriptor(ownerClass, threadUsage, method);
	}

	/**
	 * Returns the ThreadUsage of the message.
	 * @return the ThreadUsage
	 */
	public ThreadUsage getThreadUsage() {
		return threadUsage;
	}
	
	/**
	 * Returns the method that implements the message.
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Returns the class that owns the message (not the class that implements it, it may be
	 * inherited!!). This is not always the same class as {@link Method#getDeclaringClass()}
	 * of {@link #getMethod()} returns. 
	 * @return the message's owner
	 */
	public Class<?> getOwnerClass() {
		return ownerClass;
	}
}
