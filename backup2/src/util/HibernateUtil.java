package util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;



public class HibernateUtil {
	 
	private static Log log = LogFactory.getLog(HibernateUtil.class);
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            log.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

	    
	    @SuppressWarnings("unchecked")
		public static Session currentSession() throws HibernateException {
	        Session s = (Session) session.get();
	        // Open a new Session, if this Thread has none yet
	        if (s == null) {
	            s = sessionFactory.openSession();
	            session.set(s);
	        }
	        return s;
	    }
	    
	    @SuppressWarnings("unchecked")
		public static final ThreadLocal session = new ThreadLocal();
	 
	    public static SessionFactory getSessionFactory() {
	        return sessionFactory;
	    }
	 
	    @SuppressWarnings("unchecked")
		public static void closeSession() throws HibernateException {
	        Session s = (Session) session.get();
	        session.set(null);
	        if (s != null)
	            s.close();
	    }

}
