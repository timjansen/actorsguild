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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.Assert;

import org.actorsguildframework.ActorRuntimeException;
import org.junit.Test;

public class ImmutableSetTest {
	
	@Test
	public void testEquals() {
		ImmutableSet<Integer> l1 = ImmutableSet.create(2, 4, 1, 5, 6);
		ImmutableSet<Integer> l2 = ImmutableSet.create(5, 10, 522, 5, 1, 2);
		ImmutableSet<Integer> l3 = ImmutableSet.create(2, 4, 1, 5, 6, 9);
		ImmutableSet<Integer> l4 = ImmutableSet.create(2, 4, 1, 5, 6);
		ImmutableSet<Integer> l5 = ImmutableSet.create();
		ImmutableSet<Integer> l6 = ImmutableSet.create(2, 2, 4, 4, 1, 1, 5, 5, 6, 6);
		ImmutableSet<Integer> l7 = ImmutableSet.create(2, 4, null, 5, 6);
		
		Assert.assertTrue(l1.equals(l4));
		Assert.assertTrue(l1.equals(l1));
		Assert.assertTrue(l4.equals(l1));
		Assert.assertFalse(l6.equals(l7));
		Assert.assertFalse(l1.equals(l5));
		Assert.assertFalse(l5.equals(l1));
		Assert.assertFalse(l1.equals(l2));
		Assert.assertFalse(l1.equals(l3));
		Assert.assertTrue(l1.equals(l6));
		Assert.assertFalse(l2.equals(l1));
		Assert.assertFalse(l2.equals(l3));
		Assert.assertFalse(l2.equals(l4));
		Assert.assertFalse(l3.equals(l1));
		Assert.assertFalse(l3.equals(l2));
		Assert.assertFalse(l3.equals(l4));
		Assert.assertFalse(l1.equals(null));
		Assert.assertFalse(l5.equals(null));
		Assert.assertTrue(l6.equals(l1));
	}
	
	@Test
	public void testHashCode() {
		ImmutableSet<Integer> l1 = ImmutableSet.create(2, 4, 1, 5, 6);
		ImmutableSet<Integer> l2 = ImmutableSet.create(5, 10, null, null, null, 522, 5);
		
		Assert.assertTrue(l1.hashCode() != l2.hashCode());
	}

	private <T> void compareSets(Set<T> s, ImmutableSet<T> l) {
		Assert.assertEquals(s.size(), l.size());
		Iterator<T> it = l.iterator();
		while (it.hasNext())
			Assert.assertTrue(s.remove(it.next()));
		Assert.assertFalse(it.hasNext());
	}
	
	@Test
	public void testConstructor() {
		HashSet<Integer> s = new HashSet<Integer>();
		s.addAll(Arrays.asList(215, null, 551));
		
		ImmutableSet<Integer> l1 = new ImmutableSet<Integer>(s);
		compareSets(s, l1);
		
		ImmutableSet<Integer> l2 = new ImmutableSet<Integer>(new ArrayList<Integer>());
		Assert.assertEquals(0, l2.size());
		Assert.assertTrue(l2.isEmpty());
	}
	
	@Test
	public void testConstructorMutable() {
		HashSet<Date> s = new HashSet<Date>();
		s.addAll(Arrays.asList(new Date(215), null, new Date(551)));
		
		ImmutableSet<Date> l1 = new ImmutableSet<Date>(s);
		compareSets(s, l1);
	}



	@Test
	public void testFactory() {
		ImmutableSet<Integer> l1 = ImmutableSet.create(1, null, 2, 3);
		Assert.assertEquals(4, l1.size());
		
		HashSet<Integer> s = new HashSet<Integer>();
		s.addAll(Arrays.asList(1, null, 2, 3));
		compareSets(s, l1);

		ImmutableSet<Integer> l2 = ImmutableSet.create();
		Assert.assertEquals(0, l2.size());
		Assert.assertTrue(l2.isEmpty());
	}
	
