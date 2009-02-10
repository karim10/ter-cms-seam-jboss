package cms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

@SuppressWarnings("serial")
@Name("gestionUtilisateur")
@Scope(ScopeType.SESSION)
public class GestionUtilisateur implements GestionUtilisateurLocal,Serializable{

	@DataModel
	private List<Utilisateur> listUtilisateur;
	
	public void setListUtilisateur(ArrayList<Utilisateur> listUtilisateur) {
		this.listUtilisateur = listUtilisateur;
	}
	
	public List<Utilisateur> getListUtilisateur() {		
		return this.listUtilisateur;
	}

	@Create
	@Factory("listUtilisateur")
	public void maj(){
		chargeUtilisateurs();
	}
	
	@SuppressWarnings("unchecked")
	public void chargeUtilisateurs() throws HibernateException {
		 
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    listUtilisateur = (List<Utilisateur>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery(" from UtilisateurAbstrait u").list();
	  
	}
	
	public void addUtilisateur(Utilisateur u) throws HibernateException {
		
	    Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    
	    HibernateUtil.getSessionFactory().getCurrentSession().save(u);
	    
	    tx.commit();
	    
	}

	public void removeUtilisateur(Utilisateur u) throws HibernateException {
	
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		HibernateUtil.getSessionFactory().getCurrentSession().delete(u);
	    
	    tx.commit();
		
	}
	
	@Destroy
	public void destroy() {}


}
