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
 * ImmediateResult is an AsyncResult implementation that can be used when the result is already available.
 * It is the easiest and most common way to implement the return value in a Message method.
 * @param <T> the type of the result
 * @see Actor#result(Object)
 * @see Actor#noResult()
 */
public final class ImmediateResult<T> implements AsyncResult<T>, Immutable {
	private static final long serialVersionUID = -6806759712550292575L;
	
	private final T result;
	
	/**
	 * Creates a new ImmediateResult instance with the given result value.
	 * ImmediateResult is usually used inside an {@link Actor}, which offers the
	 * convenient helper function @{link Actor#result(Object)} to create new instances.   
	 * @param value the result value
	 * @see Actor#result(Object)
	 */
	public ImmediateResult(T value) {
		this.result = value;
	}
	
	/**
	 * Returns the value immediately, as the result is always ready in this implementation.
	 * It may be a copy of the original value.
	 * This implementation will never throw an exception.
	 */
	public T get() {
		return result;
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
	 * This implementation will always return null.
	 * @return always null
	 */
	public Throwable getException() {
		return null;
	}

}
