package entite;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Contenu implements Serializable{

	private static final long serialVersionUID = 7430279630785144437L;

	private long id_contenu;
	
	private NiveauAccesContenu niveauAcces;
	
	// etat courant du contenu (valeur par defaut à la création)
	private EtatContenu etatContenu = EtatContenu.EN_ATTENTE;
	
	//etat sauvegardé après dépublication du contenu
	private EtatContenu etatSauve = null;
	
	private String titreContenu;
	
	private String lienWeb;
	
	private String texte;
	
	private Date dateCreation;
	
	private Date dateMaj;
	
	private String logo;
	
	private Utilisateur auteur;

	private Rubrique parent;

	@Id @Column(name="ID_CONTENU")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId_contenu() {
		return id_contenu;
	}

	public void setId_contenu(long id_contenu) {
		this.id_contenu = id_contenu;
	}

	@Column(name="NIVEAU_ACCES", updatable=true, nullable=false)
	public NiveauAccesContenu getNiveauAcces() {
		return niveauAcces;
	}

	public void setNiveauAcces(NiveauAccesContenu niveauAcces) {
		this.niveauAcces = niveauAcces;
	}

	@Column(name="ETAT_CONTENU", updatable=true, nullable=false)
	@Enumerated(EnumType.STRING)
	public EtatContenu getEtatContenu() {
		return etatContenu;
	}

	public void setEtatContenu(EtatContenu etatContenu) {
		this.etatContenu = etatContenu;
	}

	/**
	 * @param etatSauve the etatSauve to set
	 */
	public void setEtatSauve(EtatContenu etatSauve) {
		this.etatSauve = etatSauve;
	}

	/**
	 * @return the etatSauve
	 */
	@Column(name="ETAT_CONTENU_SAUVE", updatable=true, nullable=true)
	@Enumerated(EnumType.STRING)
	public EtatContenu getEtatSauve() {
		return etatSauve;
	}

	//verifie qu'un contenu a été modifié 

	public Boolean etatCotenuModifie(){
		return (!this.etatSauve.equals(null));
	}


	@Column(name="TITRE", updatable=true, nullable=false, length=50)
	@NotNull
	public String getTitreContenu() {
		return titreContenu;
	}

	public void setTitreContenu(String titreContenu) {
		this.titreContenu = titreContenu;
	}

	@Column(name="LIEN_WEB", updatable=true, length=50)
	public String getLienWeb() {
		return lienWeb;
	}

	public void setLienWeb(String lienWeb) {
		this.lienWeb = lienWeb;
	}

	@Column(name="TEXTE", updatable=true)
	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	@Column(name="DATE_CREATION", updatable=false, nullable=false)	
	@Temporal(TemporalType.DATE)
	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	@Column(name="DATE_MAJ", updatable=true)
	@Temporal(TemporalType.DATE)
	public Date getDateMaj() {
		return dateMaj;
	}

	public void setDateMaj(Date dateMaj) {
		this.dateMaj = dateMaj;
	}
	
	@OneToOne
	@JoinColumn(name="AUTEUR")
	public Utilisateur getAuteur() {
		return auteur;
	}

	public void setAuteur(Utilisateur auteur) {
		this.auteur = auteur;
	}

	@Column(name="LOGO", updatable=true, length=30)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@OneToOne
	@JoinColumn(name="PARENT")
	@NotNull
	public Rubrique getParent() {
		return parent;
	}

	public void setParent(Rubrique parent) {
		this.parent = parent;
	}
	
}
