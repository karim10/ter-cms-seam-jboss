package cms;

import javax.persistence.EntityExistsException;

import org.hibernate.Transaction;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;


@Name("inscription")
public class Inscription {

	@In
	private Utilisateur utilisateur;
	
	@Logger
	private Log log;
	
	private String confirmation;

	
	public Inscription(){}
	
	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
	   
	
	public String inscription(){
	     if (confirmation == null || !confirmation.equals(utilisateur.getMotDePasse())){
	         FacesMessages.instance().addToControl("confirmation", "les 2 mot de passe ne correspondent pas");
	         return "/login.seam";
	      }
	      Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();	      
	      if (HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Utilisateur where login = :login")
	            .setParameter("login", utilisateur.getLogin())
	            .list().size() > 0){
	         FacesMessages.instance().addToControl("login", 
	               "Ce login a d�j� �t� choisi, choisissez-en un diff�rent");
	         return "/login.seam"; 
	      }
	     
	      try{
	    	  utilisateur.setAdmin(false);
	    	  utilisateur.setCompteActive(false);
	    	  HibernateUtil.getSessionFactory().getCurrentSession().save(utilisateur);
	    	  tx.commit();
	    	  return "/cms.seam";
	      }
	      catch (EntityExistsException ex){
	         FacesMessages.instance().addToControl("login", 
	               "Ce login a d�j� �t� choisi, choisissez-en un diff�rent");
	         return "/login.seam";  
	      }
	}
	   
}