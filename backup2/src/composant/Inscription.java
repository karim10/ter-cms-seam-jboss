package composant;

import javax.persistence.EntityExistsException;

import org.hibernate.Transaction;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;


import entite.Utilisateur;

import util.HibernateUtil;


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
	   
	/**
	 * <p>Inscrit un nouveau utilisateur
	 * @return vrai si l'inscription s'est effectuée
	 *         faux sinon</p>
	 */
	public Boolean inscription(){
	     if (confirmation == null || !confirmation.equals(utilisateur.getMotDePasse())){
	         FacesMessages.instance().addToControl("confirmation", "les deux mot de passe ne correspondent pas");
	         return false;
	      }
	      Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();	      
	      if (HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Utilisateur where login = :login")
	            .setParameter("login", utilisateur.getLogin())
	            .list().size() > 0){
	         FacesMessages.instance().addToControl("login", 
	               "Ce login a déjà été choisi, choisissez-en un différent");
	         return false; 
	      }
	     
	      try{
	    	  utilisateur.setAdmin(false);
	    	  utilisateur.setCompteActive(false);
	    	  HibernateUtil.getSessionFactory().getCurrentSession().save(utilisateur);
	    	  tx.commit();
	    	  return true;
	      }
	      catch (EntityExistsException ex){
	         FacesMessages.instance().addToControl("login", 
	               "Ce login a déjà été choisi, choisissez-en un différent");
	         return false;  
	      }
	}
	   
}
