package entite;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@SuppressWarnings("serial")
@Entity
@Name("rubrique")
@Table(name="RUBRIQUE")
public class Rubrique extends Contenu{

	private List<Contenu> listEnfant = new ArrayList<Contenu>();
	
	private List<IUtilisateur> listRedacteur = new ArrayList<IUtilisateur>();
	
	private List<IUtilisateur> listGestionnaire = new ArrayList<IUtilisateur>();
	
	public Rubrique(){}
	
	
	@OneToMany(
			fetch=FetchType.EAGER,
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
	public List<IUtilisateur> getListRedacteur() {
		return listRedacteur;
	}

	public void setListRedacteur(List<IUtilisateur> listRedacteur) {
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
	public List<IUtilisateur> getListGestionnaire() {
		return listGestionnaire;
	}

	public void setListGestionnaire(List<IUtilisateur> listGestionnaire) {
		this.listGestionnaire = listGestionnaire;
	}
	
	/**
	 * @param contenu
	 * ajout un contenu dans la liste des enfants
	 */
	public void addEnfant(Contenu contenu){
		getListEnfant().add(contenu);
	}

	public void removeEnfant(Contenu contenu){
		getListEnfant().remove(contenu);
	}
	
	public void addRedacteur(IUtilisateur utilisateur){
		getListRedacteur().add(utilisateur);
	}

	public void removeRedacteur(IUtilisateur u){
		getListRedacteur().remove(u);
	}

	public void addGestionnaire(IUtilisateur u){
		getListGestionnaire().add(u);
	}

	public void removeGestionnaire(IUtilisateur u){
		getListGestionnaire().remove(u);
	}
}
