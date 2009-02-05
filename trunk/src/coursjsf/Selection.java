
package coursjsf;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


@Name("selection")
@Scope(ScopeType.PAGE)
public class Selection {
	
	@In
	private Catalogue catalogue;
	
	Produit produit;
	
	
	@Create
	public void init(){
		produit=catalogue.getProduitAuHasard();
	}
	
	public Produit getProduit() {
		return produit;
	}
	
	public void select(Produit produit){
		this.produit = produit;
		
	}

}
