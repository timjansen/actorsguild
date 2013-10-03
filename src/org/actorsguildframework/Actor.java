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

import org.actorsguildframework.annotations.Bean;
import org.actorsguildframework.annotations.Initializer;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;
import org.actorsguildframework.annotations.Prop;
import org.actorsguildframework.annotations.Usage;
import org.actorsguildframework.internal.ActorProxy;
import org.actorsguildframework.internal.ActorState;
import org.actorsguildframework.internal.MessageInvocation;
import org.actorsguildframework.internal.ThreadState;

/**
 * Actor is the class that all actors need to implement.
 * <p>
 * You can not create an actor using the 'new' operator. Doing so would throw an exception. 
 * Instead, {@link Agent#create(Class)} must be used to create a new instance. Agent returns a 
 * proxy for the actual Actor which must be used to access the actor. Most of the features of the 
 * Actor's Guild framework are being done by this invisible proxy.  
 * <p>
 * An actor does not need a constructor, but you can define one without arguments. Anything you
 * write in it will be visible in all normal messages, but not in multi-threaded messages 
 * (you need to synchronize manually for them). 
 * <p>
 * For the Actor's initialization you implement bean properties, preferably with the {@link Prop}
 * annotation which will ensure that they are thread-safe (the property implementations offered
 * by most IDEs are not!). Properties can be initialized upon creation of the Actor in the
 * {@link Agent#create(Class, Props)} invocation. After <code>create</code> completed setting the
 * properties, the Agent will invoke the Actor's {@link Initializer} method. Implement one for any
 * additional initialization work.  
 * <p>
 * Every actor should define one or more methods to receive messages. These methods must be
 * annotated with {@link Message}, and follow the rules given in the documentation of @Message. 
 * All the actual work is being done by the message implementations. 
 * <p>
 * By default, an actor receives only one message at a time. Thus is it operated single-threaded,
 * and does not need to take care of such things as synchronization. In some cases, especially
 * to integrate external interfaces and code, it is useful or even required for an actor to
 * process several messages simultaneously.  This can be declared using the {@link Model}
 * and {@link Usage} annotation. 
 * <p>
 * An actor can have public methods that are not @Message. However, you are responsible for 
 * the synchronization of such methods, otherwise they will not work correctly. 
 * @see Agent
 * @see Agent#create(Class, Props)
 */
@Bean(threadSafe=true)
public abstract class Actor {
	
	/**
	 * The default constructor for all Actors. It will check whether the new Actor sub-class
	 * is a proxy. If it is not a proxy, an exception will be thrown to prevent this.
	 * You must create all Actors using the methods {@link Agent#create(Class)}. 
	 * @throws ActorRuntimeException if the instantiated actor is not a proxy class
	 */
	public Actor() {
		if (!(this instanceof ActorProxy))
			throw new ActorRuntimeException("You must not use an Actor's constructor directly, because "+
					"then the would not have its proxy. Use Agent.createActor() to create instances "+
					"of Actors instead.");
	}

	/**
	 * Returns the Agent of this Actor.
	 * @return the Actor's Agent
	 * @see Actor#getCurrentAgent()
	 */
	public final Agent getAgent() {
		return ActorState.getState(this).getController().getAgent();
	}
	
	/**
	 * Helper method to create a new ImmediateResult instance.
	 * @param <T> the type of result
	 * @param value the result value
	 * @return the ImmediateResult instance
	 */
	public <T> ImmediateResult<T> result(T value) {
		return new ImmediateResult<T>(value);
	}
	
	/**
	 * Helper method to create a new ImmediateResult instance for methods that have
	 * no result.
	 * @return the ImmediateResult instance (carries null as value)
	 */
	public ImmediateResult<Void> noResult() {
		return new ImmediateResult<Void>(null);
	}

	/**
	 * Returns the current thread's agent, if the current thread is a thread processing an actor message.
	 * Otherwise it returns null.
	 * @return the current agent, or null
	 * @see Actor#getAgent()
	 */
	public static Agent getCurrentAgent() {
		MessageInvocation<?> mi = ThreadState.get().getCurrentInvocation();
		if (mi == null)
			return null;
		return mi.getTargetActor().getController().getAgent();
	}
	
}
