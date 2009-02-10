package cms;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;


@Name("actionUtilisateur")
public class ActionUtilisateur implements ActionUtilisateurLocal, Serializable {

	private static final long serialVersionUID = 5969738410960647515L;

	@In @Out(required=false, scope = ScopeType.SESSION)
	private Utilisateur utilisateur;
	
	@Logger
	private Log log;
	
	@In
	private Identity identity;
	
	private String confirmation;

	
	public ActionUtilisateur(){}
	
	

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}


	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}


	public Log getLog() {
		return log;
	}


	public void setLog(Log log) {
		this.log = log;
	}


	public Identity getIdentity() {
		return identity;
	}


	public void setIdentity(Identity identity) {
		this.identity = identity;
	}


	public String getConfirmation() {
		return confirmation;
	}


	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
	   
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public boolean authentification() {
		try{    
			Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();			
			List result = HibernateUtil.getSessionFactory().getCurrentSession().createQuery(
	            "from Utilisateur where login = :login and motDePasse = :motdepasse")
	            .setParameter("login", identity.getUsername())
	            .setParameter("motdepasse", identity.getPassword())
	            .list();
			tx.commit();
			if (result.size()==0){
				return false;
			}
			else{
				utilisateur = (Utilisateur) result.get(0);
				Contexts.getSessionContext().set("authenticatedUser", utilisateur);         
				return true;		
			}
		}
		catch (NoResultException ex){
	         return false;
		}
	}
	
	   public String inscription(){
	      if (confirmation == null || !confirmation.equals(utilisateur.getMotDePasse())){
	         FacesMessages.instance().addToControl("confirmation", "les 2 mot de passe ne correspondent pas");
	         return "/login.seam";
	      }
	      Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();	      
	      if (HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Utilisateur where login = :login")
	            .setParameter("login", utilisateur.getLogin())
	            .list().size() > 0)
	      {
	         FacesMessages.instance().addToControl("login", 
	               "Ce login a déjà été choisi, choisissez-en un différent");
	         return "/login.seam"; 
	      }
	     
	      try
	      {
	    	  utilisateur.setAdmin(false);
	    	  utilisateur.setCompteActive(false);
	    	  HibernateUtil.getSessionFactory().getCurrentSession().save(utilisateur);
	    	  tx.commit();
	         utilisateur = null;
	         return "/cms.seam";
	      }
	      
	      
	      catch (EntityExistsException ex)
	      {
	         FacesMessages.instance().addToControl("login", 
	               "Ce login a déjà été choisi, choisissez-en un différent");
	         return "/login.seam";  
	      }
	}
	   
	      
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

}
