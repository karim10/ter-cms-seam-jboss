package composant;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import util.DataUtil;

import entite.Article;
import entite.Contenu;

@Name("gestionNews")
@Scope(ScopeType.SESSION)
public class GestionNews {

	@DataModel
	private List<Contenu> listNews;
	
	@DataModelSelection
	private Contenu news;
	
	@Create
	@Factory("listNews")
	public void initNews(){
		setListNews(DataUtil.chargeDernierePublication());
	}

	/**
	 * @param listNews the listNews to set
	 */
	public void setListNews(List<Contenu> listNews) {
		this.listNews = listNews;
	}

	/**
	 * @return the listNews
	 */
	public List<Contenu> getListNews() {
		return listNews;
	}

	/**
	 * @param news the news to set
	 */
	public void setNews(Contenu news) {
		this.news = news;
	}

	/**
	 * @return the news
	 */
	public Contenu getNews() {
		return news;
	}
	/**
	 * <p>Vérifie si le contenu est un article</p>
	 * @param contenu
	 * @return {@link Boolean}
	 */
	public boolean estArticle(Contenu contenu){
		return contenu instanceof Article;
	}
}
