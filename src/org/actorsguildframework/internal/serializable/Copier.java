/*
 *    Copyright 2008,2009 Tim Jansen
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
package org.actorsguildframework.internal.serializable;

import java.io.Serializable;

/**
 * Interface for instances that are able to copy and freeze one type of values.
 */
public interface Copier {
	/**
	 * Creates a copy of the given Serializable object.
	 * @param <T> the type of the object to copy
	 * @param object the object to copy
	 * @return the copy
	 */
	public <T extends Serializable> T copy(T object);
	
	/**
	 * Takes the given Serializable object and returns a reference to a
	 * storage representation of it (of undefined format). You can give this 
	 * reference to the method {@link #thaw(Object)}, and it will then create
	 * a copy of the frozen object.
	 * @param object the object to freeze
	 * @return the storage reference. Give this to {@link #thaw(Object)}.
	 */
	public Object freeze(Serializable object);
	
	/**
	 * Creates a copy of a storage object created by {@link #freeze(Serializable)}.
	 * All Serializable types will be copied deeply. However, immutable types
	 * may be kept.
	 * You can create as many copies as you want from one storage object.
	 * @param storageReference the reference to the storage object
	 * @return a deep copy of the original object
	 */
	public Serializable thaw(Object storageReference);
}
