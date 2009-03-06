package util;

import java.util.List;

import org.hibernate.HibernateException;
import org.jboss.seam.annotations.Name;

import entite.Article;
import entite.Contenu;
import entite.File;
import entite.IUtilisateur;
import entite.Nouvelle;
import entite.Rubrique;

@Name("datautil")
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
	
	public static List<Rubrique> chargeRubrique() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Rubrique>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Rubrique r").list();
	    	    
	}
	
	public static List<Nouvelle> chargeNouvelle() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Nouvelle>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Nouvelle n").list();
	    	    
	}

	public static List<IUtilisateur> chargeUtilisateurs() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<IUtilisateur>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Utilisateur u").list();
	    	    
	}
	
	public static List<Contenu> chargeContenuMembre() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu u where u.").list();
	    	    
	}
	
	public static List<Contenu> chargeContenuPrivee() throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu u where u.").list();
	    	    
	}
	
	public static List<File> chargeFiles(Article article) throws HibernateException {
		
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
	    return (List<File>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from File f where :article.files.Id_File = f.Id_File")
	    .setParameter("article", article)
	    .list();
	    	    
	}
}
