package cms;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class GestionUtilisateur implements GestionUtilisateurLocal{

	private List<Utilisateur> listUtilisateur;
	
	public void setListUtilisateur(ArrayList<Utilisateur> listUtilisateur) {
		this.listUtilisateur = listUtilisateur;
	}
	
	public List<Utilisateur> getListUtilisateur() {		
		return this.listUtilisateur;
	}

	@SuppressWarnings("unchecked")
	public static List<Utilisateur> chargeUtilisateurs() throws HibernateException {
		
		List<Utilisateur> listU;
		
		/** Getting the Session Factory and session */
	    SessionFactory session = HibernateUtil.getSessionFactory();
	    Session sess = session.getCurrentSession();
	    
	    /** Starting the Transaction */
	    Transaction tx = sess.beginTransaction();
		
	    listU = (List<Utilisateur>)sess.createCriteria(Utilisateur.class).list();
	    
	    
	    tx.commit();
	    sess.close();
	    
	    return listU;
	}
	
	public void addUtilisateur(Utilisateur u) throws HibernateException {
		
		/** Getting the Session Factory and session */
	    SessionFactory session = HibernateUtil.getSessionFactory();
	    Session sess = session.getCurrentSession();
	    
	    /** Starting the Transaction */
	    Transaction tx = sess.beginTransaction();
		
	    sess.save(u);
	    
	    tx.commit();
	    sess.close();
	    
	}

	public void removeUtilisateur(Utilisateur u) throws HibernateException {
	
		/** Getting the Session Factory and session */
	    SessionFactory session = HibernateUtil.getSessionFactory();
	    Session sess = session.getCurrentSession();
	    
	    /** Starting the Transaction */
	    Transaction tx = sess.beginTransaction();
		
	    sess.delete(u);
	    
	    tx.commit();
	    sess.close();
		
	}

}
