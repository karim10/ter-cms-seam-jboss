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
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.security.Restrict;

import entite.Utilisateur;

import util.DataUtil;
import util.HibernateUtil;

@Name("gestionUtilisateur")
@Scope(ScopeType.SESSION)
public class GestionUtilisateur{

	@DataModel
	private List<Utilisateur> listUtilisateur = new ArrayList<Utilisateur>();
	
	public GestionUtilisateur(){}
	
	public void setListUtilisateur(ArrayList<Utilisateur> listUtilisateur) {
		this.listUtilisateur = listUtilisateur;
	}
	
	@Restrict("#{s:hasRole('admin')}")
	public List<Utilisateur> getListUtilisateur() {		
		return this.listUtilisateur;
	}

	/**
	 * <p>Crée la listUtilisateur lors de l'initialisation du composant 
	 * à partir du contenu de la base de donnée.</p>
	 */
	@Create
	@Factory("listUtilisateur")
	public void maj(){
		listUtilisateur = DataUtil.chargeUtilisateurs();
	}
	
	/**
	 * <p>Crée un nouveau utilisateur</p>
	 * @param u
	 * @throws HibernateException
	 */
	public void addUtilisateur(Utilisateur u) throws HibernateException {
		
	    Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    
	    HibernateUtil.getSessionFactory().getCurrentSession().save(u);
	    
	    getListUtilisateur().add(u);
	    
	    tx.commit();
	    
	}

	/**
	 * <p>Modifie un utilisateur</p>
	 * @param u
	 * @throws HibernateException
	 */
	public void modifierUtilisateur(Utilisateur u) throws HibernateException {
	
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		Object o = HibernateUtil.getSessionFactory().getCurrentSession().load(Utilisateur.class, u.getId_utilisateur());
		
		HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(o);
		
		getListUtilisateur().remove(u);
	    
	    tx.commit();
		
	}
	
	/**
	 * <p>Supprime un utilisateur</p>
	 * @param u
	 * @throws HibernateException
	 */
	public void removeUtilisateur(Utilisateur u) throws HibernateException {
	
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		Object o = HibernateUtil.getSessionFactory().getCurrentSession().load(Utilisateur.class, u.getId_utilisateur());
		
		HibernateUtil.getSessionFactory().getCurrentSession().delete(o);
		
		getListUtilisateur().remove(u);
	    
	    tx.commit();
		
	}
	
	@Destroy
	public void destroy() {}


}
