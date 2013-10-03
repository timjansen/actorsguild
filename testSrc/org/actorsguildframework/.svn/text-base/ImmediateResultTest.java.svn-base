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

import org.actorsguildframework.AsyncResult.Notifier;
import org.easymock.EasyMock;
import org.junit.Test;

public class ImmediateResultTest {
	@Test
	public void testNotifier() {
		ImmediateResult<String> r = new ImmediateResult<String>("foo");

		Notifier<String> n = EasyMock.createStrictMock(Notifier.class);
		n.resultReady(r);
		EasyMock.replay(n);
		r.addNotifier(n);
		r.removeNotifier(n);
		EasyMock.verify(n);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNotifierNull() {
		ImmediateResult<String> r = new ImmediateResult<String>("foo");
		r.addNotifier(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRemoveNotifierNull() {
		ImmediateResult<String> r = new ImmediateResult<String>("foo");
		r.removeNotifier(null);
	}
	
	@Test
	public void testAwait() {
		ImmediateResult<String> r = new ImmediateResult<String>("foo");
		r.await();
	}
	
	@Test
	public void testGet() {
		ImmediateResult<String> r = new ImmediateResult<String>("foo");
		Assert.assertEquals("foo", r.get());

		ImmediateResult<String> t = new ImmediateResult<String>(null);
		Assert.assertNull(t.get());
	}
	
	@Test
	public void testGetException() {
		ImmediateResult<String> t = new ImmediateResult<String>(null);
		Assert.assertNull(t.getException());
	}
	
	@Test
	public void testIsReady() {
		ImmediateResult<String> r = new ImmediateResult<String>("foo");
		Assert.assertTrue(r.isReady());
	}
}
