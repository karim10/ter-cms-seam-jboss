package cms;

import java.util.List;

public interface GestionContenuLocal {
	
	public List<Contenu> getListContenu();
	
	public void addContenu(Contenu contenu);
	
	public void removeContenu(Contenu contenu);
	
	public void mettreCorbeille(Contenu contenu);
	
	public void publierContenu(Contenu contenu);
	
	public void depublierContenu(Contenu contenu);
	
}
