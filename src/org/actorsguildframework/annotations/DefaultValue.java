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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.actorsguildframework.Agent;

/**
 * Declares that the annotated static final field contains the default value for a
 * property (which may be either {@link Prop} annotated or a user-written property).
 * This annotation is used by {@link Agent#create(Class)} and 
 * {@link Agent#create(Class, org.actorsguildframework.Props)} to set the default value
 * for those properties for which no value was specified.
 * <p> 
 * The field that contains the default value must always be both 'static' and 
 * 'final'. You can either make it 'public', 'protected' or use the default
 * accessibility (package). The latter is recommended if you do not wish the 
 * value to appear in API documentation. 'private' is not possible.
 * <p>
 * Example:
 * <pre>@Bean
 * class MyBean {
 *     @DefaultValue("percentage")
 *     final static int DEFAULT_PERCENTAGE = 100;
 *     
 *     @Prop
 *     public abstract int getPercentage();
 *     public abstract int setPercentage();
 * }
 * </pre>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DefaultValue {
	/**
	 * The name of the property whose default value this field sets.
	 * @return the name of the property
	 */
	String value();
}
