package entite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@Entity
@Name("article")
@Table(name="ARTICLE")
public class Article extends Contenu{
	
	private static final long serialVersionUID = -3749690144340630747L;

	private String description;
	
	private String sousTitre;
	
	public Article(){}

	@Column(name="DESCRIPTION", updatable=true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="SOUS_TITRE", updatable=true)
	public String getSousTitre() {
		return sousTitre;
	}

	public void setSousTitre(String sousTitre) {
		this.sousTitre = sousTitre;
	}
	

}
