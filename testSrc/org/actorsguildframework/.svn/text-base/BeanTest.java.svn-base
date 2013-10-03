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
package org.actorsguildframework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.actorsguildframework.annotations.Bean;
import org.actorsguildframework.annotations.DefaultValue;
import org.actorsguildframework.annotations.Initializer;
import org.actorsguildframework.annotations.Prop;
import org.junit.Test;

/**
 * Unit tests for Beans.
 */
public class BeanTest {
	@Bean(threadSafe=false)
	public static class EmptyBean {
	}

	@Bean(threadSafe=false)
	public static class EmptyAnnotatedBean {
	}
	
	@Bean(threadSafe=false)
	public static class SimplePropertyBean {
		private int a;
		public void setA(int a) { this.a = a; }
		public int getA() { return a; }
	}
	
	@Bean(threadSafe=false)
	public static abstract class SimpleGeneratedPropertyBean {
		@Prop
		public abstract int getA();
		public abstract void setA(int a);
	}
	
	@Bean(threadSafe=false)
	public static class SetterOnlyBean {
		private int a;
		public void setA(int a) { this.a = a; }
		public int giveMeA() { return a; }
	}
	
	@Bean(threadSafe=false)
	public static abstract class ComplicatedNamesBean {
		// a* properties: user-defined
		private int aAA, aaa, aeaeae, _Aaa, _aaa, _aAA;
		public void setAAA(int aAA) { this.aAA = aAA; }
		public int getAAA() { return aAA; }
		public void setAaa(int aaa) { this.aaa = aaa; }
		public int getAaa() { return aaa; }
		public void setAeAeAe(int aeaeae) { this.aeaeae = aeaeae; }
		public int getAeAeAe() { return aeaeae; }
		public void set_aaa(int _aaa) { this._aaa = _aaa; }
		public int get_aaa() { return _aaa; }
		public void set_Aaa(int _Aaa) { this._Aaa = _Aaa; }
		public int get_Aaa() { return _Aaa; }
		public void set_AAA(int _aAA) { this._aAA = _aAA; }
		public int get_AAA() { return _aAA; }

		// b* properties: setter-only
		private int bBB, bbb, bebebe, _bbb, _Bbb, _bBB;
		public void setBBB(int bBB) { this.bBB = bBB; }
		public int giveBBB() { return bBB; }
		public void setBbb(int bbb) { this.bbb = bbb; }
		public int giveBbb() { return bbb; }
		public void setBeBeBe(int bebebe) { this.bebebe = bebebe; }
		public int giveBeBeBe() { return bebebe; }
		public void set_bbb(int _bbb) { this._bbb = _bbb; }
		public int give_bbb() { return _bbb; }
		public void set_Bbb(int _Bbb) { this._Bbb = _Bbb; }
		public int give_Bbb() { return _Bbb; }
		public void set_BBB(int _bBB) { this._bBB = _bBB; }
		public int give_BBB() { return _bBB; }
		
		// c* properties: @Prop annotations (partially no setter)
		@Prop public abstract int getCCC();
		public abstract void setCCC(int c);
		@Prop public abstract int getCcc();
		@Prop public abstract int getCeCeCe();
		public abstract void setCeCeCe(int c);
		@Prop public abstract int get_Ccc();
		@Prop public abstract int get_ccc();
		public abstract void set_ccc(int c);
		@Prop public abstract int get_CCC();
	}
	
