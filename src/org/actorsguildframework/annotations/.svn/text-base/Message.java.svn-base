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

import java.lang.annotation.*;

/**
 * Message marks a method of an Actor as message receiver. 
 * <p>
 * There are several prerequisites for such a method, beside the annotation:
 * <ol>
 * <li>All arguments must be immutable (Java primitives, String, Enum or classes with
 * implementing Immutable) or Serializable (much slower, will be serialized and
 * deserialized)</li>
 * <li>The method must return the type AsyncResult<T> (with T being immutable as well)</li>
 * <li>The method must not be abstract, static or final</li>
 * <li>The method must be public</li>
 * </ol>
 * 
 * A Message implementation can throw any exception. Please note that non-RuntimeExceptions will
 * be wrapped and transported to the caller as WrappedException.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Message {
}
