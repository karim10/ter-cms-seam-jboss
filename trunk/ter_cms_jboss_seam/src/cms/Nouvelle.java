package cms;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author keira
 * <p>Classe entit� Nouvelle
 * @see Contenu
 */
@Entity
@Table(name="NOUVELLE")
public class Nouvelle extends Contenu{

	private static final long serialVersionUID = 6241550494166669352L;

	public Nouvelle(){}
	
}
