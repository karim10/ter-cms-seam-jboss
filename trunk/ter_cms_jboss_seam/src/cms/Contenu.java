package cms;

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

/**
 * 
 * @author keira
 * <p> Classe abstraite entité Contenu<br />
 * On utilise la stratégie de mapping Joined </p> 
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Contenu implements Serializable{

	private static final long serialVersionUID = 7430279630785144437L;

	/**
	 * <p> L'identifiant (clé primaire) d'un contenu </p>
	 */
	private long id_contenu;
	
	/**
	 * <p> Niveau d'accès d'un contenu (public ou privé)</p>
	 */
	private NiveauAccesContenu niveauAcces;
	
	/**
	 * <p>Etat d'un contenu : <br />publié, non publié, corbeille, en attente</p>
	 */
	private EtatContenu etatContenu;
	
	/**
	 * <p>titre du contenu</p>
	 */
	private String titreContenu;
	
	/**
	 * <p>Url d'un site web lié au contenu</p>
	 */
	private String lienWeb;
	
	/**
	 * <p>texte du contenu</p>
	 */
	private String texte;
	
	private Date dateCreation;
	
	private Date dateMaj;
	
	private String logo;
	
	private Utilisateur auteur;

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

	@Column(name="TITRE", updatable=true, nullable=false, length=50)
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
	@JoinColumn(name="ID_UTILISATEUR")
	@NotNull
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
	
	
	
}
