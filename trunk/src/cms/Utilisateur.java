package cms;



import javax.persistence.Entity;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@SuppressWarnings("serial")
@Entity
@Name("utilisateur")
@Table(name="utilisateur")
public class Utilisateur extends UtilisateurAbstrait {

	public Utilisateur () {}

}
