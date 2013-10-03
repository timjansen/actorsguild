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

import org.actorsguildframework.annotations.Message;
import org.junit.Test;

/**
 * Tests various broken actors.
 */
public class BrokenActorTest {
	public static class FinalMessageActor extends Actor {
		@Message
		public final AsyncResult<Void> finalMessage() {
			// do nothing
			return null;
		}
	}

	public static class WrongReturnTypeActor extends Actor {
		@Message
		public String wrongReturnTypeMessage() {
			// do nothing
			return null;
		}
	}
	
	public static class PrivateMessageActor extends Actor {
		@Message
		private AsyncResult<Void> wrongVisibilityMessage() {
			// do nothing
			return null;
		}
	}
	
	public static class NoMessageAsyncResultAcync extends Actor {
		public AsyncResult<Void> notDeclaredAsMessage() {
			return noResult();
		}	
	}
	
	public static class IllegalArgClass {
		public int a, b;
	}

	public static class WrongArgumentActor extends Actor {
		@Message
		public AsyncResult<Void> wrongArgumentMessage(IllegalArgClass v) {
			// do nothing
			return null;
		}
	}
	
	private static class PrivateActor extends Actor {
	}

	public static final class FinalActor extends Actor {
	}

	public static abstract class AbstractActor extends Actor {
	}
	
	public static class PrivateCtorActor extends Actor {
		private PrivateCtorActor() {
		}
	}
	
	public static class NoDefaultCtorActor extends Actor {
		public NoDefaultCtorActor(int a) {
		}
	}
	
	public class NonStaticInnerActor extends Actor {
	}
	
	public static class NonThreadSafeMethodActor extends Actor {
		public void a() {}
	}
	
	@Test(expected=ConfigurationException.class)
	public void testFinalMessage() {
		DefaultAgent a = new DefaultAgent();
		a.create(FinalMessageActor.class);
		System.out.println(FinalMessageActor.class.getName());
	}
	
	@Test(expected=ConfigurationException.class)
	public void testWrongReturnTypeActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(WrongReturnTypeActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testPrivateMessageActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(PrivateMessageActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testAsyncResultReturnWithoutMessage() {
		DefaultAgent a = new DefaultAgent();
		a.create(NoMessageAsyncResultAcync.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testWrongArgumentActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(WrongArgumentActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testPrivateConstructorActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(PrivateActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testFinalActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(FinalActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testPrivateCtorActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(PrivateCtorActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNoDefaultCtorActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(NoDefaultCtorActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonStaticInnerActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(NonStaticInnerActor.class);
	}

	@Test(expected=ConfigurationException.class)
	public void testNonThreadSafeMethodActor() {
		DefaultAgent a = new DefaultAgent();
		a.create(NonThreadSafeMethodActor.class);
	}
}
