package fx.dao;

import fx.model.Subject;
import fx.model.Topic;

import java.util.List;

public interface TopicDao {
    void add(Topic topic);

    void edit(Topic topic);

    void delete(long id);

    Topic getTopicById(long id);

    List<Topic> listTopics();

    List<Topic> listTopics(Subject subject);
}
