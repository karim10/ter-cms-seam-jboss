package composant;

import java.util.Date;

import org.hibernate.Transaction;
import org.jboss.seam.annotations.Name;

import entite.EtatContenu;
import entite.NiveauAccesContenu;
import entite.Rubrique;
import entite.Utilisateur;

import util.HibernateUtil;

@Name("initialisation")
public class Initialisation {
	
	
	private Utilisateur admin;
	
	private Rubrique root;
    
	public Initialisation(){
		admin = new Utilisateur();
		root = new Rubrique();
	}
	/**
	 * <p>initialise le CMS<br/>
	 * crée la rubrique racine et l'utilisateur administrateur</p>
	 */
	public void init() {
    	// creation de l'utilisateur administrateur
		admin.setLogin("administrateur");
    	admin.setNom("admin");
    	admin.setPrenom("admin");
    	admin.setMotDePasse("administrateur");
    	admin.setEmail("admin@admin.fr");
    	admin.setAdmin(true);
    	admin.setCompteActive(true);
    	admin.setAccesBackend(true);
    	// creation de la rubrique racine
		root.setNiveauAcces(NiveauAccesContenu.PUBLIC);
		root.setEtatContenu(EtatContenu.PUBLIE);
		root.setTitreContenu("ROOT");
		root.setDateCreation(new Date());
		root.setDateMaj(new Date());
		root.setAuteur(admin);
		root.setParent(root);
		// sauvegarde de l'utilisateur et la rubrique dans la bd
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		HibernateUtil.getSessionFactory().getCurrentSession().save(admin);
		HibernateUtil.getSessionFactory().getCurrentSession().save(root);
  	  	tx.commit();
	}
	/**
	 * @return the admin
	 */
	public Utilisateur getAdmin() {
		return admin;
	}
	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(Utilisateur admin) {
		this.admin = admin;
	}
	/**
	 * @return the root
	 */
	public Rubrique getRoot() {
		return root;
	}
	/**
	 * @param root the root to set
	 */
	public void setRoot(Rubrique root) {
		this.root = root;
	}
}
