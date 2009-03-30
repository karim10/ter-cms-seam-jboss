package composant;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;

import entite.IUtilisateur;
import entite.Utilisateur;
import exception.ContenuException;

import util.DataUtil;
import util.HibernateUtil;

@Name("gestionUtilisateur")
@Scope(ScopeType.SESSION)
public class GestionUtilisateur{

	@DataModel
	private List<IUtilisateur> listUtilisateur;
	
	@DataModelSelection
	@Out(required=false)
	private IUtilisateur utilisateur;
	
	public GestionUtilisateur(){}
	
	
	
	/**
	 * <p>Initialise listUtilisateur lors de l'initialisation du composant 
	 * à partir du contenu de la base de donnée.</p>
	 */
	@Create
	@Factory("listUtilisateur")
	public void init(){
		listUtilisateur = DataUtil.chargeUtilisateurs();
	}
	
	/**
	 * <p>Inscrit un nouveau utilisateur
	 * @return vrai si l'inscription s'est effectuée
	 *         faux sinon</p>
	 */
	public Boolean inscription(IUtilisateur utilisateur){
	     if (utilisateur.getConfirmation() == null || !utilisateur.getConfirmation().equals(utilisateur.getMotDePasse())){
	         FacesMessages.instance().addToControl("confirmation", "les deux mot de passe ne correspondent pas");
	         setUtilisteur(null);
	         return false;
	      }
	      Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();	      
	      int result = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Utilisateur where login = :login")
	            .setParameter("login", utilisateur.getLogin())
	            .list().size();
	      tx.commit();
	            if(result > 0){
	         FacesMessages.instance().addToControl("login", 
	               "Ce login a déjà été choisi, choisissez-en un différent");
	         setUtilisteur(null);
	         return false; 
	      }
	     
	      try{
	    	  utilisateur.setAdmin(false);
	    	  utilisateur.setCompteActive(false);
	    	  utilisateur.setAccesBackend(false);
	    	  addUtilisateur(utilisateur);
	    	  setUtilisteur(null);
	    	  return true;
	      }
	      catch (EntityExistsException ex){
	         FacesMessages.instance().addToControl("login", 
	               "Ce login a déjà été choisi, choisissez-en un différent");
	         setUtilisteur(null);
	         return false;  
	      }
	}
	
	/**
	 * <p>Crée un nouveau utilisateur</p>
	 * @param utilisateur
	 * @throws HibernateException
	 */
	public void addUtilisateur(IUtilisateur utilisateur) throws HibernateException {
		
	    Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    HibernateUtil.getSessionFactory().getCurrentSession().save(utilisateur);
	    
	    getListUtilisateur().add(utilisateur);
	    
	    setUtilisteur(null);
	    
	    tx.commit();
	    
	}

	/**
	 * <p>Modifie un utilisateur</p>
	 * @param utilisateur
	 * @throws HibernateException
	 */
	public void modifierUtilisateur(IUtilisateur utilisateur) throws HibernateException {
		Long id = utilisateur.getId_utilisateur();
		if(utilisateur!=null){
			Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			IUtilisateur u = (Utilisateur)HibernateUtil.getSessionFactory().getCurrentSession().load(Utilisateur.class,id);
			u.setAccesBackend(utilisateur.isAccesBackend());
			u.setAdmin(utilisateur.isAdmin());
			u.setWebsite(utilisateur.getWebsite());
			setUtilisteur(u);
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(u);
			tx.commit();
			for(int i=0 ;i<getListUtilisateur().size(); i++){
				if(getListUtilisateur().get(i).getId_utilisateur() == id){
					getListUtilisateur().remove(i);
					getListUtilisateur().add(i,utilisateur);
					return;
				}				
			}
		} 
	}
	
	/**
	 * <p>Supprime un utilisateur</p>
	 * @param utilisateur
	 * @throws HibernateException
	 */
	public void removeUtilisateur(IUtilisateur utilisateur) throws HibernateException, ContenuException {
		Long id = utilisateur.getId_utilisateur();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		IUtilisateur c = (IUtilisateur)HibernateUtil.getSessionFactory().getCurrentSession().load(IUtilisateur.class,id);
		if(utilisateur!=null){
			Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			HibernateUtil.getSessionFactory().getCurrentSession().delete(c);
			tx.commit();
			for(IUtilisateur u : listUtilisateur){
				if(u.getId_utilisateur() == id){
					getListUtilisateur().remove(utilisateur);
					setUtilisteur(null);
					return;
				}
			}
			setUtilisteur(null);
		}    
	}
	
	@Destroy
	public void destroy() {}

	// GETTER SETTER
	
	public IUtilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisteur(IUtilisateur utilisteur) {
		this.utilisateur = utilisteur;
	}
	
	@Restrict("#{s:hasRole('admin')}")
	public List<IUtilisateur> getListUtilisateur() {		
		return this.listUtilisateur;
	}
	
	public void setListUtilisateur(List<IUtilisateur> listUtilisateur) {
		this.listUtilisateur = listUtilisateur;
	}

}
