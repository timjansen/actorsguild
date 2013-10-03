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
package org.actorsguildframework.internal.util;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the fast queue.
 */
public class FastQueueTest {
	Entry e1, e2, e3, e4;
	FastQueue<Entry> fq;
	
	/**
	 * Simple entry class.
	 */
	public static class Entry extends FastQueue.Entry {

	}
	
	/**
	 * Setting up Queue and Entries.
	 */
	@Before
	public void setUp() {
		fq = new FastQueue<Entry>();
		e1 = new Entry();
		e2 = new Entry();
		e3 = new Entry();
		e4 = new Entry();
	}
	
	/**
	 * Tests queue with one or no element.
	 */
	@Test
	public void testOneAndTwo() {
		Assert.assertEquals(0, fq.size());
		Assert.assertTrue(fq.isEmpty());
		
		fq.add(e1);
		Assert.assertEquals(1, fq.size());
		Assert.assertSame(e1, fq.peek());
		Assert.assertSame(e1, fq.getLast());
		Assert.assertSame(e1, fq.rotate());
		Assert.assertTrue(fq.isInQueue(e1));
		Assert.assertFalse(fq.isInQueue(e2));
		fq.remove(e1);
		Assert.assertEquals(0, fq.size());
		Assert.assertNull(fq.peek());
		Assert.assertNull(fq.getLast());
		Assert.assertTrue(fq.isEmpty());

		fq.add(e1);
		fq.add(e2);
		Assert.assertTrue(fq.isInQueue(e1));
		Assert.assertTrue(fq.isInQueue(e2));
		Assert.assertEquals(2, fq.size());
		Assert.assertSame(e2, fq.getLast());
		Assert.assertSame(e1, fq.peek());
		Assert.assertSame(e1, fq.pop());
		Assert.assertEquals(1, fq.size());
		Assert.assertSame(e2, fq.peek());
		Assert.assertSame(e2, fq.getLast());
		Assert.assertSame(e2, fq.pop());
		Assert.assertEquals(0, fq.size());
		Assert.assertNull(fq.peek());
		Assert.assertNull(fq.getLast());
		Assert.assertTrue(fq.isEmpty());
	}
	
	/**
	 * Test queue with 3 elements.
	 */
	@Test
	public void testThree() {
		fq.addFront(e1);
		fq.add(e2);
		fq.addFront(e3);
		Assert.assertTrue(fq.isInQueue(e1));
		Assert.assertTrue(fq.isInQueue(e2));
		Assert.assertTrue(fq.isInQueue(e3));
		Assert.assertEquals(3, fq.size());
		Assert.assertSame(e2, fq.getLast());
		Assert.assertSame(e3, fq.peek());
		Assert.assertSame(e3, fq.pop());
		Assert.assertSame(e1, fq.peek());
		Assert.assertSame(e1, fq.pop());
		Assert.assertSame(e2, fq.peek());
		Assert.assertSame(e2, fq.pop());
		Assert.assertEquals(0, fq.size());
		Assert.assertNull(fq.peek());
		Assert.assertNull(fq.getLast());
		Assert.assertTrue(fq.isEmpty());
	}
	
	/**
	 * Test Queue with 4 elements.
	 */
	@Test
	public void testFour() {
		fq.addFront(e1);
		fq.add(e2);
		fq.addFront(e3);
		fq.add(e4);
		Assert.assertTrue(fq.isInQueue(e4));
		Assert.assertEquals(4, fq.size());
		Assert.assertSame(e4, fq.getLast());
		Assert.assertSame(e3, fq.peek());
		Assert.assertSame(e3, fq.rotate());
		Assert.assertSame(e1, fq.rotate());
		Assert.assertSame(e2, fq.rotate());
		Assert.assertSame(e4, fq.rotate());
		
		fq.remove(e1);
		Assert.assertEquals(3, fq.size());
		Assert.assertSame(e3, fq.rotate());
		Assert.assertSame(e2, fq.rotate());
		Assert.assertSame(e4, fq.rotate());
		
		fq.remove(e4);
		Assert.assertEquals(2, fq.size());
		Assert.assertSame(e3, fq.rotate());
		Assert.assertSame(e2, fq.rotate());

		fq.remove(e3);
		Assert.assertEquals(1, fq.size());
		Assert.assertSame(e2, fq.rotate());
		
		fq.remove(e2);
		Assert.assertTrue(fq.isEmpty());
	}

	/**
	 * Tests iterator.
	 */
	@Test
	public void testIterator() {
		Assert.assertFalse(fq.iterator().hasNext());
		
		fq.add(e1);
		fq.add(e2);
		fq.add(e3);
		fq.add(e4);
		
		Iterator<Entry> it = fq.iterator();
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e1, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e2, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e3, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e4, it.next());
		Assert.assertFalse(it.hasNext());
		
		fq.remove(e4);
		it = fq.iterator();
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e1, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e2, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e3, it.next());
		Assert.assertFalse(it.hasNext());

		fq.pop();
		it = fq.iterator();
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e2, it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertSame(e3, it.next());
		Assert.assertFalse(it.hasNext());

		fq.remove(e2);
		it = fq.iterator();
		Assert.assertSame(e3, it.next());
		Assert.assertFalse(it.hasNext());

		fq.remove(e3);
		it = fq.iterator();
		Assert.assertFalse(it.hasNext());
	}
	
	@Test
	public void testClear() {
		fq.clear();
		Assert.assertTrue(fq.isEmpty());

		fq.addFront(e1);
		fq.addFront(e2);
		fq.addFront(e3);
		fq.clear();
		Assert.assertTrue(fq.isEmpty());

		fq.addFront(e1);
		fq.clear();
		Assert.assertTrue(fq.isEmpty());
	}
}
