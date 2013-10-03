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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.sql.DataSource;

import org.actorsguildframework.Actor;
import org.actorsguildframework.Immutable;

/**
 * The <code>Shared</code> annotation is used to state that a variable, field or property
 * refers to a thread-safe object that can be shared among actors.<p>
 * 
 * <code>@Shared</code> is used for three purposes:
 * <ol><li>In a method argument with {@link Message} annotations, to mark an argument as 
 *   shared between caller and called actor
 * <li>In a field of an {@link Actor}, to mark the field as refering to an object that 
 *   can be safely used in a {@link ConcurrencyModel#Stateless} actor or method
 * <li>In a {@link Prop} annotated getter, to mark the property as refering to an object 
 *   that can be safely used in a {@link ConcurrencyModel#Stateless} actor or method</ol>
 *
 * In any case, the class marjed as as <code>@Shared</code> <b>must</b> be fully 
 * multi-thead-safe, as they will be used in several threads simultanously.
 * 
 * <h2>@Shared in Method Arguments</h2>
 * The effect of @Shared in method arguments is that the argument will always be parsed 
 * by reference, and thus it is shared by both caller and callee.
 * This is useful if you have an object that can not be Serialized and is not an Actor.
 * <p>
 * <code>@Shared</code> as method argument annotation should be avoided if possible, 
 * as shared can only work if caller and callee run in the same VM. Currently ActorsGuild 
 * supports only actors in the same VM, and thus it will always work, but in future 
 * versions this may be different.
 * <p>Example:
 * <pre>@Message
 * public AsyncResult<Void> sendMessage(@Shared Socket socket, String msg) throws Exception {
 * 	try {
 * 		String path = HTTPHelper.readHeader(socket.getInputStream());
 *      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
 *      bw.write(msg);
 *      bw.flush();
 *	}
 *	finally {
 *		socket.close();
 *	}
 *	return noResult();
 * }
 * </pre>
 * 
 * <h2>@Shared in Fields and @Prop properties</h2>
 * As {@link ConcurrencyModel#Stateless} actors must not have any state, it is forbidden
 * for them to have any non-<code>final</code> field, but they actors to be configured at 
 * their creation with final fields referring to {@link Immutable} values or actors. 
 * However, an exception is being made for final fields annotated with <code>@Shared</code>.
 * This is needed sometimes, for example to configure a JDBC {@link DataSource}. 
 * <p>
 * {@link ConcurrencyModel#Stateless} messages in non-stateless actors are required to use
 * only stateless fields of the actor, thus final fields refering to immutable values,
 * actors or that use a <code>@Shared</code> annotation.
 *   
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shared {

}
