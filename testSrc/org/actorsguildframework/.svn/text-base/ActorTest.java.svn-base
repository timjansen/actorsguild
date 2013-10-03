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
package org.actorsguildframework;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit tests for Actor.
 */
public class ActorTest {
	/**
	 * Test actor.
	 */
	public static class MyActor extends Actor {
	}
	
	/**
	 * Tests direct instantiation of MyActor.
	 */
	@Test(expected=ActorException.class)
	public void testDirectActorInstatiation() {
		new MyActor();
	}
	
	/**
	 * Tests {@link Actor#getAgent()}.
	 */
	@Test
	public void testGetAgent() {
		DefaultAgent a = new DefaultAgent();
		MyActor b = a.create(MyActor.class);
		Assert.assertSame(a, b.getAgent());
	}
	
	/**
	 * Tests {@link Actor#result(Object)}.
	 */
	@Test
	public void testResult() {
		DefaultAgent a = new DefaultAgent();
		MyActor b = a.create(MyActor.class);
		ImmediateResult<String> r = b.result("foo");
		Assert.assertTrue(r.isReady());
		Assert.assertEquals("foo", r.get());
	}
	
	/**
	 * Tests {@link Actor#noResult()}.
	 */
	@Test
	public void testNoResult() {
		DefaultAgent a = new DefaultAgent();
		MyActor b = a.create(MyActor.class);
		ImmediateResult<Void> r = b.noResult();
		Assert.assertTrue(r.isReady());
		Assert.assertEquals(null, r.get());
	}
}
