package cms;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * 
 * @author keira
 * <p>Classe entité Rubrique pouvant contenir des articles, des nouvelles et d'autres rubriques.
 * @see Contenu</p>
 */
@Entity
@Table(name="RUBRIQUE")
public class Rubrique extends Contenu{

	private static final long serialVersionUID = -1284946822374112514L;

	/**
	 * <p>Liste des enfants d'une rubrique</p>
	 */
	private List<Contenu> listEnfant;
	
	/**
	 * <p>Liste des utilisateurs ayant le droit de Redacteur sur cette rubrique</p>
	 */
	private List<Utilisateur> listRedacteur;

	/**
	 * <p>Liste des utilisateurs ayant le droit de Gestionnaire sur cette rubrique</p>
	 */
	private List<Utilisateur> listGestionnaire;
	
	public Rubrique(){}
	
	/**
	 * 
	 * @return 
	 * <p>L'association entre La rubrique pere et ses enfants</p> 
	 */
	@OneToMany(
	        targetEntity=Contenu.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE}
	    )
	    @JoinTable(
	        name="RUBRIQUE_CONTENU",
	        joinColumns=@JoinColumn(name="ID_RUBRIQUE_PERE"),
	        inverseJoinColumns=@JoinColumn(name="ID_CONTENU_ENFANT")
	    )
	public List<Contenu> getListEnfant() {
		return listEnfant;
	}

	public void setListEnfant(List<Contenu> listEnfant) {
		this.listEnfant = listEnfant;
	}

	@ManyToMany(
	        targetEntity=Utilisateur.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE}
	    )
	    @JoinTable(
	        name="RUBRIQUE_UTILISATEUR_REDACTEUR",
	        joinColumns=@JoinColumn(name="ID_RUBRIQUE"),
	        inverseJoinColumns=@JoinColumn(name="ID_REDACTEUR")
	    )
	public List<Utilisateur> getListRedacteur() {
		return listRedacteur;
	}

	public void setListRedacteur(List<Utilisateur> listRedacteur) {
		this.listRedacteur = listRedacteur;
	}
	
	@ManyToMany(
	        targetEntity=Utilisateur.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE}
	    )
	    @JoinTable(
	        name="RUBRIQUE_UTILISATEUR_GESTIONNAIRE",
	        joinColumns=@JoinColumn(name="ID_RUBRIQUE"),
	        inverseJoinColumns=@JoinColumn(name="ID_GESTIONNAIRE")
	    )
	public List<Utilisateur> getListGestionnaire() {
		return listGestionnaire;
	}

	public void setListGestionnaire(List<Utilisateur> listGestionnaire) {
		this.listGestionnaire = listGestionnaire;
	}

	/**
	 * <p>Ajout d'un contenu enfant à la liste des enfants
	 * @param contenu</p>
	 * @throws ContenuException 
	 */
	public void addEnfant(Contenu contenu) throws ContenuException{
		if(listEnfant.contains(contenu)){
			throw new ContenuException("Cet objet existe déjà");
		}else {
			getListEnfant().add(contenu);	
		}
	}

	/**
	 * <p>Suppression d'un contenu enfant à la liste des enfants
	 * @param contenu</p>
	 * @throws ContenuException 
	 */
	public void removeEnfant(Contenu contenu) throws ContenuException{
		if(!listEnfant.contains(contenu)) {
			throw new ContenuException("Cet objet n'existe pas, impossible de supprimer");
		}else {
			getListEnfant().remove(contenu);
		}
	}
	
	/**
	 * <p>Ajout d'un redacteur 
	 * @param u</p>
	 */
	public void addRedacteur(Utilisateur u){
		getListRedacteur().add(u);
	}

	/**
	 * <p>Suppressin d'un redacteur </p>
	 * @param u</p>
	 */
	public void removeRedacteur(Utilisateur u){
		getListRedacteur().remove(u);
	}

	/**
	 * <p>Ajout d'un gestionnaire 
	 * @param u</p>
	 */
	public void addGestionnaire(Utilisateur u){
		getListGestionnaire().add(u);
	}

	/**
	 * <p>Suppression d'un gestionnaire 
	 * @param u</p>
	 */
	public void removeGestionnaire(Utilisateur u){
		getListGestionnaire().remove(u);
	}
}
