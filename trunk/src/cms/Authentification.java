package cms;

import java.util.List;

import javax.persistence.NoResultException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.security.Identity;

@Name("authentification")
public class Authentification {

	@SuppressWarnings("unused")
	@Out(required=false)
	private SessionUtilisateur sessionUtilisateur;
	
	@In
	private Identity identity;
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public boolean authentification() {
		try{    
			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();			
			
			List result = HibernateUtil.getSessionFactory().getCurrentSession().createQuery(
	            "from Utilisateur where login = :login and motDePasse = :motdepasse")
	            .setParameter("login", identity.getUsername())
	            .setParameter("motdepasse", identity.getPassword())
	            .list();
			if (result.size()==0){
				sessionUtilisateur = null;
				return false;
			}
			else{
				Utilisateur u = (Utilisateur)result.get(0);
				if(u.isAdmin()){identity.addRole("admin");}
				sessionUtilisateur = new SessionUtilisateur(u,DataUtil.chargeContenu());
				//Contexts.getSessionContext().set("authenticatedUser", utilisateur);         
				
				return true;		
			}
		}
		catch (NoResultException ex){
	         return false;
		}
	}
}
