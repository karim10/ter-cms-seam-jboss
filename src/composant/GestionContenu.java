package composant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import util.DataUtil;
import util.FileUploadBean;
import util.HibernateUtil;
import entite.Contenu;
import exception.ContenuException;
import entite.Article;
import entite.EtatContenu;
import entite.File;
import entite.IUtilisateur;
import entite.Nouvelle;
import entite.Rubrique;
import entite.Utilisateur;

/**
 * 
 * Composant Seam permettant de gérer les contenus
 *
 */
@Name("gestionContenu")
@Scope(ScopeType.SESSION)
public class GestionContenu{

	public GestionContenu() {}

	/**
	 * Injection du composant {@link SessionUtilisateur}<br />
	 */
	@In
	private SessionUtilisateur sessionUtilisateur;

	@DataModel
	private List<Contenu> listContenu;

	@In(required=false)@Out(required=false)
	public FileUploadBean fileUploadBean;

	@DataModelSelection
	@Out(required=false)
	private Contenu contenu;

	/**
	 * <p>Crée la listContenu lors de l'initialisation du composant 
	 * à partir du contenu de la base de donnée.</p>
	 */
	@Create
	@Factory("listContenu")
	public void init(){
		listContenu = new LinkedList<Contenu>();
	}

	/**
	 * Crée un nouveau Contenu 
	 * @param contenu
	 * @throws HibernateException
	 */
	public void addContenu(Contenu contenu)throws HibernateException {
		// set le contenu
		contenu.setAuteur(sessionUtilisateur.getUtilisateur());
		contenu.setDateCreation(new Date());
		contenu.setDateMaj(new Date());
		contenu.setEtatContenu(EtatContenu.EN_ATTENTE);		
		// Transaction
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		// si logo, ajoute logo
		if(fileUploadBean.getLogo()!=null && fileUploadBean.getLogo().getData()!=null){
			// sauvegarde le logo dans la bd 
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(fileUploadBean.getLogo());
			contenu.setLogo(fileUploadBean.getLogo());
		}
		/*
		// si article et si fichiers à joindre, ajoute fichiers joints  
		if(estArticle(contenu)){
			if(fileUploadBean.getFiles()!= null){
				for(File f : fileUploadBean.getFiles()){
					// sauvegarde les fichiers dans la bd
					HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(f);
				}
				((Article)contenu).setFiles(fileUploadBean.getFiles());
			}
		}
		 */
		// si Rubrique, ajoute le createur de la rubrique en tant que redacteur de celle-ci
		if(estRubrique(contenu)){
			((Rubrique)contenu).addGestionnaire(sessionUtilisateur.getUtilisateur());
		}
		//
		// ajoute le contenu
		getListContenu().add(contenu);
		// mise à jour la liste du contenu enfant de la rubrique parent
		Rubrique contenuParent = contenu.getParent();
		contenuParent.addEnfant(contenu);	
		// sauvegarde dans la BD
		HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenuParent);
		HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
		tx.commit();
		// remise à null car fileUploadBean est de scope Session
		fileUploadBean = null;
	}

	/**
	 *<p> Modifie un contenu
	 * @param contenu
	 * @throws HibernateException, ContenuException</p>
	 */
	public void modifierContenu(Contenu contenu) throws HibernateException, ContenuException {
		if(contenu==null)throw new ContenuException("Le contenu est null");
		// Transaction 
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		// set si logo
		if(contenu.getLogo()!=null && contenu.getLogo().getData()!=null){
			// sauvegarde le logo dans la bd
			File l = contenu.getLogo();
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(l);
		}
		/*
		//set si article et si fichiers à joindre, set 
		if(estArticle(contenu) && ((Article)contenu).getFiles()!= null){
			// récupération des éventuelles fichiers joints à l'article 
			List<File> lf = DataUtil.chargeFiles(((Article)contenu));
			// suppression d'un fichier de la BD si il n'est plus joint à l'article aprés modification
			for(File f : lf){
				if(!((Article)contenu).getFiles().contains(f)){
					HibernateUtil.getSessionFactory().getCurrentSession().delete(f);
				}
			}
			// ajout ou mise à jour des fichiers à ajouter
			for(File f : ((Article)contenu).getFiles()){
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(f);
			}
		}
		 */
		// mise à jour de la liste de contenu 
		for(int i=0 ;i<getListContenu().size(); i++){
			if(getListContenu().get(i).getId_contenu() == contenu.getId_contenu()){
				// mise à jour de listContenu
				getListContenu().remove(i);
				contenu.setDateMaj(new Date());
				getListContenu().add(i,contenu);
				// mise à jour de contenu dans la bd
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
			}
		}
		tx.commit();
		// remise à null car fileUploadBean est de scope Session
		fileUploadBean = null;
	}

	public void modifierGestionnaireRedacteur(Rubrique contenu) throws Exception{
		modifierRedacteur(contenu);
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		reorganiserDroitsRedacteur(DataUtil.chargeRoot());
		tx.commit();
		modifierGestionnaire(contenu);
		Transaction tx2 = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		reorganiserDroitsGestionnaire(DataUtil.chargeRoot());
		tx2.commit();
	}

	/**
	 * 
	 * @param contenu
	 * @throws Exception 
	 */
	public void modifierGestionnaire(Contenu contenu) throws Exception{
		if(!estRubrique(contenu))throw new ContenuException("Le contenu doit etre une rubrique");
		// Transaction
		Transaction tx = null;
		try{
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			for(IUtilisateur u : ((Rubrique)contenu).getListGestionnaire()){
				u.setAccesBackend(true);
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(u);
			}
			recursiveModifierGestionnaire(((Rubrique)contenu).getListGestionnaire(), contenu);
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
			tx.commit();
		}
		catch (Exception e) { 
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
	}



	/**
	 * 
	 * @param contenu
	 * @throws Exception 
	 */
	public void modifierRedacteur(Contenu contenu) throws Exception{
		if(!estRubrique(contenu))throw new ContenuException("Le contenu doit etre une rubrique");
		// Transaction
		Transaction tx = null;
		try{
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			for(IUtilisateur u : ((Rubrique)contenu).getListRedacteur()){
				u.setAccesBackend(true);
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(u);
			}
			recursiveModifierRedacteur(((Rubrique)contenu).getListRedacteur(), contenu);
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
			tx.commit();
		}
		catch (Exception e) { 
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
	}


	/**
	 * <p>fonction qui ajoute une liste de gestionnaire à un Contenu et tous ces enfants de facon recursive.
	 * ATTENTION : une transaction doit etre ouverte pour avant d'appeler cette methode recursive
	 *          et un commit doit etre effectué ensuite</p>
	 * 
	 * @param lu
	 * @param contenu
	 */
	@SuppressWarnings("unchecked")
	public void recursiveModifierGestionnaire(List<IUtilisateur> lu, Contenu contenu){
		// exeption si contenu n'est pas une rubrique
		if(!estRubrique(contenu))throw new ContenuException("Le contenu doit etre une rubrique");
		// appel recursif si la liste des enfants du contenu contient des rubriques
		// suppression des gestionnaires , puis sauvegarde dans la BD
		// suppression des doublons
		HashSet h = new HashSet(lu);
		lu.clear();
		lu.addAll(h);
		for(Contenu c : ((Rubrique)contenu).getListEnfant()){
			if(estRubrique(c)){
				recursiveModifierGestionnaire(lu, c);
			}
		}
		List<IUtilisateur> l = DataUtil.chargeUtilisateurs();
		l.removeAll(lu);
		for(IUtilisateur u : l){
			if(((Rubrique)contenu).getListGestionnaire().contains(u)){
				((Rubrique)contenu).getListGestionnaire().remove(u);
			}
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
		}
		// ajout des rédacteurss si l'utilisateur ne l'est pas déjà, puis sauvegarde dans la BD
		for(IUtilisateur u : lu){
			if(!(((Rubrique)contenu).getListGestionnaire().contains(u))){
				((Rubrique)contenu).getListGestionnaire().add(u);
			}
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
		}
	}

	public void reorganiserDroitsGestionnaire(Contenu contenu) throws Exception{
		if(!estRubrique(contenu))throw new ContenuException("Le contenu doit etre une rubrique");


		for(Contenu c : ((Rubrique)contenu).getListEnfant()){
			if(estRubrique(c)){
				for(IUtilisateur u : ((Rubrique)c).getParent().getListGestionnaire()){
					if(!(((Rubrique)c).getListGestionnaire().contains(u))){
						((Rubrique)c).getListGestionnaire().add(u);
					}
				}
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(c);
				reorganiserDroitsGestionnaire(c);
			}
		}

	}

	/**
	 * <p>fonction qui ajoute une liste de gestionnaire à un Contenu et tous ces enfants de facon recursive.
	 * ATTENTION : une transaction doit etre ouverte pour avant d'appeler cette methode recursive
	 *          et un commit doit etre effectué ensuite</p>
	 * 
	 * @param lu
	 * @param contenu
	 */
	@SuppressWarnings("unchecked")
	public void recursiveModifierRedacteur(List<IUtilisateur> lu, Contenu contenu){
		// exeption si contenu n'est pas une rubrique
		if(!estRubrique(contenu))throw new ContenuException("Le contenu doit etre une rubrique");
		// appel recursif si la liste des enfants du contenu contient des rubriques
		// suppression des gestionnaires , puis sauvegarde dans la BD
		// suppression des doublons
		HashSet h = new HashSet(lu);
		lu.clear();
		lu.addAll(h);
		for(Contenu c : ((Rubrique)contenu).getListEnfant()){
			if(estRubrique(c)){
				recursiveModifierRedacteur(lu, c);
			}
		}
		List<IUtilisateur> l = DataUtil.chargeUtilisateurs();
		l.removeAll(lu);
		for(IUtilisateur u : l){
			if(((Rubrique)contenu).getListRedacteur().contains(u)){
				((Rubrique)contenu).getListRedacteur().remove(u);
			}
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
		}
		// ajout des rédacteurss si l'utilisateur ne l'est pas déjà, puis sauvegarde dans la BD
		for(IUtilisateur u : lu){
			if(!(((Rubrique)contenu).getListRedacteur().contains(u))){
				((Rubrique)contenu).getListRedacteur().add(u);
			}
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
		}
	}

	public void reorganiserDroitsRedacteur(Contenu contenu) throws Exception{
		if(!estRubrique(contenu))throw new ContenuException("Le contenu doit etre une rubrique");


		for(Contenu c : ((Rubrique)contenu).getListEnfant()){
			if(estRubrique(c)){
				for(IUtilisateur u : ((Rubrique)c).getParent().getListRedacteur()){
					if(!(((Rubrique)c).getListRedacteur().contains(u))){
						((Rubrique)c).getListRedacteur().add(u);
					}
				}
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(c);
				reorganiserDroitsRedacteur(c);
			}
		}

	}

	/**
	 *<p> Supprime un contenu
	 * @param contenu
	 * @throws Exception 
	 */
	public boolean removeContenu(Contenu contenu) throws Exception {
		if(contenu==null)throw new ContenuException("Le contenu est null");
		// Transaction
		Transaction tx = null;
		// démmarre la transaction
		try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			// récupération du contenu à supprimer
			Contenu contenuAsupprimer = (Contenu)HibernateUtil.getSessionFactory().getCurrentSession().load(Contenu.class, contenu.getId_contenu());
			// récupération de la rubrique parent du contenu à supprimer
			Rubrique cParent = (Rubrique)HibernateUtil.getSessionFactory().getCurrentSession().load(Rubrique.class,contenuAsupprimer.getParent().getId_contenu());
			// suppression du logo de la bd
			if(contenuAsupprimer.getLogo()!=null && contenuAsupprimer.getLogo().getData()!=null){
				File f = (File)HibernateUtil.getSessionFactory().getCurrentSession().load(File.class,contenuAsupprimer.getLogo().getId_File());
				HibernateUtil.getSessionFactory().getCurrentSession().delete(f);
				contenuAsupprimer.setLogo(null);
			}
			/*
			// si le contenu est un article, suppression des eventuelles fichiers joints
			if(estArticle(contenuAsupprimer)){
				if(((Article)contenuAsupprimer).getFiles()!= null){
					for(File file : ((Article)contenuAsupprimer).getFiles()){
						File f = (File)HibernateUtil.getSessionFactory().getCurrentSession().load(File.class,file.getId_File());
						HibernateUtil.getSessionFactory().getCurrentSession().delete(f);	
					}
					((Article)contenuAsupprimer).setFiles(null);
				}
			}
			 */
			// si le contenu est une rubrique, la rubrique parent du contenu à supprimer
			// devient la rubrique parent de tous les contenus enfants de la rubrique à supprimer
			if(estRubrique(contenuAsupprimer)){
				if(((Rubrique)contenuAsupprimer).getListEnfant()!= null){
					for(Contenu ctn :((Rubrique)contenuAsupprimer).getListEnfant()){
						Contenu cEnfant = (Contenu)HibernateUtil.getSessionFactory().getCurrentSession().load(Contenu.class, ctn.getId_contenu());
						cEnfant.setParent(cParent);
						cParent.addEnfant(cEnfant);
						HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(cEnfant);
					}
				}
				// set null pour supprimer le contenu à supprimer sans souci des contraintes
				((Rubrique)contenuAsupprimer).setListEnfant(null);
				((Rubrique)contenuAsupprimer).setListGestionnaire(null);
				((Rubrique)contenuAsupprimer).setListRedacteur(null);
			}
			contenuAsupprimer.setParent(null);
			contenuAsupprimer.setAuteur(null);
			// Mise à jour du parent du contenu à supprimer
			cParent.removeEnfant(contenuAsupprimer);
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(cParent);
			//Suppression du contenu à supprimer
			HibernateUtil.getSessionFactory().getCurrentSession().delete(contenuAsupprimer);
			// mise à jour de le rubrique parent

			// commit si element à été supprimé de listContenu
			tx.commit();
		} catch (Exception e) { 
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		} 
		// destruction du composant
		fileUploadBean = null;
		return true;
	}

	public void saveChange(ValueChangeEvent e){
		modifierContenu(contenu);
	}

	/**
	 * <p>retourne la liste des rubriques dont
	 * l'utilisateur a pas le droit d'ajout.<br />
	 * retourne une exception si le contenu en parametre n'est pas une rubrique</p>
	 * @param contenu
	 * @return Liste de Rubrique
	 * @throws Exception 
	 */
	public List<Rubrique> rubriquesDroitAjout() throws Exception{
		List<Rubrique> lu = new ArrayList<Rubrique>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		List<Rubrique> lr = (List<Rubrique>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Rubrique r").list();
		// suppression des rubrique si l'utilisateur n'a pas les droits nécessaire pour l'ajout de contenu
		for(Rubrique r : lr){
			if(droitAjouterContenu(r))lu.add(r);
		}
		return lu;
	}

	/**
	 * 
	 * @param utilisateur
	 * @param rubrique
	 * @return
	 * @throws Exception
	 */
	public Boolean droitAjouterContenu(Rubrique rubrique) throws Exception{
		return (sessionUtilisateur.getUtilisateur().isAdmin() 
				|| estGestionnaire(rubrique) 
				|| estRedacteur(rubrique)
		);
	}

	/**
	 * 
	 * @param utilisateur
	 * @param contenu
	 * @return
	 * @throws Exception
	 */
	public Boolean droitModifierContenu(Contenu contenu) throws Exception{
		return (sessionUtilisateur.getUtilisateur().isAdmin() 
				|| estGestionnaire(contenu) 
				|| (estRedacteur(contenu) && estAuteur(contenu))
		);
	}

	/**
	 * 
	 * @param utilisateur
	 * @param contenu
	 * @return
	 * @throws Exception
	 */
	public Boolean droitSupprimerContenu(Contenu contenu) throws Exception{
		return sessionUtilisateur.getUtilisateur().isAdmin();
	}

	/**
	 * 
	 * @param utilisateur
	 * @param contenu
	 * @return
	 * @throws Exception
	 */
	public Boolean droitPublicationContenu(Contenu contenu) throws Exception{
		return (sessionUtilisateur.getUtilisateur().isAdmin() 
				|| estGestionnaire(contenu)
		);
	}

	/**
	 * 
	 * @param contenu
	 * @return
	 * @throws Exception
	 */
	public Boolean droitDefinirGestionnaire(Contenu contenu) throws Exception{
		return (sessionUtilisateur.getUtilisateur().isAdmin() 
				|| estGestionnaire(contenu.getParent())
		);
	}

	/**
	 * 
	 * @param contenu
	 * @return
	 * @throws Exception
	 */
	public Boolean droitDefinirRedacteur(Contenu contenu) throws Exception{
		return (sessionUtilisateur.getUtilisateur().isAdmin() 
				|| estGestionnaire(contenu)
		);
	}

	public Boolean estAdmin(){
		return sessionUtilisateur.getUtilisateur().isAdmin();
	}

	/**
	 * 
	 * @param utilisateur
	 * @param contenu
	 * @return vrai si l'utilisateur est gestionnaire du contenu
	 * 		   faux sinon
	 * @throws Exception
	 */
	public boolean estGestionnaire(Contenu contenu){ 
		if(estRubrique(contenu)){
			return (((Rubrique)contenu).getListGestionnaire().contains(sessionUtilisateur.getUtilisateur()));
		}
		if(estArticle(contenu)){
			return contenu.getParent().getListGestionnaire().contains(sessionUtilisateur.getUtilisateur());
		}
		return false;
	}

	/**
	 * 
	 * @param utilisateur
	 * @param contenu
	 * @return vrai si l'utilisateur est rédacteur du contenu
	 * 		   faux sinon
	 * @throws Exception
	 */
	public boolean estRedacteur(Contenu contenu) throws Exception{
		if(estRubrique(contenu)){
			return ((Rubrique)contenu).getListRedacteur().contains(sessionUtilisateur.getUtilisateur());
		}
		return contenu.getParent().getListRedacteur().contains(sessionUtilisateur.getUtilisateur());
	}

	/**
	 * 
	 * @param utilisateur
	 * @param contenu
	 * @return vrai si l'utilisateur est auteur du contenu
	 * 		   faux sinon
	 * @throws Exception
	 */
	public boolean estAuteur(Contenu contenu) throws Exception{
		if(sessionUtilisateur.getUtilisateur()==null || contenu==null)throw new Exception("La liste de redacteur est null");
		return contenu.getAuteur().equals(sessionUtilisateur.getUtilisateur());
	}

	/**
	 * <p>Vérifie si le contenu est une rubrique</p>
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estRubrique(Contenu contenu){
		return contenu instanceof Rubrique;
	}

	/**
	 * <p>Vérifie si le contenu est une nouvelle<:p>
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estNouvelle(Contenu contenu ){
		return contenu instanceof Nouvelle;
	}

	/**
	 * <p>Vérifie si le contenu est un article</p>
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estArticle(Contenu contenu){
		return contenu instanceof Article;
	}

	/**
	 * <p>Vérifie si la rubrique est le ROOT : racine du CMS non modifiable</p>
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estRoot(Rubrique rubrique){
		return (rubrique.getTitreContenu().equals("ROOT"));
	}

	/**
	 * <p>Destruction du composant</p>
	 */
	@Destroy
	public void destroy(){}


	// GETTER SETTER

	public SessionUtilisateur getSessionUtilisateur() {
		return sessionUtilisateur;
	}

	public void setSessionUtilisateur(SessionUtilisateur sessionUtilisateur) {
		this.sessionUtilisateur = sessionUtilisateur;
	}

	public List<Contenu> getListContenu() {
		return listContenu;
	}

	public void setListContenu(List<Contenu> listContenu) {
		this.listContenu = listContenu;
	}

	public Contenu getContenu() {
		return contenu;
	}

	public void setContenu(Contenu contenu) {
		this.contenu = contenu;
	}

	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

}