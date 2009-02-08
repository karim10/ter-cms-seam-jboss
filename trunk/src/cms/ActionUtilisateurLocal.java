package cms;

public interface ActionUtilisateurLocal {

	
	public Boolean isAdmain();
	
	public Boolean isAuteur(Rubrique u);
	
	public Boolean isRedacteur(Rubrique u);
	
	public Boolean isGestionnaire(Rubrique u);
	
	public boolean authentification();
	
	public String inscription();
	
}
