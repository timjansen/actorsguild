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
package org.actorsguildframework.test;

import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;

/**
 * Test Actor
 */
@Model(ConcurrencyModel.MultiThreaded)
public class TestActorMultiThreaded extends Actor {

	@Message
	public AsyncResult<Integer> add(int a, int b) {
		return result(a+b);
	}
	
	@Message
	public AsyncResult<String> cat(String a, String b) {
		return result(a+b);
	}
	
	@Message
	public AsyncResult<Integer> slowMultiThreadedAdd(int a, int b) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result(a+b);
	}
	
	@Message
	public AsyncResult<Integer> multiThreadedAdd(int a, int b) {
		return result(a+b);
	}
}
