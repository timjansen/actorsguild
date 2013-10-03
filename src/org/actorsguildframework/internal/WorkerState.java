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

import org.actorsguildframework.annotations.ThreadUsage;


/**
 * Describes the state of a worker thread.
 */
public enum WorkerState {
	/**
	 * The thread is running (spending CPU-time, not necessarily executing a message).
	 */
	Running,
	/**
	 * The thread is waiting for a message to process.
	 */
	Idle,
	/**
	 * The message is running and the message is marked as {@link ThreadUsage#IO}.
	 */
	RunningIO, 
	/**
	 * The thread is either waiting for another message to finish (via {@link AsyncResultImpl}),
	 * or for an actor to become free.
	 */
	WaitingInternal,
	/**
	 * The thread is running in a message marked as {@link ThreadUsage#Waiting}.
	 */
	WaitingExternal
}