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
package org.actorsguildframework.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.ImmediateResult;
import org.actorsguildframework.ImmediateResultTest;
import org.actorsguildframework.immutable.StringList;


/**
 */
public class ActorMock extends Actor implements ActorProxy{
	
	/**
	 * Initializer.
	 * @param x some value
	 * @return nothing
	 */
	public AsyncResult<String> init(int x) {
		return new ImmediateResult<String>(null);
	}
	
	/**
	 * Concatenate.
	 * @param x some value
	 * @param y 
	 * @return x
	 */
	public AsyncResult<String> cat(String x, String y) {
		return new ImmediateResult<String>(x+y);
	}
	
	/**
	 * Adds the two arguments, returns the result.
	 * @param a 
	 * @param b 
	 * @return the sum
	 */
	public AsyncResult<Integer> add(int a, int b) {
		return new ImmediateResult<Integer>(a+b);
	}
	
	/**
	 * Returns a string list with a single element.
	 * @param a 
	 * @param b 
	 * @return the list
	 */
	public AsyncResult<List<String>> oneElementList(String a) {
		ArrayList<String> r = new ArrayList<String>();
		r.add(a);
		return new ImmediateResult<List<String>>(r);
	}
	
	/**
	 * Returns a list with complex type.
	 * @return the list
	 */
	public AsyncResult<List<? extends Date>> listOfDates() {
		ArrayList<? extends Date> r = new ArrayList<Date>();
		return new ImmediateResult<List<? extends Date>>(r);
	}
	
	/**
	 * Simple method to include all primitive types as well as some reference types.
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 * @param g
	 * @param h
	 * @param o
	 * @param s
	 * @param i
	 * @return void
	 */
	public AsyncResult<String> all(boolean a, byte b, short c, char d, int e, long f, 
			float g, double h, Object o, String s, StringList i) {
		return new ImmediateResult<String>(""+a+b+c+d+e+f+g+h+o+s+i);
	}

	public ActorState getState__ACTORPROXYMETHOD() {
		// TODO Auto-generated method stub
		return null;
	}
}
