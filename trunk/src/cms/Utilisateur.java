package cms;



import javax.persistence.Entity;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@SuppressWarnings("serial")
@Entity
@Name("utilisateur")
@Table(name="utilisateur")
public class Utilisateur extends UtilisateurAbstrait {

	public Utilisateur () {}

	public Utilisateur(String login, String nom, String prenom,
			String motDePasse, String email, Boolean compteActive, Boolean admin) {
		this.setLogin(login);
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setMotDePasse(motDePasse);
		this.setEmail(email);
		this.setCompteActive(compteActive);
		this.setAdmin(admin);
	}
}
