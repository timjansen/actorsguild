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

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.Assert;

import org.actorsguildframework.ActorRuntimeException;
import org.junit.Test;

public class ImmutableListTest {
	public static class BrokenSerializer implements Serializable {
		 private void writeObject(java.io.ObjectOutputStream out)
		 	throws IOException {
			 throw new IOException("Test IOException");
		 }
		 private void readObject(java.io.ObjectInputStream in)
	     	throws IOException, ClassNotFoundException {
		 }
	}
	
	@Test
	public void testEquals() {
		ImmutableList<Integer> l1 = ImmutableList.create(2, 4, 1, 5, 6);
		ImmutableList<Integer> l2 = ImmutableList.create(5, 10, 522, 5);
		ImmutableList<Integer> l3 = ImmutableList.create(2, 4, 1, 5, 6, 9);
		ImmutableList<Integer> l4 = ImmutableList.create(2, 4, 1, 5, 6);
		ImmutableList<Integer> l5 = ImmutableList.create();
		ImmutableList<Integer> l6 = ImmutableList.create(2, 4, null, 5, 6);
		ImmutableList<Integer> l7 = ImmutableList.create(2, 4, null, 5, 6);
		
		Assert.assertTrue(l1.equals(l4));
		Assert.assertTrue(l1.equals(l1));
		Assert.assertTrue(l4.equals(l1));
		Assert.assertTrue(l6.equals(l7));
		Assert.assertFalse(l1.equals(l5));
		Assert.assertFalse(l5.equals(l1));
		Assert.assertFalse(l1.equals(l2));
		Assert.assertFalse(l1.equals(l3));
		Assert.assertFalse(l1.equals(l6));
		Assert.assertFalse(l2.equals(l1));
		Assert.assertFalse(l2.equals(l3));
		Assert.assertFalse(l2.equals(l4));
		Assert.assertFalse(l3.equals(l1));
		Assert.assertFalse(l3.equals(l2));
		Assert.assertFalse(l3.equals(l4));
		Assert.assertFalse(l1.equals(null));
		Assert.assertFalse(l5.equals(null));
		Assert.assertFalse(l6.equals(l1));
	}
	
	@Test
	public void testHashCode() {
		ImmutableList<Integer> l1 = ImmutableList.create(2, 4, 1, 5, 6);
		ImmutableList<Integer> l2 = ImmutableList.create(5, 10, null, null, null, 522, 5);
		
		Assert.assertTrue(l1.hashCode() != l2.hashCode());
	}
	
	@Test
	public void testArrayConstructor() {
		ImmutableList<Integer> l1 = new ImmutableList<Integer>(new Integer[]{1, null, 2, 3});
		Assert.assertEquals(4, l1.size());
		Assert.assertEquals(1, l1.get(0).intValue());
		Assert.assertNull(l1.get(1));
		Assert.assertEquals(2, l1.get(2).intValue());
		Assert.assertEquals(3, l1.get(3).intValue());

		ImmutableList<Integer> l2 = new ImmutableList<Integer>(new Integer[]{});
		Assert.assertEquals(0, l2.size());
	}
	
	@Test
	public void testArrayConstructorMutable() {
		ImmutableList<Date> l1 = new ImmutableList<Date>(new Date[]{new Date(215), null, new Date(551)});
		Assert.assertEquals(3, l1.size());
		Assert.assertEquals(215, l1.get(0).getTime());
		Assert.assertNull(l1.get(1));
		Assert.assertEquals(551, l1.get(2).getTime());
	}

	@Test
	public void testFactory() {
		ImmutableList<Integer> l1 = ImmutableList.create(1, null, 2, 3);
		Assert.assertEquals(4, l1.size());
		Assert.assertEquals(1, l1.get(0).intValue());
		Assert.assertNull(l1.get(1));
		Assert.assertEquals(2, l1.get(2).intValue());
		Assert.assertEquals(3, l1.get(3).intValue());

		ImmutableList<Integer> l2 = ImmutableList.create();
		Assert.assertEquals(0, l2.size());
	}
	
	@Test
	public void testFactoryMutable() {
		ImmutableList<Date> l1 = ImmutableList.create(new Date(215), null, new Date(551));
		Assert.assertEquals(3, l1.size());
		Assert.assertEquals(215, l1.get(0).getTime());
		Assert.assertNull(l1.get(1));
		Assert.assertEquals(551, l1.get(2).getTime());

	}
	
