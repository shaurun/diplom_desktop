package fx.controller;

import fx.dao.LessonDao;
import fx.dao.LessonDaoImpl;
import fx.dao.TopicDao;
import fx.dao.TopicDaoImpl;
import fx.model.Lesson;
import fx.model.Subject;
import fx.model.Topic;
import fx.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubjectController implements Initializable {
    private Stage stage;
    private Subject subject = UserSession.getSubject();
    private LessonDao lessonDao = new LessonDaoImpl();
    private TopicDao topicDao = new TopicDaoImpl();

    @FXML private TableView tableLessons;
    @FXML private TableColumn colLessonName;
    @FXML private TableColumn colLessonEdit;
    @FXML private TableColumn colLessonDelete;
    @FXML private TextField txtLessonName;
    @FXML private Button btnAddLesson;
    @FXML private TableView tableTopics;
    @FXML private TableColumn colTopicName;
    @FXML private TableColumn colTopicEdit;
    @FXML private TableColumn colTopicDelete;
    @FXML private TextField txtTopicName;
    @FXML private ColorPicker cpTopicColor;
    @FXML private Button btnAddTopic;

    Lesson lesson;
    Topic topic;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateLessons();
        updateTopics();
    }

    protected void updateLessons() {
        List<LessonV> lessonVs = new ArrayList<>();
        List<Lesson> lessons = lessonDao.listLessons(subject);
        for (Lesson lesson : lessons) {
            lessonVs.add(new LessonV(lesson));
        }

        ObservableList<LessonV> data =
                FXCollections.observableArrayList(lessonVs);
        tableLessons.setItems(data);
        colLessonName.setCellValueFactory(new PropertyValueFactory<LessonV,Hyperlink>("les"));
        colLessonEdit.setCellValueFactory(new PropertyValueFactory<LessonV, Hyperlink>("edit"));
        colLessonDelete.setCellValueFactory(new PropertyValueFactory<LessonV, Hyperlink>("delete"));
    }

    protected void updateTopics() {
        List<TopicV> topicVs = new ArrayList<>();
        List<Topic> topics = topicDao.listTopics(subject);
        for (Topic topic : topics) {
            topicVs.add(new TopicV(topic));
        }

        ObservableList<TopicV> data =
                FXCollections.observableArrayList(topicVs);
        tableTopics.setItems(data);
        colTopicName.setCellValueFactory(new PropertyValueFactory<TopicV,Hyperlink>("top"));
        colTopicEdit.setCellValueFactory(new PropertyValueFactory<TopicV, Hyperlink>("edit"));
        colTopicDelete.setCellValueFactory(new PropertyValueFactory<TopicV, Hyperlink>("delete"));
    }

    private void editLesson(ActionEvent event) {
        Long id = Long.parseLong(((Hyperlink) event.getSource()).getId());
        lesson = lessonDao.getLessonById(id);
        txtLessonName.setText(lesson.getName());
        btnAddLesson.setText("Изменить урок");
        final EventHandler eventHandler = btnAddLesson.getOnAction();
        btnAddLesson.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lesson.setName(txtLessonName.getText());
                lessonDao.edit(lesson);
                txtLessonName.setText("");
                btnAddLesson.setText("Добавить урок");
                btnAddLesson.setOnAction(eventHandler);
                updateLessons();
            }
        });
    }

    private void editTopic(ActionEvent event) {
        Long id = Long.parseLong(((Hyperlink) event.getSource()).getId());
        topic = topicDao.getTopicById(id);
        txtTopicName.setText(topic.getName());
        btnAddTopic.setText("Изменить тему");
        cpTopicColor.setValue(Color.web(topic.getColor()));
        final EventHandler eventHandler = btnAddTopic.getOnAction();
        btnAddTopic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                topic.setName(txtTopicName.getText());
                topic.setColor("#"+Integer.toHexString(cpTopicColor.getValue().hashCode()).substring(0, 6).toUpperCase());
                topicDao.edit(topic);
                txtTopicName.setText("");
                cpTopicColor.setValue(Color.WHITE);
                btnAddTopic.setText("Добавить тему");
                btnAddTopic.setOnAction(eventHandler);
                updateTopics();
            }
        });
    }

    public void deleteLesson(ActionEvent event) {
        Long id = Long.parseLong(((Hyperlink) event.getSource()).getId());
        lessonDao.delete(id);
        updateLessons();
    }

    public void deleteTopic(ActionEvent event) {
        Long id = Long.parseLong(((Hyperlink) event.getSource()).getId());
        topicDao.delete(id);
        updateTopics();
    }

    public void addLesson(ActionEvent event) {
        Lesson lesson = new Lesson();
        lesson.setSubject(subject);
        lesson.setName(txtLessonName.getText());
        lessonDao.add(lesson);
        txtLessonName.setText("");
        updateLessons();
    }

    public void addTopic(ActionEvent event) {
        Topic topic = new Topic();
        topic.setSubject(subject);
        topic.setName(txtTopicName.getText());
        topic.setColor("#"+Integer.toHexString(cpTopicColor.getValue().hashCode()).substring(0, 6).toUpperCase());
        topicDao.add(topic);
        txtTopicName.setText("");
        updateTopics();
    }

    public class LessonV {
        private Hyperlink les;
        private Hyperlink edit;
        private Hyperlink delete;

        public LessonV(Lesson lesson) {
            setLes(lesson);
            setEdit(lesson);
            setDelete(lesson);
        }

        public Hyperlink getLes() {
            return les;
        }

        public void setLes(Lesson lesson) {
            this.les = new Hyperlink(lesson.getName());
            this.les.setId(String.valueOf(lesson.getId()));
            this.les.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        openLesson(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public Hyperlink getEdit() {
            return edit;
        }

        public void setEdit(Lesson lesson) {
            this.edit = new Hyperlink("Изменить");
            this.edit.setId(String.valueOf(lesson.getId()));
            this.edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editLesson(event);
                }
            });
        }

        public Hyperlink getDelete() {
            return delete;
        }

        public void setDelete(Lesson lesson) {
            this.delete = new Hyperlink("Удалить");
            this.delete.setId(String.valueOf(lesson.getId()));
            this.delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    deleteLesson(event);
                }
            });
        }
    }

    public class TopicV {
        private Hyperlink top;
        private Hyperlink edit;
        private Hyperlink delete;

        public TopicV(Topic topic) {
            setTop(topic);
            setEdit(topic);
            setDelete(topic);
        }

        public Hyperlink getTop() {
            return top;
        }

        public void setTop(Topic topic) {
            this.top = new Hyperlink(topic.getName());
            this.top.setId(String.valueOf(topic.getId()));
            try {
                this.top.setBackground(new Background(
                        new BackgroundFill(Color.web(topic.getColor()), null, null)));
            } catch (Exception e) {

            }
            this.top.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                }
            });
        }

        public Hyperlink getEdit() {
            return edit;
        }

        public void setEdit(Topic topic) {
            this.edit = new Hyperlink("Изменить");
            this.edit.setId(String.valueOf(topic.getId()));
            this.edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editTopic(event);
                }
            });
        }

        public Hyperlink getDelete() {
            return delete;
        }

        public void setDelete(Topic topic) {
            this.delete = new Hyperlink("Удалить");
            this.delete.setId(String.valueOf(topic.getId()));
            this.delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    deleteTopic(event);
                }
            });
        }
    }

    private void openLesson(ActionEvent event) throws IOException {
        UserSession.setLesson(lessonDao.getLessonById(Long.parseLong(((Hyperlink)(event.getSource())).getId())));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lesson.fxml"));
        Parent panel = loader.load();
        VocabularyController controller = (VocabularyController) loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(panel, 600, 763);
        stage.setScene(scene);
    }
}
