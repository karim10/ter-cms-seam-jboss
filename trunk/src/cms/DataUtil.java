package cms;

import java.util.List;

import org.hibernate.HibernateException;

@SuppressWarnings("unchecked")
public class DataUtil {
	
	
	public static List<Contenu> chargeContenu() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu c").list();
	    	    
	}
	
	public static List<Article> chargeArticle() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Article>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Article a").list();
	    	    
	}
	
	public static List<Nouvelle> chargeNouvelle() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Nouvelle>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Nouvelle n").list();
	    	    
	}

	public static List<Utilisateur> chargeUtilisateurs() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Utilisateur>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu u").list();
	    	    
	}
	
	public static List<Contenu> chargeContenuMembre() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu u where u.").list();
	    	    
	}
	
	public static List<Contenu> chargeContenuPrivee() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu u where u.").list();
	    	    
	}
}
