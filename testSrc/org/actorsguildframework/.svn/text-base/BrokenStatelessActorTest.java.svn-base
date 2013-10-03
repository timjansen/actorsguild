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

import java.util.List;

import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;
import org.actorsguildframework.annotations.Prop;
import org.junit.Test;

/**
 * Tests various broken actors.
 */
public class BrokenStatelessActorTest {
	@Model(ConcurrencyModel.Stateless)
	public static class NonFinalFieldActor extends Actor {
		private int a;
		
		@Message public AsyncResult<Void> dummyMessage() { return noResult(); }
	}

	@Model(ConcurrencyModel.Stateless)
	public static class NonImmutableFieldActor extends Actor {
		private final List<?> a = null;
		
		@Message public AsyncResult<Void> dummyMessage() { return noResult(); }
	}
	
	@Model(ConcurrencyModel.Stateless)
	public static abstract class NonImmutablePropActor extends Actor {
		@Prop abstract Object getA();
		
		@Message public AsyncResult<Void> dummyMessage() { return noResult(); }
	}
	
	@Model(ConcurrencyModel.Stateless)
	public static abstract class ReadWritePropActor extends Actor {
		@Prop abstract int getA();
		abstract void setA(int a);
		
		@Message public AsyncResult<Void> dummyMessage() { return noResult(); }
	}

		
	@Model(ConcurrencyModel.Stateless)
	public static class InheritingNonFinalFieldActor extends NonFinalFieldActor {
		@Message public AsyncResult<Void> dummyMessage2() { return noResult(); }
	}
	
	@Model(ConcurrencyModel.Stateless)
	public static abstract class InheritingReadWritePropActor extends ReadWritePropActor {
		@Message public AsyncResult<Void> dummyMessage2() { return noResult(); }
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonFinalField() {
		DefaultAgent a = new DefaultAgent();
		a.create(NonFinalFieldActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonImmutableField() {
		DefaultAgent a = new DefaultAgent();
		a.create(NonImmutableFieldActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonImmutableProp() {
		DefaultAgent a = new DefaultAgent();
		a.create(NonImmutablePropActor.class);
	}

	@Test(expected=ConfigurationException.class)
	public void testReadWriteProp() {
		DefaultAgent a = new DefaultAgent();
		a.create(ReadWritePropActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testInheritingNonFinalField() {
		DefaultAgent a = new DefaultAgent();
		a.create(InheritingNonFinalFieldActor.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testInheritingReadWriteProp() {
		DefaultAgent a = new DefaultAgent();
		a.create(InheritingReadWritePropActor.class);
	}
}
