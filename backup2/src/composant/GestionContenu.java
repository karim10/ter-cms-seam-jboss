package composant;

import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.hibernate.HibernateException;
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
import util.HibernateUtil;
import entite.Article;
import entite.Contenu;
import exception.ContenuException;
import entite.EtatContenu;
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

	/**
	 * Injection du composant {@link SessionUtilisateur}<br />
	 */
	@In @Out(scope=ScopeType.SESSION)
	private SessionUtilisateur sessionUtilisateur;
	
	
	@DataModel
	private List<Contenu> listContenu;
	
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
		listContenu = DataUtil.chargeContenu();
	}
	
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

	public GestionContenu() {}

	/**
	 * Crée un nouveau Contenu 
	 * @param contenu
	 * @throws HibernateException
	 */
	public Boolean addContenu(Contenu contenu)throws HibernateException {
		
		contenu.setAuteur((Utilisateur) sessionUtilisateur.getUtilisateur());
		
		contenu.setDateCreation(new Date());
		contenu.setDateMaj(new Date());
		
		contenu.setEtatContenu(EtatContenu.EN_ATTENTE);
		
		getListContenu().add(contenu);
		contenu.getParent().getListEnfant().add(contenu);
		
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);

		HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu.getParent());
		
		tx.commit();
		
		return true;

	}

	/**
	 *<p> Modifie un contenu
	 * @param contenu
	 * @throws HibernateException, ContenuException</p>
	 */
	public Boolean modifierContenu(Contenu contenu) throws HibernateException, ContenuException {
		Long id = contenu.getId_contenu();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		Contenu c = (Contenu)HibernateUtil.getSessionFactory().getCurrentSession().load(Contenu.class,id);
		if(contenu!=null){
			if(aLeDroit(c)){
				Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(contenu);
				tx.commit();
				for(int i=0 ;i<getListContenu().size(); i++){
					if(getListContenu().get(i).getId_contenu() == id){
						getListContenu().remove(i);
						contenu.setDateMaj(new Date());
						getListContenu().add(i,contenu);
						return true;
					}
				}
			}
			return false;
		} else {
			throw new ContenuException("L'élément n'exite pas");
		}   
	}
	
	
	/**
	 *<p> Supprime un contenu
	 * @param contenu
	 * @throws HibernateException, ContenuException</p>
	 */
	public void removeContenu(Contenu contenu) throws HibernateException, ContenuException {
		Long id = contenu.getId_contenu();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		Contenu c = (Contenu)HibernateUtil.getSessionFactory().getCurrentSession().load(Contenu.class,id);
		if(contenu!=null){
			if(aLeDroit(c)){
				Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
				HibernateUtil.getSessionFactory().getCurrentSession().delete(c);
				tx.commit();
				for(Contenu ctn : getListContenu()){
					if(ctn.getId_contenu() == id){
						getListContenu().remove(contenu);
						return;
					}
				}
			}
		} else {
			throw new ContenuException("L'élément n'exite pas");
		}   
	}
	
	public void saveChange(ValueChangeEvent e){
		modifierContenu(contenu);
	}
	
	/**
	 * <p>Vérifie les droits de l'utilisateur courant<br />
	 * @param contenu
	 * @return {@link Boolean}
	 * </p>
	 */
	public Boolean aLeDroit(Contenu contenu){
		Rubrique rTemp = null;
		Boolean resultat = false;

		// si l'utilisateur courant est admin il a le droit
		if(sessionUtilisateur.getUtilisateur().getAdmin()){
			resultat = true;
		}
		//si le contenu est une rubrique
		else if (estRubrique(contenu)){
			rTemp = (Rubrique) contenu;
			// Si l'utilisateur courant est dans la liste des gestionnaires du contenu, il a le droit
			if(sessionUtilisateur.getUtilisateur().isGestionnaire(rTemp)){
				resultat = true;
			}
		} else {
			//C'est pas une Rubrique, on vérifie que l'utilisateur a le droit sur le parent du contenu en question
			if(sessionUtilisateur.getUtilisateur().isGestionnaire(contenu.getParent()) ||
					sessionUtilisateur.getUtilisateur().getAdmin()){
				resultat = true;
			}
		}
		return resultat;
	}

	/**
	 * <p>Dépublie un contenu.<br />
	 * L'utilisateur courant doit avoir les droits necessaires,<br />
	 * Sinon une exception est levée.
	 * Si le contenu est déjà dépublié, une exception est levée <br />
	 * Sinon le contenu est dépublié
	 * @param contenu
	 * @throws ContenuException
	 * </p>
	 */
	public void depublierContenu(Contenu contenu) throws ContenuException {

		//Si l'utilisateur courant a le droit 
		if(aLeDroit(contenu)) {
			contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
		}else {
			//L'utilisateur n'a pas les droits nécessaires
			throw new ContenuException("Vous n'avez pas le droit de dépublier ce contenu ! ");
		}
	}

	/**
	 * <p>Methode permattant de publier un contenu.<br />
	 * L'utilisateur courant doit avoir les droits necessaires,<br />
	 * Sinon une exception est levée.
	 * Si le contenu est déjà publié, une exception est levée <br />
	 * Sinon le contenu est publié
	 * @param contenu
	 * @throws ContenuException
	 * </p>
	 */
	public void publierContenu(Contenu contenu) throws ContenuException {

		//Si l'utilisateur courant a le droit 
		if(aLeDroit(contenu)) {
			contenu.setEtatContenu(EtatContenu.PUBLIE);
		}else {
			//L'utilisateur n'a pas les droits nécessaires
			throw new ContenuException("Vous n'avez pas le droit de publier ce contenu ! ");
		}
	}	/**
	 * <p>Methode permattant de mettre à la corbeille un contenu.<br />
	 * L'utilisateur courant doit avoir les droits necessaires,<br />
	 * Sinon une exception est levée.
	 * Si le contenu est déjà dans la corbeille, une exception est levée <br />
	 * Sinon le contenu est mis dans la corbeille
	 * @param contenu
	 * @throws ContenuException
	 * </p>
	 */
	public void mettreCorbeille(Contenu contenu) throws ContenuException {

		//Si l'utilisateur courant a le droit 
		if(aLeDroit(contenu)) {
			contenu.setEtatContenu(EtatContenu.CORBEILLE);
		}else {
			//L'utilisateur n'a pas les droits nécessaires
			throw new ContenuException("Vous n'avez pas le droit de mettre ce contenu à la poubelle ! ");
		}
	}

	
	/**
	 * Vérifie si le contenu est une rubrique
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estRubrique(Contenu contenu){
		return contenu instanceof Rubrique;
	}
	
	/**
	 * Vérifie si le contenu est une nouvelle
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estNouvelle(Contenu contenu ){
		return contenu instanceof Nouvelle;
	}
	
	/**
	 * Vérifie si le contenu est un article
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estArticle(Contenu contenu){
		return contenu instanceof Article;
	}
	
	public boolean estRoot(Rubrique rubrique){
		return (rubrique.getTitreContenu().equals("ROOT"));
	}
	
	/**
	 * <p>retourne la liste des rubriques sans la rubrique en parametre.<br />
	 * retourne une exception si le contenu en parametre n'est pas une rubrique</p>
	 * @param contenu
	 * @return Liste de Rubrique
	 */
	@SuppressWarnings("unchecked")
	public List<Rubrique> chargeParent(Contenu contenu,Utilisateur utilisateur){
		List<Rubrique> lu;
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		if(contenu instanceof Rubrique || contenu!=null){
		  lu = (List<Rubrique>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Rubrique r where r.id_contenu!= :id")
			.setParameter("id", contenu.getId_contenu())
			.list();
		} 
		else {
			lu = (List<Rubrique>)HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Rubrique r")
			.list();	
		}
		for(Rubrique r : lu){
			if(!utilisateur.droitModifierContenu(r))lu.remove(r);
		}
		return lu;
	}
	
	/**
	 * <p>mise à jour de contenu avec la BD</p>
	 */
	public void majContenu(){
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		contenu = (Contenu)HibernateUtil.getSessionFactory().getCurrentSession().get(Contenu.class,contenu.getId_contenu());
	}
	
	/**
	 * <p>sauve la liste de contenu redacteur</p>
	 * @param utilisateur
	 */
	public void saveListContenuRedacteur(Utilisateur utilisateur){
		//on ajoute et supprime les redacteurs des rubriques si modification
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		for(Rubrique r : DataUtil.chargeRubrique()){
			if(r.getListRedacteur().contains(utilisateur) && !utilisateur.getContenuRedacteur().contains(r)){
				r.getListRedacteur().remove(utilisateur);
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(r);
			} else
			if(!r.getListRedacteur().contains(utilisateur) && utilisateur.getContenuRedacteur().contains(r)){
				r.getListRedacteur().add(utilisateur);
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(r);
			}	
		}
		HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(utilisateur);
		tx.commit();
	}
	
	/**
	 * <p>sauve la liste de contenu redacteur</p>
	 * @param utilisateur
	 */
	public void saveListContenuGestionnaire(Utilisateur utilisateur){
		//on ajoute et supprime les redacteurs des rubriques si modification
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		for(Rubrique r : DataUtil.chargeRubrique()){
			if(r.getListGestionnaire().contains(utilisateur) && !utilisateur.getContenuGestionnaire().contains(r)){
				r.getListGestionnaire().remove(utilisateur);
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(r);
			} else
			if(!r.getListGestionnaire().contains(utilisateur) && utilisateur.getContenuGestionnaire().contains(r)){
				r.getListGestionnaire().add(utilisateur);
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(r);
			}	
		}
		HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(utilisateur);
		tx.commit();
	}
	
	@Destroy
	public void destroy(){}

}