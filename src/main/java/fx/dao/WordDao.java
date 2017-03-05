package fx.dao;

import fx.model.Lesson;
import fx.model.Topic;
import fx.model.Word;

import java.util.List;

public interface WordDao {
    void add(Word word);

    void edit(Word word);

    void delete(long id);

    Word getWordById(long id);

    List<Word> listWords();

    List<Word> listLessonWords(Lesson lesson);

    List<Word> listTopicWords(Topic topic);
}
