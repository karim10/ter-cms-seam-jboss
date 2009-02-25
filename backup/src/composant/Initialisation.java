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
    
	/**
	 * <p>initialise le CMS<br/>
	 * cr�e la rubrique racine et l'utilicateur administrateur</p>
	 */
	public void init() {
    	admin = new Utilisateur("administrateur","admin","admin","administrateur","admin@admin.admin",true,true);
    	admin.setAccesBackend(true);
    	
		 root = new Rubrique(NiveauAccesContenu.PUBLIC,EtatContenu.PUBLIE,"ROOT",new Date(),admin);
		root.setParent(root);
		Transaction tx2 = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		HibernateUtil.getSessionFactory().getCurrentSession().save(admin);
		HibernateUtil.getSessionFactory().getCurrentSession().save(root);
  	  	tx2.commit();
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
