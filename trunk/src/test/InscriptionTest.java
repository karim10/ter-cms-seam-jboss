package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cms.Inscription;
import cms.Utilisateur;

public class InscriptionTest {

	Inscription insc;
	Utilisateur u ;
	@Before
	public void setUp() throws Exception {
		insc = new Inscription();
		u = new Utilisateur();
	}

	@After
	public void tearDown() throws Exception {
		insc = null;
		u= null;
	}

	@Test
	public void testInscription() {
		u.setNom("soule");
		u.setLogin("login");
		u.setPrenom("prenom");
		u.setMotDePasse("motDePasse");
		insc.inscription();
		assertEquals(false, insc.inscription());
	}

}
