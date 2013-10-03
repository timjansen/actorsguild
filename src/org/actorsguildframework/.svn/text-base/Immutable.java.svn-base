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

import java.io.Serializable;

/**
 * The <code>Immutable</code> interface declares a class as immutable. Thus after construction the class can not
 * be modified anymore. The actor's guild can not check whether the implementing class is actually
 * adhering to this, so you should carefully check or design any class that is <code>Immutable</code>. 
 * If a <code>Immutable</code>, instead of a regular <code>Serializable</code>, is passed to a message, it can be
 * passed by reference and is this must faster. Every immutable class must 
 * be {@link Serializable} as well.
 * The Java primitives and {@link java.lang.String} are being treated as implicitly 
 * immutable by the framework.  
 * @see <a href="http://code.google.com/p/actorsguildframework/wiki/WritingImmutableClasses">Writing immutable classes (Wiki)</a> 
 */
public interface Immutable extends java.io.Serializable {
}
