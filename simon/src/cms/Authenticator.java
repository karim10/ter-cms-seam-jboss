package cms;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("authenticator")
public class Authenticator {
		
		    private @Logger Log log;
		    
		    private @In Identity identity;
		   
		    public boolean authenticate()
		    {
		        
		        return true;
		    }
		
	
}