	@Bean(threadSafe=false)
	public static abstract class VariousTypesBean {
		int a, a2;
		long b, b2;
		byte c, c2;
		String d, d2;
		char e, e2;
		float f, f2;
		double g, g2;
		short h, h2;
		List<String> i, i2;
		boolean j, j2;
		Object o, o2;
		public void setA(int a) { this.a = a; }
		public int getA() { return a; }
		public void setA2(int a) { this.a2 = a; }
		public void setB(long b) { this.b = b; }
		public long getB() { return b; }
		public void setB2(long b) { this.b2 = b; }
		public void setC(byte c) { this.c = c; }
		public byte getC() { return c; }
		public void setC2(byte c) { this.c2 = c; }
		public void setD(String d) { this.d = d; }
		public String getD() { return d; }
		public void setD2(String d) { this.d2 = d; }
		public void setE(char e) { this.e = e; }
		public char getE() { return e; }
		public void setE2(char e) { this.e2 = e; }
		public void setF(float f) { this.f = f; }
		public float getF() { return f; }
		public void setF2(float f) { this.f2 = f; }
		public void setG(double g) { this.g = g; }
		public double getG() { return g; }
		public void setG2(double g) { this.g2 = g; }
		public void setH(short h) { this.h = h; }
		public short getH() { return h; }
		public void setH2(short h) { this.h2 = h; }
		public void setI(List<String> i) { this.i = i; }
		public List<String> getI() { return i; }
		public void setI2(List<String> i) { this.i2 = i; }
		public void setJ(boolean j) { this.j = j; }
		public boolean getJ() { return j; }
		public void setJ2(boolean j) { this.j2 = j; }
		public void setO(Object o) { this.o = o; }
		public Object getO() { return o; }
		public void setO2(Object o) { this.o2 = o; }
		
		@Prop public abstract int getA3();
		public abstract void setA3(int a);
		@Prop public abstract long getB3();
		@Prop public abstract byte getC3();
		public abstract void setC3(byte c);
		public abstract void setD3(String d);
		@Prop public abstract String getD3();
		@Prop public abstract char getE3();
		public abstract void setE3(char e);
		@Prop public abstract float getF3();
		@Prop public abstract double getG3();
		public abstract void setG3(double g);
		@Prop public abstract short getH3();
		public abstract void setH3(short h);
		@Prop public abstract List<String> getI3();
		public abstract void setI3(List<String> i);
		@Prop public abstract boolean getJ3();
		@Prop public abstract Object getO3();

	}
	
	@Bean(threadSafe=false)
	public static abstract class DefaultValueBean {
		int a;
		byte c;
		String d;
		double g;
		List<String> i;
		boolean j;
		
		@DefaultValue("a")
		final static int A = 5;
		@DefaultValue("b")
		final static long B = 50l;
		@DefaultValue("c")
		final static byte C = -5;
		@DefaultValue("d")
		final static String D = "eek";
		@DefaultValue("e")
		final static char E = 'e';
		@DefaultValue("f")
		final static float F = 54.2f;
		@DefaultValue("g")
		final static double G = -15.7;
		@DefaultValue("i")
		final static List<String> I = Arrays.asList("a", "b", "c");
		@DefaultValue("j")
		final static boolean J = true;
		@DefaultValue("o")
		final static String O = "boo";
		
		public void setA(int a) { this.a = a; }
		public int getA() { return a; }
		@Prop public abstract long getB();
		public void setC(byte c) { this.c = c; }
		public void setD(String d) { this.d = d; }
		public String getD() { return d; }
		@Prop public abstract char getE();
		public abstract void setE(char e);
		@Prop public abstract float getF();
		public void setG(double g) { this.g = g; }
		public double getG() { return g; }
		public void setI(List<String> i) { this.i = i; }
		public List<String> getI() { return i; }
		public void setJ(boolean j) { this.j = j; }
		public boolean getJ() { return j; }
		@Prop public abstract Object getO();
	}
	
	
	@Bean(threadSafe=true)
	public static abstract class SynchronizingBean {
		@DefaultValue("a")
		final static int A = 5;
		@DefaultValue("b")
		final static long B = 50l;
		@DefaultValue("c")
		final static byte C = -5;
		@DefaultValue("d")
		final static String D = "eek";

		@Prop public abstract int getA();
		public abstract void setA(int a);
		@Prop public abstract long getB();
		@Prop public abstract byte getC();
		public abstract void setC(byte c);
		@Prop public abstract String getD();
		public abstract void setD(String d);
	}
	
	@Bean(threadSafe=false)
	public static abstract class InitBaseBean {
		int initNum = 1;
		public int a, b;
		
		@Initializer
		protected void initA() {
			a = initNum++;
		}

		@Initializer
		public void initB() {
			b = initNum++;
		}
	}
	
	public static abstract class SuperBaseBean extends InitBaseBean {
		public int c, d;
		
		@Initializer
		void initC() {
			c = initNum++;
		}

		@Initializer
		protected void initD() {
			d = initNum++;
		}
	}
	
	@Bean(threadSafe=false)
	public static class ExceptionBean {
		@Initializer
		void init() throws Exception {
			throw new Exception();
		}
	}
	
