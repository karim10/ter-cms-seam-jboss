package cms;

import java.util.List;

public interface GestionUtilisateurLocal {
	
	public List<Utilisateur> getListUtilisateur();
	
	public void addUtilisateur(Utilisateur u);
	
	public void removeUtilisateur(Utilisateur u);
}
