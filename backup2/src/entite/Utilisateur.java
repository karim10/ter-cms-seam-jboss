package entite;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;

@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "login"))
@Name("utilisateur")
public class Utilisateur implements IUtilisateur, Serializable{
	
	public Utilisateur(){}
	
	private long id_utilisateur;
	
	private String login;
	
	private String nom;
	
	private String prenom;
	
	private String motDePasse;
	
	private String email;
	
	private boolean compteActive;
	
	private String website;
	
	private boolean admin;
	
	private boolean accesBackend;
	
	private String confirmation;
	
	
	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

	@Id @Column(name="ID_UTILISATEUR")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId_utilisateur() {
		return id_utilisateur;
	}

	public void setId_utilisateur(long id_utilisateur) {
		this.id_utilisateur = id_utilisateur;
	}

	@Column(name="LOGIN",nullable=false, unique=true, length=20)
	@NotNull 
	@Length(max=20)
	@Pattern(regex="[a-zA-Z]?[a-zA-Z0-9]+", 
         message="le nom d'utilisateur doit commencer par une lettre et contenir seulement des lettres ou des chiffres")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	@Column(name="NOM", updatable=true, nullable=false, length=50)
	@NotNull
	@Pattern(regex="[a-zA-Z]+", 
	         message="Le Nom doit seulement contenir des lettres")
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Pattern(regex="[a-zA-Z]+", 
	         message="Le prénom doit seulement contenir des lettres")
	@Column(name="PRENOM", updatable=true, nullable=false, length=50)
	@NotNull
	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	@Column(name="MOT_DE_PASSE", updatable=true, length=20, nullable=false)
	@NotNull
	@Length(min=8)
	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	
	/*@Pattern(regex="[a-zA-Z0-9]+[@][a-zA-Z0-9]+[.][a-zA-Z]+", 
	         message="L'email n'est pas valide")
	*/
	@Length(max = 30)
	@Column(name="EMAIL", updatable=true, unique=true, length=30, nullable=false)
	@NotNull
	@Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="COMPTE_ACTIVE", updatable=true, nullable=false)
	public boolean isCompteActive() {
		return compteActive;
	}

	public void setCompteActive(boolean compteActive) {
		this.compteActive = compteActive;
	}

	@Column(name="WEBSITE", updatable=true, length=50,unique=false)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Column(name="ADMIN", updatable=true, nullable=false)
	public boolean isAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Column(name="ACCESS_BACKEND",updatable=true, nullable=false)
	public boolean isAccesBackend() {
		return accesBackend;
	}

	public void setAccesBackend(boolean accesBackend) {
		this.accesBackend = accesBackend;
	}
}
