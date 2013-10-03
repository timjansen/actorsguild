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
package org.actorsguildframework.test;

import java.util.ArrayList;

import junit.framework.Assert;

import org.actorsguildframework.DefaultAgent;
import org.actorsguildframework.AsyncResult;
import org.junit.Before;
import org.junit.Test;

/**
 */
public class MultiThreadedTests {
	DefaultAgent.Configuration conf = new DefaultAgent.Configuration();
	
	/**
	 * Sets up logging.
	 */
	@Before
	public void setUp() {
		conf.setLoggingActions(true);
	}
	
	/**
	 * Simple queuing test.
	 */
	@Test
	public void testQueuing() {
		DefaultAgent a = new DefaultAgent(conf);
		TestActorMultiThreaded t = a.create(TestActorMultiThreaded.class);
		
		ArrayList<AsyncResult<Integer>> results = new ArrayList<AsyncResult<Integer>>();
		for (int i = 0; i < 20; i++)
			results.add(t.add(i, i*3+1));
		for (AsyncResult<Integer> r: results) {
			r.await();
		}
		for (int i = 0; i < 20; i++)
			Assert.assertEquals(4*i+1, results.get(i).get().intValue());
	}
	
	/**
	 * Simple multi-actor test.
	 */
	@Test
	public void testManyActors() {
		DefaultAgent a = new DefaultAgent(conf);
		TestActorMultiThreaded t[] = new TestActorMultiThreaded[20];
		for (int i = 0; i < t.length; i++)
			t[i] = a.create(TestActorMultiThreaded.class);
		
		ArrayList<AsyncResult<Integer>> results = new ArrayList<AsyncResult<Integer>>();
		for (int i = 0; i < t.length; i++)
			results.add(t[i].add(i, i*3+1));
		for (AsyncResult<Integer> r: results)
			r.await();
		for (int i = 0; i < t.length; i++)
			Assert.assertEquals(4*i+1, results.get(i).get().intValue());
	}
	
	/**
	 * Simple multi-threaded
	 */
	@Test
	public void testMultiThreaded() {
		DefaultAgent a = new DefaultAgent(conf);
		TestActorMultiThreaded t = a.create(TestActorMultiThreaded.class);
		
		ArrayList<AsyncResult<Integer>> results = new ArrayList<AsyncResult<Integer>>();
		for (int i = 0; i < 20; i++)
			results.add(t.multiThreadedAdd(i, i*3+1));
		for (AsyncResult<Integer> r: results) {
			r.await();
		}
		for (int i = 0; i < 20; i++)
			Assert.assertEquals(4*i+1, results.get(i).get().intValue());
	}
	
	/**
	 * Simple multi-threaded slow method.
	 */
	@Test
	public void testMultiThreadedSlow() {
		DefaultAgent a = new DefaultAgent(conf);
		TestActorMultiThreaded t = a.create(TestActorMultiThreaded.class);
		
		ArrayList<AsyncResult<Integer>> results = new ArrayList<AsyncResult<Integer>>();
		for (int i = 0; i < 10; i++)
			results.add(t.slowMultiThreadedAdd(i, i*3+1));
		for (AsyncResult<Integer> r: results) {
			r.await();
		}
		for (int i = 0; i < 10; i++)
			Assert.assertEquals(4*i+1, results.get(i).get().intValue());
	}
}
