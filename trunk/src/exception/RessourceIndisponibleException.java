package exception;

import org.jboss.seam.annotations.exception.HttpError;
import org.jboss.seam.annotations.exception.Redirect;

@SuppressWarnings("serial")
@HttpError(errorCode=404)
@Redirect(viewId ="/error.xhtml",message="La ressource que vous demandez n'est pas disponible")
public class RessourceIndisponibleException  extends Exception{

}
