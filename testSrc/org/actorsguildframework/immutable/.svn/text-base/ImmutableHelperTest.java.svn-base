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
import java.util.Vector;

import junit.framework.Assert;

import org.actorsguildframework.Actor;
import org.actorsguildframework.ActorRuntimeException;
import org.actorsguildframework.DefaultAgent;
import org.actorsguildframework.Immutable;
import org.junit.Test;

/**
 * Unit tests for immutable helper
 */
public class ImmutableHelperTest {
	enum ABC {A, B, c}
	
	public static class MyImmutable implements Immutable {
	}
	
	public static class MyActor extends Actor {
	}
	
	public static class MySerializable implements Serializable {
		int a;
	}
	
	public static class BrokenSerializer implements Serializable {
		 private void writeObject(java.io.ObjectOutputStream out)
		 	throws IOException {
			 throw new IOException("Test IOException");
		 }
		 private void readObject(java.io.ObjectInputStream in)
	     	throws IOException, ClassNotFoundException {
		 }
	}
	
	public static class BrokenDeserializer implements Serializable {
		 private void writeObject(java.io.ObjectOutputStream out)
		 	throws IOException {
		 }
		 private void readObject(java.io.ObjectInputStream in)
	     	throws IOException, ClassNotFoundException {
			 throw new IOException("Test IOException");
		 }
	}
	
	@Test
	public void testIsImmutableType() {
		
		Assert.assertTrue(ImmutableHelper.isImmutableType(String.class));
		Assert.assertTrue(ImmutableHelper.isImmutableType(Integer.class));
		Assert.assertTrue(ImmutableHelper.isImmutableType(Integer.TYPE));
		Assert.assertTrue(ImmutableHelper.isImmutableType(ABC.class));
		Assert.assertTrue(ImmutableHelper.isImmutableType(MyImmutable.class));
		Assert.assertFalse(ImmutableHelper.isImmutableType(Object.class));
		Assert.assertFalse(ImmutableHelper.isImmutableType(Serializable.class));
		Assert.assertFalse(ImmutableHelper.isImmutableType(Vector.class));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsImmutableTypeNull() {
		ImmutableHelper.isImmutableType(null);
	}
	
	@Test
	public void testHandleValue() {
		Assert.assertEquals("foobar", ImmutableHelper.handleValue("urg", "foobar"));
		Assert.assertEquals(5, ImmutableHelper.handleValue("urg", 5).intValue());
		Assert.assertNull(ImmutableHelper.handleValue("urg", null));
		
		MyImmutable mi = new MyImmutable();
		Assert.assertSame(mi, ImmutableHelper.handleValue("urg", mi));
		
		MySerializable ms = new MySerializable();
		ms.a = 11;
		MySerializable ms2 = ImmutableHelper.handleValue("urg", ms);
		Assert.assertNotSame(ms, ms2);
		Assert.assertEquals(ms.a, ms2.a);		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHandleValueNull() {
		ImmutableHelper.handleValue(null, null);
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testBrokenSerialization() {
		BrokenSerializer bs = new BrokenSerializer();
		ImmutableHelper.handleValue("a", bs);
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testBrokenDeserialization() {
		BrokenDeserializer bd = new BrokenDeserializer();
		ImmutableHelper.handleValue("a", bd);
	}
}
