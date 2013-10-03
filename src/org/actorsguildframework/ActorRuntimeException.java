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
 * ActorRuntimeException is being thrown when use an Actor in a wrong
 * way at runtime. 
 */
public class ActorRuntimeException extends ActorException {
	private static final long serialVersionUID = -3221198258295970599L;

	/**
	 * Creates a new instance.
	 * @param message the message of the instance
	 */
	public ActorRuntimeException(String message) {
		super(message);
	}
	
	/**
	 * Creates a new instance.
	 * @param message the message of the instance
	 * @param cause the cause 
	 */
	public ActorRuntimeException(String message, Exception cause) {
		super(message, cause);
	}
}
