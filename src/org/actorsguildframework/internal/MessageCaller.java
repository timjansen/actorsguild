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

import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.immutable.SerializableFreezer;

/**
 * Base class for generated classes to invoke a message-implementing method.
 * @param <T> the result type
 */
public abstract class MessageCaller<T> {

	/**
	 * Invokes the message method. Arguments that are Serializable and mutable 
	 * will be wrapped as {@link SerializableFreezer}.
	 * @param instance the instance of the method
	 * @param arguments the arguments (partially wrapped as {@link SerializableFreezer})
	 * @return the result of the method
	 */
	public abstract AsyncResult<T> invoke(Actor instance, Object[] arguments);
	
	/**
	 * Returns the name of the message that is being called by this class (
	 * method name or signature).
	 * @return the message name
	 */
	public abstract String getMessageName();
}
