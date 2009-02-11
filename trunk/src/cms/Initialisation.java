package cms;

import java.util.Date;

import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("initialisation")
public class Initialisation {
	
    public void init() {
    	Utilisateur admin = new Utilisateur("administrateur","admin","admin","administrateur","admin@admin.admin",true,true);
		Rubrique root = new Rubrique(NiveauAccesContenu.PUBLIC,EtatContenu.PUBLIE,"ROOT",new Date(),admin);
		Transaction tx2 = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		HibernateUtil.getSessionFactory().getCurrentSession().save(admin);
		HibernateUtil.getSessionFactory().getCurrentSession().save(root);
  	  	tx2.commit();
	}
}