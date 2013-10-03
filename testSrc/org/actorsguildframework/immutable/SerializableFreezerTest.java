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

import java.util.Arrays;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests the SerializableFreezer.
 */
public class SerializableFreezerTest {
	@Test
	public void testFreezing() {
		Date d = new Date (555);
		SerializableFreezer<Date> f1 = SerializableFreezer.freeze(d);
		Date dc1 = f1.get();
		Date dc2 = f1.get();
		
		Assert.assertNotSame(d, dc1);
		Assert.assertNotSame(dc1, dc2);
		
		Assert.assertEquals(d, dc1);
		Assert.assertEquals(d, dc2);
	}
	
	@Test
	public void testArray() {
		Object[] a = new Object[] { 24, new Date(2), "abc", Arrays.asList(1, 2, 3) };
		SerializableFreezer<Object[]> f1 = SerializableFreezer.freeze(a);
		Object[] ac1 = f1.get();
		Object[] ac2 = f1.get();

		Assert.assertTrue(Arrays.deepEquals(a, ac1));
		Assert.assertTrue(Arrays.deepEquals(a, ac2));
		
		Assert.assertNotSame(a, ac1);
		Assert.assertNotSame(ac1, ac2);

		Assert.assertNotSame(a[1], ac1[1]);
		Assert.assertNotSame(ac1[1], ac2[1]);
		
		Assert.assertNotSame(a[3], ac1[3]);
		Assert.assertNotSame(ac1[3], ac2[3]);
	}
}
