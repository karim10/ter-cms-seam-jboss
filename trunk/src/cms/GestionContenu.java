package cms;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;

@Name("gestionContenu")
public class GestionContenu {
	
	@In @Out(scope=ScopeType.SESSION)
	private SessionUtilisateur sessionUtilisateur;
	
	public GestionContenu() {}
	
	public void majContenu() throws HibernateException {
		sessionUtilisateur.setListContenu(DataUtil.chargeContenu());
	}

	public void addContenu(Contenu contenu)throws HibernateException {
		
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
	    
	    contenu.setAuteur(sessionUtilisateur.getUtilisateur());
	    contenu.setDateCreation(new Date());
	    contenu.setEtatContenu(EtatContenu.EN_ATTENTE);
	    contenu.setNiveauAcces(NiveauAccesContenu.PUBLIC);
	    contenu.setDateMaj(new Date());
	    
	    HibernateUtil.getSessionFactory().getCurrentSession().save(contenu);
	    
	    sessionUtilisateur.getListContenu().add(contenu);
	    
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