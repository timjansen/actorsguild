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
package org.actorsguildframework.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.actorsguildframework.Actor;
import org.actorsguildframework.Agent;

/**
 * The <code>Prop</code> annotation can be used on abstract getter methods 
 * to tell the Actors Guild framework generate the property accessor methods. 
 * <code>Prop</code> can be used in any {@link Actor} and any {@link Bean}. When you 
 * create such a class using {@link Agent#create(Class, org.actorsguildframework.Props)},
 * it will create an instance of a sub-class of your original class which implements 
 * the setter and getter methods. <code>create</code> also allows you to specify  
 * values for the properties.
 * <p>
 * For example, this code snippet defines a property that the framework will implement for you:
 * <pre>
 * @Prop
 * public abstract int getValue();
 * public abstract void setValue(int value);
 * </pre>
 * <p>
 * The @Prop annotation must be always in the front of the getter. The setter method is optional,
 * and you can define a read-only property by omitting it. Both methods must be abstract.
 * <p>
 * You can specify a default value for <code>Prop</code> properties using the
 * {@link DefaultValue} annotation. If no <code>DefaultValue</code> has been provided, 
 * it will take the type's default value (0 for numbers, null for references). For 
 * example, in the following example the default for a is 0 and for b is 1:
 * <code>
 * @DefaultValue("b")
 * static final int DEFAULT_B = 1;
 * 
 * @Prop
 * public abstract int getA();
 * @Prop
 * public abstract int getB();
 * </code>
 * <p>
 * Generated accessors will be thread-safe (synchronized) if {@link Bean#threadSafe()} is
 * set to true. 
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Prop {
}
