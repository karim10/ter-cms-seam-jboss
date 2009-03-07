package composant;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("menuFrontEnd")
@Scope(ScopeType.PAGE)

public class GestionMenuFrontEnd {
	private String lien = "contenu-front.xhtml";

	/**
	 * @param lien the lien to set
	 */
	public void setLien(String lien) {
		this.lien = lien;
	}

	/**
	 * @return the lien
	 */
	public String getLien() {
		return lien;
	}
	
	public void afficheNew(){
		setLien("afficheNew.xhtml");
	}
	
	public void afficheAccueil(){
		setLien("contenu-front.xhtml");
	}
	
	public void afficheArchive(){
		setLien("archive.xhtml");
	}
	
	public void afficheArticle(){
		setLien("afficheArticle.xhtml");
	}
	
	public void afficheRubrique(){
		setLien("afficheRubrique.xhtml");
	}
	
	public boolean homeF(){
		return (lien.equals("contenu-front.xhtml"));
	}
}
