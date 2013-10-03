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

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.actorsguildframework.Actor;
import org.actorsguildframework.DefaultAgent;
import org.actorsguildframework.annotations.ThreadUsage;
import org.actorsguildframework.internal.util.FastQueue;
import org.actorsguildframework.internal.util.L;


/**
 * Describes the state of an actor.
 */
public abstract class ActorState extends FastQueue.Entry{
	private final static L log = new L(DefaultAgent.class);
	private final static AtomicLong actorIdGenerator = new AtomicLong();
	
	/**
	 * A reference to the Actor's scheduler.
	 */
	protected final Controller controller;

	/**
	 * The actor implementation instance the state belongs to.
	 */
	private final Actor actor;
	
	/**
	 * The unique number of the actor (only for logging).
	 */
	private final long actorNumber = actorIdGenerator.addAndGet(1);
	
	/**
	 * Contains the list of all incoming messages that need to be processed in this 
	 * actor.
	 * 
	 * Locking policy: you must lock {@link ControllerImplementation#actorLock} before accessing this field.
	 * @see #mailboxMT 
	 */	
	protected final FastQueue<MessageInvocation<?>> mailbox;
	
	/**
	 * Creates a new ActorState instance.
	 * @param scheduler the Actor's scheduler
	 * @param actor the actor whose state this instance is representing
	 */
	public ActorState(Controller scheduler, Actor actor) {
		this.controller = scheduler;
		this.actor = actor;
		this.mailbox = new FastQueue<MessageInvocation<?>>();
	}

	/**
	 * Returns the actor (without proxy).
	 * @return the actual actor
	 */
	public Actor getActor() {
		return actor;
	}
	
	/**
	 * Adds the given message invocation to the appropriate queue.
	 * 
	 * @param invk the invocation of the initializer
	 */
	private void addInvokationMessage(MessageInvocation<?> invk) {
		controller.getActorLock().lock();
		try {
			mailbox.add(invk);
			updateControllerQueueUnsynchronized();
		}
		finally {
			controller.getActorLock().unlock();
		}
	}
	
	/**
	 * Queues a message for execution.
	 * @param caller the caller to use
	 * @param usage the thread usage of the message
	 * @param args the arguments of the message. Will be copied (deep copy for mutables)
	 * @return the result handle
	 */
	@SuppressWarnings("unchecked")
	public AsyncResultImpl<?> queueMessage(MessageCaller caller, ThreadUsage usage, Object[] args) {
		
		MessageInvocation superInvk = ThreadState.get().getCurrentInvocation();
		MessageInvocation invk = new MessageInvocation(this, caller, args, superInvk, 
				usage, false);

		if (controller.isLoggingActions())
			log.info("Queueing message #%d for #%d (%s): %s(%s)",
					invk.getMessageNumber(),
					ActorState.getState(actor).getActorNumber(), actor.getClass().getName(),
					caller.getMessageName(), Arrays.deepToString(args));

		addInvokationMessage(invk);
		return invk.getAsyncResult();
	}
	
	/**
	 * Uses the invoking thread to execute all messages in the Actor's queues. May
	 * run for a long time or even forever, especially if the messages send more messages
	 * to the Actor.
	 * You must lock the {@link ControllerImplementation#actorLock} before calling this method.
	 * @param ts the current ThreadState
	 * @param keepRunning a interface that can be used to stop the operation: if it is set, the 
	 *                    method will return as soon as it processed the current message
	 * @return the number of messages that have been executed
	 */
	public abstract int executeAllQueuedMessagesUnsynchronized(ThreadState ts, KeepRunningInterface keepRunning);

	/**
	 * Executes the given message now in the current thread, if it is not already 
	 * running or finished. 
	 * This allows getting the message result as soon as possible, and not to wait for another
	 * thread to pick it up.
	 * There is no guarantee that the message result is available after the invocation. It is possible
	 * that another thread is currently executing the message and the method returns immediately
	 * without a result being finished.
	 * @param msgI the message invocation to execute
	 * @param ts the current ThreadState
	 * @return true if the message has been executed, false otherwise
	 */
	public abstract boolean tryExecuteNow(MessageInvocation<?> msgI, ThreadState ts);
	
	/**
	 * Updates the registration of the ActorState in the controller.
	 * Should be invoked every time it may have changed before the actorLock is unlocked.
	 */
	public abstract void updateControllerQueueUnsynchronized();
	
	/**
	 * Returns the Controller of the actor.
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Returns the actor number (used only for logging).
	 * @return the actorNumber
	 */
	public long getActorNumber() {
		return actorNumber;
	}

	/**
	 * Returns the state of the given actor instance.
	 * @param actor the actor
	 * @return the state
	 */
	public static ActorState getState(Actor actor) {
		ActorProxy ap = (ActorProxy) actor;
		return ap.getState__ACTORPROXYMETHOD();
	}

}
