package coursjsf;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Classe de stockage correspondant à une ligne du catalogue des produits, regroupant un produit et 
 * son tarif.
 */
@Entity
public class LigneCatalogue {

	@Id
	@GeneratedValue
	@SuppressWarnings("unused")
	private long id;
	
	@OneToOne
	private Produit produit;
	
	private double prix;
	
	LigneCatalogue() {
	}
	
	/**
	 * Crée une ligne de catalogue, avec un produit et son prix.
	 * @param produit
	 * @param prix
	 */
	public LigneCatalogue(Produit produit, double prix) {
		this.produit = produit;
		this.prix = prix;
	}
	
	/**
	 * Renvoie l'objet produit de la ligne de catalogue.
	 * @return le produit
	 */
	public Produit getProduit() {
		return produit;
	}
	
	/**
	 * Renvoie le prix du produit de la ligne de catalogue.
	 * @return le prix du produit
	 */
	public double getPrix() {
		return prix;
	}
	
}
