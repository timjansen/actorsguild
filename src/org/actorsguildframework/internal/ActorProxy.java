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

import org.actorsguildframework.internal.codegenerator.ActorProxyCreator;

/**
 * Interface for actor proxies. Must only be used by sub-classes of Actor. 
 * The method names of ActorProxy are intentionally long, so they do not
 * conflict with any of the Actor's methods.
 * <p>
 * ActorProxys also need a special constructor:
 * <pre>
 *   public ActorProxyImpl(Controller controller);
 * </pre>
 * 
 * @see ActorProxyCreator
 */
public interface ActorProxy {

	/**
	 * Returns the actor state.
	 * @return the actor's state
	 */
	public ActorState getState__ACTORPROXYMETHOD();

}
