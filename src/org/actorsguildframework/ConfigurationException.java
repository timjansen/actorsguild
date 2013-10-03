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

import org.actorsguildframework.annotations.ConcurrencyModel;;

/**
 * ConfigurationException will be thrown when an agent is not configured correctly.
 * For example declaring a {@link ConcurrencyModel#Stateless} with regular fields will 
 * throw this exception. The exception is usually thrown at the time the Agent is 
 * instantiated for the first time.
 */
public class ConfigurationException extends ActorException {
	private static final long serialVersionUID = 7410617240404880878L;

	/**
	 * Creates a new instance.
	 * @param msg the message
	 */
	public ConfigurationException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new instance.
	 * @param msg the message
	 * @param cause
	 */
	public ConfigurationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
