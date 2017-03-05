package fx.dao;

import fx.model.Lesson;
import fx.model.Subject;
import fx.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

import static fx.util.HibernateUtil.getSessionFactory;

public class LessonDaoImpl implements LessonDao{
    private static final Logger LOG = LogManager.getLogger();

    private SessionFactory sessionFactory = getSessionFactory();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Lesson lesson) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(lesson);
        session.getTransaction().commit();
        LOG.debug("Saved lesson: {}", lesson.toString());
    }

    @Override
    public void edit(Lesson lesson) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(lesson);
        session.getTransaction().commit();
        LOG.debug("Lesson {} was updated", lesson.toString());
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("delete Lesson l where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        LOG.debug("Lesson with id {} was deleted", id);
    }

    @Override
    public Lesson getLessonById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Lesson lesson = (Lesson) session.createQuery("FROM Lesson l WHERE l.id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.getTransaction().commit();
        LOG.debug("Lesson found by id {}: {}", lesson.getId(), lesson.toString());
        return lesson;
    }

    @Override
    public List<Lesson> listLessons() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Lesson> lessonsList = session.createQuery("FROM Lesson").list();
        session.getTransaction().commit();
        LOG.debug("Lessons list: {}", lessonsList.toString());
        return lessonsList;
    }

    @Override
    public List<Lesson> listUserLessons(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Lesson> lessonsList = session.createQuery("FROM Lesson l WHERE l.userId = :userId")
                .setParameter("userId", user.getId())
                .list();
        session.getTransaction().commit();
        LOG.debug("Lessons list: {}", lessonsList.toString());
        return lessonsList;
    }

    @Override
    public List<Lesson> listLessons(Subject subject) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Lesson> lessonsList = session.createQuery("FROM Lesson l WHERE l.subject = :subject")
                .setParameter("subject", subject)
                .list();
        session.getTransaction().commit();
        LOG.debug("Lessons list: {}", lessonsList.toString());
        return lessonsList;
    }

    @Override
    public Subject getLessonSubject(Lesson lesson) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Subject subject = (Subject) session.load(Subject.class, (Long) lesson.getSubject().getId());
        session.getTransaction().commit();
        LOG.debug("Loaded subject: {}", subject.toString());
        return subject;
    }
}
