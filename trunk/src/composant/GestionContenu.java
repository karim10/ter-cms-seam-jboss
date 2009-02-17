package composant;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
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
import entite.Contenu;
import entite.ContenuException;
import entite.EtatContenu;
import entite.NiveauAccesContenu;
import entite.Rubrique;
import entite.TypeContenu;
import entite.Utilisateur;

/**
 * 
 * Composant Seam permettant de g�rer les contenus
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
	
	@Create
	public void init(){
		listContenu = DataUtil.chargeContenu();
	}
	
	@DataModelSelection
	@Out(required=false)
	private Contenu contenu;
	
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


	public void majContenu() throws HibernateException {
		setListContenu(DataUtil.chargeContenu());
	}

	/**
	 * Cr�ation d'un nouveau Contenu 
	 * @param contenu
	 * @throws HibernateException
	 */
	public void addContenu(Contenu contenu)throws HibernateException {

		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		contenu.setAuteur((Utilisateur) sessionUtilisateur.getUtilisateur());
		contenu.setDateCreation(new Date());
		contenu.setEtatContenu(EtatContenu.EN_ATTENTE);
		contenu.setNiveauAcces(NiveauAccesContenu.PUBLIC);
		contenu.setDateMaj(new Date());
		contenu.setTypeContenu(TypeContenu.ARTICLE);
		contenu.setParent((Rubrique)getListContenu().get(0));

		HibernateUtil.getSessionFactory().getCurrentSession().save(contenu);

		getListContenu().add(contenu);

		tx.commit();

	}

	/**
	 * Methode qui v�rifie si un contenu est une rubrique
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public Boolean estRubrique(Contenu contenu){
		return (contenu instanceof Rubrique);
	}

	/**
	 * <p>Methode permettant de v�rifier les droits de l'utilisateur courant<br />
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
			//C'est pas une Rubrique, on v�rifie que l'utilisateur a le droit sur le parent du contenu en question
			if(sessionUtilisateur.getUtilisateur().isGestionnaire(contenu.getParent()) ||
					sessionUtilisateur.getUtilisateur().isAdmin()){
				resultat = true;
			}
		}
		return resultat;
	}

	/**
	 * <p>Methode permattant de d�publier un contenu.<br />
	 * L'utilisateur courant doit avoir les droits necessaires,<br />
	 * Sinon une exception est lev�e.
	 * Si le contenu est d�j� d�publi�, une exception est lev�e <br />
	 * Sinon le contenu est d�publi�
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
				// on verifie que la rubrique n'est pas d�j� d�publi�
				if(rubrique.getEtatContenu().equals(EtatContenu.NON_PUBLIE)){
					//FacesMessages.instance().add(new FacesMessage("Cette rubrique est d�j� d�publi�e ! "));
					throw new ContenuException("Cette rubrique est d�j� d�publi�e ! ");
				} else {
					// on sauve son �tat
					rubrique.setEtatSauve(rubrique.getEtatContenu());
					// on d�publie la rubrique
					rubrique.setEtatContenu(EtatContenu.NON_PUBLIE);
					//on v�rifie si elle a des enfants
					if(rubrique.getListEnfant().size() != 0) {
						//parcours de tous les enfants de la rubrique
						for(Contenu c : rubrique.getListEnfant()){
								//appel recursif sur l'enfant
								depublierContenu(c);							
						}
					}
				}
			} else {
				//C'est pas une rubrique, on v�rifie qu'il n'est pas d�j� d�publi�
				if(contenu.getEtatContenu().equals(EtatContenu.NON_PUBLIE)){
					throw new ContenuException("Ce contenu est d�j� d�publi� ! ");
				}else {
					//on sauve son �tat
					contenu.setEtatSauve(contenu.getEtatContenu());
					//c'est bon on le d�publie
					contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
				}
			}

		}else {
			//L'utilisateur n'a pas les droits n�cessaires
			//FacesMessages.instance().add(new FacesMessage("Vous n'avez pas le droit de d�publier ce contenu ! "));
			throw new ContenuException("Vous n'avez pas le droit de d�publier ce contenu ! ");
		}
	}

	/**
	 * <p>Methode permattant de publier un contenu.<br />
	 * L'utilisateur courant doit avoir les droits necessaires,<br />
	 * Sinon une exception est lev�e.
	 * Si le contenu est d�j� publi�, une exception est lev�e <br />
	 * Sinon le contenu est publi�
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
				// on verifie que la rubrique n'est pas d�j� publi�
				if(rubrique.getEtatContenu().equals(EtatContenu.PUBLIE)){
					//FacesMessages.instance().add(new FacesMessage("Cette rubrique est d�j� publi�e ! "));
					throw new ContenuException("Cette rubrique est d�j� publi�e ! ");
				} else {
					
					// on publie la rubrique
					rubrique.setEtatContenu(EtatContenu.PUBLIE);
					//on v�rifie si elle a des enfants
					if(rubrique.getListEnfant().size() != 0) {
						//parcours de tous les enfants de la rubrique
						for(Contenu c : rubrique.getListEnfant()){
							// on verifie son etata sauv�
							if(c.etatCotenuModifie()){
								//on restore son etat sauv�
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
				//C'est pas une rubrique, on v�rifie qu'il n'est pas d�j� publi�
				if(contenu.getEtatContenu().equals(EtatContenu.PUBLIE)){
					throw new ContenuException("Ce contenu est d�j� publi� ! ");
				}else {
					
					//c'est bon on le publie
					contenu.setEtatContenu(EtatContenu.PUBLIE);
				}
			}

		}else {
			//L'utilisateur n'a pas les droits n�cessaires
			//FacesMessages.instance().add(new FacesMessage("Vous n'avez pas le droit de d�publier ce contenu ! "));
			throw new ContenuException("Vous n'avez pas le droit de d�publier ce contenu ! ");
		}
	}
	/**
	 * <p>Methode permattant de mettre � la corbeille un contenu.<br />
	 * L'utilisateur courant doit avoir les droits necessaires,<br />
	 * Sinon une exception est lev�e.
	 * Si le contenu est d�j� dans la corbeille, une exception est lev�e <br />
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
				// on verifie que la rubrique n'est pas d�j� dans la corbeille
				if(rubrique.getEtatContenu().equals(EtatContenu.CORBEILLE)){
					throw new ContenuException("Cette rubrique est d�j� dans la corbeille ! ");
				} else {
					// on met la rubrique dans la corbeille
					rubrique.setEtatContenu(EtatContenu.CORBEILLE);
					//on v�rifie si elle a des enfants
					if(rubrique.getListEnfant().size() != 0) {
						//parcours de tous les enfants de la rubrique
						for(Contenu c : rubrique.getListEnfant()){
							c.setEtatContenu(EtatContenu.CORBEILLE);

						}
					}
				}
			} else {
				//C'est pas une rubrique, on v�rifie qu'il n'est pas d�j� dans la corbeille
				if(contenu.getEtatContenu().equals(EtatContenu.CORBEILLE)){
					throw new ContenuException("Ce contenu est d�j� dans la corbeille ! ");
				}else {
					//c'est bon on le met dans la corbeille
					contenu.setEtatContenu(EtatContenu.CORBEILLE);
				}
			}

		}else {
			//L'utilisateur n'a pas les droits n�cessaires
			throw new ContenuException("Vous n'avez pas le droit de mettre � la corbeille ce contenu ! ");
		}
	}

	/**
	 *<p> Methode permettant de supprimer un contenu
	 * @param contenu</p>
	 */
	public void modifierContenu(Contenu contenu) throws ContenuException {
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
					getListContenu().add(i,contenu);
					return;
				}
			}
		}
		}else {
			throw new ContenuException("L'�l�ment n'exite pas");
		}   
	}
	
	/**
	 *<p> Methode permettant de modifier un contenu
	 * @param contenu</p>
	 */
	public String modifier(){
		return "/modifier.xhtml" ;
	}
	
	public void removeContenu(Contenu contenu) throws ContenuException {
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
		}else {
			throw new ContenuException("L'�l�ment n'exite pas");
		}   
	}
	
	public boolean estRubrique(){
		return contenu.getTypeContenu().equals(TypeContenu.RUBRIQUE);
	}
	
	public boolean estNouvelle(){
		return contenu.getTypeContenu().equals(TypeContenu.NOUVELLE);
	}
	
	public boolean estArticle(){
		return contenu.getTypeContenu().equals(TypeContenu.ARTICLE);
	}

	@Destroy
	public void destroy(){}

}