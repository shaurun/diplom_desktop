package fx.dao;

import fx.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import static fx.util.HibernateUtil.getSessionFactory;

public class UserDaoImpl implements UserDao {
    private static final Logger LOG = LogManager.getLogger();

    private SessionFactory sessionFactory = getSessionFactory();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        User user = (User) session.createQuery("FROM User u WHERE u.username = :username")
                .setParameter("username", username)
                .uniqueResult();
        session.getTransaction().commit();
        LOG.debug("User found by username {}: {}", username, user);
        return user;
    }
}
