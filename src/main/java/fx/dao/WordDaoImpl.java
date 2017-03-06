package fx.dao;

import fx.model.Lesson;
import fx.model.Topic;
import fx.model.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;;

import java.util.List;

import static fx.util.HibernateUtil.getSessionFactory;

public class WordDaoImpl implements WordDao{
    private static final Logger LOG = LogManager.getLogger();

    private SessionFactory sessionFactory = getSessionFactory();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Word word) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(word);
        session.getTransaction().commit();
        LOG.debug("Saved new word: {}", word.toString());
    }

    @Override
    public void edit(Word word) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("update Word set word = :word, translation = :translation where id = :id")
                .setParameter("word", word.getWord())
                .setParameter("translation", word.getTranslation())
                .setParameter("id", word.getId())
                .executeUpdate();
        session.getTransaction().commit();
        LOG.debug("Updated word: {}", word.toString());
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("delete Word w where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        LOG.debug("Deleted word with id {} ", id);
    }

    @Override
    public Word getWordById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Word word = (Word) session.createQuery("FROM Word w WHERE w.id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.getTransaction().commit();
        LOG.debug("Word found by id {}: {}", id, word.toString());
        return word;
    }

    @Override
    public List<Word> listWords() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Word> wordsList = session.createQuery("FROM Word").list();
        session.getTransaction().commit();
        LOG.debug("Words list: {}", wordsList.toString());
        return wordsList;
    }

    @Override
    public List<Word> listLessonWords(Lesson lesson) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Word> wordsList = session.createQuery("FROM Word w WHERE w.lesson = :lesson")
                .setParameter("lesson", lesson)
                .list();
        session.getTransaction().commit();
        LOG.debug("Words list: {}", wordsList.toString());
        return wordsList;
    }

    @Override
    public List<Word> listTopicWords(Topic topic) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Word> wordsList = session.createQuery("FROM Word w WHERE w.topic = :topic")
                .setParameter("topic", topic)
                .list();
        session.getTransaction().commit();
        LOG.debug("Words list: {}", wordsList.toString());
        return wordsList;
    }
}
