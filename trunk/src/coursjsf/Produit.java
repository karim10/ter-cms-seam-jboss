package coursjsf;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.Length;

/**
 * Entité produit, qui dans un cas réel serait une entité persistante dans la base de données.
 * Ici les produits sont simplement créés au démarrage, lors de l'initialisation du composant "catalogue".  
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
	 * Crée un produit, avec un code servant d'identifiant, un libellé court et une description plus détaillé.
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
	 * Renvoie le code du produit, qui sert d'identifiant. Chaque produit a un code différent.
	 * @return le code du produit.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Renvoie le libellé du produit.
	 * @return le libellé du produit.
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * Renvoie la description détaillée du produit, qui est au format HTML.
	 * @return la description du produit.
	 */
	public String getDescription() {
		return description;
	}

}
