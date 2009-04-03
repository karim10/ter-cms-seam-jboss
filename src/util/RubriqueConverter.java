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

@Name("rubriqueConverter")
@BypassInterceptors
@Converter
public class RubriqueConverter implements javax.faces.convert.Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		
		
	    try {
	    	 Long id = new Long (arg2);
	 		if (id == null || id.equals(0)) {
	 			return null;
	 			
	 		}
	 		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			return (Rubrique)HibernateUtil.getSessionFactory().getCurrentSession().load(Rubrique.class, id);
		    
		} catch( NumberFormatException nfe ) {                
			throw new ConverterException(new FacesMessage("id rubrique invalide"));
		} catch (EntityNotFoundException e) {
			throw new ConverterException(new FacesMessage("Rubrique non trouve"));
		}
	   
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 == null) {
			return null;
		} else if (arg2 instanceof Rubrique) {
			Rubrique r = ((Rubrique) arg2);
			return String.valueOf(r.getId_contenu());
		} else {
			throw new ConverterException("type invalide");
		}
	}
}