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
 * WrappedException is used to transport non-runtime exceptions that the actor has thrown.
 * The wrapped exception can be retrieved using {@link #getCause()}, or it can be rethrown
 * using {@link #rethrow()}. 
 */
public class WrappedException extends ActorException {

	private static final long serialVersionUID = 7326064257106415700L;

	/**
	 * Creates a new WrappedException.
	 * @param msg the message of the exception.
	 * @param e the wrapped exception
	 */
	public WrappedException(String msg, Throwable e) {
		super(msg, e);
	}
	
	/**
	 * Rethrows the wrapped exception.
	 * @throws Throwable
	 * @see #getCause()
	 * @see #rethrowIf(Class)
	 */
	public void rethrow() throws Throwable {
		throw getCause();
	}
	
	/**
	 * Rethrows the wrapped exception if is is an instance of the given exception class.
	 * @param <E> the exception to rethrow
	 * @param exceptionClass the exception class to test
	 * @throws E throws the wrapped exception E, if the wrapped exception if actually an instance
	 * @see #rethrow()
	 * @see #isWrapping(Class)
	 */
	@SuppressWarnings("unchecked")
	public <E extends Throwable> void rethrowIf(Class<E> exceptionClass) throws E {
		if (isWrapping(exceptionClass))
			throw (E) getCause();
	}

	/**
	 * Checks whether the wrapped exception is is an instance of the given exception class.
	 * @param exceptionClass the exception class to test
	 * @return true if it is wrapping the given exception, false otherwise
	 * @see #rethrowIf(Class)
	 */
	public boolean isWrapping(Class<? extends Throwable> exceptionClass) {
		return exceptionClass.isInstance(getCause());
	}
}
