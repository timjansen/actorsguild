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
 * A Copier implementation to "copy" immutables (actually it does not copy, just returns
 * the reference).
 */
public class SerializingCopier implements Copier {

	/* (non-Javadoc)
	 * @see org.actorsguildframework.internal.serializable.Copier#copy(java.io.Serializable)
	 */
	public <T extends Serializable> T copy(T object) {
		return object;
	}

	/* (non-Javadoc)
	 * @see org.actorsguildframework.internal.serializable.Copier#freeze(java.io.Serializable)
	 */
	public Object freeze(Serializable object) {
		return object;
	}

	/* (non-Javadoc)
	 * @see org.actorsguildframework.internal.serializable.Copier#thaw(java.lang.Object)
	 */
	public Serializable thaw(Object storageReference) {
		return (Serializable)storageReference;
	}

}
