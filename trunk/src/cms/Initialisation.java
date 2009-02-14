package cms;

import java.util.Date;

import org.hibernate.Transaction;
import org.jboss.seam.annotations.Name;

@Name("initialisation")
public class Initialisation {
	
	private Utilisateur admin;
	private Rubrique root;
	
    public void init() {
    	admin = new Utilisateur("administrateur","admin","admin","administrateur","admin@admin.admin",true,true);
		 root = new Rubrique(NiveauAccesContenu.PUBLIC,EtatContenu.PUBLIE,"ROOT",new Date(),admin);
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
