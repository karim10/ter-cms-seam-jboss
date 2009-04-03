package entite;

public interface IUtilisateur {
	
	public long getId_utilisateur();
	public void setId_utilisateur(long id_utilisateur);
	
	public String getLogin();
	public void setLogin(String login);
	
	public String getNom();
	public void setNom(String nom);
	
	public String getPrenom();
	public void setPrenom(String prenom);

	public String getMotDePasse();
	public void setMotDePasse(String motDePasse);
	
	public String getConfirmation();
	public void setConfirmation(String confirmation);
	
	public String getEmail();
	public void setEmail(String email);

	public boolean isCompteActive();
	public void setCompteActive(boolean compteActive);

	public String getWebsite();
	public void setWebsite(String website);

	public boolean isAdmin();
	public void setAdmin(boolean admin);
	
	public boolean isAccesBackend();
	public void setAccesBackend(boolean accesBackend);
	
}
