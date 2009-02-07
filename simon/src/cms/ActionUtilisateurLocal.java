package cms;

public interface ActionUtilisateurLocal {

	public String inscrire();
	
	public Boolean isAdmain();
	
	public Boolean isAuteur(Rubrique u);
	
	public Boolean isRedacteur(Rubrique u);
	
	public Boolean isGestionnaire(Rubrique u);
}
