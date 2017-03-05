package fx.dao;

import fx.model.Subject;
import fx.model.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

import static fx.util.HibernateUtil.getSessionFactory;

public class TopicDaoImpl implements TopicDao{
    private static final Logger LOG = LogManager.getLogger();

    private SessionFactory sessionFactory = getSessionFactory();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Topic topic) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(topic);
        session.getTransaction().commit();
        LOG.debug("Saved topic: {}", topic.toString());
    }

    @Override
    public void edit(Topic topic) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(topic);
        session.getTransaction().commit();
        LOG.debug("Topic {} was updated", topic.toString());
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("delete Topic t where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        LOG.debug("Topic with id {} was deleted", id);
    }

    @Override
    public Topic getTopicById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Topic topic = (Topic) session.createQuery("FROM Topic t WHERE t.id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.getTransaction().commit();
        LOG.debug("Topic found by id {}: {}", topic.getId(), topic.toString());
        return topic;
    }

    @Override
    public List<Topic> listTopics() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Topic> topicsList = session.createQuery("FROM Topic").list();
        session.getTransaction().commit();
        LOG.debug("Topics list: {}", topicsList.toString());
        return topicsList;
    }

    @Override
    public List<Topic> listTopics(Subject subject) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Topic> topicsList = session.createQuery("FROM Topic t WHERE t.subject = :subject")
                .setParameter("subject", subject)
                .list();
        session.getTransaction().commit();
        LOG.debug("Topics list: {}", topicsList.toString());
        return topicsList;
    }
}
