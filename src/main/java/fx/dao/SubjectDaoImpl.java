package fx.dao;

import fx.model.Lesson;
import fx.model.Subject;
import fx.model.Topic;
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
        session.persist(subject);
        LOG.debug("Saved subject: {}", subject.toString());
    }

    @Override
    public void edit(Subject subject) {
        Session session = sessionFactory.getCurrentSession();
        session.update(subject);
        LOG.debug("Subject {} was updated", subject.toString());
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        Subject subject = (Subject) session.load(Subject.class, new Long(id));
        if (subject != null){
            session.delete(subject);
            LOG.debug("Subject {} was deleted", subject.toString());
        } else {
            LOG.debug("Request to delete subject {} was declined. No subject with id {} was found in database",
                    subject.toString(), subject.getId());
        }
    }

    @Override
    public Subject getSubjectById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Subject subject = (Subject) session.load(Subject.class, new Long(id));
        LOG.debug("Subject found by id {}: {}", subject.getId(), subject.toString());
        return subject;
    }

    @Override
    public List<Subject> listSubjects() {
        Session session = sessionFactory.getCurrentSession();
        List<Subject> subjectsList = session.createQuery("FROM Subject").list();
        LOG.debug("Subjects list: {}", subjectsList.toString());
        return subjectsList;
    }

    @Override
    public List<Lesson> listSubjectLessons(long subjectId) {
        Session session = sessionFactory.getCurrentSession();
        List<Lesson> lessonsList = session.createQuery("FROM Lesson l WHERE l.subject = :subject")
                .setParameter("subject", getSubjectById(subjectId))
                .list();
        return lessonsList;
    }

    @Override
    public List<Topic> listSubjectTopics(long subjectId) {
        Session session = sessionFactory.getCurrentSession();
        List<Topic> topicsList = session.createQuery("FROM Topic t WHERE t.subject = :subject")
                .setParameter("subject", getSubjectById(subjectId))
                .list();
        return topicsList;
    }
}
