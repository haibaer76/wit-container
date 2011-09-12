package working_it.witcontainer;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import working_it.witcontainer.sample.A;
import working_it.witcontainer.sample.B;
import working_it.witcontainer.sample.D;

public class ParentContainerTest {

	private WitContainer childContainer;
	
	private WitContainer parentContainer;
	
	@Before
	public void setUp() throws Exception {
		parentContainer = new WitContainer();
		parentContainer.bind(A.class);
		parentContainer.bind(B.class);
		parentContainer.bind("db_name").to("DB");
		childContainer = new WitContainer(parentContainer);
		childContainer.bind(D.class);
	}
	
	@Test
	public void testGetD() {
		D d = childContainer.getInstance(D.class);
		A a = parentContainer.getInstance(A.class);
		assertNotNull("A was not injected properly", d.getA());
		assertEquals("The child container has another A than parent", d.getA(), a);
	}

}
