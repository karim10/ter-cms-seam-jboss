package coursjsf;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Composant Seam g�rant le catalogue des produits de la boutique.
 * C'est un composant charg� dans le contexte de l'application, ce qui signifie qu'il en existe 
 * une seule instance pour toute l'application.
 */
@Name("catalogue")
@Scope(ScopeType.APPLICATION)
public class Catalogue {
	
	@In
	private Session sessionHibernate;
	
	// Lignes du catalogues, parcourues pour l'affichage sur la page web.
	List<LigneCatalogue> lignes = new LinkedList<LigneCatalogue>();
	
	// Map des tarifs, permettant d'acc�der directement au prix d'un produit.
	Map<String, Double> tarifs = new HashMap<String, Double>();
	
	/**
	 * Renvoie les lignes du catalogue.
	 * @return une liste d'objets de la classe LigneCatalogue, contenant chacun un produit et un prix.
	 */
	public List<LigneCatalogue> getLignes() {
		return lignes;
	}
	
	/**
	 * Renvoie un produit prix au hasard dans le catalogue.
	 * @return un produit au hasard.
	 */
	public Produit getProduitAuHasard() {
		int index = (int) (Math.random()*lignes.size());
		return lignes.get(index).getProduit();
	}

	/**
	 * Recherche et renvoie le prix d'un produit. 
	 * @param produit
	 * @return
	 */
	public double getPrix(Produit produit) {
		// Le prix est retrouv� dans la map des tarifs.
		return tarifs.get(produit.getCode()).doubleValue();
	}


	/* --------- Chargement des donn�es ------------------------------ */

	/**
	 * Initialisation du composant. Charge toutes les lignes de produits, avec les prix.
	 * L'annotation @Create entra�ne l'ex�cution automatique de la m�thode apr�s 
	 * la cr�ation du composant par le framework Seam. 
	 */
	@Create
	public void init() throws IOException {
		// Initialisation depuis la base de donn�es.
		initDB();
	}
	
	/**
	 * Initialisation depuis la base de donn�es.
	 */	
	@SuppressWarnings("unchecked")
	public void initDB() {
		lignes = (List<LigneCatalogue>)sessionHibernate.createQuery(" from LigneCatalogue l").list();
	
		// Remplit la map des tarifs, qui permet de retrouver directement le prix d'un produit, 
		// sans avoir � rechercher dans la liste des lignes.
		for (LigneCatalogue ligneCatalogue : lignes) {
			tarifs.put(ligneCatalogue.getProduit().getCode(), ligneCatalogue.getPrix());
		}
	}
	
	/**
	 * Initialisation depuis un fichier texte.
	 */	
	public void initFichier() throws IOException {
		// Cr�e les lignes de catalogue
		this.lignes = CatalogueDataUtil.chargeLignesCatalogue();
		
		// Remplit la map des tarifs, qui permet de retrouver directement le prix d'un produit, 
		// sans avoir � rechercher dans la liste des lignes.
		for (LigneCatalogue ligneCatalogue : lignes) {
			tarifs.put(ligneCatalogue.getProduit().getCode(), ligneCatalogue.getPrix());
		}
	}
	
}
