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

/**
 * Specifies the way a Message is spending its time when it is not waiting for another
 * Actor. This setting allows the framework to allocate a sufficient number of threads.
 */
public enum ThreadUsage {
	/**
	 * Specifies that a message implementation is CPU-bound. Thus the message is either waiting
	 * for other Actors, or it is burning CPU cycles, but it does not wait for any external
	 * data or event.  
	 */
	CpuBound,
	/**
	 * Specifies that a message implementation spends time using IO, e.g. reading a file.
	 * You should use this when your Actor is using blocking IO calls (like a regular read())
	 * that may take some time (seconds), but usually finish within a short time. 
	 * You should not use this when doing network communication, as this may need a very long
	 * time (minutes) to finish - then {@value #Waiting} would be the right specifier.
	 * <p>
	 * The consequence of this option is usually, that the framework may consider to create more
	 * threads if too many actors spend their time doing I/O.
	 */
	IO,
	/**
	 * Specifies that a message implementation spends most of its time waiting for some external
	 * event, for example an incoming network connection, incoming network data or some timeout.
	 * This is what you should use for a message implementation that is doing network operations
	 * or that waits for some timeout.
	 * <p>
	 * The consequence of this option is usually that the framework makes sure that there is a spare
	 * thread.
	 */
	Waiting
}
