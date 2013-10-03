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

/**
 * AsyncResult represents a result that may have not been determined yet. It is the 
 * return type for all Message methods. 
 * @param <T> the type of the result
 */
public interface AsyncResult<T> {

	/** 
	 * Implement this interface to receive a notification when the result is ready. You
	 * must register your notifier at the AsyncResult 
	 * using {@link AsyncResult#addNotifier(Notifier)}.
	 * @param <T> the type of the return type
	 * @see AsyncResult#addNotifier(org.actorsguildframework.AsyncResult.Notifier)
	 */
	public interface Notifier<T> {
		/**
		 * Invoked by the AsyncResult implementation when the result is ready.
		 * @param result the AsyncResult that is done
		 */
		public void resultReady(AsyncResult<T> result);
	}
	
	/**
	 * Waits until the result is ready, and then returns it. If the implementation 
	 * threw an exception, get() will re-throw the exception. 
	 * @return the result
	 * @throws WrappedException if the message implementation threw a non-RuntimeException that
	 *   needs to be declared
	 * @throws RuntimeException if the message implementation threw an exception
	 * @see #await()
	 * @see #addNotifier(org.actorsguildframework.AsyncResult.Notifier)
	 */
	public T get();
	
	/**
	 * Blocks until the AsyncResult is ready.
	 * @see #addNotifier(org.actorsguildframework.AsyncResult.Notifier)
	 * @see #isReady()
	 * @see org.actorsguildframework.DefaultAgent#awaitAll(AsyncResult...)
	 * @see org.actorsguildframework.DefaultAgent#awaitAny(AsyncResult...)
	 */
	public void await();

	/**
	 * Checks whether a result (value or exception) is ready. 
	 * Implementations should implement this in a way that wait for the result,
	 * but returns as fast as possible.
	 * @return true if the result is ready, false otherwise
	 * @see #await()
	 */
	public boolean isReady();

	/**
	 * Checks whether the result is ready but the implementation threw an exception.
	 * Only in this case the method returns this exception. In all other cases,
	 * the method must return null immediately.
	 * @return the exception, if there was one. Null otherwise.
	 */
	public Throwable getException();

	/**
	 * Adds the given Notifier to the AsyncResult. It will be invoked (possibly from another thread)
	 * as soon as the result is ready. If the result is ready at the time of the invocation, the
	 * AsyncResult may invoke the notifier while the addNotifier() is running (probably in the same
	 * thread).
	 * After the notification the notifier will be removed automatically. It is not needed to remove
	 * it then anymore.
	 * If a notifier will be added more than once it will be called for each time it has been added.
	 * @param notifier the notifier to be called
	 * @see #removeNotifier(org.actorsguildframework.AsyncResult.Notifier)
	 * @throws IllegalArgumentException if the notifier was null
	 */
	public void addNotifier(Notifier<T> notifier);

	/**
	 * Removes the given Notifier from the AsyncResult. This means that the notifier will probably
	 * not be called when the result is ready. This is not guaranteed though, it may be called
	 * anyway.
	 * It is not necessary to remove the notifier after receiving a notification. 
	 * If you try to remove a notifier that is not registered, the invocation will be ignored.
	 * @param notifier the notifier to be called
	 * @see #addNotifier(org.actorsguildframework.AsyncResult.Notifier)
	 * @throws IllegalArgumentException if the notifier was null
	 */
	public void removeNotifier(Notifier<T> notifier);

}
