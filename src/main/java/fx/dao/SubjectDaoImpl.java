package fx.dao;

import fx.model.Lesson;
import fx.model.Subject;
import fx.model.Topic;
import fx.util.UserSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

import static fx.util.HibernateUtil.getSessionFactory;

public class SubjectDaoImpl implements SubjectDao{
    private static final Logger LOG = LogManager.getLogger();

    private SessionFactory sessionFactory = getSessionFactory();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addSubject(Subject subject) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(subject);
        session.getTransaction().commit();
        LOG.debug("Saved subject: {}", subject.toString());
    }

    @Override
    public void edit(Subject subject) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("update Subject set name = :name where id = :id")
                .setParameter("name", subject.getName())
                .setParameter("id", subject.getId())
                .executeUpdate();
        //session.update(subject);
        session.getTransaction().commit();
        LOG.debug("Subject {} was updated", subject.toString());
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("delete Subject s where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();

        LOG.debug("Subject with id {} was deleted", id);
    }

    @Override
    public Subject getSubjectById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Subject subject = (Subject) session.createQuery("FROM Subject s WHERE s.id = :id")
                .setParameter("id", id)
                .uniqueResult();
        //Subject subject = (Subject) session.load(Subject.class, new Long(id));
        session.getTransaction().commit();
        LOG.debug("Subject found by id {}: {}", subject.getId(), subject.toString());
        return subject;
    }

    @Override
    public List<Subject> listSubjects() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Subject> subjectsList = session.createQuery("FROM Subject s WHERE s.user = :user")
                .setParameter("user", UserSession.getUser())
                .list();
        session.getTransaction().commit();
        LOG.debug("Subjects list: {}", subjectsList.toString());
        return subjectsList;
    }

    @Override
    public List<Lesson> listSubjectLessons(long subjectId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Lesson> lessonsList = session.createQuery("FROM Lesson l WHERE l.subject = :subject")
                .setParameter("subject", getSubjectById(subjectId))
                .list();
        session.getTransaction().commit();
        return lessonsList;
    }

    @Override
    public List<Topic> listSubjectTopics(long subjectId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Topic> topicsList = session.createQuery("FROM Topic t WHERE t.subject = :subject")
                .setParameter("subject", getSubjectById(subjectId))
                .list();
        session.getTransaction().commit();
        return topicsList;
    }
}
