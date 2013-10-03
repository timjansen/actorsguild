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
package org.actorsguildframework;

import junit.framework.Assert;

import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;
import org.actorsguildframework.annotations.ThreadUsage;
import org.actorsguildframework.annotations.Usage;
import org.actorsguildframework.internal.ActorProxy;
import org.junit.Test;

/**
 * Unit tests for Agent.
 */
public class AgentTest {
	/**
	 * Multi-threaded test actor.
	 */
	@Model(ConcurrencyModel.MultiThreaded)
	public static class MyActor extends Actor {
		/**
		 * Contains the agent determined in {@link #setCurrentAgent()}.
		 */
		public volatile Agent agent;
		
		/**
		 * A flag to control {@link #waitForFlag()}.
		 */
		public volatile boolean runFlag;
		
		/**
		 * Sets the current agent in {@link #agent}.
		 * @return void
		 */
		@Message
		public AsyncResult<Void> setCurrentAgent() {
			this.agent = Actor.getCurrentAgent();
			return noResult();
		}
		
		/**
		 * Waits a few ms.
		 * @return void
		 */
		@Message
		@Usage(ThreadUsage.Waiting)
		public AsyncResult<Void> waitABit() {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return noResult();
		}
		
		/**
		 * Runs until {@link #runFlag} is set.
		 * @return void
		 */
		@Message
		@Usage(ThreadUsage.Waiting)
		public AsyncResult<Void> waitForFlag() {
			while (runFlag) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return noResult();
		}
		
		/**
		 * Always throws an exception with the message "foobar"
		 * @return never returns, always throws
		 * @throws Exception the exception
		 */
		@Message
		public AsyncResult<Void> throwSomething() throws Exception {
			throw new Exception("foobar");
		}
	}
	
	
	/**
	 * Test actor that throws an exception in the ctor.
	 */
	public static class AgentCtorException extends Actor {
		/**
		 * Throws a RuntimeException.
		 */
		public AgentCtorException() {
			throw new RuntimeException("argh");
		}
	}
	
	/**
	 * Tests Agent.getCurrentAgent.
	 */
	@Test
	public void testGetCurrentAgent() {
		Assert.assertNull(Actor.getCurrentAgent());
		
		DefaultAgent a = new DefaultAgent();
		Assert.assertNull(Actor.getCurrentAgent());
		
		MyActor ma = a.create(MyActor.class);
		ma.setCurrentAgent().await();
		Assert.assertSame(a, ma.agent);
		Assert.assertNull(Actor.getCurrentAgent());
	}
	
	/**
	 * Tests Agent.create.
	 */
	@Test
	public void testCreate() {
		DefaultAgent a = new DefaultAgent();
		MyActor ma = a.create(MyActor.class);
		Assert.assertTrue(ma instanceof MyActor);
		Assert.assertTrue(ma instanceof ActorProxy);
	}
	
	/**
	 * Tests Agent.create, lets Agent throw exception.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateNullClass() {
		DefaultAgent a = new DefaultAgent();
		a.create(null, null);
	}
	
	/**
	 * Tests Agent.create, lets Agent throw exception.
	 */
	@Test(expected=WrappedException.class)
	public void testCreateWrappedException() {
		DefaultAgent a = new DefaultAgent();
		a.create(AgentCtorException.class);
	}
	
	
	/**
	 * Tests Agent.awaitAll.
	 */
	@Test
	public void testAwaitAll() {
		DefaultAgent a = new DefaultAgent();
		MyActor ma = a.create(MyActor.class);
		AsyncResult<Void> r1, r2, r3, r4;
		r1 = ma.waitABit();
		r2 = ma.waitABit();
		r3 = ma.waitABit();
		r4 = ma.setCurrentAgent();
		a.awaitAll(r1, r2, r3, r4);
		Assert.assertTrue(r1.isReady());
		Assert.assertTrue(r2.isReady());
		Assert.assertTrue(r3.isReady());
		Assert.assertTrue(r4.isReady());
		a.awaitAll(); // should return immediately
	}
	
	/**
	 * Tests Agent.awaitAll
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testAwaitAllIllegalArgumentException() {
		AsyncResult<Void> r1;
		DefaultAgent a = new DefaultAgent();
		MyActor ma = a.create(MyActor.class);
		
		r1 = ma.setCurrentAgent();
		a.awaitAll(r1, null);
	}
	
	/**
	 * Tests Agent.awaitAny
	 */
	@Test
	public void testAwaitAny() {
		AsyncResult<?> r;
		AsyncResult<Void> r1, r2, r3, r4;
		DefaultAgent a = new DefaultAgent();
		MyActor ma = a.create(MyActor.class);
				
		a.awaitAny(); // should return immediately
				
		r1 = ma.waitABit();
		r = a.awaitAny(r1);
		Assert.assertTrue(r == r1);
		Assert.assertTrue(r1.isReady());
		
		ma.runFlag = true;
		r1 = ma.waitForFlag();
		r2 = ma.setCurrentAgent();
		r = a.awaitAny(r1, r2);
		Assert.assertTrue(r == r2);
		Assert.assertFalse(r1.isReady());
		Assert.assertTrue(r2.isReady());
		ma.runFlag = false;
		Assert.assertSame(r1, a.awaitAny(r1));
		Assert.assertTrue(r1.isReady());
		
		r1 = ma.waitABit();
		r2 = ma.waitABit();
		r3 = ma.waitABit();
		a.awaitAny(r1, r2, r3);
		Assert.assertTrue(r1.isReady() || r2.isReady() || r3.isReady());
		a.awaitAll(r1, r2, r3);
		Assert.assertTrue(r1.isReady() && r2.isReady() && r3.isReady());
		
		ma.runFlag = true;
		r1 = ma.waitForFlag();
		r2 = ma.waitABit();
		r3 = ma.waitABit();
		r4 = ma.setCurrentAgent();
		r = a.awaitAny(r1, r2, r3, r4);
		Assert.assertTrue(r == r2 || r == r3 || r ==r4);
		Assert.assertFalse(r1.isReady());
		Assert.assertTrue(r2.isReady() || r3.isReady() || r4.isReady());
		ma.runFlag = false;
		Assert.assertSame(r1, a.awaitAny(r1));
		Assert.assertTrue(r1.isReady());

	}
	
	/**
	 * Tests Agent.awaitAny
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testAwaitAnyIllegalArgumentException() {
		AsyncResult<Void> r1;
		DefaultAgent a = new DefaultAgent();
		MyActor ma = a.create(MyActor.class);
		
		r1 = ma.setCurrentAgent();
		a.awaitAny(r1, null);
	}
	
	
	/**
	 * Tests Agent.awaitAllAndThrow.
	 * @throws Exception 
	 */
	@Test(expected=WrappedException.class)
	public void testAwaitAllAndThrow() throws Exception {
		AsyncResult<Void> r1, r2;
		DefaultAgent a = new DefaultAgent();
		MyActor ma = a.create(MyActor.class);
		r1 = ma.setCurrentAgent();
		r2 = ma.throwSomething();
		a.awaitAllUntilError(r1, r2);
	}

}
