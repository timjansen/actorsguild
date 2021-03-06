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

import org.actorsguildframework.Actor;

/**
 * ActorState implementation for single-threaded actors.
 */
public final class MultiThreadedActorState extends ActorState {
	/**
	 * Contains the number of additional threads that could now start running for this actor.
	 */
	private int numberOfThreadsNeeded;
	
	/**
	 * Creates a new MultiThreadedActorState instance.
	 * @param scheduler the Actor's scheduler
	 * @param actor the actor whose state this instance is representing
	 */
	public MultiThreadedActorState(Controller scheduler, Actor actor) {
		super(scheduler, actor);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.actorsguildframework.internal.ActorState#tryExecuteNow(org.actorsguildframework.internal.MessageInvocation, org.actorsguildframework.internal.ThreadState)
	 */
	public boolean tryExecuteNow(MessageInvocation<?> msgI, ThreadState ts) {
		if (!removeMessageFromQueue(msgI))
			return false;
		msgI.runMessageNow(ts);
		return true;
	}
	
	/**
	 * Removes the given message from the queue.
	 * @param message the message to remove. Must be multi-threaded
	 * @return true if the message has been removed. False if it was not in the queue anymore.
	 */
	private boolean removeMessageFromQueue(MessageInvocation<?> message) {
		boolean success;
		controller.getActorLock().lock();
		try {	
			success = mailbox.remove(message);
			updateControllerQueueUnsynchronized();	
		}
		finally {
			controller.getActorLock().unlock();
		}
		return success;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.actorsguildframework.internal.ActorState#executeAllQueuedMessagesUnsynchronized(org.actorsguildframework.internal.ThreadState, org.actorsguildframework.internal.KeepRunningInterface)
	 */
	public int executeAllQueuedMessagesUnsynchronized(ThreadState ts, KeepRunningInterface keepRunning) {
		int msgsExecuted = 0;
		while (keepRunning.shouldContinue()) {
			MessageInvocation<?> msg = mailbox.pop();
			if (msg == null) {// no msg left -> leave
				updateControllerQueueUnsynchronized();
				return msgsExecuted;
			}
			
			updateControllerQueueUnsynchronized();
			controller.getActorLock().unlock(); // unlock for the execution!!
			try {
				msg.runMessageNow(ts);
			}
			finally {
				controller.getActorLock().lock();
			}

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
		numberOfThreadsNeeded = mailbox.size();
		if (oldNumberOfThreadsNeeded == numberOfThreadsNeeded)
			return;
		controller.updateActorStateQueueUnsynchronized(this, oldNumberOfThreadsNeeded, numberOfThreadsNeeded);
	}
}
