package composant;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
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
import entite.ContenuException;
import entite.EtatContenu;
import entite.NiveauAccesContenu;
import entite.Nouvelle;
import entite.Rubrique;
import entite.TypeContenu;
import entite.Utilisateur;

/**
 * 
 * Composant Seam permettant de gérer les contenus
 *
 */
@Name("gestionContenu")
@Scope(ScopeType.SESSION)
public class GestionContenu {

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
	public void addContenu(Contenu contenu)throws HibernateException {

		if(contenu instanceof Rubrique)contenu.setTypeContenu(TypeContenu.RUBRIQUE);
		else if(contenu instanceof Article)contenu.setTypeContenu(TypeContenu.ARTICLE);
		else if(contenu instanceof Nouvelle)contenu.setTypeContenu(TypeContenu.NOUVELLE);
		
		contenu.setAuteur((Utilisateur) sessionUtilisateur.getUtilisateur());
		
		contenu.setDateCreation(new Date());
		contenu.setDateMaj(new Date());
		
		contenu.setEtatContenu(EtatContenu.EN_ATTENTE);
		
		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		HibernateUtil.getSessionFactory().getCurrentSession().save(contenu);

		getListContenu().add(contenu);

		tx.commit();

	}

	/**
	 *<p> Modifie un contenu
	 * @param contenu
	 * @throws HibernateException, ContenuException</p>
	 */
	public void modifierContenu(Contenu contenu) throws HibernateException, ContenuException {
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
						return;
					}
				}
			}
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
		if(sessionUtilisateur.getUtilisateur().isAdmin()){
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
					sessionUtilisateur.getUtilisateur().isAdmin()){
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
		Rubrique rubrique = null;
		//Si l'utilisateur courant a le droit 
		if(aLeDroit(contenu)) {
			//Si le contenu est une rubrique
			if(estRubrique(contenu)){
				rubrique = (Rubrique)contenu;
				// on verifie que la rubrique n'est pas déjà dépublié
				if(rubrique.getEtatContenu().equals(EtatContenu.NON_PUBLIE)){
					//FacesMessages.instance().add(new FacesMessage("Cette rubrique est déjà dépubliée ! "));
					throw new ContenuException("Cette rubrique est déjà dépubliée ! ");
				} else {
					// on sauve son état
					rubrique.setEtatSauve(rubrique.getEtatContenu());
					// on dépublie la rubrique
					rubrique.setEtatContenu(EtatContenu.NON_PUBLIE);
					//on vérifie si elle a des enfants
					if(rubrique.getListEnfant().size() != 0) {
						//parcours de tous les enfants de la rubrique
						for(Contenu c : rubrique.getListEnfant()){
								//appel recursif sur l'enfant
								depublierContenu(c);							
						}
					}
				}
			} else {
				//C'est pas une rubrique, on vérifie qu'il n'est pas déjà dépublié
				if(contenu.getEtatContenu().equals(EtatContenu.NON_PUBLIE)){
					throw new ContenuException("Ce contenu est déjà dépublié ! ");
				}else {
					//on sauve son état
					contenu.setEtatSauve(contenu.getEtatContenu());
					//c'est bon on le dépublie
					contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
				}
			}

		}else {
			//L'utilisateur n'a pas les droits nécessaires
			//FacesMessages.instance().add(new FacesMessage("Vous n'avez pas le droit de dépublier ce contenu ! "));
			throw new ContenuException("Vous n'avez pas le droit de dépublier ce contenu ! ");
		}
	}

	/**
	 * <p>Publie un contenu.<br />
	 * L'utilisateur courant doit avoir les droits necessaires,<br />
	 * Sinon une exception est levée.
	 * Si le contenu est déjà publié, une exception est levée <br />
	 * Sinon le contenu est publié
	 * @param contenu
	 * @throws ContenuException
	 * </p>
	 */
	public void publierContenu(Contenu contenu) throws ContenuException {
		Rubrique rubrique = null;
		//Si l'utilisateur courant a le droit 
		if(aLeDroit(contenu)) {
			//Si le contenu est une rubrique
			if(estRubrique(contenu)){
				rubrique = (Rubrique)contenu;
				// on verifie que la rubrique n'est pas déjà publié
				if(rubrique.getEtatContenu().equals(EtatContenu.PUBLIE)){
					//FacesMessages.instance().add(new FacesMessage("Cette rubrique est déjà publiée ! "));
					throw new ContenuException("Cette rubrique est déjà publiée ! ");
				} else {
					
					// on publie la rubrique
					rubrique.setEtatContenu(EtatContenu.PUBLIE);
					//on vérifie si elle a des enfants
					if(rubrique.getListEnfant().size() != 0) {
						//parcours de tous les enfants de la rubrique
						for(Contenu c : rubrique.getListEnfant()){
							// on verifie son etata sauvé
							if(c.etatCotenuModifie()){
								//on restore son etat sauvé
								c.setEtatContenu(contenu.getEtatSauve());
							}
							if(estRubrique(c)){
								//appel recursif sur l'enfant
								publierContenu(c);
							}							
						}
					}
				}
			} else {
				//C'est pas une rubrique, on vérifie qu'il n'est pas déjà publié
				if(contenu.getEtatContenu().equals(EtatContenu.PUBLIE)){
					throw new ContenuException("Ce contenu est déjà publié ! ");
				}else {
					
					//c'est bon on le publie
					contenu.setEtatContenu(EtatContenu.PUBLIE);
				}
			}

		}else {
			//L'utilisateur n'a pas les droits nécessaires
			//FacesMessages.instance().add(new FacesMessage("Vous n'avez pas le droit de dépublier ce contenu ! "));
			throw new ContenuException("Vous n'avez pas le droit de dépublier ce contenu ! ");
		}
	}
	/**
	 * <p>Met à la corbeille un contenu.<br />
	 * L'utilisateur courant doit avoir les droits necessaires,<br />
	 * Sinon une exception est levée.
	 * Si le contenu est déjà dans la corbeille, une exception est levée <br />
	 * Sinon le contenu est mis dans la corbeille
	 * @param contenu
	 * @throws ContenuException
	 * </p>
	 */
	public void mettreCorbeille(Contenu contenu) throws ContenuException {
		Rubrique rubrique = null;
		//Si l'utilisateur courant a le droit 
		if(aLeDroit(contenu)) {
			//Si le contenu est une rubrique
			if(estRubrique(contenu)){
				rubrique = (Rubrique)contenu;
				// on verifie que la rubrique n'est pas déjà dans la corbeille
				if(rubrique.getEtatContenu().equals(EtatContenu.CORBEILLE)){
					throw new ContenuException("Cette rubrique est déjà dans la corbeille ! ");
				} else {
					// on met la rubrique dans la corbeille
					rubrique.setEtatContenu(EtatContenu.CORBEILLE);
					//on vérifie si elle a des enfants
					if(rubrique.getListEnfant().size() != 0) {
						//parcours de tous les enfants de la rubrique
						for(Contenu c : rubrique.getListEnfant()){
							c.setEtatContenu(EtatContenu.CORBEILLE);

						}
					}
				}
			} else {
				//C'est pas une rubrique, on vérifie qu'il n'est pas déjà dans la corbeille
				if(contenu.getEtatContenu().equals(EtatContenu.CORBEILLE)){
					throw new ContenuException("Ce contenu est déjà dans la corbeille ! ");
				}else {
					//c'est bon on le met dans la corbeille
					contenu.setEtatContenu(EtatContenu.CORBEILLE);
				}
			}

		}else {
			//L'utilisateur n'a pas les droits nécessaires
			throw new ContenuException("Vous n'avez pas le droit de mettre à la corbeille ce contenu ! ");
		}
	}
	
	/**
	 * Vérifie si le contenu est une rubrique
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estRubrique(Contenu contenu){
		return contenu.getTypeContenu().equals(TypeContenu.RUBRIQUE);
	}
	
	/**
	 * Vérifie si le contenu est une nouvelle
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estNouvelle(Contenu contenu ){
		return contenu.getTypeContenu().equals(TypeContenu.NOUVELLE);
	}
	
	/**
	 * Vérifie si le contenu est un article
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estArticle(Contenu contenu){
		return contenu.getTypeContenu().equals(TypeContenu.ARTICLE);
	}

	@Destroy
	public void destroy(){}

}