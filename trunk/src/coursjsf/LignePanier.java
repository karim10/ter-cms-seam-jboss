package coursjsf;

/**
 * Classe de stockage correspondant � une ligne du panier du client, regroupant un produit et 
 * la quantit� d�sir�e.
 */
public class LignePanier {

	private Produit produit;
	private int quantite;

	/**
	 * Le composant Seam "catalogue", utilis� pour trouver le prix du produit de la ligne de panier.
	 */
	private Catalogue catalogue;
	
	/**
	 * Cr�e une nouvelle ligne du panier, pour un produit donn�, avec une quantit� � 1.
	 * Il est n�cessaire de passer le catalogue, qui est utilis� pour retrouver le prix du produit.
	 * @param catalogue
	 * @param produit
	 */
	public LignePanier(Catalogue catalogue, Produit produit) {
		this.catalogue = catalogue;
		this.produit = produit;
		this.quantite = 1;
	}
	
	/**
	 * Incr�mente la quantit� demand�e du produit.
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
	 * Renvoie la quantit� demand�e sur la ligne du panier.
	 * @return la quantit�
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
	 * Calcule et renvoie le prix du lot, c'est-�-dire le prix unitaire du produit multipli� par la quantit�.
	 * @return le prix du lot en fonction de la quantit�
	 */
	public double getPrix() {
		return getPrixUnitaire() * getQuantite();
	}
	
	/**
	 * Modifie la quantit� du produit (utile lorsque la quantit� est saisissable dans le panier).
	 * @param la nouvelle quantit�.
	 */
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
}
