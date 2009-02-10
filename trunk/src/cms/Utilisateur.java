package cms;



import javax.persistence.Entity;
import javax.persistence.Table;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("utilisateur")
@Table(name="utilisateur")
public class Utilisateur extends UtilisateurAbstrait {

	private static final long serialVersionUID = -3655311686394408087L;

	public Utilisateur () {}

}
