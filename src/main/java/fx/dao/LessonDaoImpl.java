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
        session.persist(lesson);
        LOG.debug("Saved lesson: {}", lesson.toString());
    }

    @Override
    public void edit(Lesson lesson) {
        Session session = sessionFactory.getCurrentSession();
        session.update(lesson);
        LOG.debug("Lesson {} was updated", lesson.toString());
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        Lesson lesson = (Lesson) session.load(Lesson.class, new Long(id));
        if (lesson != null){
            session.delete(lesson);
            LOG.debug("Lesson {} was deleted", lesson.toString());
        } else {
            LOG.debug("Request to delete lesson {} was declined. No lesson with id {} was found in database",
                    lesson.toString(), lesson.getId());
        }
    }

    @Override
    public Lesson getLessonById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Lesson lesson = (Lesson) session.load(Lesson.class, new Long(id));
        LOG.debug("Lesson found by id {}: {}", lesson.getId(), lesson.toString());
        return lesson;
    }

    @Override
    public List<Lesson> listLessons() {
        Session session = sessionFactory.getCurrentSession();
        List<Lesson> lessonsList = session.createQuery("FROM Lesson").list();
        LOG.debug("Lessons list: {}", lessonsList.toString());
        return lessonsList;
    }

    @Override
    public List<Lesson> listUserLessons(User user) {
        Session session = sessionFactory.getCurrentSession();
        List<Lesson> lessonsList = session.createQuery("FROM Lesson l WHERE l.userId = :userId")
                .setParameter("userId", user.getId())
                .list();
        LOG.debug("Lessons list: {}", lessonsList.toString());
        return lessonsList;
    }

    @Override
    public Subject getLessonSubject(Lesson lesson) {
        Session session = sessionFactory.getCurrentSession();
        Subject subject = (Subject) session.load(Subject.class, (Long) lesson.getSubject().getId());
        LOG.debug("Loaded subject: {}", subject.toString());
        return subject;
    }
}
