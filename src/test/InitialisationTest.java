/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import composant.Initialisation;

/**
 * @author keira
 *
 */
public class InitialisationTest {
	
	Initialisation init;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		init = new Initialisation();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		init = null;
	}

	/**
	 * Test method for {@link composant.Initialisation#init()}.
	 */
	@Test
	public void testInit() {
		init.init();
		assertEquals("administrateur", init.getAdmin().getLogin());
	}

}
