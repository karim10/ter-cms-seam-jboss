package entite;

/**
 * Interface définissant de l'utilisateur 
 */

public interface IUtilisateur {

	public boolean getAdmin();
	public boolean isRedacteur(Contenu rubrique);
	public boolean isGestionnaire(Contenu rubrique);
	public boolean isAuteur(Contenu contenu);
	public Boolean droitModifierContenu(Contenu contenu);
	//public String getInfos();

}
