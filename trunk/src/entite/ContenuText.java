package entite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class ContenuText extends Contenu{

	private static final long serialVersionUID = 64026926974710773L;
	
	private String texte;
	
	public ContenuText(){}
	
	@Column(name="TEXTE", updatable=true)
	@Lob
	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}
	
}
