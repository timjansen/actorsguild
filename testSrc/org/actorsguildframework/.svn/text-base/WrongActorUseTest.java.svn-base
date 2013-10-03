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

import java.io.IOException;
import java.io.Serializable;

import junit.framework.Assert;

import org.actorsguildframework.annotations.Message;
import org.junit.Test;

/**
 * Tests wrong usage of actors.
 */
public class WrongActorUseTest {


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
	
	public static class BrokenSerializationActor extends Actor {
		@Message
		public AsyncResult<Void> testMessageWithBrokenSerialization(BrokenSerializer ser) {
System.err.println("Result is "+ser);
			return noResult();
		}
		@Message
		public AsyncResult<Void> testMessageWithBrokenDeserialization(BrokenDeserializer ser) {
System.err.println("Result is "+ser);
			return noResult();
		}
		@Message
		public AsyncResult<BrokenSerializer> testMessageWithBrokenSerialization() {
			return result(new BrokenSerializer());
		}
		@Message
		public AsyncResult<BrokenDeserializer> testMessageWithBrokenDeserialization() {
			return result(new BrokenDeserializer());
		}
	}
	
	public static interface ISomething {
	}
	
	public static class NonActorSomething implements ISomething {
	}

	public static class SomethingUser extends Actor {
		@Message
		public AsyncResult<Void> useSomething(ISomething something) {
			return noResult();
		}
	}

	public static class NullReturnActor extends Actor {
		@Message
		public AsyncResult<Void> testNullReturn() {
			return null;
		}
	}
	
	
	@Test(expected=ActorRuntimeException.class)
	public void testDirectConstruction() {
		new NullReturnActor();
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testBrokenSerialization() {
		DefaultAgent a = new DefaultAgent();
		a.create(BrokenSerializationActor.class).testMessageWithBrokenSerialization(new BrokenSerializer());
	}

	@Test(expected=WrappedException.class)
	public void testBrokenDeserialization() {
		DefaultAgent a = new DefaultAgent();
		a.create(BrokenSerializationActor.class).testMessageWithBrokenDeserialization(new BrokenDeserializer()).get();
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testBrokenSerializationReturnValue() throws Throwable {
		DefaultAgent a = new DefaultAgent();
		try {
			a.create(BrokenSerializationActor.class).testMessageWithBrokenSerialization().get();
		}
		catch (WrappedException e) {
			e.rethrow();
		}
	}

	@Test(expected=ActorRuntimeException.class)
	public void testBrokenDeserializationReturnValue() throws Throwable {
		DefaultAgent a = new DefaultAgent();
		try {
			a.create(BrokenSerializationActor.class).testMessageWithBrokenDeserialization().get();
		}
		catch (WrappedException e) {
			e.rethrow();
		}
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testNonActorInterface() {
		DefaultAgent a = new DefaultAgent();
		a.create(SomethingUser.class).useSomething(new NonActorSomething());
	}
	
	@Test(expected=ActorRuntimeException.class)
	public void testNullReturnActor() throws Throwable {
		DefaultAgent a = new DefaultAgent();
		try {
			a.create(NullReturnActor.class).testNullReturn().get();
		}
		catch (WrappedException e) {
			e.rethrow();
		}
	}

}
