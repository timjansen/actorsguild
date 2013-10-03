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

import org.actorsguildframework.Props;
import org.actorsguildframework.annotations.Initializer;

/**
 * ActorProxyFactory is an interface for factories that generate actors and beans.
 */
public interface BeanFactory {
	/**
	 * Creates a new instance of the bean this is the factory for and invokes 
	 * its {@link Initializer}s.
	 * @param controller the controller to use (needed for Actors only, may be null otherwise)
	 * @param props the initial properties of the new bean
	 * @return the new bean
	 */
	public Object createNewInstance(Controller controller, Props props);
}
