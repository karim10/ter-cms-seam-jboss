package cms;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

@Name("gestionContenu")
@Scope(ScopeType.STATELESS)
public class GestionContenu implements GestionContenuLocal,Serializable {

	private static final long serialVersionUID = -4743948469219937768L;

	@In	
	private Utilisateur utilisateur;
	
	private List<Contenu> listContenu;
	
	public GestionContenu() {}

	public List<Contenu> getListContenu() {
		return this.listContenu;
	}
	
	public void setListContenu(List<Contenu> listContenu) {
		this.listContenu = listContenu;
	}

	@Create
	public void init(){
		listContenu = chargeContenu();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Contenu> chargeContenu() throws HibernateException {
		
		List<Contenu> listC;
		
		/** Getting the Session Factory and session */
	    SessionFactory session = HibernateUtil.getSessionFactory();
	    Session sess = session.getCurrentSession();
	    
	    /** Starting the Transaction */
	    Transaction tx = sess.beginTransaction();
		
	    listC = (List<Contenu>)sess.createCriteria(Contenu.class).list();
	    
	    return listC;
	}

	public void addContenu(Contenu contenu) {
		
		/** Getting the Session Factory and session */
	    SessionFactory session = HibernateUtil.getSessionFactory();
	    Session sess = session.getCurrentSession();
	    
	    /** Starting the Transaction */
	    Transaction tx = sess.beginTransaction();
	    
	    contenu.setAuteur(utilisateur);
	    contenu.setDateCreation(new Date());
	    contenu.setEtatContenu(EtatContenu.EN_ATTENTE);
	    contenu.setNiveauAcces(NiveauAccesContenu.PUBLIC);
	    contenu.setDateMaj(new Date());
	    sess.save(contenu);
	    
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

}
