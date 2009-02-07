package cms;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;


@Name("actionUtilisateur")
@Scope(ScopeType.STATELESS)
public class ActionUtilisateur implements ActionUtilisateurLocal {

	@In
	Utilisateur utilisateur;
	
	@Logger
	Log log;
	
	public ActionUtilisateur(){}
	
	public Boolean isAdmain() {
		return utilisateur.getAdmin();
	}

	public Boolean isAuteur(Rubrique u) {
		return utilisateur.getContenuAuteur().contains(u);
	}

	public Boolean isGestionnaire(Rubrique u) {
		return utilisateur.getContenuGestionnaire().contains(u);
	}

	public Boolean isRedacteur(Rubrique u) {
		return utilisateur.getContenuRedacteur().contains(u);
	}
	
	public String inscrire() {
		
		 /** Getting the Session Factory and session */
	    SessionFactory session = HibernateUtil.getSessionFactory();
	    Session sess = session.getCurrentSession();
	    
	    /** Starting the Transaction */
	    Transaction tx = sess.beginTransaction();
		
		long nbRows =((Long) sess.createQuery("select count(*) from Utilisateur u where u.login =:log ").setParameter("log", utilisateur.getLogin()).iterate().next()).intValue();
		if(nbRows == 0){
			utilisateur.setAdmin(false);
			utilisateur.setCompteActive(false);
			sess.save(utilisateur);
			tx.commit();
			log.info("Registered new user utilisateur.login");
			sess.close();
			return null;
		}
		else{
			FacesMessages.instance().add("User utilisateur.login already exists");
			return null;
		}
		
	}

}
