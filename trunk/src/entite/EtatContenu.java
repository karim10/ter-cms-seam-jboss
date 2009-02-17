package entite;

public enum EtatContenu {

	PUBLIE("Publié"),
	
	CORBEILLE("Corbeille"),
	
	NON_PUBLIE("Non Publié"),
	
	EN_ATTENTE("En Attente");

	private final String label;

	EtatContenu(String label){
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	public String toString(){
		return this.label;
	}
}
