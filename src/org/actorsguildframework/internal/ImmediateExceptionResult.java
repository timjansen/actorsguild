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

import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.WrappedException;

/**
 * AsyncResult implementation that always throws an exception.
 * @param <T> some type. Will be ignored in this implementation.
 */
public class ImmediateExceptionResult<T> implements AsyncResult<T> {
	private Exception exception;
	
	/**
	 * Creates a new ImmediateResult instance with the given result Exception.
	 * @param exception the result exception
	 */
	public ImmediateExceptionResult(Exception exception) {
		this.exception = exception;
	}
	
	/**
	 * Throws the exception.
	 */
	public T get() {
		throw new WrappedException("Got exception", exception);
	}

	/**
	 * Does nothing, as the result is always ready in this implementation.
	 */
	public void await() {
		// do nothing, result is already there
	}

	/**
	 * Always returns true, as the result is always ready in this implementation.
	 * @return always true
	 */
	public boolean isReady() {
		return true;
	}

	/**
	 * Adds a notifier. This implementation will always invoke the notifier immediately
	 * in the same thread.
	 * @param notifier the notifier to add
	 * @throws IllegalArgumentException if the argument was null
	 */
	public void addNotifier(org.actorsguildframework.AsyncResult.Notifier<T> notifier) {
		if (notifier == null)
			throw new IllegalArgumentException("notifier argument was null");
		notifier.resultReady(this);
	}

	/**
	 * Adds a notifier. This implementation will do nothing, as the notifier is 
	 * always invoked immediately in the same thread.
	 * @param notifier the notifier to remove
	 * @throws IllegalArgumentException if the argument was null
	 */
	public void removeNotifier(
			org.actorsguildframework.AsyncResult.Notifier<T> notifier) {
		if (notifier == null)
			throw new IllegalArgumentException("notifier argument was null");
		// do nothing - ImmediateResult always notifies during addNotifier
	}

	/**
	 * This implementation will always return the exception.
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}
}
