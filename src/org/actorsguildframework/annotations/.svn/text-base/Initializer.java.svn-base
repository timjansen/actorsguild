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
package org.actorsguildframework.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.actorsguildframework.Actor;
import org.actorsguildframework.Agent;

/**
 * Methods marked as <code>@Initializer</code> will be invoked in {@link Actor}s and {@link Bean}s, 
 * after the completion of the object's construction phase. All properties that have been given
 * to {@link Agent#create(Class, org.actorsguildframework.Props)} will be set before the initializer
 * is called. Initializers must not take or return any arguments. 
 * <p>
 * You can have any number of initializers in your class, including its sub-class(es). The framework 
 * will first invoke the initializers at the top of the class hierarchy (the super class) 
 * and then go down until it reaches the initializers of the instantiated class. If you override a 
 * initializer, only the new initializer will be called. The order of execution within one level of 
 * the class hierarchy is undefined. 
 * <p>
 * The main purpose of an initializer is to complete the initialization of a bean. You can also use it
 * to check the validity of properties (for example, whether all required properties have been set).
 * <p>
 * In actors, the initializer has the same synchronization guarantees as the constructor. Thus,
 * everything you do in the initializer will be visible to all single-threaded messages. In
 * plain beans, there are no synchronization guarantees.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Initializer {
}
