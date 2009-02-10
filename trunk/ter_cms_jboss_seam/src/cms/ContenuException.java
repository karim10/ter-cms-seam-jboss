package cms;

import org.jboss.seam.annotations.Name;

@Name("contenuException")
@SuppressWarnings("serial")
public class ContenuException extends Exception{

	public ContenuException(){}
	public ContenuException(String msg){
		super(" Problème : "+ msg);
	}
	
	
}
