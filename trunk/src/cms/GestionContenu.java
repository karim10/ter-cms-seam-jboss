package cms;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;

@Name("gestionContenu")
public class GestionContenu {
	
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
	 * <p>Methode permattant de dépublier un contenu
	 * Si le contenu est déjà dépublié, une exception est levée
	 * Sinon le contenu est dépublié
	 * </p>
	 */
	public void depublierContenu(Contenu contenu) throws ContenuException {
		Boolean aLeDroit =false;
		Rubrique rubrique = null;
		// Si l'utilisateur courant est admin, il a le droit
		if(sessionUtilisateur.getUtilisateur().isAdmin()){
			if(contenu instanceof Rubrique){
				rubrique = (Rubrique) contenu;
			}
			aLeDroit = true;
		}
		 else {
		// Sinon Si l'utilisateur courant est redacteur et/ou 
		// gestionnaire de la rubrique parent, il a le droit
			if(contenu instanceof Rubrique){
				rubrique = (Rubrique) contenu;
				if(rubrique.getListRedacteur().contains(sessionUtilisateur.getUtilisateur())){
					aLeDroit = true;
				} else {
					if(rubrique.getListGestionnaire().contains(sessionUtilisateur.getUtilisateur())){
						aLeDroit = true;
					}
				}
			}
			else{
				if(contenu.getParent().getListRedacteur().contains(sessionUtilisateur.getUtilisateur())){
					aLeDroit = true;
				} else {
					if(contenu.getParent().getListGestionnaire().contains(sessionUtilisateur.getUtilisateur())){
						aLeDroit = true;
					}
				}
			}
		}
		// Si l'utilisateur a le droit
		if(aLeDroit){
			if(contenu.getEtatContenu().equals(EtatContenu.NON_PUBLIE)){
				throw new ContenuException("Elément déjà dépublié");
			} else {
				// on dépublie le contenu
				contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
				// parcours de tous les enfants de la rubrique pour leur dépublication 
				if(contenu instanceof Rubrique){
				for(int i=0; i< rubrique.getListEnfant().size();i++){
					if(rubrique.getListEnfant().get(i) instanceof Rubrique){
						depublierContenu(rubrique);
					}
					rubrique.getListEnfant().get(i).setEtatContenu(EtatContenu.NON_PUBLIE);
				}
				}
				else{
					contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
				}
			}
		} else {
			throw new ContenuException("Vous n'avez pas le droit de dépublication ");
		}
	}

	/**
	 * <p>Methode permattant de publier un contenu
	 * Si le contenu est déjà publié, une exception est levée
	 * Sinon le contenu est publié
	 * </p>
	 */
	public void publierContenu(Contenu contenu) throws ContenuException {
		Boolean aLeDroit =false;
		Rubrique rubrique = null;
		// Si l'utilisateur courant est admin, il a le droit
		if(sessionUtilisateur.getUtilisateur().isAdmin()){
			aLeDroit = true;
		}
		if(contenu instanceof Rubrique){
			rubrique = (Rubrique) contenu;
			if(rubrique.getListRedacteur().contains(sessionUtilisateur.getUtilisateur())){
				aLeDroit = true;
			}
			if(rubrique.getListGestionnaire().contains(sessionUtilisateur.getUtilisateur())){
				aLeDroit = true;
			}
		} else {
			if(!contenu.getParent().getEtatContenu().equals(EtatContenu.PUBLIE)){
				throw new ContenuException("Veillez publier la rubrique : "+contenu.getParent().getTitreContenu());
			}else {
				contenu.setEtatContenu(EtatContenu.PUBLIE);
			}
		}

		if(aLeDroit){
			if(contenu.getEtatContenu().equals(EtatContenu.PUBLIE)){
				throw new ContenuException("Elément déjà publié");
			} else {
				// on publie la rubrique
				contenu.setEtatContenu(EtatContenu.PUBLIE);
				//parcours de tous les enfants de la rubrique
				for(int i=0; i< rubrique.getListEnfant().size();i++){
					if(rubrique.getListEnfant().get(i) instanceof Rubrique){
						publierContenu(rubrique);
					}
					rubrique.getListEnfant().get(i).setEtatContenu(EtatContenu.PUBLIE);
				}
			}
		}else {
			throw new ContenuException("Vous n'avez pas le droit de publication ");
		}
	}
	
	/**
	 * Methode permettant de mettre à la corbeille un contenu
	 * @param contenu
	 */
	public void mettreCorbeille(Contenu contenu) throws ContenuException {
		Boolean aLeDroit =false;
		Rubrique rubrique = null;
		// Si l'utilisateur courant est admin, il a le droit
		if(sessionUtilisateur.getUtilisateur().isAdmin()){
			aLeDroit = true;
		}
		if(contenu instanceof Rubrique){
			rubrique = (Rubrique) contenu;
			if(rubrique.getListGestionnaire().contains(sessionUtilisateur.getUtilisateur())){
				aLeDroit = true;
			}
		} else {
			if(!contenu.getParent().getEtatContenu().equals(EtatContenu.CORBEILLE)){
				throw new ContenuException("Veillez dépublier la rubrique : "+contenu.getParent().getTitreContenu());
			}else {
				contenu.setEtatContenu(EtatContenu.CORBEILLE);
			}
		}

		if(aLeDroit){
			if(contenu.getEtatContenu().equals(EtatContenu.CORBEILLE)){
				throw new ContenuException("Elément déjà publié");
			} else {
				// on publie la rubrique
				contenu.setEtatContenu(EtatContenu.CORBEILLE);
				//parcours de tous les enfants de la rubrique
				for(int i=0; i< rubrique.getListEnfant().size();i++){
					if(rubrique.getListEnfant().get(i) instanceof Rubrique){
						mettreCorbeille(rubrique);
					}
					rubrique.getListEnfant().get(i).setEtatContenu(EtatContenu.CORBEILLE);
				}
			}
		}else {
			throw new ContenuException("Vous n'avez pas le droit de Mise à la corbeille");
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
			throw new ContenuException("L'élément de titre "+contenu.getTitreContenu()+" n'exite pas");
		}    
	}


	@Destroy
	public void destroy(){}

}