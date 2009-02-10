package cms;

@SuppressWarnings("serial")
public class ContenuException extends Exception{

	public ContenuException(String msg){
		super("Problème contenu : "+msg);
	}
}
