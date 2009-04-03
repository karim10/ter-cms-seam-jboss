package entite;

public enum NiveauAccesContenu {

	PUBLIC("Public"), 
	PRIVEE("Privee");

	public final String label;

	NiveauAccesContenu(String label){
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

	public String toString(){
		return this.label;
	}
}
