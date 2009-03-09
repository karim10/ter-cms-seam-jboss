package composant;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import util.DataUtil;

import entite.Article;
import entite.Contenu;

@Name("gestionFrontEnd")
@Scope(ScopeType.SESSION)
public class GestionFrontEnd {
	
	public GestionFrontEnd(){ 
		
	}

	@DataModel
	private List<Contenu> listContenuFront;
	
	@DataModelSelection
	@Out(required=false)
	private Contenu contenu;
	
	/**
	 * @param contenu the contenu to set
	 */
	public void setContenu(Contenu contenu) {
		this.contenu = contenu;
	}

	/**
	 * @return the contenu
	 */
	public Contenu getContenu() {
		return contenu;
	}

	/**
	 * @param listNews the listNews to set
	 */
	public void setListContenuFront(List<Contenu> listContenuFront) {
		this.listContenuFront = listContenuFront;
	}

	/**
	 * @return the listNews
	 */
	public List<Contenu> getListContenuFront() {
		return listContenuFront;
	}
	
	/**
	 * <p>Vérifie si le contenu est un article</p>
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estArticle(Contenu contenu){
		return contenu instanceof Article;
	}

	/**
	 * <p>Crée la listContenu lors de l'initialisation du composant 
	 * à partir du contenu de la base de donnée.</p>
	 */
	
	@Create
	@Factory("listContenuFront")
	public void initFrontEnd(){
		setListContenuFront(DataUtil.chargeContenuPublie());
	}	
	
}