	@Test
	public void testSimpleBeanInstatiation() {
		DefaultAgent ag = new DefaultAgent();
		EmptyBean eb = ag.create(EmptyBean.class);
		Assert.assertTrue(eb instanceof EmptyBean);
		EmptyAnnotatedBean eab = ag.create(EmptyAnnotatedBean.class);
		Assert.assertTrue(eab instanceof EmptyAnnotatedBean);
	}
	
	@Test
	public void testSimplePropertyBean() {
		DefaultAgent ag = new DefaultAgent();
		SimplePropertyBean b = ag.create(SimplePropertyBean.class);
		Assert.assertTrue(b instanceof SimplePropertyBean);
		Assert.assertEquals(0, b.getA());

		b = ag.create(SimplePropertyBean.class, new Props("a", 78));
		Assert.assertTrue(b instanceof SimplePropertyBean);
		Assert.assertEquals(78, b.getA());
	}
	
	@Test
	public void testSimpleGeneratedPropertyBean() {
		DefaultAgent ag = new DefaultAgent();
		SimpleGeneratedPropertyBean b = ag.create(SimpleGeneratedPropertyBean.class);
		Assert.assertTrue(b instanceof SimpleGeneratedPropertyBean);
		Assert.assertEquals(0, b.getA());
		
		b.setA(-77);
		Assert.assertEquals(-77, b.getA());
		
		b = ag.create(SimpleGeneratedPropertyBean.class, new Props("a", -5));
		Assert.assertTrue(b instanceof SimpleGeneratedPropertyBean);
		Assert.assertEquals(-5, b.getA());
	}
	
	@Test
	public void testSetterOnlyBean() {
		DefaultAgent ag = new DefaultAgent();
		SetterOnlyBean b = ag.create(SetterOnlyBean.class);
		Assert.assertTrue(b instanceof SetterOnlyBean);
		Assert.assertEquals(0, b.giveMeA());

		b = ag.create(SetterOnlyBean.class, new Props("a", 112));
		Assert.assertTrue(b instanceof SetterOnlyBean);
		Assert.assertEquals(112, b.giveMeA());
	}
	
	@Test
	public void testComplicatedNamesBean() {
		DefaultAgent ag = new DefaultAgent();
		ComplicatedNamesBean b = ag.create(ComplicatedNamesBean.class, 
				new Props("aAA", 112).add("aaa", 56).add("aeAeAe", 15).add("_aaa", 3).add("_Aaa", 44).add("_AAA", 51)
				.add("bBB", 100).add("bbb", -24).add("beBeBe", 5).add("_bbb", 1).add("_Bbb", 33).add("_BBB", -51)
				.add("cCC", 111).add("ccc", -2).add("ceCeCe", 25).add("_ccc", 2).add("_Ccc", 22).add("_CCC", -1));

		Assert.assertEquals(112, b.getAAA());
		Assert.assertEquals(56, b.getAaa());
		Assert.assertEquals(15, b.getAeAeAe());
		Assert.assertEquals(3, b.get_aaa());
		Assert.assertEquals(44, b.get_Aaa());
		Assert.assertEquals(51, b.get_AAA());
		
		Assert.assertEquals(100, b.giveBBB());
		Assert.assertEquals(-24, b.giveBbb());
		Assert.assertEquals(5, b.giveBeBeBe());
		Assert.assertEquals(1, b.give_bbb());
		Assert.assertEquals(33, b.give_Bbb());
		Assert.assertEquals(-51, b.give_BBB());
		
		Assert.assertEquals(111, b.getCCC());
		Assert.assertEquals(-2, b.getCcc());
		Assert.assertEquals(25, b.getCeCeCe());
		Assert.assertEquals(2, b.get_ccc());
		Assert.assertEquals(22, b.get_Ccc());
		Assert.assertEquals(-1, b.get_CCC());
	}
	
