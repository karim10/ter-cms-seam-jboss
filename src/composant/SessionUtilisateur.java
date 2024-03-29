package composant;


import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

import util.DataUtil;

import entite.IUtilisateur;
import entite.Rubrique;

@Name("sessionUtilisateur")
@Scope(ScopeType.SESSION)
public class SessionUtilisateur {

	private IUtilisateur utilisateur;
	
	public SessionUtilisateur() {}

	public SessionUtilisateur(IUtilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}


	public IUtilisateur getUtilisateur() {
		return utilisateur;
	}
	
	public void setUtilisateur(IUtilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	
	
	/**
	 * Verifie les droits d'acces d'un utilisateur
	 * @return vrai si gestionnaire, redacteur et/ou admin d'au moins un contenu
	 *         faux sinon
	 */
	public Boolean accesBackend(){
		if(getUtilisateur().isAdmin())return true;
		List<Rubrique> l = DataUtil.chargeRubrique();
		for(Rubrique r : l){
			if(r.getListGestionnaire().contains(getUtilisateur()))return true;
			if(r.getListRedacteur().contains(getUtilisateur()))return true;
		}
		return false;
	}
	
}
