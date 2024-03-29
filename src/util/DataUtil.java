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

		return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu c where c.titreContenu != 'ROOT' ").list();

	}

	public static List<Article> chargeArticle() throws HibernateException {

		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		return (List<Article>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Article a").list();

	}

	public static List<Rubrique> chargeRubrique() throws HibernateException {

		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		return (List<Rubrique>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Rubrique r where r.titreContenu != 'ROOT' ").list();

	}

	public static List<Rubrique> chargeRubriqueAvecRoot() throws HibernateException {

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
	
	public static List<IUtilisateur> chargeUtilisateursSansAdmin() throws HibernateException {

		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		return (List<IUtilisateur>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Utilisateur u where u.admin = false").list();
	}
	
	public static Contenu chargeRoot() throws HibernateException {

		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		return (Contenu)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Rubrique r where r.titreContenu = 'ROOT' ").uniqueResult();
	}


	/**
	 * Methode retournant la liste des 5 derniers contenus publi�s 
	 * @return
	 * @throws HibernateException
	 */
	public static List<Contenu> chargeDernierePublication() throws HibernateException {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Contenu c where c.etatContenu='publie' and c.titreContenu != 'ROOT' order by c.id_contenu desc").setMaxResults(5).list();
	}
	
	/**
	 * Methode qui charge que les contenus publi�s
	 * @return
	 * @throws HibernateException
	 */
	public static List<Contenu> chargeContenuPublie() throws HibernateException {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery(" from Contenu c where c.titreContenu != 'ROOT' and c.etatContenu = 'publie' order by c.id_contenu desc").list();
	}
	
	/**
	 * Methode qui charge que les articles publi�s
	 * @return
	 * @throws HibernateException
	 */
	public static List<Contenu> chargeArticlePublie() throws HibernateException {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery(" from Article a where a.etatContenu = 'publie' order by a.id_contenu desc").list();
	}
	/**
	 * Methode qui charge que les rubriques publi�es
	 * @return
	 * @throws HibernateException
	 */
	public static List<Contenu> chargeRubriquePublie() throws HibernateException {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery(" from Rubrique r where r.titreContenu != 'ROOT' and r.etatContenu = 'publie' order by r.id_contenu desc").list();
	}
	
	public static List<Contenu> chargePublication() throws HibernateException {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		return (List<Contenu>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery(" from Contenu c where c.titreContenu != 'ROOT' and c.etatContenu = 'publie' order by r.id_contenu desc").list();
	}
	
	
	public static List<File> chargeFiles(Article article) throws HibernateException {

		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		return (List<File>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from File f where :article.files.Id_File = f.Id_File")
		.setParameter("article", article)
		.list();    
	}

}
