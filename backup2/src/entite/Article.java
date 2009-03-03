package entite;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;





@Entity
@Name("article")
@Table(name="ARTICLE")
public class Article extends ContenuText{
	
	private static final long serialVersionUID = -3749690144340630747L;

	private String description;
	
	private String sousTitre;
	
	private List<File> files; 
	
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
	
	@OneToMany(
	        targetEntity=File.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE}
	    )
	    @JoinTable(
	        name="ARTICLE_FILE",
	        joinColumns=@JoinColumn(name="ID_ARTICLE"),
	        inverseJoinColumns=@JoinColumn(name="ID_FILE")
	    )
	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	
}