	@Test
	public void testCollectionConstructor() {
		ImmutableList<Integer> l1 = new ImmutableList<Integer>(Arrays.asList(1, null, 2, 3));
		Assert.assertEquals(4, l1.size());
		Assert.assertEquals(1, l1.get(0).intValue());
		Assert.assertNull(l1.get(1));
		Assert.assertEquals(2, l1.get(2).intValue());
		Assert.assertEquals(3, l1.get(3).intValue());

		ImmutableList<Integer> l2 = new ImmutableList<Integer>(Arrays.asList(new Integer[]{}));
		Assert.assertEquals(0, l2.size());
	}
	
	@Test
	public void testCollectionConstructorMutable() {
		ImmutableList<Date> l1 = new ImmutableList<Date>(Arrays.asList(new Date(215), null, new Date(551)));
		Assert.assertEquals(3, l1.size());
		Assert.assertEquals(215, l1.get(0).getTime());
		Assert.assertNull(l1.get(1));
		Assert.assertEquals(551, l1.get(2).getTime());
	}
	
	@Test
	public void testGetMutable() {
		Date d1 = new Date(215); 
		Date d2 = new Date(551);
		ImmutableList<Date> l1 = new ImmutableList<Date>(Arrays.asList(d1, d2));
		Assert.assertEquals(215, l1.get(0).getTime());
		Assert.assertNotSame(d1, l1.get(0));
		Assert.assertEquals(551, l1.get(1).getTime());
		Assert.assertNotSame(d2, l1.get(1));
	}
	
	@Test
	public void testContains() {
		ImmutableList<Integer> l1 = ImmutableList.create(1, null, 2, 3);
		Assert.assertFalse(l1.contains(0));
		Assert.assertTrue(l1.contains(null));
		Assert.assertTrue(l1.contains(1));
		Assert.assertTrue(l1.contains(2));
		Assert.assertTrue(l1.contains(3));

		ImmutableList<Integer> l2 = ImmutableList.create(1, 2, 3);
		Assert.assertFalse(l2.contains(null));
		
		ImmutableList<Integer> l3 = ImmutableList.create();
		Assert.assertFalse(l3.contains(0));
		Assert.assertFalse(l3.contains(null));
	}
	
	@Test
	public void testIsEmpty() {
		ImmutableList<Integer> l1 = ImmutableList.create(null, 1, 2, 3);
		Assert.assertFalse(l1.isEmpty());

		ImmutableList<Integer> l2 = ImmutableList.create();
		Assert.assertTrue(l2.isEmpty());
	}
	
