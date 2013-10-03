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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.actorsguildframework.immutable.ImmutableHelper;

/**
 * Tools to freeze and/or copy Serializable types.
 */
public class SerializableHelper {
	private final static Copier defaultCopier = new SerializingCopier();
	private final static Map<Class<? extends Serializable>, Copier> classCopiers;
	
	static {
		HashMap<Class<? extends Serializable>, Copier> cc = new HashMap<Class<? extends Serializable>, Copier>();
		
		classCopiers = Collections.unmodifiableMap(cc);
	}
	
	/**
	 * Creates a copy of the given Serializable object.
	 * @param <T> the type of the object to copy
	 * @param object the object to copy. May be a null reference.
	 * @return the copy (null reference if null was given)
	 */
	public static <T extends Serializable> T copy(T object) {
		if (object == null)
			return null;
		if (ImmutableHelper.isImmutable(object))
			return object;
		
		Copier cc = classCopiers.get(object.getClass());
		if (cc == null)
			return defaultCopier.copy(object);
		else
			return cc.copy(object);
	}
	
	/**
	 * Takes the given Serializable object and returns a reference to a
	 * storage representation of it (of undefined format). You can give this 
	 * reference to the method {@link #thaw(Object)}, and it will then create
	 * a copy of the frozen object.
	 * @param object the object to freeze (may be null)
	 * @return the storage reference (may be null). Give this to {@link #thaw(Object)}.
	 */
	public static Object freeze(Serializable object) {
		if (object == null)
			return null;
		return null;
	}
	
	/**
	 * Creates a copy of a storage object created by {@link #freeze(Serializable)}.
	 * All Serializable types will be copied deeply. However, immutable types
	 * may be kept.
	 * You can create as many copies as you want from one storage object.
	 * @param storageReference the reference to the storage object
	 * @return a deep copy of the original object (possibly null)
	 */
	public static Serializable thaw(Object storageReference) {
		if (storageReference == null)
			return null;
		return null;
	}
}
