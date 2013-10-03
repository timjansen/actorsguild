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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.actorsguildframework.Agent;

/**
 * Declares this class to be a {@link Agent}-managed bean. You must create such beans using
 * {@link Agent#create(Class, org.actorsguildframework.Props)}. 
 * The @Bean annotation is optional and only needed if you want to change the bean's
 * default configuration.
 * Agent-managed beans can use the {@link Prop} and {@link DefaultValue} annotations.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Bean {
	/**
	 * Specifies whether the bean uses thread-safe property and constructor implementations.
	 * If true, all values set in the constructor, initializer and {@link Prop} 
	 * property implementations are synchronized on the bean instance. 
	 * If false, the bean does not use any synchronization  and can only be used
	 * in a single threaded or with manualsynchronization.
	 * <p>
	 * Please note that even with threadSafe set to true, {@link Prop} annotated
	 * read-write fields are not synchronized and thus not thread-safe. In Actors 
	 * this is not necessarily a problem though, because in Actors all messages are 
	 * automatically synchronized.  
	 */
	boolean threadSafe();
}
