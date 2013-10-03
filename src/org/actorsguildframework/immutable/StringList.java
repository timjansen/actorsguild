/*
 *    Copyright 2008 Tim Jansen
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

import java.util.Collection;

/**
 * StringList is an immutable list of Strings. It offers a much higher performance in the construction
 * phase than the regular ImmutableList and some additional functions.
 */
public final class StringList extends ImmutableList<String> {
	/**
	 * Creates a new StringList that contains the given elements. The list will be copied.
	 * @param elements the list content (will be copied)
	 */
	public StringList(String... elements) {
		super(elements, true, 0, elements.length, true);
	}
	
	/**
	 * Creates a new StringList that contains the given elements. The Collection will be copied.
	 * @param elements the list content (will be copied)
	 */
	public StringList(Collection<String> elements) {
		super(elements.toArray(), false, 0, elements.size(), true);
	}


	/**
	 * Concatenates the strings of the list into a single string, possibly with a separator. 
	 * Any occurrence of null strings in the list will be replaced by the string "null".
	 * @param separator s separator between the strings, or null for no separator
	 * @return the resulting string
	 */
	public String join(String separator) {
		String s = (separator == null) ? "" : separator;
		StringBuilder sb = new StringBuilder();
		int len = size();
		if (len > 0)
			sb.append(get(0));
		for (int i = 1; i < len; i++) {
			sb.append(s);
			sb.append(get(i));
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return join(",");
	}
	
	private static final long serialVersionUID = 6519499910566196329L;
}
