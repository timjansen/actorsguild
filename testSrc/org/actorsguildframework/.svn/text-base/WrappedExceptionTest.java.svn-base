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

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit tests for WrappedException.
 */
public class WrappedExceptionTest {
	@Test(expected=IOException.class)
	public void testRethrow() throws Throwable {
		IOException ie = new IOException("foo");
		WrappedException we = new WrappedException("bar", ie);
		Assert.assertSame(ie, we.getCause());
		we.rethrow();
	}

	@Test(expected=IOException.class)
	public void testRethrowIf() throws Exception {
		IOException ie = new IOException("foo");
		WrappedException we = new WrappedException("bar", ie);
		we.rethrowIf(IndexOutOfBoundsException.class);
		we.rethrowIf(IOException.class);
	}
	
	@Test
	public void testIsWrapping() throws Exception {
		IOException ie = new IOException("foo");
		WrappedException we = new WrappedException("bar", ie);
		Assert.assertFalse(we.isWrapping(IndexOutOfBoundsException.class));
		Assert.assertTrue(we.isWrapping(IOException.class));
	}

}
