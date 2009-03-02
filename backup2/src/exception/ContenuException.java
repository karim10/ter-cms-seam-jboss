package exception;

import org.jboss.seam.annotations.ApplicationException;

@SuppressWarnings("serial")
@ApplicationException

public class ContenuException extends RuntimeException {

	public ContenuException(String msg){
		super("Problème contenu : "+msg);
	}
}
