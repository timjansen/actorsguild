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

import org.actorsguildframework.annotations.Bean;
import org.actorsguildframework.annotations.DefaultValue;
import org.actorsguildframework.annotations.Initializer;
import org.actorsguildframework.annotations.Prop;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BrokenBeanTest {
	DefaultAgent da;
	
	public static interface MyInterface {
		
	}
	
	public static class MyClass {
		
	}
	
	@Bean(threadSafe=false)
	public static final class FinalBean {
	}
	
	@Bean(threadSafe=false)
	public class InnerClassBean {
	}
	
	@Bean(threadSafe=false)
	public static class CtorBean {
		public CtorBean(int a) {}
	}
	
	@Bean(threadSafe=false)
	public static class PrivateCtorBean {
		private PrivateCtorBean() {}
	}
	
	@Bean(threadSafe=false)
	public static abstract class PrivateDefaultBean {
		@DefaultValue("a")
		private static final int A = 1;
		@Prop public abstract int getA();
	}
	
	@Bean(threadSafe=false)
	public static abstract class UnspecifiedDefaultBean {
		@DefaultValue("")
		private static final int A = 1;
		@Prop public abstract int getA();
	}
	
	@Bean(threadSafe=false)
	public static abstract class DuplicateDefaultBean {
		@DefaultValue("a")
		private static final int A = 1;
		@DefaultValue("a")
		private static final int B = 1;
		@Prop public abstract int getA();
	}
	
	@Bean(threadSafe=false)
	public static abstract class NonFinalDefaultBean {
		@DefaultValue("a")
		private static int A = 1;
		@Prop public abstract int getA();
	}
	
	@Bean(threadSafe=false)
	public static abstract class NonStaticDefaultBean {
		@DefaultValue("a")
		private final int A = 1;
		@Prop public abstract int getA();
	}
	
	@Bean(threadSafe=false)
	public static abstract class WrongDefaultBean {
		@DefaultValue("b")
		private final int B = 1;
		@Prop public abstract int getA();
	}
	
	@Bean(threadSafe=false)
	public static abstract class NonGetterPropBean {
		@Prop public abstract void a(int a);
	}
	
	@Bean(threadSafe=false)
	public static abstract class StaticGetterPropBean {
		@Prop public static int getA() { return 0;}
	}
	
	@Bean(threadSafe=false)
	public static abstract class ArgumentGetterPropBean {
		@Prop public abstract int getA(int a);
	}
	
	@Bean(threadSafe=false)
	public static abstract class ExceptionGetterPropBean {
		@Prop public abstract int getA() throws java.io.IOException;
	}
	
	@Bean(threadSafe=false)
	public static abstract class AccessorMismatchBean {
		@Prop public abstract int getA();
		public abstract void setA(String a);
	}
	
	@Bean(threadSafe=false)
	public static abstract class AbstractMethodBean {
		public abstract int baaaa();
	}
	
	@Bean(threadSafe=false)
	public static abstract class AbstractGetterBean {
		public abstract int getA();
	}
	
	@Bean(threadSafe=false)
	public static abstract class AbstractSetterBean {
		public abstract void setA(String a);
	}
	
	@Bean(threadSafe=false)
	public static abstract class SetterWithWrongDefaultBean {
		@DefaultValue("a")
		static final List<String> A = null;
		public void setA(String a) {}
	}
	
	@Bean(threadSafe=false)
	public static abstract class SetterWithExceptionBean {
		@DefaultValue("a")
		static final String A = null;
		public void setA(String a) throws Exception {}
	}
	
	@Bean(threadSafe=false)
	public static abstract class GetterWithWrongDefaultBean {
		@DefaultValue("a")
		static final List<String> A = null;
		public String getA() {return null;}
		public void setA(String a) {}
	}
	
	@Bean(threadSafe=false)
	public static abstract class NonAbstractPropGetterBean {
		@Prop public int getA() {return 0;}
	}
	
	@Bean(threadSafe=false)
	public static abstract class StaticSetterBean {
		@Prop public abstract int getA();
		public static void setA(int a) {}
	}
	
	@Bean(threadSafe=false)
	public static abstract class NonVoidSetterBean {
		@Prop public abstract int getA();
		public abstract int setA(int a);
	}
	
	@Bean(threadSafe=false)
	public static abstract class ExceptionSetterBean {
		@Prop public abstract int getA();
		public abstract void setA(int a) throws Exception;
	}
	
	@Bean(threadSafe=false)
	public static abstract class NonAbstractSetterWithPropBean {
		@Prop public abstract int getA();
		public void setA(int a) {};
	}
	
	@Bean(threadSafe=false)
	public static abstract class NonAbstractSetterWithGetterBean {
		public int getA() {return 0;}
		public abstract void setA(int a);
	}
	
	@Bean(threadSafe=false)
	public static abstract class TestBean {
		public static int getA() { return 0; }
		public static void setA(int a) { }
		
		@Prop public abstract int getB();
		@Prop public abstract String getC();
		
		private int getD() { return 0; }
		private void setD(int a) { }
	}
	
	@Bean(threadSafe=false)
	public static class WrongReturnTypeInitializerBean {
		@Initializer
		public int init() {
			return 0;
		}
	}
	
	@Bean(threadSafe=false)
	public static class WrongArgumentInitializerBean {
		@Initializer
		public void init(Object o) {
		}
	}
	
	@Before
	public void before() {
		da = new DefaultAgent();
	}
	
	@After
	public void after() {
		da.shutdown();
	}
	
	@Test(expected=ConfigurationException.class)
	public void testInterfaceAsBean() {
		da.create(MyInterface.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testClassAsBean() {
		da.create(MyClass.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testFinalBean() {
		da.create(FinalBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testInnerClassBean() {
		da.create(InnerClassBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testConstructorBean() {
		da.create(CtorBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testPrivateConstructorBean() {
		da.create(PrivateCtorBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testPrivateDefaultBean() {
		da.create(PrivateDefaultBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testUnspecifiedDefaultBean() {
		da.create(UnspecifiedDefaultBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testDuplicateDefaultBean() {
		da.create(DuplicateDefaultBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonFinalDefaultBean() {
		da.create(NonFinalDefaultBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonStaticDefaultBean() {
		da.create(NonStaticDefaultBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testWrongDefaultBean() {
		da.create(WrongDefaultBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonGetterPropBean() {
		da.create(NonGetterPropBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testStaticGetterPropBean() {
		da.create(StaticGetterPropBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testArgumentGetterPropBean() {
		da.create(ArgumentGetterPropBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testExceptionGetterPropBean() {
		da.create(ExceptionGetterPropBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testAccessorMismatchBean() {
		da.create(AccessorMismatchBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testAbstractMethodBean() {
		da.create(AbstractMethodBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testAbstractGetterBean() {
		da.create(AbstractGetterBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testAbstractSetterBean() {
		da.create(AbstractSetterBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testSetterWithWrongDefaultBean() {
		da.create(SetterWithWrongDefaultBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testSetterWithExceptionBean() {
		da.create(SetterWithExceptionBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testGetterWithWrongDefaultBean() {
		da.create(GetterWithWrongDefaultBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonAbstractPropGetterBean() {
		da.create(NonAbstractPropGetterBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testStaticSetterBean() {
		da.create(StaticSetterBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonVoidSetterBean() {
		da.create(NonVoidSetterBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testExceptionSetterBean() {
		da.create(ExceptionSetterBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonAbstractSetterWithPropBean() {
		da.create(NonAbstractSetterWithPropBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNonAbstractSetterWithGetterBean() {
		da.create(NonAbstractSetterWithGetterBean.class);
	}
	
	@Test(expected=ConfigurationException.class)
	public void testWrongReturnTypeInitializerBean() {
		da.create(WrongReturnTypeInitializerBean.class);
	}

	@Test(expected=ConfigurationException.class)
	public void testWrongArgumentInitializerBean() {
		da.create(WrongArgumentInitializerBean.class);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNonPropertySetting() {
		da.create(TestBean.class, new Props("a", 0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNonPropertySetting2() {
		da.create(TestBean.class, new Props("d", 0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongTypeSetting1() {
		da.create(TestBean.class, new Props("b", "haha"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWrongTypeSetting2() {
		da.create(TestBean.class, new Props("c", 32));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWrongTypeSetting3() {
		da.create(TestBean.class, new Props("c", false));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWrongTypeSetting4() {
		da.create(TestBean.class, new Props("b", false));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongTypeSetting5() {
		da.create(TestBean.class, new Props("b", null));
	}

}
