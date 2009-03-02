package composant;

import java.util.List;

import entite.Contenu;

public interface IGestionContenu {
	
	public List<Contenu> getListContenu();
	
	public void addContenu(Contenu contenu);
	
	public void modifierContenu(Contenu contenu);
	
	public void removeContenu(Contenu contenu);
	
	public void mettreCorbeille(Contenu contenu);
	
	public void publierContenu(Contenu contenu);
	
	public void depublierContenu(Contenu contenu);
	
}
