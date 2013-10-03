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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.Assert;

import org.actorsguildframework.ActorRuntimeException;
import org.junit.Test;

public class StringListTest {
	
	@Test
	public void testArrayConstructor() {
		StringList l1 = new StringList("a", null, "b", "c");
		Assert.assertEquals(4, l1.size());
		Assert.assertEquals("a", l1.get(0));
		Assert.assertNull(l1.get(1));
		Assert.assertEquals("b", l1.get(2));
		Assert.assertEquals("c", l1.get(3));

		StringList l2 = new StringList();
		Assert.assertEquals(0, l2.size());
	}
	
	@Test
	public void testCollectionConstructor() {
		StringList l1 = new StringList(Arrays.asList("a", null, "b", "c"));
		Assert.assertEquals(4, l1.size());
		Assert.assertEquals("a", l1.get(0));
		Assert.assertNull(l1.get(1));
		Assert.assertEquals("b", l1.get(2));
		Assert.assertEquals("c", l1.get(3));
	}
	
	@Test
	public void testJoin() {
		StringList l1 = new StringList("a", null, "b", "c");
		Assert.assertEquals("anullbc", l1.join(null));
		Assert.assertEquals("anullbc", l1.join(""));
		Assert.assertEquals("a, null, b, c", l1.join(", "));
		
		StringList l2 = new StringList("a", "b", "c");
		Assert.assertEquals("abc", l2.join(null));
		Assert.assertEquals("abc", l2.join(""));
		Assert.assertEquals("a, b, c", l2.join(", "));
		
		StringList l3 = new StringList();
		Assert.assertEquals("", l3.join(null));
		Assert.assertEquals("", l3.join(""));
		Assert.assertEquals("", l3.join(", "));
	}
	
}
