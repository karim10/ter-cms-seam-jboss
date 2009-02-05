package coursjsf;

import java.util.LinkedList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("panier")
@Scope(ScopeType.SESSION)
public class Panier {
	
	List<LignePanier> lignes = new LinkedList<LignePanier>();
	
	private double montantPanier; 
	
	@In
	private Catalogue catalogue;
	
	public List<LignePanier> getLignes() {
		return lignes;
	}
	
	public Panier(){
		montantPanier = 0;
	}
	
	public double getMontantPanier(){
		double montant = 0;
		for (LignePanier lignePanier : lignes) {
			montant+=lignePanier.getPrix();
		}
		montantPanier = montant;
		return montantPanier;
	}
	
	public void ajout(Produit produit){
		for(LignePanier lignepanier : lignes){
			if(lignepanier.getProduit().getLibelle().equals(produit.getLibelle())){
				lignepanier.incremente();
				return;
			}
		}
		this.lignes.add(new LignePanier(this.catalogue,produit));
	}
	
	public void viderPanier(){
		lignes.clear();
	}
	
}
