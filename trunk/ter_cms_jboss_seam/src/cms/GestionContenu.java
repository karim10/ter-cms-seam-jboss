package cms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
/**
* Cette classe gere le contenu du CMS 
* @see Nouvelle
* @see Article
* @see Rubrique
**/

@Name("gestionContenu")
@Scope(ScopeType.SESSION)
public class GestionContenu implements GestionContenuLocal {

	private List<Contenu> listContenu = new ArrayList<Contenu>();
	
	@In
	private Session sessionHibernate;
	public GestionContenu(){}
	
	/**
	 * Methode d'initialisation du CMS depuis la BD
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Create
	public void init() {
		listContenu = (List<Contenu>)sessionHibernate.createQuery(" from Contenu c").list();

	}
	
	public List<Contenu> getList() {
		return listContenu;
	}

	public void setList(List<Contenu> listContenu) {
		this.listContenu = listContenu;
	}

	public void addContenu(Contenu contenu) throws ContenuException{
		getListContenu().add(contenu);
	}

	public void depublierContenu(Contenu contenu) throws ContenuException {
		if(contenu.getEtatContenu().equals("NON_PUBLIE")) {
			throw new ContenuException("Elément déjà dépublié");
		} else {
			contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
		}
	}

	public List<Contenu> getListContenu() {
		return this.getListContenu();
	}

	public void mettreCorbeille(Contenu contenu) {
		contenu.setEtatContenu(EtatContenu.CORBEILLE);
	}

	public void publierContenu(Contenu contenu) throws ContenuException {
		if(contenu.getEtatContenu().equals("PUBLIE")){
			throw new ContenuException("Elément déjà publié");
		} else {
			contenu.setEtatContenu(EtatContenu.PUBLIE);
		}
	}

	public void removeContenu(Contenu contenu) throws ContenuException {
		// TODO Auto-generated method stub
		if(listContenu.contains(contenu)){
			listContenu.remove(contenu);
		}else {
			throw new ContenuException("L'élément de titre "+contenu.getTitreContenu()+" n'exite pas");
		}
		
	}

	public void creerArticle() {
		// TODO Auto-generated method stub
		
	}

	public void creerNouvelle() {
		// TODO Auto-generated method stub
		
	}

	public void creerRubrique() {
		// TODO Auto-generated method stub
		
	}

}