	@Test 
	public void testTypes() {
		DefaultAgent ag = new DefaultAgent();
		VariousTypesBean b = ag.create(VariousTypesBean.class);
		Assert.assertEquals(0, b.getB3());
		Assert.assertEquals(0, b.getC3());
		Assert.assertNull(b.getD3());
		Assert.assertEquals(0, b.getE3());
		Assert.assertEquals(0f, b.getF3());
		Assert.assertEquals(0d, b.getG3());
		Assert.assertEquals(0, b.getH3());
		Assert.assertNull(b.getI3());
		Assert.assertFalse(b.getJ3());
		Assert.assertNull(b.getO3());
		
		VariousTypesBean b2 = ag.create(VariousTypesBean.class, 
				new Props("a", 112).add("a2", 56).add("a3", 15)
				.add("b", 23l).add("b2", 44l).add("b3", -554444444444444l)
				.add("c", (byte)1).add("c2", (byte)22).add("c3", (byte)111)
				.add("d", "huhu").add("d2", "haha").add("d3", "foobar")
				.add("e", 's').add("e2", 't').add("e3", 'o')
				.add("f", 23.34f).add("f2", -3.2f).add("f3", -2f)
				.add("g", 13.34).add("g2", -22.0).add("g3", -4.2)
				.add("h", (short)1334).add("h2", (short)-22).add("h3", (short)-42)
				.add("i", new ArrayList<String>()).add("i2", Arrays.asList("a", "b")).add("i3", Collections.emptyList())
				.add("j", true).add("j2", true).add("j3", true)
				.add("o", new Object()).add("o2", "bla").add("o3", new Date(22))
			);
		Assert.assertEquals(112, b2.getA());
		Assert.assertEquals(56, b2.a2);
		Assert.assertEquals(15, b2.getA3());
		Assert.assertEquals(23l, b2.getB());
		Assert.assertEquals(44l, b2.b2);
		Assert.assertEquals(-554444444444444l, b2.getB3());
		Assert.assertEquals((byte)1, b2.getC());
		Assert.assertEquals((byte)22, b2.c2);
		Assert.assertEquals((byte)111, b2.getC3());
		Assert.assertEquals("huhu", b2.getD());
		Assert.assertEquals("haha", b2.d2);
		Assert.assertEquals("foobar", b2.getD3());
		Assert.assertEquals('s', b2.getE());
		Assert.assertEquals('t', b2.e2);
		Assert.assertEquals('o', b2.getE3());
		Assert.assertEquals(23.34f, b2.getF());
		Assert.assertEquals(-3.2f, b2.f2);
		Assert.assertEquals(-2f, b2.getF3());
		Assert.assertEquals(13.34, b2.getG());
		Assert.assertEquals(-22.0, b2.g2);
		Assert.assertEquals(-4.2, b2.getG3());
		Assert.assertEquals((short)1334, b2.getH());
		Assert.assertEquals((short)-22, b2.h2);
		Assert.assertEquals((short)-42, b2.getH3());
		Assert.assertEquals(0, b2.getI().size());
		Assert.assertEquals(2, b2.i2.size());
		Assert.assertEquals(0, b2.getI3().size());
		Assert.assertTrue(b2.getJ());
		Assert.assertTrue(b2.j2);
		Assert.assertTrue(b2.getJ3());
		Assert.assertTrue(b2.getO() != null);
		Assert.assertEquals("bla", b2.o2);
		Assert.assertEquals(22, ((Date)b2.getO3()).getTime());
		
		VariousTypesBean b3 = ag.create(VariousTypesBean.class, 
				new Props("a", 112L).add("a2", 5.6).add("a3", (char)2)
				.add("b", 23).add("b2", (byte)2).add("b3", -1.2)
				.add("c", 1).add("c2", 22L).add("c3", 111.14f)
				.add("d", null).add("d2", null).add("d3", null)
				.add("e", 12).add("e2", 100).add("e3", 11)
				.add("f", 23.34).add("f2", -20).add("f3", -2l)
				.add("g", 13.3f).add("g2", -22L).add("g3", -4)
				.add("h", 1334).add("h2", -22.3).add("h3", (byte)22)
				.add("i", null).add("i2", null).add("i3", null)
				.add("j", false).add("j2", false).add("j3", false)
				.add("o", null).add("o2", null).add("o3", null)
			);
		Assert.assertEquals(112, b3.getA());
		Assert.assertEquals(5, b3.a2);
		Assert.assertEquals(2, b3.getA3());
		Assert.assertEquals(23l, b3.getB());
		Assert.assertEquals(2l, b3.b2);
		Assert.assertEquals(-1l, b3.getB3());
		Assert.assertEquals((byte)1, b3.getC());
		Assert.assertEquals((byte)22, b3.c2);
		Assert.assertEquals((byte)111, b3.getC3());
		Assert.assertNull(b3.getD());
		Assert.assertNull(b3.d2);
		Assert.assertNull(b3.getD3());
		Assert.assertEquals((char)12, b3.getE());
		Assert.assertEquals((char)100, b3.e2);
		Assert.assertEquals((char)11, b3.getE3());
		Assert.assertEquals(23.34f, b3.getF());
		Assert.assertEquals(-20f, b3.f2);
		Assert.assertEquals(-2f, b3.getF3());
		Assert.assertTrue(Math.abs(13.3 - b3.getG()) < 0.1);
		Assert.assertTrue(Math.abs(-22.0 - b3.g2) < 0.1);
		Assert.assertTrue(Math.abs(-4.0  - b3.getG3()) < 0.1);
		Assert.assertEquals((short)1334, b3.getH());
		Assert.assertEquals((short)-22, b3.h2);
		Assert.assertEquals((short)22, b3.getH3());
		Assert.assertNull(b3.getI());
		Assert.assertNull(b3.i2);
		Assert.assertNull(b3.getI3());
		Assert.assertFalse(b3.getJ());
		Assert.assertFalse(b3.j2);
		Assert.assertFalse(b3.getJ3());
		Assert.assertNull(b3.getO());
		Assert.assertNull(b3.o2);
		Assert.assertNull(b3.getO3());
	}
	
