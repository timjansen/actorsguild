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
package org.actorsguildframework.immutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.actorsguildframework.ActorRuntimeException;
import org.junit.Assert;
import org.junit.Test;

public class FreezableListTest {
	@Test
	public void testBasics() {
		FreezableList<String> l  = new FreezableList<String>(String.class);
		Assert.assertEquals(0, l.size());
		Assert.assertTrue(l.isEmpty());
		l.add("foo");
		Assert.assertEquals(1, l.size());
		Assert.assertFalse(l.isEmpty());
		l.add("bar");
		Assert.assertEquals(2, l.size());
		Assert.assertEquals("foo", l.get(0));
		Assert.assertEquals("bar", l.get(1));
		
		FreezableList<Integer> x = FreezableList.create(1, 2, 3);
		Assert.assertEquals(3, x.size());
		Assert.assertEquals(new Integer(1), x.get(0));
		Assert.assertEquals(new Integer(2), x.get(1));
		Assert.assertEquals(new Integer(3), x.get(2));
	}
	
	@Test
	public void testIterator() {
		FreezableList<Integer> x = FreezableList.create(1, 2, 3);
		Iterator<Integer> it = x.iterator();
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Integer(1), it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Integer(2), it.next());
		it.remove();
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Integer(3), it.next());
		Assert.assertEquals(2, x.size());
	}

	/**
	 * Tests get() with pos > 0.
	 */
	@Test
	public void testGetWithPos() {
		FreezableList<Integer> x = FreezableList.create(1, 2, 3);
		x.remove(0);
		Assert.assertEquals(new Integer(2), x.get(0));
		Assert.assertEquals(new Integer(3), x.get(1));
	}
	
	@Test
	public void testAddIndex() {
		FreezableList<String> l  = new FreezableList<String>(String.class);
		l.add(0, "foo");
		Assert.assertEquals(1, l.size());
		Assert.assertEquals("foo", l.get(0));

		l.add(1, "bar");
		Assert.assertEquals("bar", l.get(1));
		Assert.assertEquals(2, l.size());
		
		l.add(0, "bla");
		Assert.assertEquals(3, l.size());
		Assert.assertEquals("bla", l.get(0));
		Assert.assertEquals("foo", l.get(1));
		Assert.assertEquals("bar", l.get(2));
		
		l.add(1, "blub");
		Assert.assertEquals(4, l.size());
		Assert.assertEquals("bla", l.get(0));
		Assert.assertEquals("blub", l.get(1));
		Assert.assertEquals("foo", l.get(2));
		Assert.assertEquals("bar", l.get(3));
		
		l.remove(0);       // test special cases, pos>0
		l.add(0, "blab");
		Assert.assertEquals(4, l.size());
		Assert.assertEquals("blab", l.get(0));
		Assert.assertEquals("blub", l.get(1));
		
		l.remove(0);
		l.add(3, "xxx");
		Assert.assertEquals(4, l.size());
		Assert.assertEquals("blub", l.get(0));
		Assert.assertEquals("foo", l.get(1));
		Assert.assertEquals("bar", l.get(2));
		Assert.assertEquals("xxx", l.get(3));
		
		l.remove(0);
		l.add(2, "yyy");
		Assert.assertEquals(4, l.size());
		Assert.assertEquals("foo", l.get(0));
		Assert.assertEquals("bar", l.get(1));
		Assert.assertEquals("yyy", l.get(2));
		Assert.assertEquals("xxx", l.get(3));
	}
	
	private <T> void compare(FreezableList<T> list, T... content) {
		Assert.assertEquals(content.length, list.size());
		for (int i = 0; i < content.length; i++)
			Assert.assertEquals(content[i], list.get(i));
	}
	
	@Test
	public void testAddAllIndex() {
		FreezableList<String> l = FreezableList.create("tick", "trick", "track");
		Assert.assertFalse(l.addAll(0, Collections.<String>emptyList()));
		Assert.assertFalse(l.addAll(1, Collections.<String>emptyList()));
		Assert.assertFalse(l.addAll(3, Collections.<String>emptyList()));
		compare(l, "tick", "trick", "track");
		
		Assert.assertTrue(l.addAll(1, Arrays.asList("bli", "bla")));
		compare(l, "tick", "bli", "bla", "trick", "track");
		
		Assert.assertTrue(l.addAll(0, Arrays.asList("bar", "foo")));
		compare(l, "bar", "foo", "tick", "bli", "bla", "trick", "track");

		Assert.assertTrue(l.addAll(7, Arrays.asList("xxx")));
		compare(l, "bar", "foo", "tick", "bli", "bla", "trick", "track", "xxx");
		
		l.remove(0);
		l.remove(0);
		l.remove(0);
		Assert.assertTrue(l.addAll(0, Arrays.asList("bar", "foo")));
		compare(l, "bar", "foo", "bli", "bla", "trick", "track", "xxx");
		
		Assert.assertTrue(l.addAll(0, Arrays.asList("yyy")));
		compare(l, "yyy", "bar", "foo", "bli", "bla", "trick", "track", "xxx");
		
		l.remove(0);
		Assert.assertTrue(l.addAll(1, Arrays.asList("aaa")));
		compare(l, "bar", "aaa", "foo", "bli", "bla", "trick", "track", "xxx");
		
		l.remove(0);
		l.remove(0);
		l.remove(0);
		Assert.assertTrue(l.addAll(5, Arrays.asList("bbb")));
		compare(l, "bli", "bla", "trick", "track", "xxx", "bbb");
		
		Assert.assertTrue(l.addAll(0, Arrays.asList("a", "b", "c", "d", "e", "f", "g")));
		compare(l, "a", "b", "c", "d", "e", "f", "g", "bli", "bla", "trick", "track", "xxx", "bbb");
	}
	
	@Test
	public void testClear() {
		FreezableList<String> l = FreezableList.create("tick", "trick", "track");
		l.clear();
		Assert.assertTrue(l.isEmpty());
		
		FreezableList<String> l2 = new FreezableList<String>(String.class, 10000);
		l2.clear();
		Assert.assertTrue(l2.isEmpty());
	}
	
	@Test
	public void testRemove() {
		FreezableList<String> l = FreezableList.create("tick", "trick", "track");
		Assert.assertEquals("track", l.remove(2));
		compare(l, "tick", "trick");
		Assert.assertEquals("trick", l.remove(1));
		compare(l, "tick");
		Assert.assertEquals("tick", l.remove(0));
		compare(l);
		
		FreezableList<String> l2 = FreezableList.create("tick", "trick", "track");
		Assert.assertEquals("trick", l2.remove(1));
		compare(l2, "tick", "track");
		Assert.assertEquals("track", l2.remove(1));
		compare(l2, "tick");
		Assert.assertEquals("tick", l2.remove(0));
		compare(l2);

		FreezableList<String> l3 = FreezableList.create("a", "b", "c", "d", "e", "f");
		Assert.assertEquals("a", l3.remove(0));
		compare(l3, "b", "c", "d", "e", "f");
		Assert.assertEquals("b", l3.remove(0));
		compare(l3, "c", "d", "e", "f");
		Assert.assertEquals("e", l3.remove(2));
		compare(l3, "c", "d", "f");
		Assert.assertEquals("f", l3.remove(2));
		compare(l3, "c", "d");
		Assert.assertEquals("c", l3.remove(0));
		compare(l3, "d");
		
		FreezableList<String> l4 = FreezableList.create("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
		Assert.assertEquals("a", l4.remove(0));
		compare(l4, "b", "c", "d", "e", "f", "g", "h", "i", "j");
		Assert.assertEquals("b", l4.remove(0));
		compare(l4, "c", "d", "e", "f", "g", "h", "i", "j");
		Assert.assertEquals("c", l4.remove(0));
		compare(l4, "d", "e", "f", "g", "h", "i", "j");
		Assert.assertEquals("d", l4.remove(0));
		compare(l4, "e", "f", "g", "h", "i", "j");
		Assert.assertEquals("e", l4.remove(0));
		compare(l4, "f", "g", "h", "i", "j");
		Assert.assertEquals("f", l4.remove(0));
		compare(l4, "g", "h", "i", "j");
		Assert.assertEquals("g", l4.remove(0));
		compare(l4, "h", "i", "j");
		Assert.assertEquals("h", l4.remove(0));
		compare(l4, "i", "j");
		Assert.assertEquals("i", l4.remove(0));
		compare(l4, "j");
		Assert.assertEquals("j", l4.remove(0));
		compare(l4);
	}
	
	@Test
	public void testAddAllImmutable() {
		FreezableList<String> l = FreezableList.create("tick", "trick", "track");
		l.addAll(ImmutableList.<String>create());
		compare(l, "tick", "trick", "track");
		
		l.addAll(1, ImmutableList.create("bli", "bla"));
		compare(l, "tick", "bli", "bla", "trick", "track");
		
		l.addAll(0, ImmutableList.create("bar", "foo"));
		compare(l, "bar", "foo", "tick", "bli", "bla", "trick", "track");

		l.addAll(7, ImmutableList.create("xxx"));
		compare(l, "bar", "foo", "tick", "bli", "bla", "trick", "track", "xxx");
		
		l.addAll(ImmutableList.create("yyy"));
		compare(l, "bar", "foo", "tick", "bli", "bla", "trick", "track", "xxx", "yyy");
		l.remove(8);
		
		l.remove(0);
		l.remove(0);
		l.remove(0);
		l.addAll(0, ImmutableList.create("bar", "foo"));
		compare(l, "bar", "foo", "bli", "bla", "trick", "track", "xxx");
		
		l.addAll(0, ImmutableList.create("yyy"));
		compare(l, "yyy", "bar", "foo", "bli", "bla", "trick", "track", "xxx");
		
		l.remove(0);
		l.addAll(1, ImmutableList.create("aaa"));
		compare(l, "bar", "aaa", "foo", "bli", "bla", "trick", "track", "xxx");
		
		l.remove(0);
		l.remove(0);
		l.remove(0);
		l.addAll(5, ImmutableList.create("bbb"));
		compare(l, "bli", "bla", "trick", "track", "xxx", "bbb");
		
		l.addAll(0, ImmutableList.create("a", "b", "c", "d", "e", "f", "g"));
		compare(l, "a", "b", "c", "d", "e", "f", "g", "bli", "bla", "trick", "track", "xxx", "bbb");
	}
	
	@Test
	public void testSetWithPos() {
		FreezableList<String> x = FreezableList.create("0", "a", "b", "c");
		x.remove(0);
		compare(x, "a", "b", "c");
		x.set(1, "1");
		compare(x, "a", "1", "c");
		x.set(0, "0");
		compare(x, "0", "1", "c");
		x.set(2, "2");
		compare(x, "0", "1", "2");
	}
	
	@Test
	public void testToArray() {
		FreezableList<String> x = FreezableList.create("0", "a", "b", "c");
		x.remove(0);

		Object[] a = x.toArray();
		Assert.assertEquals(3, a.length);
		Assert.assertEquals("a", a[0]);
		Assert.assertEquals("b", a[1]);
		Assert.assertEquals("c", a[2]);
	}
	
	@Test
	public void testToArrayWithArg() {
		FreezableList<String> x = FreezableList.create("0", "a", "b", "c");
		x.remove(0);

		String[] a = x.toArray(new String[0]);
		Assert.assertEquals(3, a.length);
		Assert.assertEquals("a", a[0]);
		Assert.assertEquals("b", a[1]);
		Assert.assertEquals("c", a[2]);

		String[] b = x.toArray(new String[3]);
		Assert.assertEquals(3, b.length);
		Assert.assertEquals("a", b[0]);
		Assert.assertEquals("b", b[1]);
		Assert.assertEquals("c", b[2]);

		String[] s = new String[10];
		Arrays.fill(s, "buh");
		String[] c = x.toArray(s);
		Assert.assertEquals(10, c.length);
		Assert.assertEquals("a", c[0]);
		Assert.assertEquals("b", c[1]);
		Assert.assertEquals("c", c[2]);
		Assert.assertNull(c[3]);
		Assert.assertEquals("buh", c[4]);
	}
	
	@Test
	public void testCapacityAdjustment1() {
		FreezableList<String> l = new FreezableList<String>(String.class, 0);
		for (int i = 0; i < 2500; i++) {
			l.add(Integer.toString(i));
			Assert.assertEquals(i+1, l.size());
			Assert.assertEquals(Integer.toString(i), l.get(i));
			Assert.assertEquals("0", l.get(0));
		}
		
		for (int i = 0; i < 2500; i++)
			Assert.assertEquals(Integer.toString(i), l.get(i));
		
		for (int i = 0; i < 2500; i++) {
			Assert.assertEquals("2499", l.get(l.size()-1));
			Assert.assertEquals(Integer.toString(i), l.remove(0));
			Assert.assertEquals(2499-i, l.size());
		}
	}
	
	/**
	 * Same as testCapacityAdjustment1(), but with pos=1 from the beginning.
	 */
	@Test
	public void testCapacityAdjustment2() {
		FreezableList<String> l = FreezableList.create("X", "0");
		l.remove(0);
		for (int i = 1; i < 2500; i++) {
			l.add(Integer.toString(i));
			Assert.assertEquals(i+1, l.size());
			Assert.assertEquals(Integer.toString(i), l.get(i));
			Assert.assertEquals("0", l.get(0));
		}
		
		for (int i = 0; i < 2500; i++)
			Assert.assertEquals(Integer.toString(i), l.get(i));
		
		for (int i = 0; i < 2500; i++) {
			Assert.assertEquals("2499", l.get(l.size()-1));
			Assert.assertEquals(Integer.toString(i), l.remove(0));
			Assert.assertEquals(2499-i, l.size());
		}
	}
		
	@Test
	public void testCapacityAdjustment3() {
		FreezableList<String> l = FreezableList.create("X", "250");
		l.remove(0);
		for (int i = 251; i < 625; i++) {
			l.add(l.size(), Integer.toString(i));
			Assert.assertEquals(i+1-250, l.size());
			Assert.assertEquals(Integer.toString(i), l.get(i-250));
			Assert.assertEquals("250", l.get(0));
		}
		for (int i = 249; i >= 0; i--) {
			l.add(0, Integer.toString(i));
			Assert.assertEquals(Integer.toString(i), l.get(0));
		}
		for (int i = 625+1250; i < 2500; i++) {
			l.add(l.size(), Integer.toString(i));
			Assert.assertEquals(i+1-1250, l.size());
			Assert.assertEquals("0", l.get(0));
		}
		
		for (int i = 0; i < 625; i++)
			Assert.assertEquals(Integer.toString(i), l.get(i));
		for (int i = 625; i < 1250; i++)
			Assert.assertEquals(Integer.toString(i+1250), l.get(i));
		
		for (int i = 625; i < 625+1250; i++) {
			l.add(i, Integer.toString(i));
			Assert.assertEquals("0", l.get(0));
			Assert.assertEquals("2499", l.get(l.size()-1));
		}

		for (int i = 0; i < 2500; i++)
			Assert.assertEquals(Integer.toString(i), l.get(i));

		for (int i = 0; i < 2500; i++) {
			Assert.assertEquals("0", l.get(0));
			Assert.assertEquals(Integer.toString(2499-i), l.remove(l.size()-1));
			Assert.assertEquals(2499-i, l.size());
		}
	}
	
	@Test
	public void testCapacityAdjustment4() {
		FreezableList<String> l = FreezableList.create("X", "500");
		l.remove(0);

		ArrayList<String> a = new ArrayList<String>();
		for (int i = 501; i < 1000; i++)
			a.add(Integer.toString(i));
		l.addAll(1, a);
		
		a.clear();
		for (int i = 0; i < 500; i++)
			a.add(Integer.toString(i));
		l.addAll(0, a);

		a.clear();
		for (int i = 1500; i < 2000; i++)
			a.add(Integer.toString(i));
		l.addAll(1000, a);

		a.clear();
		for (int i = 2000; i < 2500; i++)
			a.add(Integer.toString(i));
		l.addAll(a);

		a.clear();
		for (int i = 1000; i < 1500; i++)
			a.add(Integer.toString(i));
		l.addAll(1000, a);

		for (int i = 0; i < 2500; i++)
			Assert.assertEquals(Integer.toString(i), l.get(i));

		ArrayList<String> reference = new ArrayList<String>();
		reference.addAll(l);
		for (int i = 2; i < 2500; i++) {
			Assert.assertEquals("0", l.get(0));
			Assert.assertEquals("2499", l.get(l.size()-1));
			Assert.assertEquals(reference.remove(l.size()/2), l.remove(l.size()/2));
			Assert.assertEquals(2499+2-i, l.size());
		}
	}
	
	@Test
	public void testFreeze1() {
		FreezableList<String> l = FreezableList.create("a", "b", "c");
		ImmutableList<String> il = l.freeze();
		Assert.assertSame(il, l.freeze());
		Assert.assertEquals(l.size(), il.size());
		for (int i = 0; i < l.size(); i++)
			Assert.assertEquals(l.get(i), il.get(i));
	}

	@Test
	public void testFreeze2() {
		FreezableList<String> l = FreezableList.create();
		ImmutableList<String> il = l.freeze();
		Assert.assertSame(il, l.freeze());
		Assert.assertEquals(l.size(), il.size());
	}

	@Test
	public void testFreeze3() {
		FreezableList<Date> l = FreezableList.create(new Date(1), new Date(222222222));
		ImmutableList<Date> il = l.freeze();
		Assert.assertSame(il, l.freeze());
		Assert.assertEquals(l.size(), il.size());
		for (int i = 0; i < l.size(); i++) {
			Assert.assertEquals(l.get(i), il.get(i));
			Assert.assertNotSame(l.get(i), il.get(i));
		}
	}

	@Test(expected=ActorRuntimeException.class)
	public void testFreezeNonSerializable() {
		FreezableList<Runnable> l = FreezableList.create(new Runnable[] {new Runnable(){ public void run() {} }});
		l.freeze();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testAddFrozen() {
		FreezableList<String> l = FreezableList.create("a");
		l.freeze();
		l.add("b");
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testAddFrozen2() {
		FreezableList<String> l = FreezableList.create("a");
		l.freeze();
		l.add(0, "b");
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testClearFrozen() {
		FreezableList<String> l = FreezableList.create("a");
		l.freeze();
		l.clear();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testRemoveFrozen() {
		FreezableList<String> l = FreezableList.create("a");
		l.freeze();
		l.remove(0);
	}
}
