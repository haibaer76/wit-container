package crmcontainerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import crmcontainer.ConsoleMonitor;
import crmcontainer.CrmContainer;
import crmcontainer.DuplicateBindException;
import crmcontainerTest.sample.A;
import crmcontainerTest.sample.AExtended;
import crmcontainerTest.sample.B;
import crmcontainerTest.sample.C;
import crmcontainerTest.sample.SampleSimpleClassProvider;
import crmcontainerTest.sample.SampleStringProvider;
import crmcontainerTest.sample.SimpleClass;

public class CrmContainerTest {

	private static CrmContainer crmContainer;
	private static A a;
	private static B b;


	@Before
	public void setUp() throws Exception {
		crmContainer = new CrmContainer();
		bindClasses();
		
		a = crmContainer.getInstance(A.class);
		b = crmContainer.getInstance(B.class);
	}

	private void bindClasses() {
		crmContainer.setMonitor(new ConsoleMonitor());
		crmContainer.bind(A.class);
		crmContainer.bind(B.class);
		crmContainer.bind(C.class);
		crmContainer.bind("db_name").to("db1");
	}
	
	/**
	 * Testen der korrekten Erzeugung von Class A
	 * mit seiner Abhaengigkeit zu Class B
	 * und der Eigenschaft name
	 */
	@Test
	public void testA() {
		assertTrue(a instanceof A);
		assertEquals(a.getB(),b);
		assertEquals("db1", a.getName());
	}

	/**
	 * Testen der korrekten Erzeugung von Class B mit
	 * - seiner Abhaengigkeit zu Class A
	 * - seiner Abhaengikeit zu Classe C
	 * - Methode start aufgerufen wurde
	 * - Methode Start der Instance von Classe C aufgerufen wurde
	 * 
	 */
	@Test
	public void testB() {
		assertTrue(b instanceof B);
		assertEquals(b.getA(), a);
		assertTrue("b.start not called!",b.isStarted());
		C c = b.getC();
		assertNotNull(c);
		assertTrue("c.start not called!", c.isStarted());
	}

	
	/**
	 * Testen des Bindens eines Klasse unter einem
	 * Object-Key
	 *
	 */
	@Test
	public void testBindClassWithObjectAsKey() {
		crmContainer.bind("other-a-instance").to(A.class);
		A a2 = (A) crmContainer.getInstance("other-a-instance");
		assertTrue(a2 instanceof A);
		assertNotSame(a2, a);
	}
	
	enum TestEnum { EINS, ZWEI }
	
	/**
	 * Test das Binden und holen von Konstanten
	 */
	@Test
	public void testBindConstant() {
		crmContainer.bind("key1").to("value1");
		crmContainer.bind(TestEnum.EINS).to(new Integer(1));
		crmContainer.bind(TestEnum.ZWEI).to(new Integer(2));
		assertEquals("value1", crmContainer.getInstance("key1"));
		assertEquals(1, ((Integer)crmContainer.getInstance(TestEnum.EINS)).intValue() );
		assertEquals(2, ((Integer)crmContainer.getInstance(TestEnum.ZWEI)).intValue() );
	}

	/**
	 * Test des Erkennens des Versuches 
	 * Konstanten unter selbem Key zu binden  
	 */
	@Test
	public void testBindConstantDuplicate() {
		crmContainer.bind("key1").to("value1");
		boolean throwException = false;
		try {
			crmContainer.bind("key1").to("value2");
		} catch (DuplicateBindException e) {
			throwException = true;
		}
		assertTrue(throwException);
	}
	

	/**
	 * Test des Erkennens des Versuches 
	 * Klassen unter selbem Key zu binden  
	 */
	@Test
	public void testBindClassDuplicate() {
		boolean throwException = false;
		try {
			crmContainer.bind(A.class);
		} catch (DuplicateBindException e) {
			throwException = true;
		}
		assertTrue(throwException);
	}
	

	/**
	 * Testen der Provider-Funktionalitaet,
	 * d.h. der Container muss bei einer Implementierungsklasse, die das Interface Provider
	 * implementiert, nicht die Klasse selbst sondern ein Object der zu prividenden Klasse liefern
	 * (also ein get auf dieser Klasse aufrufen)
	 *
	 */
	@Test
	public void testProvider() {
		System.out.println("=== testProvider ===");
		crmContainer.bind(SampleStringProvider.class);
		crmContainer.bind(SampleSimpleClassProvider.class);
		
		crmContainer.bind("message").toProvider(SampleStringProvider.class);
		crmContainer.bind(SimpleClass.class).toProvider(SampleSimpleClassProvider.class);

		SimpleClass o = crmContainer.getInstance(SimpleClass.class); 
		assertNotNull("SimpleClass-Instance is null! ", o);
		String s = o.getMsg();
		assertEquals("instance by provider", s);
		
		SimpleClass o2 = crmContainer.getInstance(SimpleClass.class);
		assertEquals(o, o2);
	}
	
	@Test
	public void testInjectionOverExtendedClass() {
		crmContainer.bind(AExtended.class);
		
		AExtended a = crmContainer.getInstance(AExtended.class);
		assertNotNull(a.getB());
	}
	
	
	@Test
	public void testThreadSafeGetInstance() {
		//TODO
	}
	
	
	@Test
	public void testRuntime() {
		System.out.println("\n=== testRuntime ===");
		long time = System.currentTimeMillis();
		crmContainer = new CrmContainer();
		bindClasses();
		for(int i=1; i< 4500; i++) {
			crmContainer.bind("key"+i).to("xxx");
		}
		time = printRunTime(time);
		
		a = crmContainer.getInstance(A.class);
		b = crmContainer.getInstance(B.class);
		time = printRunTime(time);
	}

	
	@Test
	public void testRuntimeWithInitialCapa() {
		System.out.println("\n=== testRuntimeWithInitialCapa ===");
		long time = System.currentTimeMillis();
		crmContainer = new CrmContainer(5000);
		bindClasses();
		for(int i=1; i< 4500; i++) {
			crmContainer.bind("key"+i).to("xxx");
		}
		time = printRunTime(time);
		
		a = crmContainer.getInstance(A.class);
		b = crmContainer.getInstance(B.class);
		time = printRunTime(time);
	}


	private static long printRunTime(long time) {
		long currentTime = System.currentTimeMillis();
		System.out.println("runtime= "+(currentTime-time)+"ms");
		return currentTime;
	}
	

}
