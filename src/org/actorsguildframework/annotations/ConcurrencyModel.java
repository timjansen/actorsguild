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
package org.actorsguildframework.annotations;

import org.actorsguildframework.Actor;
import org.actorsguildframework.Immutable;

/**
 * ConcurrencyModel describes the way concurrency is handled in the actor and its methods.
 */
public enum ConcurrencyModel {
	/**
	 * Specifies a single-threaded model: only one simultaneous thread in the actor is allowed.
	 * <p>
	 * All messages will be processed in the order that they have been queued.
	 */
	SingleThreaded,
	/**
	 * Specifies a multi-threaded model: several simultaneous thread can run in the actor.
	 * <p>
	 * The order of execution for multi-threaded messages is not defined. Thus they may
	 * be executed in a different order than the order to queuing.
	 * <p>
	 * Please note that {@link ConcurrencyModel#Stateless} should be preferred to MultiThreaded where
	 * possible, because it is much safer.  
	 */
	MultiThreaded,
	/**
	 * Specifies a stateless model: several simultaneous thread can run in the actor,
	 * but they can not share any state in the actor. 
	 * At runtime <code>Stateless</code> works exactly like {@link #MultiThreaded}. However,
	 * at the first instantiation actors declared as <code>Stateless</code>
	 * run through a number of checks to validate that the actor does not maintain
	 * any state other than configuration data set at creation. 
	 * <p>
	 * In order to ensure this, there are strict rules for <code>Stateless</code> actors. 
	 * In an actor that is declared as <code>Stateless</code> (using a 
	 * {@link Model} annotation for the class), there are the following restrictions 
	 * for the fields used in the actor and any super-classes:
	 * <ul>
	 * <li>All fields must be <code>final</code>.
	 * <li>Any {@link Prop} annotated properties must properties using the 
	 *    {@link PropSynchronization} {@link PropSynchronization#Final} (note that this
	 *    is the default for read-only properties without setter)
	 * <li>The type of all fields and <code>@Prop</code> annotated properties must be
	 *  either {@link Immutable} (includes Java primitives and String), or an {@link Actor},
	 *  or a multi-threading-safe class annotated with {@link Shared}
	 * </ul>
	 * 
	 * <p>
	 * The order of execution for stateless messages is not defined. Thus they may
	 * be executed in a different order than the order to queuing.
	 */
	Stateless;
	
	/**
	 * Returns true if this model is multi-threaded, and thus either {@link #MultiThreaded} or 
	 * {@link #Stateless}.
	 * @return true for multi-threaded models, false otherwise
	 */
	public boolean isMultiThreadingCapable() {
		return this == MultiThreaded || this == Stateless;
	}
}
