package cms;

import java.util.List;

public interface GestionContenuLocal {
	
	public List<Contenu> getListContenu() throws ContenuException;
	
	public void addContenu(Contenu contenu) throws ContenuException;
	
	public void removeContenu(Contenu contenu) throws ContenuException;
	
	public void mettreCorbeille(Contenu contenu) throws ContenuException;
	
	public void publierContenu(Contenu contenu) throws ContenuException;
	
	public void depublierContenu(Contenu contenu) throws ContenuException;
	
	/**
	 * Methode permettant de créer une rubrique
	 */
	public void creerRubrique() throws ContenuException;
	
	public void creerArticle() throws ContenuException;
	
	public void creerNouvelle();
	
}
