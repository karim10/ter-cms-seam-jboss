package composant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;

import entite.ActionEnum;
import entite.Contenu;
import entite.Rubrique;
import entite.Utilisateur;
import exception.ContenuException;

import util.DataUtil;
import util.HibernateUtil;

@Name("gestionUtilisateur")
@Scope(ScopeType.SESSION)
public class GestionUtilisateur{

	@DataModel
	private List<Utilisateur> listUtilisateur;
	
	@DataModelSelection
	@Out(required=false)
	private Utilisateur utilisateur;
	
	public GestionUtilisateur(){}
	
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisteur(Utilisateur utilisteur) {
		this.utilisateur = utilisteur;
	}
	
	@Restrict("#{s:hasRole('admin')}")
	public List<Utilisateur> getListUtilisateur() {		
		return this.listUtilisateur;
	}
	
	public void setListUtilisateur(List<Utilisateur> listUtilisateur) {
		this.listUtilisateur = listUtilisateur;
	}
	
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
	public Boolean inscription(Utilisateur utilisateur){
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
	public void addUtilisateur(Utilisateur utilisateur) throws HibernateException {
		
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
	public void modifierUtilisateur(Utilisateur utilisateur) throws HibernateException {
		Long id = utilisateur.getId_utilisateur();
		if(utilisateur!=null){
			Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			Utilisateur u = (Utilisateur)HibernateUtil.getSessionFactory().getCurrentSession().load(Utilisateur.class,id);
			u.setAccesBackend(utilisateur.getAccesBackend());
			u.setAdmin(utilisateur.getAdmin());
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
	public void removeUtilisateur(Utilisateur utilisateur) throws HibernateException, ContenuException {
		Long id = utilisateur.getId_utilisateur();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		Utilisateur c = (Utilisateur)HibernateUtil.getSessionFactory().getCurrentSession().load(Utilisateur.class,id);
		if(utilisateur!=null){
			Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			HibernateUtil.getSessionFactory().getCurrentSession().delete(c);
			tx.commit();
			for(Utilisateur u : getListUtilisateur()){
				if(u.getId_utilisateur() == id){
					getListUtilisateur().remove(utilisateur);
					setUtilisteur(null);
					return;
				}
			}
			setUtilisteur(null);
		}    
	}
	
	
	
	/*****************************************
	 * gestion des gestionnaires et rédacteurs
	 */
	
	private Map<Long, Boolean> selectGestionnaire = new HashMap<Long, Boolean>();
	private Map<Long, Boolean> selectRedacteur = new HashMap<Long, Boolean>();
    private List<Utilisateur> selectGestionnaireDataList;
    private List<Utilisateur> selectNonGestionnaireDataList;
    private List<Utilisateur> selectRedacteurDataList;
    private List<Utilisateur> selectNonRedacteurDataList;
    
	/**
	 * <p> modifie le statut de gestionnaire des utilisateurs d'un contenu</p>
	 * @param listUtilisateur
	 * @param contenu
	 * @param actionEnum
	 * @throws Exception
	 */
	@Transactional
	public void modifierStatutGestionnaire(List<Utilisateur> listUtilisateur,Contenu contenu,ActionEnum actionEnum) throws Exception{
		if(listUtilisateur!=null && !listUtilisateur.isEmpty()){
			if(contenu instanceof Rubrique){
				Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
				Transaction tx = null;
				try {
				     tx = sess.beginTransaction();
				     Long idc = contenu.getId_contenu();
				     Rubrique rubrique = (Rubrique)sess.get(Rubrique.class,idc);
				     for(Utilisateur u : listUtilisateur){
				    	 Long idu = u.getId_utilisateur();
				    	 Utilisateur utilisateur = (Utilisateur)sess.get(Utilisateur.class,idu);
				    	 switch(actionEnum){
							case ADD :
								if(!utilisateur.getContenuGestionnaire().contains(rubrique))utilisateur.getContenuGestionnaire().add(rubrique);
								if(!rubrique.getListGestionnaire().contains(utilisateur))rubrique.getListGestionnaire().add(utilisateur);
								break;
							case DELETE :
								utilisateur.getContenuGestionnaire().remove(rubrique);
								rubrique.getListGestionnaire().remove(utilisateur);
								break;
								default : break;	
				    	 }
				    	 sess.saveOrUpdate(utilisateur);
				     }
				     sess.saveOrUpdate(rubrique);
				     tx.commit();
				 }
				 catch (Exception e) {
				     if (tx!=null) tx.rollback();
				     throw e;
				 }
			} else {
				new ContenuException("Vous ne pouvez ajoutez un gestionnaire à un contenu autre qu'une rubrique");
			}
		}
	}
	
	/**
	 * /**
	 * <p> modifie le statut de rédacteur des utilisateurs d'un contenu</p>
	 * @param listUtilisateur
	 * @param contenu
	 * @param actionEnum
	 * @throws Exception
	 */
	@Transactional
	public void modifierStatutRedacteur(List<Utilisateur> listUtilisateur,Contenu contenu,ActionEnum actionEnum) throws Exception{
		if(listUtilisateur!=null && !listUtilisateur.isEmpty()){
			if(contenu instanceof Rubrique){
				Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
				Transaction tx = null;
				try {
				     tx = sess.beginTransaction();
				     Long idc = contenu.getId_contenu();
				     Rubrique rubrique = (Rubrique)sess.get(Rubrique.class,idc);
				     for(Utilisateur u : listUtilisateur){
				    	 Long idu = u.getId_utilisateur();
				    	 Utilisateur utilisateur = (Utilisateur)sess.get(Utilisateur.class,idu);
				    	 switch(actionEnum){
							case ADD :
								if(!utilisateur.getContenuRedacteur().contains(rubrique))utilisateur.getContenuRedacteur().add(rubrique);
								if(!rubrique.getListRedacteur().contains(utilisateur))rubrique.getListRedacteur().add(utilisateur);
								break;
							case DELETE :
								utilisateur.getContenuRedacteur().remove(rubrique);
								rubrique.getListRedacteur().remove(utilisateur);
								break;
								default : break;	
				    	 }
				    	 sess.saveOrUpdate(utilisateur);
				     }
				     sess.saveOrUpdate(rubrique);
				     tx.commit();
				 }
				 catch (Exception e) {
				     if (tx!=null) tx.rollback();
				     throw e;
				 }
			} else {
				new ContenuException("Vous ne pouvez ajoutez un redacteur à un contenu autre qu'une rubrique");
			}
		}
	}
	
	
	
    /**
     * <p>initialisation des HashMap {@link selectGestionnaire} et {@link selectRedacteur} avec la BD</p>
     * @param contenu
     */
	public void initHashMap(Contenu contenu) {
		listUtilisateur = DataUtil.chargeUtilisateurs();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		Contenu c =(Contenu)HibernateUtil.getSessionFactory().getCurrentSession().get(Contenu.class, contenu.getId_contenu());
    	selectGestionnaire.clear();
		selectRedacteur.clear();
		for (Utilisateur u : getListUtilisateur()) {
			selectGestionnaire.put(u.getId_utilisateur(),u.isGestionnaire(c));
			selectRedacteur.put(u.getId_utilisateur(), u.isRedacteur(c));
		}
	}
    
    /**
     * <p>Modifie les gestionnaires et les rédacteurs d'une rubrique donnée</p>
     * @param contenu
     * @throws Exception 
     */
    public void getSelectUtilisateur(Contenu contenu) throws Exception {
    	
        // Réinitialisation des dataLists
    	selectGestionnaireDataList = new ArrayList<Utilisateur>();
    	selectNonGestionnaireDataList = new ArrayList<Utilisateur>();
    	selectRedacteurDataList = new ArrayList<Utilisateur>();
    	selectNonRedacteurDataList = new ArrayList<Utilisateur>();
    	
        for (Utilisateur u : getListUtilisateur()) {
            // rempli la liste des gestionnaires et la liste des non gesitonnaires 
        	if (selectGestionnaire.get(u.getId_utilisateur()).booleanValue()) {
            	selectGestionnaireDataList.add(u);
            	selectRedacteur.get(u.getId_utilisateur());
            } else {
            	selectNonGestionnaireDataList.add(u);
            }
        	// rempli la liste des rédacteurs et la liste des non rédacteurs
            if (selectRedacteur.get(u.getId_utilisateur()).booleanValue()) {
            	selectRedacteurDataList.add(u);
            } else {
            	selectNonRedacteurDataList.add(u);
            }  
        }
        //modification
        modifierStatutGestionnaire(selectGestionnaireDataList,contenu,ActionEnum.ADD);
        modifierStatutGestionnaire(selectNonGestionnaireDataList,contenu,ActionEnum.DELETE);
        modifierStatutRedacteur(selectRedacteurDataList, contenu, ActionEnum.ADD);
        modifierStatutRedacteur(selectNonRedacteurDataList, contenu, ActionEnum.DELETE);
    }


	public Map<Long, Boolean> getSelectGestionnaire() {
		return selectGestionnaire;
	}

	public Map<Long, Boolean> getSelectRedacteur() {
		return selectRedacteur;
	}

	public List<Utilisateur> getSelectGestionnaireDataList() {
		return selectGestionnaireDataList;
	}

	public List<Utilisateur> getSelectNonGestionnaireDataList() {
		return selectNonGestionnaireDataList;
	}

	public List<Utilisateur> getSelectRedacteurDataList() {
		return selectRedacteurDataList;
	}

	public List<Utilisateur> getSelectNonRedacteurDataList() {
		return selectNonRedacteurDataList;
	}

	public void setSelectGestionnaire(Map<Long, Boolean> selectGestionnaire) {
		this.selectGestionnaire = selectGestionnaire;
	}

	public void setSelectRedacteur(Map<Long, Boolean> selectRedacteur) {
		this.selectRedacteur = selectRedacteur;
	}

	public void setSelectGestionnaireDataList(
			List<Utilisateur> selectGestionnaireDataList) {
		this.selectGestionnaireDataList = selectGestionnaireDataList;
	}

	public void setSelectNonGestionnaireDataList(
			List<Utilisateur> selectNonGestionnaireDataList) {
		this.selectNonGestionnaireDataList = selectNonGestionnaireDataList;
	}

	public void setSelectRedacteurDataList(List<Utilisateur> selectRedacteurDataList) {
		this.selectRedacteurDataList = selectRedacteurDataList;
	}

	public void setSelectNonRedacteurDataList(
			List<Utilisateur> selectNonRedacteurDataList) {
		this.selectNonRedacteurDataList = selectNonRedacteurDataList;
	}

	@Destroy
	public void destroy() {}


}