	@Test
	public void testDefaultValues() {
		DefaultAgent ag = new DefaultAgent();
		DefaultValueBean b = ag.create(DefaultValueBean.class);
		Assert.assertEquals(5, b.getA());
		Assert.assertEquals(50l, b.getB());
		Assert.assertEquals(-5, b.c);
		Assert.assertEquals("eek", b.getD());
		Assert.assertEquals('e', b.getE());
		Assert.assertEquals(54.2f, b.getF());
		Assert.assertEquals(-15.7, b.getG());
		Assert.assertEquals(3, b.getI().size());
		Assert.assertEquals(true, b.getJ());
		Assert.assertEquals("boo", b.getO());

		DefaultValueBean b2 = ag.create(DefaultValueBean.class, 
				new Props("a", 6).add("d", "boo").add("j", false).add("o", null));
		Assert.assertEquals(6, b2.getA());
		Assert.assertEquals("boo", b2.getD());
		Assert.assertEquals(false, b2.getJ());
		Assert.assertNull(b2.getO());
	}
	
	@Test
	public void testSynchronization() {
		DefaultAgent ag = new DefaultAgent();
		SynchronizingBean b = ag.create(SynchronizingBean.class);
		Assert.assertEquals(5, b.getA());
		Assert.assertEquals(50l, b.getB());
		Assert.assertEquals(-5, b.getC());
		Assert.assertEquals("eek", b.getD());

		b.setA(-5);
		b.setC((byte)0);
		b.setD("boo");
		Assert.assertEquals(-5, b.getA());
		Assert.assertEquals(0, b.getC());
		Assert.assertEquals("boo", b.getD());

		SynchronizingBean b2 = ag.create(SynchronizingBean.class, 
				new Props("a", 6).add("b", 21).add("c", 22).add("d", null));
		Assert.assertEquals(6, b2.getA());
		Assert.assertEquals(21, b2.getB());
		Assert.assertEquals(22, b2.getC());
		Assert.assertNull(b2.getD());
	}
	
	@Test
	public void testInitializer() {
		DefaultAgent ag = new DefaultAgent();
		SuperBaseBean b = ag.create(SuperBaseBean.class);
		Assert.assertTrue((b.a == 1) || (b.a == 2));
		Assert.assertTrue((b.b == 1) || (b.b == 2));
		Assert.assertTrue(b.a != b.b);
		Assert.assertTrue((b.c == 3) || (b.c == 4));
		Assert.assertTrue((b.d == 3) || (b.d == 4));
		Assert.assertTrue(b.c != b.d);
	}

	@Test(expected=WrappedException.class)
	public void testInitializerException() {
		DefaultAgent ag = new DefaultAgent();
		ExceptionBean b = ag.create(ExceptionBean.class);
	}
	
	
}
