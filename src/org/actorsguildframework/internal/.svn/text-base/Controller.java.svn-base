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

import java.util.concurrent.locks.ReentrantLock;

import org.actorsguildframework.Agent;

/**
 * Controller interface. This is the central interface for the library code. 
 */
public interface Controller {

	/**
	 * Returns the next Actor to process from the queue and puts it at the end of the queue.
	 * Thus the thread that takes it does not own it and it stays in the queue until the
	 * thread actually empties the queue or locks the actor.
	 * You must lock actorLock before calling this!
	 * @return the actor. Null if the actor queue is empty.
	 * @throws InterruptedException if the thread is interrupted
	 */
	public ActorState getNextFromQueueUnsynchronized()
			throws InterruptedException;

	/**
	 * Makes sure that the given actor is either in the queue or not, depending on the second 
	 * argument. 
	 * You must lock actorLock before calling this!
	 * @param actorState the actor to check and possibly to add/remove
	 * @param oldNumberOfOpenParallelTasks the number of open tasks that has been registered before for the Actor
	 * @param newNumberOfOpenParallelTasks the number of tasks needed for the Actor
	 */
	public void updateActorStateQueueUnsynchronized(ActorState actorState,
			int oldNumberOfOpenParallelTasks, int newNumberOfOpenParallelTasks);

	/**
	 * Changes the state of a thread from the old to the given new state.
	 * @param oldState the thread's old state
	 * @param newState the thread's new state
	 */
	public void changeWorkerThreadState(WorkerState oldState,
			WorkerState newState);

	/**
	 * Removes the state of a thread from the statistics.
	 * @param oldState the thread's old state
	 */
	public void removeWorkerThreadState(WorkerState oldState);

	/**
	 * Returns the agent of the controller.
	 * @return the agent
	 */
	public Agent getAgent();

	/**
	 * Returns a KeepRunningInterface for a worker that will tell the worker when to stop. 
	 * @return the interface
	 */
	public KeepRunningInterface createKeepRunningInterface();

	/**
	 * Returns true if action like message will be logged.
	 * @return true for logging, false otherwise
	 */
	public boolean isLoggingActions();

	/**
	 * Returns the actor lock for accessing an ActorState of this Controller or and the 
	 * mActorsWithWork list. 
	 * @return the actor lock
	 */
	public ReentrantLock getActorLock();
	
	/**
	 * Tries to shut down the controller with all its threads as soon as possible. Messages 
	 * that have not been processed yet may not be processed. There is no guarantee
	 * that this operation succeeds.
	 */
	public void shutdown();
}