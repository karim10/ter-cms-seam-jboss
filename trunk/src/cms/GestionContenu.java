package cms;

import java.io.Serializable;
import java.util.Date;
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

@Name("gestionContenu")
@Scope(ScopeType.SESSION)
public class GestionContenu implements GestionContenuLocal, Serializable{
	
	private static final long serialVersionUID = -7565868612122554426L;

	@In	
	private Utilisateur utilisateur;
	
	@DataModel
	private List<Contenu> listContenu;
	
	public GestionContenu() {}

	public List<Contenu> getListContenu() {
		return this.listContenu;
	}
	
	public void setListContenu(List<Contenu> listContenu) {
		this.listContenu = listContenu;
	}

	@Create
	@Factory("listContenu")
	public void maj(){
		chargeContenu();
	}
	
	@SuppressWarnings("unchecked")
	public void chargeContenu() throws HibernateException {
		
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    this.listContenu= (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu c").list();
	    	    
	}

	public void addContenu(Contenu contenu)throws HibernateException {
		
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
	    
	    contenu.setAuteur(utilisateur);
	    contenu.setDateCreation(new Date());
	    contenu.setEtatContenu(EtatContenu.EN_ATTENTE);
	    contenu.setNiveauAcces(NiveauAccesContenu.PUBLIC);
	    contenu.setDateMaj(new Date());
	    
	    HibernateUtil.getSessionFactory().getCurrentSession().save(contenu);
	    
	    tx.commit();
	    
	    
	}

	public void depublierContenu(Contenu contenu) {
		contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
	}


	public void mettreCorbeille(Contenu contenu) {
		contenu.setEtatContenu(EtatContenu.CORBEILLE);
	}

	public void publierContenu(Contenu contenu) {
		contenu.setEtatContenu(EtatContenu.PUBLIE);
	}

	public void removeContenu(Contenu contenu) {
		// TODO Auto-generated method stub
		
	}
	
	@Destroy
	public void destroy(){}

}
