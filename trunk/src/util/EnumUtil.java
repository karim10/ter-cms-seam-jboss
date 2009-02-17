package util;



import org.jboss.seam.annotations.Name;

import entite.EtatContenu;
import entite.NiveauAccesContenu;
import entite.TypeContenu;

@Name("enum")
public class EnumUtil {
	
	
	
	public static NiveauAccesContenu[] getListNiveauAcces(){

		return NiveauAccesContenu.values();
	
	}
	
	public static EtatContenu[] getListEtatContenu(){
	
		return EtatContenu.values();
	
	}
	
	public static TypeContenu[] getListTypeContenu(){
		
		return TypeContenu.values();
	
	}

}
