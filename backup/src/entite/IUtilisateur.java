package entite;

/**
 * Interface définissant de l'utilisateur 
 */

public interface IUtilisateur {

	public Boolean getAdmin();
	public Boolean isRedacteur(Rubrique rubrique);
	public Boolean isGestionnaire(Rubrique rubrique);
	//public String getInfos();

}
