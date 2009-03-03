package util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.persistence.EntityNotFoundException;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import entite.Rubrique;
import entite.Utilisateur;

@Name("utilisateurConverter")
@BypassInterceptors
@Converter
public class UtilisateurConverter implements javax.faces.convert.Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		
		
	    try {
	    	 Long id = new Long (arg2);
	 		if (id == null || id.equals(0)) {
	 			return null;
	 			
	 		}
	 		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			return (Utilisateur)HibernateUtil.getSessionFactory().getCurrentSession().load(Utilisateur.class, id);
		    
		} catch( NumberFormatException nfe ) {                
			throw new ConverterException(new FacesMessage("id utilisateur invalide"));
		} catch (EntityNotFoundException e) {
			throw new ConverterException(new FacesMessage("Utilisateur non trouve"));
		}
	   
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 == null) {
			return null;
		} else if (arg2 instanceof Utilisateur) {
			Utilisateur u = ((Utilisateur) arg2);
			return String.valueOf(u.getId_utilisateur());
		} else {
			throw new ConverterException("type invalide");
		}
	}
}