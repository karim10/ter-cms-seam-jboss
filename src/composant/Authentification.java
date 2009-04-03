package composant;

import java.util.List;

import javax.persistence.NoResultException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.security.Identity;

import util.HibernateUtil;

import entite.IUtilisateur;

@Name("authentification")
public class Authentification {

	@SuppressWarnings("unused")
	@Out(required=false)
	private SessionUtilisateur sessionUtilisateur;
	
	@In
	private Identity identity;
	
	/**
	 * authentifie un utilisateur
	 * @return vrai si l'authentification est effectué
	 *         faux sinon 
	 */
	@SuppressWarnings({"unchecked", "deprecation"})
	public boolean authentification() {
		try{    
			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();			
			
			List result = HibernateUtil.getSessionFactory().getCurrentSession().createQuery(
	            "from Utilisateur u where u.login = :login and u.motDePasse = :motdepasse and u.accesBackend=true")
	            .setParameter("login", identity.getUsername())
	            .setParameter("motdepasse", identity.getPassword())
	            .list();
			
			if (result.size()==0){
				sessionUtilisateur = null;
				return false;
			} else {
				IUtilisateur u = (IUtilisateur)result.get(0);
				if(u.isAdmin()){identity.addRole("admin");}
				sessionUtilisateur = new SessionUtilisateur(u);
				
				return true;		
			}
		}
		catch (NoResultException ex){
	         return false;
		}
	}
}
