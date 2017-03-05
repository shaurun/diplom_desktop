package fx.dao;

import fx.model.Lesson;
import fx.model.Subject;
import fx.model.User;

import java.util.List;

public interface LessonDao {

    void add(Lesson lesson);

    void edit(Lesson lesson);

    void delete(long id);

    Lesson getLessonById(long id);

    List<Lesson> listLessons();

    List<Lesson> listUserLessons(User user);

    List<Lesson> listLessons(Subject subject);

    Subject getLessonSubject(Lesson lesson);
}
