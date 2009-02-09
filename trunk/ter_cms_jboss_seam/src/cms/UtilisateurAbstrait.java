package cms;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.Max;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "login"))
public abstract class UtilisateurAbstrait implements Serializable{
	
	private static final long serialVersionUID = -3609700573351783330L;

	private long id_utilisateur;
	
	private String login;
	
	private String nom;
	
	private String prenom;
	
	private String motDePasse;
	
	private String email;
	
	private Boolean compteActive;
	
	private String website;
	
	private Boolean admin;
	
	private List<Contenu> contenuAuteur;

	private List<Rubrique> contenuRedacteur;
	
	private List<Rubrique> contenuGestionnaire;

	@Id @Column(name="ID_UTILISATEUR")
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId_utilisateur() {
		return id_utilisateur;
	}

	public void setId_utilisateur(long id_utilisateur) {
		this.id_utilisateur = id_utilisateur;
	}

	@Column(name="LOGIN",nullable=false, length=20)
	@NotNull 
	@Length(min=6, max=20)
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
	
	@Length(max = 30)
	@Pattern(regex="[a-zA-Z0-9]+[@][a-zA-Z0-9]+[.][a-zA-Z]+", 
	         message="L'email n'est pas valide")
	@Column(name="EMAIL", updatable=true, length=30, nullable=false)
	@NotNull
	@Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="COMPTE_ACTIVE", updatable=true, nullable=false)
	public Boolean getCompteActive() {
		return compteActive;
	}

	public void setCompteActive(Boolean compteActive) {
		this.compteActive = compteActive;
	}

	@Column(name="WEBSITE", updatable=true, length=50)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Column(name="ADMIN", updatable=true, nullable=false)
	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="ID_CONTENU")
	public List<Contenu> getContenuAuteur() {
		return contenuAuteur;
	}

	public void setContenuAuteur(List<Contenu> contenu) {
		this.contenuAuteur = contenu;
	}

	@ManyToMany(
	        targetEntity=Rubrique.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE}
	    )
	    @JoinTable(
	        name="REDACTEUR_CONTENU",
	        joinColumns=@JoinColumn(name="ID_UTILISATEUR"),
	        inverseJoinColumns=@JoinColumn(name="ID_CONTENU")
	    )
	public List<Rubrique> getContenuRedacteur() {
		return contenuRedacteur;
	}

	public void setContenuRedacteur(List<Rubrique> contenuRedacteur) {
		this.contenuRedacteur = contenuRedacteur;
	}

	@ManyToMany(
	        targetEntity=Rubrique.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE}
	    )
	    @JoinTable(
	        name="GESTIONNAIRE_CONTENU",
	        joinColumns=@JoinColumn(name="ID_UTILISATEUR"),
	        inverseJoinColumns=@JoinColumn(name="ID_CONTENU")
	    )
	public List<Rubrique> getContenuGestionnaire() {
		return contenuGestionnaire;
	}

	public void setContenuGestionnaire(List<Rubrique> contenuGestionnaire) {
		this.contenuGestionnaire = contenuGestionnaire;
	}
	
	
}
