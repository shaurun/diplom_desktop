package fx.dao;

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
        session.persist(topic);
        LOG.debug("Saved topic: {}", topic.toString());
    }

    @Override
    public void edit(Topic topic) {
        Session session = sessionFactory.getCurrentSession();
        session.update(topic);
        LOG.debug("Topic {} was updated", topic.toString());
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        Topic topic = (Topic) session.load(Topic.class, new Long(id));
        if (topic != null){
            session.delete(topic);
            LOG.debug("Topic {} was deleted", topic.toString());
        } else {
            LOG.debug("Request to delete topic {} was declined. No topic with id {} was found in database",
                    topic.toString(), topic.getId());
        }
    }

    @Override
    public Topic getTopicById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Topic topic = (Topic) session.load(Topic.class, new Long(id));
        LOG.debug("Topic found by id {}: {}", topic.getId(), topic.toString());
        return topic;
    }

    @Override
    public List<Topic> listTopics() {
        Session session = sessionFactory.getCurrentSession();
        List<Topic> topicsList = session.createQuery("FROM Topic").list();
        LOG.debug("Topics list: {}", topicsList.toString());
        return topicsList;
    }
}
