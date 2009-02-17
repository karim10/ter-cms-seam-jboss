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

	@Create
	@Factory("listUtilisateur")
	public void maj(){
		listUtilisateur = DataUtil.chargeUtilisateurs();
	}
	
	public void addUtilisateur(Utilisateur u) throws HibernateException {
		
	    Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    
	    HibernateUtil.getSessionFactory().getCurrentSession().save(u);
	    
	    getListUtilisateur().add(u);
	    
	    tx.commit();
	    
	}

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
