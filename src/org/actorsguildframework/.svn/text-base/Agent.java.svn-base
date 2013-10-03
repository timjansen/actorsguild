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

import org.actorsguildframework.annotations.Bean;
import org.actorsguildframework.annotations.DefaultValue;
import org.actorsguildframework.annotations.Prop;

/**
 * The Agent is a controller for actors. The agent is responsible for creating new actors,
 * and it will coordinate the execution of messages. It also provides a number of helper methods
 * for message implementations. 
 * 
 * In order to acquire an Agent, you must create an instance of an implementation. Currently the
 * only implementation is {@link DefaultAgent}.
 */
public interface Agent {
	/**
	 * Creates a new instance of the given {@link Bean} or {@link Actor}. 
	 * <code>Create</code> supports the following annotations: 
	 * <ol><li>{@link Bean}
	 * <li>{@link Prop}
	 * <li>{@link DefaultValue}</ol>
	 * @param <T> the type of the class to create
	 * @param actorOrBeanClass the class to create
	 * @return the new instance
	 * @throws WrappedException if the object's constructor threw an exception
	 * @throws ConfigurationException if the class is not configured correctly, or not a {@link Bean}
	 */
	public <T> T create(Class<T> actorOrBeanClass);

	/**
	 * Creates a new instance of the given {@link Bean} or {@link Actor} and sets its properties. 
	 * <code>Create</code> supports the following annotations: 
	 * <ol><li>{@link Bean}
	 * <li>{@link Prop}
	 * <li>{@link DefaultValue}</ol>
	 * The second argument allows specifying values for the instance's properties.
	 * The value of the Props must be compatible with the class of the property. If the
	 * property is a primitive (like int), specify the wrapper type ({@link Number} sub-classes, 
	 * for example Integer). <code>create</code> also supports type conversion for all numeric
	 * type, as well as between Character and Integer.
	 * For those properties that are not given, the method will take the defined
	 * {@link DefaultValue}, if specified, or the type's default value (null for references,
	 * 0 for number primitives, false for boolean).
	 * @param <T> the type of the class to create
	 * @param actorOrBeanClass the class to create
	 * @param props the values for the instance's properties. Null for no initial properties.
	 * @return the new instance
	 * @throws WrappedException if the object's constructor threw an exception
	 * @throws ConfigurationException if the class is not configured correctly, or not a {@link Bean}
	 * @throws IllegalArgumentException if there was an error in the Props (unknown property,
	 *            value can not be converted) or the given class was null
	 */
	public <T> T create(Class<T> actorOrBeanClass, Props props);

	
	/**
	 * Waits until all given AsyncResults are available.
	 * <code>awaitAll</code> can be invoked from all threads, including those that do not process a message. 
	 * @param asyncResults the list of AsyncResult instances to wait for
	 * @throws IllegalArgumentException if one of the arguments was null or the array was null
	 * @see #awaitAllUntilError(AsyncResult...)
	 * @see #awaitAny(AsyncResult...)
	 */
	@SuppressWarnings("unchecked")
	public void awaitAll(AsyncResult... asyncResults);

	/**
	 * Waits until all given AsyncResults are available, and throws the WrappedException of the first
	 * AsyncResult that failed, if one failed.
	 * <code>awaitAll</code> can be invoked from all threads, including those that do not process a message. 
	 * @param asyncResults the list of AsyncResult instances to wait for
	 * @throws IllegalArgumentException if one of the arguments was null or the array was null
	 * @throws WrappedException if one of the AsyncResults threw an exception, this
	 *   WrappedException contains the first one from the list.
	 * @see #awaitAll(AsyncResult...)
	 */
	@SuppressWarnings("unchecked")
	public void awaitAllUntilError(AsyncResult... asyncResults);

	/**
	 * Waits until one of the given AsyncResults is available.
	 * <code>awaitAny</code> can be invoked from all threads, including those that do not process a message. 
	 * @param asyncResults the list of AsyncResult instances to wait for
	 * @return the AsyncResult that is ready, null for empty lists
	 * @throws IllegalArgumentException if one of the arguments was null
	 * @see #awaitAll(AsyncResult...) 
	 */
	@SuppressWarnings("unchecked")
	public AsyncResult awaitAny(AsyncResult... asyncResults);

	/**
	 * Tries to shut down the agent with all its threads. Messages that have not been processed yet
	 * will not be processed.
	 * <p>
	 * This call is useful if you want to terminate your Java application. Without it, the Agent's
	 * non-daemon threads may keep the application from terminating. A safer alternative to
	 * <code>shutdown</code> may be calling {@link System#exit(int)}, as there is no guarantee
	 * that <code>shutdown</code> can terminate all threads.
	 * <p>
	 * A agent and its actors must not be used after calling this method. The behaviour would be
	 * undefined. 
	 */
	public void shutdown();
}