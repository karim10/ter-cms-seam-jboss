package composant;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("menu")
@Scope(ScopeType.PAGE)
public class GestionMenu {


	private final String path  = "";
	
	private String link = "contenu.xhtml";
	
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public void afficheGestionRubrique(){
		link = path + "gestionRubrique.xhtml";
	}
	
	public void afficheChoixGestionnaire(){
		link = path + "choixGestionnaire.xhtml";
	}
	
	public void afficheGestionArticle(){
		link = path + "gestionArticle.xhtml";
	}
	
	public void afficheGestionUtilisateur(){
		link = path + "gestionUtilisateur.xhtml";
	}
	
	public void afficheAjoutArticle(){
		link = path + "ajoutArticle.xhtml";
	}
	
	public void afficheAjoutRubrique(){
		link = path + "ajoutRubrique.xhtml";
	}
	
	public void afficheAjoutUtilisateur(){
		link = path + "ajoutUtilisateur.xhtml";
	}
	
	public void afficheModifierUtilisateur(){
		link = path + "modifierUtilisateur.xhtml";
	}
	
	public void afficheModifierArticle(){
		link = path + "modifierArticle.xhtml";
	}
	
	public void afficheModifierRubrique(){
		link = path + "modifierRubrique.xhtml";
	}
	
	public void afficheGestionStatutUtilisateur(){
		link = path + "gestionStatutUtilisateur.xhtml";
	}
	
	public void afficheChoixStatutRubrique(){
		link = path + "choixStatutRubrique.xhtml";
	}
	public void afficheFrontEnd(){
		link = "/front.xhtml";
	}
	
	public void afficheContenu(){
		link = path + "contenu.xhtml";
	}

}
