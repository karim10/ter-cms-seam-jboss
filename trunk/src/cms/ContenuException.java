package cms;

@SuppressWarnings("serial")
public class ContenuException extends Exception{

	public ContenuException(String msg){
		super("Probl�me contenu : "+msg);
	}
}
