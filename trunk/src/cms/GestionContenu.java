package cms;

import java.util.Date;

import javax.faces.application.FacesMessage;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;

/**
 * 
 * Composant Seam permettant de gérer les contenus
 *
 */
@Name("gestionContenu")
public class GestionContenu {

	/**
	 * Injection du composant {@link SessionUtilisateur}<br />
	 */
	@In @Out(scope=ScopeType.SESSION)
	private SessionUtilisateur sessionUtilisateur;

	public SessionUtilisateur getSessionUtilisateur() {
		return sessionUtilisateur;
	}

	public void setSessionUtilisateur(SessionUtilisateur sessionUtilisateur) {
		this.sessionUtilisateur = sessionUtilisateur;
	}

	public GestionContenu() {}

	public void majContenu() throws HibernateException {
		sessionUtilisateur.setListContenu(DataUtil.chargeContenu());
	}

	public Rubrique creerRacine(){
		Contenu root = new Rubrique("racine");
		root.setParent((Rubrique) root);
		root.setEtatContenu(EtatContenu.NON_PUBLIE);
		return (Rubrique) root;
	}

	/**
	 * Création d'un nouveau Contenu 
	 * @param contenu
	 * @throws HibernateException
	 */
	public void addContenu(Contenu contenu)throws HibernateException {

		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		contenu.setAuteur(sessionUtilisateur.getUtilisateur());
		contenu.setDateCreation(new Date());
		contenu.setEtatContenu(EtatContenu.EN_ATTENTE);
		contenu.setNiveauAcces(NiveauAccesContenu.PUBLIC);
		contenu.setDateMaj(new Date());

		HibernateUtil.getSessionFactory().getCurrentSession().save(contenu);

		sessionUtilisateur.getListContenu().add(contenu);

		tx.commit();

	}

	/**
	 * Methode qui vérifie si un contenu est une rubrique
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public Boolean estRubrique(Contenu contenu){
		return (contenu instanceof Rubrique);
	}

	/**
	 * <p>Methode permettant de vérifier les droits de l'utilisateur courant<br />
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
		if (estRubrique(contenu)){
			rTemp = (Rubrique) contenu;
			// Si l'utilisateur courant est dans la liste des gestionnaires du contenu, il a le droit
			if(rTemp.getListGestionnaire().contains(getSessionUtilisateur().getUtilisateur())){
				resultat = true;
			}
		} else {
			//C'est pas une Rubrique, on vérifie que l'utilisateur a le droit sur le parent du contenu en quesiotn
			if (contenu.getParent().getListGestionnaire().contains(sessionUtilisateur.getUtilisateur()) ||
					sessionUtilisateur.getUtilisateur().isAdmin()) {
				resultat = true;
			}
		}
		return resultat;
	}

	/**
	 * <p>Methode permattant de dépublier un contenu.<br />
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
					// on dépublie la rubrique
					rubrique.setEtatContenu(EtatContenu.NON_PUBLIE);
					//on vérifie si elle a des enfants
					if(rubrique.getListEnfant().size() != 0) {
						//parcours de tous les enfants de la rubrique
						for(Contenu c : rubrique.getListEnfant()){
							c.setEtatContenu(EtatContenu.NON_PUBLIE);
						}
					}
				}
			} else {
				//C'est pas une rubrique, on vérifie qu'il n'est pas déjà dépublié
				if(contenu.getEtatContenu().equals(EtatContenu.NON_PUBLIE)){
					throw new ContenuException("Ce contenu est déjà dépublié ! ");
				}else {
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
		Rubrique rubrique = null;
		//Si l'utilisateur courant a le droit 
		if(aLeDroit(contenu)) {
			//Si le contenu est une rubrique
			if(estRubrique(contenu)){
				rubrique = (Rubrique)contenu;
				// on verifie que la rubrique n'est pas déjà publié
				if(rubrique.getEtatContenu().equals(EtatContenu.PUBLIE)){
					throw new ContenuException("Cette rubrique est déjà publiée ! ");
				} else {
					// on publie la rubrique
					rubrique.setEtatContenu(EtatContenu.PUBLIE);
					//on vérifie si elle a des enfants
					if(rubrique.getListEnfant().size() != 0) {
						//parcours de tous les enfants de la rubrique
						for(Contenu c : rubrique.getListEnfant()){
							c.setEtatContenu(EtatContenu.PUBLIE);
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
			throw new ContenuException("Vous n'avez pas le droit de publier ce contenu ! ");
		}
	}

	/**
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
	 *<p> Methode permettant de supprimer un contenu
	 * @param contenu</p>
	 */
	public void removeContenu(Contenu contenu) throws ContenuException {
		// TODO Auto-generated method stub
		if(sessionUtilisateur.getListContenu().contains(contenu)){
			sessionUtilisateur.getListContenu().remove(contenu);
		}else {
			throw new ContenuException("L'élément de titre : "+contenu.getTitreContenu()+" n'exite pas");
		}    
	}


	@Destroy
	public void destroy(){}

}