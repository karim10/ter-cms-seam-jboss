package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cms.Article;
import cms.Contenu;
import cms.ContenuException;
import cms.EtatContenu;
import cms.GestionContenu;
import cms.Rubrique;
import cms.SessionUtilisateur;
import cms.Utilisateur;

public class GestionContenuTest {

	GestionContenu gc;
	Utilisateur u;
	SessionUtilisateur su;
	Rubrique r;
	Article a;
	
	@Before
	public void setUp() throws Exception {
		gc = new GestionContenu();
		u = new Utilisateur();
		u.setLogin("keira");
		u.setNom("Simon");
		u.setPrenom("Soulé");
		su = new SessionUtilisateur();
		r = new Rubrique();
		a = new Article();
	}

	@After
	public void tearDown() throws Exception {
		gc = null;  u = null;  su = null;
		r = null; a = null;
	}

	@Test
	public void testDepublierContenu() throws Exception {
		// initialisation
		r.setListEnfant(new LinkedList<Contenu>());
		r.setListGestionnaire(new LinkedList<Utilisateur>());
		r.setListRedacteur(new LinkedList<Utilisateur>());
		su.setListContenu(new LinkedList<Contenu>());
		su.setUtilisateur(u);
		gc.setSessionUtilisateur(su);
		//cas 1 : depublication Rubrique PUBLIE par Admin
		u.setAdmin(true);
		r.setEtatContenu(EtatContenu.PUBLIE);
		gc.depublierContenu(r);
		assertEquals(EtatContenu.NON_PUBLIE, r.getEtatContenu());
		//cas 2 : depublication Rubrique PUBLIE par Membre
		r.setEtatContenu(EtatContenu.PUBLIE);
		u.setAdmin(false);
		su.setUtilisateur(u);
		gc.setSessionUtilisateur(su);
		try{
		gc.depublierContenu(r);
		fail();
		}
		catch(ContenuException e){
			assertEquals(EtatContenu.PUBLIE, r.getEtatContenu());
		}
		//cas 3 : depublication Rubrique PUBLIE par Gestionnaire
		r.setEtatContenu(EtatContenu.PUBLIE);
		r.getListGestionnaire().add(u);
		gc.depublierContenu(r);
		assertEquals(EtatContenu.NON_PUBLIE, r.getEtatContenu());
		r.getListGestionnaire().clear();
		//cas 4 : depublication Rubrique PUBLIE par Redacteur
		r.setEtatContenu(EtatContenu.PUBLIE);
		r.getListRedacteur().add(u);
		gc.depublierContenu(r);
		assertEquals(EtatContenu.NON_PUBLIE, r.getEtatContenu());
		r.getListRedacteur().clear();
		//cas 5 : depublication Rubrique NON_PUBLIE par Admin
		u.setAdmin(true);
		r.setEtatContenu(EtatContenu.NON_PUBLIE);
		try{
		gc.depublierContenu(r);
		fail();
		}
		catch(ContenuException e){
			assertEquals(EtatContenu.NON_PUBLIE, r.getEtatContenu());
		}
		//cas 6 : depublication Contenu!= Rubrique PUBLIE par Admin
		u.setAdmin(true);
		su.setUtilisateur(u);
		gc.setSessionUtilisateur(su);
		a.setEtatContenu(EtatContenu.PUBLIE);
		r.setEtatContenu(EtatContenu.PUBLIE);
		a.setParent(r);
		gc.depublierContenu(a);
		assertEquals(EtatContenu.PUBLIE, r.getEtatContenu());
		assertEquals(EtatContenu.NON_PUBLIE, a.getEtatContenu());
		//cas 7 : depublication Contenu!= Rubrique PUBLIE par Gestionnaire
		u.setAdmin(false);
		a.setEtatContenu(EtatContenu.PUBLIE);
		r.addGestionnaire(u);
		r.setEtatContenu(EtatContenu.PUBLIE);
		gc.depublierContenu(a);
		assertEquals(EtatContenu.PUBLIE, r.getEtatContenu());
		assertEquals(EtatContenu.NON_PUBLIE, a.getEtatContenu());
		r.getListGestionnaire().clear();
		//cas 8 : depublication Contenu!= Rubrique PUBLIE par Redacteur
		a.setEtatContenu(EtatContenu.PUBLIE);
		r.addRedacteur(u);
		su.setUtilisateur(u);
		r.setEtatContenu(EtatContenu.PUBLIE);
		gc.depublierContenu(a);
		assertEquals(EtatContenu.PUBLIE, r.getEtatContenu());
		assertEquals(EtatContenu.NON_PUBLIE, a.getEtatContenu());
		r.getListRedacteur().clear();
		//cas 9 : depublication Contenu!= Rubrique PUBLIE par Membre
		a.setEtatContenu(EtatContenu.PUBLIE);
		r.setEtatContenu(EtatContenu.PUBLIE);
		try{
		gc.depublierContenu(a);
		fail();
		}
		catch(ContenuException e){
			assertEquals(EtatContenu.PUBLIE, r.getEtatContenu());
			assertEquals(EtatContenu.PUBLIE, a.getEtatContenu());
		}
	}

}
