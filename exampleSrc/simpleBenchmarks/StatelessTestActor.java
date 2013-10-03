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

import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;
import org.actorsguildframework.annotations.Prop;
import org.actorsguildframework.annotations.Shared;

/**
 * A very simple actor with just one message that does nothing.
 */
@Model(ConcurrencyModel.Stateless)
public abstract class StatelessTestActor extends Actor implements TestInterface {
	/**
	 * Returns the next actor, for invokeNext() and forwardNext().
	 * @return the next actor
	 */
	@Prop @Shared
	public abstract TestInterface getNext();
	
	@Message
	public AsyncResult<Void> nop() 
	{
		return result(null);
	}
	
	@Override
	@Message
	public AsyncResult<Void> wait1ms() {
		Object o = new Object();
		long nanos = System.nanoTime();
		do {
			synchronized(o) {
				try {
					o.wait(1);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
		while ((System.nanoTime() - nanos) < 1000000l);
		return result(null);
	}
	
	@Override
	@Message
	public AsyncResult<Void> forwardNext() {
		if (getNext() == null)
			return result(null);
		else
			return getNext().forwardNext();
	}
	
	@Override
	@Message
	public AsyncResult<Void> invokeNext() {
		if (getNext() != null)
			getNext().invokeNext().await();
		return result(null);
	}
}
