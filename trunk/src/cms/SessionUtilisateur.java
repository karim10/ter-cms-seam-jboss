package cms;

import java.util.List;

import org.hibernate.annotations.Loader;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

@Name("sessionUtilisateur")
@Scope(ScopeType.SESSION)
public class SessionUtilisateur {

	@In
	private Utilisateur utilisateur;
	
	@DataModel
	private List<Contenu> listContenu;

	
	
	public SessionUtilisateur() {}

	
	public SessionUtilisateur(Utilisateur utilisateur, List<Contenu> listContenu) {
		super();
		this.utilisateur = utilisateur;
		this.listContenu = listContenu;
	}


	public Utilisateur getUtilisateur() {
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
