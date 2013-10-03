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

import java.util.ArrayList;

import org.actorsguildframework.annotations.ThreadUsage;

/**
 * Contains the state of a thread in the actor's guild.
 */
public final class ThreadState {
	private static class MyThreadLocal extends ThreadLocal<ThreadState> {
		@Override
		protected ThreadState initialValue() {
			return new ThreadState();
		}
	}

	private static ThreadLocal<ThreadState> instances = new MyThreadLocal();
	private WorkerState currentState;
	
	/**
	 * Returns the ThreadState instance for the thread.
	 * @return the instance for the thread
	 */
	public static ThreadState get() {
		return instances.get();
	}

	private Controller controller; // null if not a worker thread
	private ArrayList<MessageInvocation<?>> currentInvocationsStack;
	
	
	private ThreadState() {
		this.currentInvocationsStack = new ArrayList<MessageInvocation<?>>();
		this.currentState = WorkerState.Running;
	}

	/**
	 * Called by a worker to initialize itself. Does not register the worker at the controller.
	 * This should have happened before the thread was created.
	 * 
	 * @param controller the controller
	 */
	public void initWorker(Controller controller) {
		this.controller = controller;
		this.currentState = WorkerState.Running;
		this.currentInvocationsStack.clear();
	}

	/**
	 * Called by a worker before it ends. 
	 */
	public void uninitWorker() {
		controller.removeWorkerThreadState(currentState);
		controller = null;
		if (currentInvocationsStack.size() > 0)
			throw new RuntimeException("Uninit worker called, but invocation stack size "+
				currentInvocationsStack.size());
	}
	
	/**
	 * Notifies the ThreadState that the current thread starts a new MessageInvocation. 
	 * @param mi the MessageInvocation
	 * @return the old state of the thread
	 * @see #endInvocation(WorkerState)
	 */
	public WorkerState startInvocation(MessageInvocation<?> mi) {
		currentInvocationsStack.add(mi);
		return setStateToInvocation(mi.getThreadUsage());
	}
	
	/**
	 * Notifies the ThreadState that the current thread just ended a MessageInvocation.
	 * @param state the new state of the thread. Should be the value returned by {@link #startInvocation(MessageInvocation)}.
	 * @see #startInvocation(MessageInvocation)
	 */
	public void endInvocation(WorkerState state) {
		setState(state);
		currentInvocationsStack.remove(currentInvocationsStack.size()-1);
	}

	/**
	 * Returns the message invocation that this thread is currently executing, or null
	 * if it is not busy.
	 * @return the current message or null
	 */
	public MessageInvocation<?> getCurrentInvocation() {
		if (currentInvocationsStack.size() > 0)
			return currentInvocationsStack.get(currentInvocationsStack.size()-1);
		else
			return null;
	}

	/**
	 * Sets the current state of the thread. This will update the scheduler's statistics.
	 * @param state the new state of the thread
	 * @return the old state, so you can restore it
	 */
	public WorkerState setState(WorkerState state)
	{
		if (state == currentState)
			return state;
		
		WorkerState oldState = currentState;
		currentState = state;
		
		if (controller != null) 
			controller.changeWorkerThreadState(oldState, state);
		
		return oldState;
	}
	
	/**
	 * Sets the current state of the thread to reflect the invocation of a message with
	 * the specified ThreadUsage.
	 * @param usage the ThreadUsage of the new message
	 * @return the old state, so you can restore it
	 */
	public WorkerState setStateToInvocation(ThreadUsage usage)
	{
		switch (usage) {
		case CpuBound:
			return setState(WorkerState.Running);
		case IO:
			return setState(WorkerState.RunningIO);
		case Waiting:
			return setState(WorkerState.WaitingExternal);
		default:
			throw new RuntimeException("unknown ThreadUsage, fix me!");
		}
	}

}
