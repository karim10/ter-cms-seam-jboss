package composant;

import java.util.Date;
import java.util.List;

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
import entite.EtatContenu;
import entite.Nouvelle;
import entite.Rubrique;
import entite.Utilisateur;
import exception.ContenuException;

/**
 * 
 * Composant Seam permettant de gérer les contenus
 *
 */
@Name("gestionRubrique")
@Scope(ScopeType.SESSION)
public class GestionContenuBack {

	/**
	 * Injection du composant {@link SessionUtilisateur}<br />
	 */
	@In @Out(scope=ScopeType.SESSION)
	private SessionUtilisateur sessionUtilisateur;
	
	
	@DataModel("listRubrique")
	private List<Rubrique> listRubrique;
	
	@DataModel("listArticle")
	private List<Article> listArticle;
	
	@DataModel("listNouvelle")
	private List<Nouvelle> listNouvelle;

	@DataModelSelection(value="listRubrique")
	@Out(required=false)
	private Contenu rubrique;
	
	@DataModelSelection(value="listArticle")
	@Out(required=false)
	private Contenu article;
	
	@DataModelSelection(value="listNouvelle")
	@Out(required=false)
	private Contenu nouvelle;
	/**
	 * <p>Crée la listContenu lors de l'initialisation du composant 
	 * à partir du contenu de la base de donnée.</p>
	 */
	@Factory("listRubrique")
	public void initRubrique(){
		setListRubrique(DataUtil.chargeRubrique());
	}
	/**
	 * <p>Crée la listContenu lors de l'initialisation du composant 
	 * à partir du contenu de la base de donnée.</p>
	 */
	@Factory("listArticle")
	public void initArticle(){
		setListArticle(DataUtil.chargeArticle());
	}
	/**
	 * <p>Crée la listContenu lors de l'initialisation du composant 
	 * à partir du contenu de la base de donnée.</p>
	 */

	@Factory("listNouvelle")
	public void initNouvelle(){
		setListNouvelle(DataUtil.chargeNouvelle());
	}
	
	@Create
	@Factory
	public void init(){
		initRubrique();
		initArticle();
		initNouvelle();
	}
	
	public SessionUtilisateur getSessionUtilisateur() {
		return sessionUtilisateur;
	}

	public void setSessionUtilisateur(SessionUtilisateur sessionUtilisateur) {
		this.sessionUtilisateur = sessionUtilisateur;
	}

	public GestionContenuBack() {}

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
		
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		HibernateUtil.getSessionFactory().getCurrentSession().save(contenu);
		
		if(estRubrique(contenu)){
			getListRubrique().add((Rubrique) contenu);
		}else if(estArticle(contenu)){
			getListArticle().add((Article) contenu);
		}else if(estNouvelle(contenu)){
			getListNouvelle().add((Nouvelle) contenu);
		}
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
				
				if(estRubrique(contenu)){
					for(int i=0 ;i<getListRubrique().size(); i++){
						if(getListRubrique().get(i).getId_contenu() == id){
							getListRubrique().remove(i);
							contenu.setDateMaj(new Date());
							getListRubrique().add(i,(Rubrique) contenu);
							return true;
						}
					}
				}else if(estArticle(contenu)){
					for(int i=0 ;i<getListArticle().size(); i++){
						if(getListArticle().get(i).getId_contenu() == id){
							getListArticle().remove(i);
							contenu.setDateMaj(new Date());
							getListArticle().add(i,(Article) contenu);
							return true;
						}
					}
				}else if(estNouvelle(contenu)){
					for(int i=0 ;i<getListNouvelle().size(); i++){
						if(getListNouvelle().get(i).getId_contenu() == id){
							getListNouvelle().remove(i);
							contenu.setDateMaj(new Date());
							getListNouvelle().add(i,(Nouvelle) contenu);
							return true;
						}
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
				if(estRubrique(contenu)){
					for(Rubrique rub : getListRubrique()){
						if(rub.getId_contenu() == id){
							getListRubrique().remove(contenu);
							return;
						}
					}
				}else if(estArticle(contenu)){
					for(Article art : getListArticle()){
						if(art.getId_contenu() == id){
							getListArticle().remove(contenu);
							return;
						}
					}
				}else if(estNouvelle(contenu)){
					for(Article art : getListArticle()){
						if(art.getId_contenu() == id){
							getListArticle().remove(contenu);
							return;
						}
					}
				}
				
			}
		} else {
			throw new ContenuException("L'élément n'exite pas");
		}   
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
	 * @param listRubrique the listRubrique to set
	 */
	public void setListRubrique(List<Rubrique> listRubrique) {
		this.listRubrique = listRubrique;
	}

	/**
	 * @return the listRubrique
	 */
	public List<Rubrique> getListRubrique() {
		return listRubrique;
	}

	/**
	 * @param listArticle the listArticle to set
	 */
	public void setListArticle(List<Article> listArticle) {
		this.listArticle = listArticle;
	}

	/**
	 * @return the listArticle
	 */
	public List<Article> getListArticle() {
		return listArticle;
	}

	/**
	 * @param listNouvelle the listNouvelle to set
	 */
	public void setListNouvelle(List<Nouvelle> listNouvelle) {
		this.listNouvelle = listNouvelle;
	}

	/**
	 * @return the listNouvelle
	 */
	public List<Nouvelle> getListNouvelle() {
		return listNouvelle;
	}

	/**
	 * @param rubrique the rubrique to set
	 */
	public void setRubrique(Rubrique rubrique) {
		this.rubrique = rubrique;
	}
	/**
	 * @return the rubrique
	 */
	public Contenu getRubrique() {
		return rubrique;
	}
	/**
	 * @param article the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}
	/**
	 * @return the article
	 */
	public Contenu getArticle() {
		return article;
	}
	/**
	 * @param nouvelle the nouvelle to set
	 */
	public void setNouvelle(Contenu nouvelle) {
		this.nouvelle = nouvelle;
	}
	/**
	 * @return the nouvelle
	 */
	public Contenu getNouvelle() {
		return nouvelle;
	}
	@Destroy
	public void destroy(){}

}