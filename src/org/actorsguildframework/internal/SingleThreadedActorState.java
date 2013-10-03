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
package org.actorsguildframework.internal;

import java.util.concurrent.locks.Condition;

import org.actorsguildframework.Actor;

/**
 * ActorState implementation for single-threaded actors.
 */
public final class SingleThreadedActorState extends ActorState {
	/**
	 * mIsBusyWith is used for single-threaded actors:
	 * If the actor is currently executing a message, it refers to this message. 
	 * No other thread can access the actor during that time. Otherwise it is null. 
	 * This means that a Thread can invoke a message, provided that 
	 * it can set the mIsBusyWith to itself successfully.
	 *  
	 * Locking policy: you must lock {@link ControllerImplementation#actorLock} before accessing this field.
	 */
	private MessageInvocation<?> isBusyWith;
	
	/**
	 * This condition of {@link ControllerImplementation#actorLock} will be signaled every time the 
	 * {@link #isBusyWith} field is freed (set to null). This allows threads to wait until
	 * they can access the actor. 
	 */
	private final Condition busyLockFreed;
	
	/**
	 * Contains the number of additional threads that could now start running for this actor.
	 * (This is the sum of unprocessed multi-threaded messages plus one of there is a single-threaded
	 * message that could be executed now)  
	 */
	private int numberOfThreadsNeeded;
	
	/**
	 * Creates a new SingleThreadedActorState instance.
	 * @param scheduler the Actor's scheduler
	 * @param actor the actor whose state this instance is representing
	 */
	public SingleThreadedActorState(Controller scheduler, Actor actor) {
		super(scheduler, actor);
		
		this.busyLockFreed = controller.getActorLock().newCondition();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.actorsguildframework.internal.ActorState#tryExecuteNow(org.actorsguildframework.internal.MessageInvocation, org.actorsguildframework.internal.ThreadState)
	 */
	public boolean tryExecuteNow(MessageInvocation<?> finalMsg, ThreadState ts) {
		controller.getActorLock().lock();
		try {
			if ((isBusyWith != null) || !mailbox.isInQueue(finalMsg))
				return false; // quick return
			
			boolean success = false;
			do  {
				MessageInvocation<?> msg = mailbox.pop();
				if (msg == null) // no message left -> leave (shouldn't happen)
					break;
				
				isBusyWith = msg;
				updateControllerQueueUnsynchronized();
				controller.getActorLock().unlock(); // unlock for the execution!!
				try {
					msg.runMessageNow(ts);
				}
				finally {
					controller.getActorLock().lock();
				}
					
				isBusyWith = null;
				if (msg == finalMsg) {
					success = true;
					break;
				}
			}
			while (mailbox.isInQueue(finalMsg));
			
			busyLockFreed.signal();
			updateControllerQueueUnsynchronized();
			return success;
		}
		finally {
			controller.getActorLock().unlock();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.actorsguildframework.internal.ActorState#executeAllQueuedMessagesUnsynchronized(org.actorsguildframework.internal.ThreadState, org.actorsguildframework.internal.KeepRunningInterface)
	 */
	public int executeAllQueuedMessagesUnsynchronized(ThreadState ts, KeepRunningInterface keepRunning) {
		int msgsExecuted = 0;
		while (keepRunning.shouldContinue()) {
			MessageInvocation<?> msg;
			if (isBusyWith == null)
				msg = mailbox.pop();
			else
				msg = null;

			if (msg == null) {// no msg left -> leave
				updateControllerQueueUnsynchronized();
				return msgsExecuted;
			}
			
			isBusyWith = msg;
			updateControllerQueueUnsynchronized();
			controller.getActorLock().unlock(); // unlock for the execution!!
			try {
				msg.runMessageNow(ts);
			}
			finally {
				controller.getActorLock().lock();
			}

			isBusyWith = null;
			busyLockFreed.signal(); 
			msgsExecuted++;
		}
		updateControllerQueueUnsynchronized();
		return msgsExecuted;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.actorsguildframework.internal.ActorState#updateControllerQueueUnsynchronized()
	 */
	public void updateControllerQueueUnsynchronized() {
		int oldNumberOfThreadsNeeded = numberOfThreadsNeeded;
		numberOfThreadsNeeded = (((isBusyWith == null) && !mailbox.isEmpty()) ? 1 : 0);
		if (oldNumberOfThreadsNeeded == numberOfThreadsNeeded)
			return;
		controller.updateActorStateQueueUnsynchronized(this, oldNumberOfThreadsNeeded, numberOfThreadsNeeded);
	}
}
