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
package simpleBenchmarks;

import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.annotations.Message;

/**
 * Common messages for stateful and stateless test actors.
 */
public interface TestInterface {
	/**
	 * Do nothing.
	 * @return returns null Void
	 */
	@Message
	public AsyncResult<Void> nop();

	/**
	 * Waits one millisecond.
	 * @return returns null Void
	 */
	@Message
	public AsyncResult<Void> wait1ms();

	/**
	 * Invokes the next actors invokeNext() and waits for it.
	 * @return returns null Void
	 */
	@Message
	public AsyncResult<Void> invokeNext();

	/**
	 * Invokes the next actors forwardNext() and returns its AsyncResult.
	 * @return the next actors AsyncResult
	 */
	@Message
	public AsyncResult<Void> forwardNext();
}