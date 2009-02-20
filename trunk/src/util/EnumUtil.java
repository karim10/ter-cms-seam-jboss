package util;



import org.jboss.seam.annotations.Name;

import entite.EtatContenu;
import entite.NiveauAccesContenu;

@Name("enum")
public class EnumUtil {
	
	
	
	public static NiveauAccesContenu[] getListNiveauAcces(){

		return NiveauAccesContenu.values();
	
	}
	
	public static EtatContenu[] getListEtatContenu(){
	
		return EtatContenu.values();
	
	}

}
