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
 * @author keira
* Cette classe g�re le contenu du CMS 
* @see Nouvelle
* @see Article
* @see Rubrique
**/

@Name("gestionContenu")
@Scope(ScopeType.APPLICATION)
public class GestionContenu implements GestionContenuLocal {

	/**
	 * Liste des contenus
	 */
	private List<Contenu> listContenu = new ArrayList<Contenu>();
    /**
     * Liste des articles
     */
	private List<Article> listArticle = new ArrayList<Article>();

	
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

	/**
	 * Methode permettant d'ajouter un contenu dans la liste des contenus
	 * 
	 * @param contenu
	 */
	public void addContenu(Contenu contenu) throws ContenuException{
		if(!listArticle.contains(contenu) && contenu instanceof Article){
			throw new ContenuException("El�ment existe d�j�");
		}else{
			getListContenu().add(contenu);	
		}
	}

	/**
	 * <p>Methode permattant de d�publier un contenu
	 * Si le contenu est d�j� d�publi�, une exception est lev�e
	 * Sinon le contenu est d�publi�
	 * </p>
	 */
	public void depublierContenu(Contenu contenu) throws ContenuException {
		if(contenu.getEtatContenu().equals("NON_PUBLIE")) {
			throw new ContenuException("El�ment d�j� d�publi�");
		} else {
			contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
		}
	}

	/**
	 * <p>Methode permattant de publier un contenu
	 * Si le contenu est d�j� publi�, une exception est lev�e
	 * Sinon le contenu est publi�
	 * </p>
	 */
	public void publierContenu(Contenu contenu) throws ContenuException {
		if(contenu.getEtatContenu().equals(contenu.getEtatContenu().PUBLIE)){
			throw new ContenuException("El�ment d�j� publi�");
		} else {
			contenu.setEtatContenu(EtatContenu.PUBLIE);
		}
	}
	/**Retourne la liste des contenus
	 * @return {@link List}
	 */
	public List<Contenu> getListContenu() {
		return this.listContenu;
	}

	public void mettreCorbeille(Contenu contenu) {
		contenu.setEtatContenu(EtatContenu.CORBEILLE);
	}

	public void removeContenu(Contenu contenu) throws ContenuException {
		// TODO Auto-generated method stub
		if(listContenu.contains(contenu)){
			listContenu.remove(contenu);
		}else {
			throw new ContenuException("L'�l�ment de titre "+contenu.getTitreContenu()+" n'exite pas");
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
