package entite;

/**
 * Interface d�finissant de l'utilisateur 
 */

public interface IUtilisateur {

	public Boolean isAdmin();
	public Boolean isRedacteur(Rubrique rubrique);
	public Boolean isGestionnaire(Rubrique rubrique);
	//public String getInfos();

}
