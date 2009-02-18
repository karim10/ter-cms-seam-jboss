package composant;

import java.util.ArrayList;
import java.util.List;

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

import entite.Contenu;
import entite.ContenuException;
import entite.Utilisateur;

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
	 * <p>Crée la listUtilisateur lors de l'initialisation du composant 
	 * à partir du contenu de la base de donnée.</p>
	 */
	@Create
	public void init(){
		listUtilisateur = DataUtil.chargeUtilisateurs();
	}
	
	/**
	 * <p>Crée un nouveau utilisateur</p>
	 * @param u
	 * @throws HibernateException
	 */
	public void addUtilisateur(Utilisateur utilisteur) throws HibernateException {
		
	    Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    HibernateUtil.getSessionFactory().getCurrentSession().save(utilisteur);
	    
	    getListUtilisateur().add(utilisteur);
	    
	    tx.commit();
	    
	}

	/**
	 * <p>Modifie un utilisateur</p>
	 * @param u
	 * @throws HibernateException
	 */
	public void modifierUtilisateur(Utilisateur utilisateur) throws HibernateException {
		Long id = utilisateur.getId_utilisateur();
		if(utilisateur!=null){
			Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(utilisateur);
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
	 * @param u
	 * @throws HibernateException
	 */
	public void removeUtilisateur(Utilisateur utilisateur) throws HibernateException, ContenuException {
		Long id = utilisateur.getId_utilisateur();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		Contenu c = (Contenu)HibernateUtil.getSessionFactory().getCurrentSession().load(Contenu.class,id);
		if(utilisateur!=null){
			Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			HibernateUtil.getSessionFactory().getCurrentSession().delete(c);
			tx.commit();
			for(Utilisateur u : getListUtilisateur()){
				if(u.getId_utilisateur() == id){
					getListUtilisateur().remove(utilisateur);
					return;
				}
			}			
		}    
	}
	
	@Destroy
	public void destroy() {}


}
