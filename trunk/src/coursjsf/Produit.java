package coursjsf;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.Length;

/**
 * Entit� produit, qui dans un cas r�el serait une entit� persistante dans la base de donn�es.
 * Ici les produits sont simplement cr��s au d�marrage, lors de l'initialisation du composant "catalogue".  
 */
@Entity
public class Produit {

	@Id
	@Length(max=10)
	private String code;
	
	@Length(max=100)
	private String libelle;
	
	@Length(max=5000)
	private String description;

	Produit() {
	}
		
	/**
	 * Cr�e un produit, avec un code servant d'identifiant, un libell� court et une description plus d�taill�.
	 * @param code
	 * @param libelle
	 * @param description
	 */
	public Produit(String code, String libelle, String description) {
		this.code = code;
		this.libelle = libelle;
		this.description = description;
	}

	/**
	 * Renvoie le code du produit, qui sert d'identifiant. Chaque produit a un code diff�rent.
	 * @return le code du produit.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Renvoie le libell� du produit.
	 * @return le libell� du produit.
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * Renvoie la description d�taill�e du produit, qui est au format HTML.
	 * @return la description du produit.
	 */
	public String getDescription() {
		return description;
	}

}
