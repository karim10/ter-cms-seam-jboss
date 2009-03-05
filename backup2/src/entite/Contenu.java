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
public abstract class Contenu implements  Serializable{

	private static final long serialVersionUID = 7430279630785144437L;

	// Attributs
	
	private long id_contenu;
	
	private NiveauAccesContenu niveauAcces;
	
	// etat courant du contenu (valeur par defaut � la cr�ation)
	private EtatContenu etatContenu;
	
	private String titreContenu;
	
	private String lienWeb;
	
	private Date dateCreation;
	
	private Date dateMaj;
	
	private File logo;
	
	private IUtilisateur auteur = null;

	private Rubrique parent = null;

	// getter setter
	
	@Id @Column(name="ID_CONTENU")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId_contenu() {
		return id_contenu;
	}

	public void setId_contenu(long id_contenu) {
		this.id_contenu = id_contenu;
	}

	@Column(name="NIVEAU_ACCES", updatable=true, nullable=false)
	@Enumerated(EnumType.STRING)
	@NotNull
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
	
	@OneToOne(targetEntity=Utilisateur.class)
	@JoinColumn(name="AUTEUR", nullable=true)
	public IUtilisateur getAuteur() {
		return auteur;
	}

	public void setAuteur(IUtilisateur auteur) {
		this.auteur = auteur;
	}

	@OneToOne
	@JoinColumn(name="LOGO", updatable=true)
	public File getLogo() {
		return logo;
	}

	public void setLogo(File logo) {
		this.logo = logo;
	}
	
	@OneToOne
	@JoinColumn(name="PARENT",nullable=true)
	public Rubrique getParent() {
		return parent;
	}

	public void setParent(Rubrique parent) {
		this.parent = parent;
	}
}
