package cms;

import java.util.List;

public class GestionContenu implements GestionContenuLocal {

	private List<Contenu> listContenu;
	
	
	
	public List<Contenu> getList() {
		return listContenu;
	}

	public void setList(List<Contenu> listContenu) {
		this.listContenu = listContenu;
	}

	public void addContenu(Contenu contenu) {
		getListContenu().add(contenu);
	}

	public void depublierContenu(Contenu contenu) {
		contenu.setEtatContenu(EtatContenu.NON_PUBLIE);
	}

	public List<Contenu> getListContenu() {
		return this.getListContenu();
	}

	public void mettreCorbeille(Contenu contenu) {
		contenu.setEtatContenu(EtatContenu.CORBEILLE);
	}

	public void publierContenu(Contenu contenu) {
		contenu.setEtatContenu(EtatContenu.PUBLIE);
	}

	public void removeContenu(Contenu contenu) {
		// TODO Auto-generated method stub
		
	}

}
