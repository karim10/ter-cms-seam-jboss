package composant;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

import util.DataUtil;

import entite.Contenu;
import entite.IUtilisateur;
import entite.Utilisateur;

@Name("sessionUtilisateur")
@Scope(ScopeType.SESSION)
public class SessionUtilisateur {

	@In
	private IUtilisateur utilisateur;
	
	@DataModel
	private List<Contenu> listContenu = DataUtil.chargeContenu();

	
	
	public SessionUtilisateur() {}

	
	public SessionUtilisateur(Utilisateur utilisateur, List<Contenu> listContenu) {
		this.utilisateur = utilisateur;
		this.listContenu = listContenu;
	}


	public IUtilisateur getUtilisateur() {
		return utilisateur;
	}
	
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<Contenu> getListContenu() {
		return listContenu;
	}

	public void setListContenu(List<Contenu> listContenu) {
		this.listContenu = listContenu;
	}
	
}
