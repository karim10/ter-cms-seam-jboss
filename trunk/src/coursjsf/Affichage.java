package coursjsf;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("affichage")
@Scope(ScopeType.PAGE)
public class Affichage {
	
	private boolean panier;
	
	public boolean isPanier(){
		return panier;
	}

	public void afficherPanier(){
		panier = true;
	}
	
	public void afficherDescription(){
		panier = false;
	}
	
	
}
