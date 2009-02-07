package cms;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="RUBRIQUE")
public class Rubrique extends Contenu{

	private static final long serialVersionUID = -1284946822374112514L;

	private List<Contenu> listEnfant;
	
	private List<Utilisateur> listRedacteur;
	
	private List<Utilisateur> listGestionnaire;
	
	public Rubrique(){}
	
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

	public void addEnfant(Contenu contenu){
		getListEnfant().add(contenu);
	}

	public void removeEnfant(Contenu contenu){
		getListEnfant().remove(contenu);
	}
	
	public void addRedacteur(Utilisateur u){
		getListRedacteur().add(u);
	}

	public void removeRedacteur(Utilisateur u){
		getListRedacteur().remove(u);
	}

	public void addGestionnaire(Utilisateur u){
		getListGestionnaire().add(u);
	}

	public void removeGestionnaire(Utilisateur u){
		getListGestionnaire().remove(u);
	}
}
