package entite;

public enum TypeContenu {

	RUBRIQUE("Rubrique"), 
	ARTICLE("Article"),
	NOUVELLE("Nouvelle");

	public final String label;

	TypeContenu(String label){
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

	public String toString(){
		return this.label;
	}
}
