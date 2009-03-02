package entite;



import javax.persistence.Entity;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@SuppressWarnings("serial")
@Entity
@Name("utilisateur")
@Table(name="utilisateur")
public class Utilisateur extends UtilisateurAbstrait implements IUtilisateur{

	public Utilisateur () {}

	public Utilisateur(String login, String nom, String prenom,
			String motDePasse, String email, Boolean compteActive, Boolean admin) {
		this.setLogin(login);
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setMotDePasse(motDePasse);
		this.setEmail(email);
		this.setCompteActive(compteActive);
		this.setAdmin(admin);
	}

	/**
	 * @param contenu
	 * @return vrai si l'utilisateur est gestionnaire du contenu
	 * 		   faux sinon
	 */
	
	public boolean isGestionnaire(Contenu contenu){
		if(getContenuGestionnaire()==null || getContenuGestionnaire().isEmpty())return false;
		if(contenu.getParent()==null)return false;
		if(contenu instanceof Rubrique){
			if(getContenuGestionnaire().contains(contenu))
			return true;
		} 
		else{
			if(getContenuGestionnaire().contains(contenu.getParent()))
			return true;
		}
		return false;
	}
	
	/**
	 * @param contenu
	 * @return vrai si l'utilisateur est rédacteur du contenu
	 * 		   faux sinon
	 */
	public boolean isRedacteur(Contenu contenu){
		if(getContenuRedacteur()==null || getContenuRedacteur().isEmpty())return false;
		if(contenu.getParent()==null)return false;
		if(contenu instanceof Rubrique){
			if(getContenuRedacteur().contains(contenu))
			return true;
		} 
		else{
			if(getContenuRedacteur().contains(contenu.getParent()))
			return true;
		}
		return false;
	}
	
	/**
	 * @param contenu
	 * @return vrai si l'utilisateur est auteur du contenu
	 * 		   faux sinon
	 */
	public boolean isAuteur(Contenu contenu){
		if(getContenuAuteur()==null || getContenuAuteur().isEmpty())return false;
		if(getContenuAuteur().contains(contenu))return true;
		return false;
	}
	
	public Boolean droitModifierContenu(Contenu contenu){
		return (getAdmin() || isGestionnaire(contenu) 
				|| (isRedacteur(contenu) && isAuteur(contenu)));
	}
}