	@Test
	public void testIterator() {
		ImmutableList<Integer> l1 = ImmutableList.create(1, null, 2, 3);
		Iterator<Integer> it = l1.iterator(); 
		Assert.assertTrue(it.hasNext());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(1, it.next().intValue());
		Assert.assertTrue(it.hasNext());
		Assert.assertNull(it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(2, it.next().intValue());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(3, it.next().intValue());
		Assert.assertFalse(it.hasNext());
		Assert.assertFalse(it.hasNext());

		ImmutableList<Integer> l2 = ImmutableList.create();
		Iterator<Integer> it2 = l2.iterator(); 
		Assert.assertFalse(it2.hasNext());
		Assert.assertFalse(it2.hasNext());
	}
	
	@Test
	public void testIteratorMutable() {
		ImmutableList<Date> l1 = ImmutableList.create(new Date(215), null, new Date(551));
		Iterator<Date> it = l1.iterator(); 
		Assert.assertTrue(it.hasNext());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(215, it.next().getTime());
		Assert.assertTrue(it.hasNext());
		Assert.assertNull(it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(551, it.next().getTime());
		Assert.assertFalse(it.hasNext());
		Assert.assertFalse(it.hasNext());
		
		Iterator<Date> it2 = l1.iterator(); 
		Assert.assertEquals(215, it2.next().getTime());
		Assert.assertNull(it2.next());
		Assert.assertEquals(551, it2.next().getTime());
		Assert.assertFalse(it2.hasNext());
		
		ImmutableList<Serializable> l3 = new ImmutableList(new Serializable[] {new Date(215), "bla"});
		Iterator<Serializable> it3 = l3.iterator();
		Assert.assertEquals(215, ((Date)it3.next()).getTime());
		Assert.assertEquals("bla", it3.next());
		Assert.assertFalse(it3.hasNext());
	}

	@Test
	public void testSubList() {
		ImmutableList<Integer> l1 = ImmutableList.create();
		ImmutableList<Integer> l3 = ImmutableList.create(2, 4, 1, 5, 6, 9);
		ImmutableList<Integer> l6 = ImmutableList.create(2, 4, null, 5, 6);
		
		Assert.assertTrue(l1.subList(0, 0).equals(ImmutableList.create()));
		Assert.assertTrue(l3.subList(0, 6).equals(ImmutableList.create(2, 4, 1, 5, 6, 9)));
		Assert.assertTrue(l3.subList(0, 1).equals(ImmutableList.create(2)));
		Assert.assertTrue(l3.subList(0, 3).equals(ImmutableList.create(2, 4, 1)));
		Assert.assertTrue(l3.subList(1, 3).equals(ImmutableList.create(4, 1)));
		Assert.assertTrue(l3.subList(3, 6).equals(ImmutableList.create(5, 6, 9)));
		Assert.assertTrue(l3.subList(5, 6).equals(ImmutableList.create(9)));
		Assert.assertTrue(l3.subList(0, 0).equals(ImmutableList.create()));
		Assert.assertTrue(l3.subList(5, 5).equals(ImmutableList.create()));
		Assert.assertTrue(l3.subList(6, 6).equals(ImmutableList.create()));

		Assert.assertTrue(l6.subList(1, 4).equals(ImmutableList.create(4, null, 5)));

	}
	
	@Test
	public void testSubListIterator() {
		ImmutableList<Integer> l = ImmutableList.create(9, 9, 1, 2, 3, 9);
		Iterator<Integer> it = l.subList(2, 5).iterator();
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Integer(1), it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Integer(2), it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Integer(3), it.next());
		Assert.assertFalse(it.hasNext());
	}
	
	@Test
	public void testToArray() {
		ImmutableList<Integer> l1 = ImmutableList.create();
		ImmutableList<Integer> l2 = ImmutableList.create(2, 4, null, 5, 6);
		
		Assert.assertEquals(0, l1.toArray().length);
		
		Object r[] = l2.toArray();
		Assert.assertEquals(5, r.length);
		Assert.assertTrue(((Integer)r[0]) == 2);
		Assert.assertTrue(((Integer)r[1]) == 4);
		Assert.assertNull(r[2]);
		Assert.assertTrue(((Integer)r[3]) == 5);
		Assert.assertTrue(((Integer)r[4]) == 6);
	}
	
	@Test
	public void testToArrayMutable() {
		Date d1 = new Date(215);
		Date d2 = new Date(551222);
		ImmutableList<Date> l2 = ImmutableList.create(d1, null, d2);
		
		Object r[] = l2.toArray();
		Assert.assertEquals(3, r.length);
		Assert.assertNotSame(d1, r[0]);
		Assert.assertEquals(d1, r[0]);
		Assert.assertNull(r[1]);
		Assert.assertNotSame(d2, r[2]);
		Assert.assertEquals(d2, r[2]);
	}
	
	@Test
	public void testToArrayTyped() {
		ImmutableList<Integer> l1 = ImmutableList.create();
		ImmutableList<Integer> l2 = ImmutableList.create(2, 4, null, 5, 6);
		
		Assert.assertEquals(0, l1.toArray(new Integer[0]).length);
		Assert.assertEquals(2, l1.toArray(new Integer[2]).length);
		
		Integer r[] = l2.toArray(new Integer[5]);
		Assert.assertEquals(5, r.length);
		Assert.assertTrue(r[0] == 2);
		Assert.assertTrue(r[1] == 4);
		Assert.assertNull(r[2]);
		Assert.assertTrue(r[3] == 5);
		Assert.assertTrue(r[4] == 6);
		
		Integer r2[] = l2.toArray(new Integer[0]);
		Assert.assertEquals(5, r2.length);
		Assert.assertTrue(r2[0] == 2);
		Assert.assertTrue(r2[1] == 4);
		Assert.assertNull(r2[2]);
		Assert.assertTrue(r2[3] == 5);
		Assert.assertTrue(r2[4] == 6);
		
		Integer r3[] = l2.toArray(new Integer[7]);
		Assert.assertEquals(7, r3.length);
		Assert.assertTrue(r3[0] == 2);
		Assert.assertTrue(r3[1] == 4);
		Assert.assertNull(r3[2]);
		Assert.assertTrue(r3[3] == 5);
		Assert.assertTrue(r3[4] == 6);
		Assert.assertNull(r3[5]);
		Assert.assertNull(r3[6]);
	}
	
	@Test
	public void testToArrayTypedMutable() {
		Date d1 = new Date(215);
		Date d2 = new Date(551222);
		ImmutableList<Date> l2 = ImmutableList.create(d1, null, d2);
		
		Date r[] = l2.toArray(new Date[3]);
		Assert.assertEquals(3, r.length);
		Assert.assertNotSame(d1, r[0]);
		Assert.assertEquals(d1, r[0]);
		Assert.assertNull(r[1]);
		Assert.assertNotSame(d2, r[2]);
		Assert.assertEquals(d2, r[2]);
		
		Date r2[] = l2.toArray(new Date[0]);
		Assert.assertEquals(3, r.length);
		Assert.assertNotSame(d1, r2[0]);
		Assert.assertEquals(d1, r2[0]);
		Assert.assertNull(r2[1]);
		Assert.assertNotSame(d2, r2[2]);
		Assert.assertEquals(d2, r2[2]);
		
		Date r3[] = l2.toArray(new Date[5]);
		Assert.assertEquals(3, r.length);
		Assert.assertNotSame(d1, r3[0]);
		Assert.assertEquals(d1, r3[0]);
		Assert.assertNull(r3[1]);
		Assert.assertNotSame(d2, r3[2]);
		Assert.assertEquals(d2, r3[2]);
		Assert.assertNull(r3[3]);
		Assert.assertNull(r3[4]);
	}
	
	@Test
	public void testToList() {
		ImmutableList<Integer> l1 = ImmutableList.create();
		ImmutableList<Integer> l2 = ImmutableList.create(2, 4, null, 5, 6);
		
		Assert.assertEquals(0, l1.toList().size());
		
		List<Integer> r = l2.toList();
		Assert.assertEquals(5, r.size());
		Assert.assertTrue(r.get(0) == 2);
		Assert.assertTrue(r.get(1) == 4);
		Assert.assertNull(r.get(2));
		Assert.assertTrue(r.get(3) == 5);
		Assert.assertTrue(r.get(4) == 6);
	}
	
	@Test
	public void testToListMutable() {
		Date d1 = new Date(215);
		Date d2 = new Date(551222);
		ImmutableList<Date> l2 = ImmutableList.create(d1, null, d2);
		
		List<Date> r = l2.toList();
		Assert.assertEquals(3, r.size());
		Assert.assertNotSame(d1, r.get(0));
		Assert.assertEquals(d1, r.get(0));
		Assert.assertNull(r.get(1));
		Assert.assertNotSame(d2, r.get(2));
		Assert.assertEquals(d2, r.get(2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCtorArrayNullArg() {
		new ImmutableList((Object[])null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCtorCollectionNullArg() {
		new ImmutableList((List<Object>)null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateNullArg() {
		ImmutableList.create(null);
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIteratorEOL() {
		ImmutableList.create().iterator().next();
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIteratorMutableEOL() {
		Iterator it = ImmutableList.create(new Date(1)).iterator();
		it.next();
		it.next();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testIteratorRemove() {
		ImmutableList.create(2, 1, 3).iterator().remove();
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetEmpty() {
		ImmutableList.create().get(0);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetNegative() {
		ImmutableList.create(1, 2, 3).get(-1);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetTooHigh() {
		ImmutableList.create(1, 2, 3).get(3);
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testGetCopyFailed() {
		ImmutableList.create(new BrokenSerializer()).get(0);
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testToArrayBroken() {
		ImmutableList.create(new BrokenSerializer()).toArray();
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testToArrayBrokenWithArg() {
		ImmutableList.create(new BrokenSerializer()).toArray(new BrokenSerializer[2]);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testToArrayNull() {
		ImmutableList.create(1).toArray(null);
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testToListBroken() {
		ImmutableList.create(new BrokenSerializer()).toList();
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testSubListTooLow() {
		ImmutableList.create(1, 2, 3).subList(-1, 2);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testSubListTooHigh() {
		ImmutableList.create(1, 2, 3).subList(0, 4);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testSubListWrongOrder() {
		ImmutableList.create(1, 2, 3).subList(2, 1);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSubListEmpty() {
		ImmutableList.create().subList(1, 0);
	}
}
