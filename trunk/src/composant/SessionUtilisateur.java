package composant;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import util.DataUtil;

import entite.IUtilisateur;
import entite.Rubrique;
import entite.Utilisateur;

@Name("sessionUtilisateur")
@Scope(ScopeType.SESSION)
public class SessionUtilisateur {

	/**
	 * injection du composant {@link Utilisateur}
	 */
	@In
	private IUtilisateur utilisateur;
	
	private Boolean accesBackend;
		
	public SessionUtilisateur() {}

	@Create
	public void init(){
		accesBackend = accesBackend();
	}
	
	public SessionUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}


	public IUtilisateur getUtilisateur() {
		return utilisateur;
	}
	
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	
	
	public Boolean getAccesBackend() {
		return accesBackend;
	}


	public void setAccesBackend(Boolean accesBackend) {
		this.accesBackend = accesBackend;
	}


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
