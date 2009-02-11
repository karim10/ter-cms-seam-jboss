package cms;

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

@Name("gestionUtilisateur")
@Scope(ScopeType.SESSION)
public class GestionUtilisateur{

	@DataModel
	private List<Utilisateur> listUtilisateur = new ArrayList<Utilisateur>();
	
	public GestionUtilisateur(){}
	
	public void setListUtilisateur(ArrayList<Utilisateur> listUtilisateur) {
		this.listUtilisateur = listUtilisateur;
	}
	
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