	@Test
	public void testFactoryMutable() {
		ImmutableSet<Date> l1 = ImmutableSet.create(new Date(215), null, new Date(551));
		HashSet<Date> s = new HashSet<Date>();
		s.addAll(Arrays.asList(new Date(215), null, new Date(551)));
		compareSets(s, l1);

	}
	
	@Test
	public void testContains() {
		ImmutableSet<Integer> l1 = ImmutableSet.create(1, null, 2, 3, 2);
		Assert.assertFalse(l1.contains(0));
		Assert.assertTrue(l1.contains(null));
		Assert.assertTrue(l1.contains(1));
		Assert.assertTrue(l1.contains(2));
		Assert.assertTrue(l1.contains(3));

		ImmutableSet<Integer> l2 = ImmutableSet.create(1, 2, 3);
		Assert.assertFalse(l2.contains(null));
		
		ImmutableSet<Integer> l3 = ImmutableSet.create();
		Assert.assertFalse(l3.contains(0));
		Assert.assertFalse(l3.contains(null));
	}
	
	@Test
	public void testIsEmpty() {
		ImmutableSet<Integer> l1 = ImmutableSet.create(null, 1, 2, 3);
		Assert.assertFalse(l1.isEmpty());

		ImmutableSet<Integer> l2 = ImmutableSet.create();
		Assert.assertTrue(l2.isEmpty());
	}
	
	@Test
	public void testIterator() {
		ImmutableSet<Integer> l1 = ImmutableSet.create(1, null, 2, 3);
		
		HashSet<Integer> s = new HashSet<Integer>();
		s.addAll(Arrays.asList(1, null, 2, 3));
		compareSets(s, l1);
		
		ImmutableSet<Integer> l2 = ImmutableSet.create();
		Iterator<Integer> it2 = l2.iterator(); 
		Assert.assertFalse(it2.hasNext());
		Assert.assertFalse(it2.hasNext());
	}
	
	@Test
	public void testIteratorMutable() {
		ImmutableSet<Date> l1 = ImmutableSet.create(new Date(215), null, new Date(551));
		HashSet<Date> s = new HashSet<Date>();
		s.addAll(Arrays.asList(new Date(215), null, new Date(551)));
		compareSets((HashSet<Date>)s.clone(), l1);
		compareSets((HashSet<Date>)s.clone(), l1);
		
		HashSet<Serializable> s2 = new HashSet<Serializable>();
		s2.addAll(Arrays.asList(new Serializable[] {new Date(215), "bla", null}));
		ImmutableSet<Serializable> l3 = new ImmutableSet(s2);
		compareSets(s2, l3);
	}


	@Test
	public void testToSet() {
		ImmutableSet<Integer> l1 = ImmutableSet.create();
		ImmutableSet<Integer> l2 = ImmutableSet.create(2, 4, null, 5, 6);
		
		Assert.assertEquals(0, l1.toSet().size());
		compareSets(l1.toSet(), l1);
		compareSets(l2.toSet(), l2); 
	}
	
	@Test
	public void testToSetMutable() {
		Date d1 = new Date(215);
		Date d2 = new Date(551222);
		ImmutableSet<Date> l2 = ImmutableSet.create(d1, null, d2);
		
		compareSets(l2.toSet(), l2);
		for (Date d: l2)
			if (d1.equals(d))
				Assert.assertNotSame(d, d1);
			else if (d2.equals(d))
				Assert.assertNotSame(d, d2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCtorCollectionNullArg() {
		new ImmutableSet((List<Object>)null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateNullArg() {
		ImmutableSet.create(null);
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIteratorEOL() {
		ImmutableSet.create().iterator().next();
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIteratorMutableEOL() {
		Iterator it = ImmutableSet.create(new Date(1)).iterator();
		it.next();
		it.next();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testIteratorRemove() {
		ImmutableSet.create(2, 1, 3).iterator().remove();
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testToSetBroken() {
		ImmutableSet.create(new ImmutableListTest.BrokenSerializer()).toSet();
	}
}
