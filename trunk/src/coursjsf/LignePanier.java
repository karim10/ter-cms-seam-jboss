package coursjsf;

/**
 * Classe de stockage correspondant à une ligne du panier du client, regroupant un produit et 
 * la quantité désirée.
 */
public class LignePanier {

	private Produit produit;
	private int quantite;

	/**
	 * Le composant Seam "catalogue", utilisé pour trouver le prix du produit de la ligne de panier.
	 */
	private Catalogue catalogue;
	
	/**
	 * Crée une nouvelle ligne du panier, pour un produit donné, avec une quantité à 1.
	 * Il est nécessaire de passer le catalogue, qui est utilisé pour retrouver le prix du produit.
	 * @param catalogue
	 * @param produit
	 */
	public LignePanier(Catalogue catalogue, Produit produit) {
		this.catalogue = catalogue;
		this.produit = produit;
		this.quantite = 1;
	}
	
	/**
	 * Incrémente la quantité demandée du produit.
	 */
	public void incremente() {
		this.quantite++;
	}
	
	/**
	 * Renvoie l'objet produit de la ligne du panier.
	 * @return le produit
	 */
	public Produit getProduit() {
		return produit;
	}
	
	/**
	 * Renvoie la quantité demandée sur la ligne du panier.
	 * @return la quantité
	 */
	public int getQuantite() {
		return quantite;
	}
	
	/**
	 * Recherche dans le catalogue le prix unitaire du produit, et le renvoie.
	 * @return le prix unitaire du produit.
	 */
	public double getPrixUnitaire() {
		return catalogue.getPrix(produit);
	}

	/**
	 * Calcule et renvoie le prix du lot, c'est-à-dire le prix unitaire du produit multiplié par la quantité.
	 * @return le prix du lot en fonction de la quantité
	 */
	public double getPrix() {
		return getPrixUnitaire() * getQuantite();
	}
	
	/**
	 * Modifie la quantité du produit (utile lorsque la quantité est saisissable dans le panier).
	 * @param la nouvelle quantité.
	 */
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
}
