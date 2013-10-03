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
package org.actorsguildframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Usage declares the way the message spends its time when it is not waiting for other actors.
 * This is useful for the framework so it can allocate an appropriate amount of threads.
 * The default is CpuBound, thus the framework assumes that you are actually doing something
 * in the message implementation while you are not waiting for another Actor. 
 * 
 * However, if your application spends its time waiting for I/O operations or some external
 * event, it must set the appropriate ThreadUsage, or the framework will not be able to allocate
 * enough threads.
 * 
 * The threadUsage can be set for both @Message and @Initializer. {@link ThreadUsage#CpuBound}
 * is the default.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Usage {

	/**
	 * The ThreadUsage value.
	 * @return the ThreadUsage. 
	 */
	ThreadUsage value() default ThreadUsage.CpuBound;
}
