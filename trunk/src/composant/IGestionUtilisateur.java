package composant;

import entite.Contenu;
import entite.Utilisateur;

public interface IGestionUtilisateur {

	public void addContenu(Contenu contenu);
	
	public void modifierUtilisateur(Utilisateur u);
	
	public void removeUtilisateur(Utilisateur u);
}
