package cms;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("utilisateur")
@Scope(ScopeType.SESSION)
@Table(name="utilisateur")
public class Utilisateur extends UtilisateurAbstrait {

	private static final long serialVersionUID = -3655311686394408087L;

	public Utilisateur () {}
	
	public Utilisateur(String nom, String prenom, String login,
			String motDePasse, String email, Boolean compteActive,
			String website, Boolean admin, List<Contenu> contenuAuteur,
			List<Rubrique> contenuRedacteur,
			List<Rubrique> contenuGestionnaire) {
		setAdmin(admin);
		setCompteActive(compteActive);
		setContenuAuteur(contenuAuteur);
		setContenuGestionnaire(contenuGestionnaire);
		setContenuRedacteur(contenuRedacteur);
		setEmail(email);
		setLogin(login);
		setMotDePasse(motDePasse);
		setNom(nom);
		setPrenom(prenom);
		setWebsite(website);
	}

}
