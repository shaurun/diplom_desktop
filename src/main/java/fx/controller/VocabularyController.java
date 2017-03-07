package fx.controller;

import fx.dao.*;
import fx.model.Lesson;
import fx.model.Subject;
import fx.model.Topic;
import fx.model.Word;
import fx.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class VocabularyController implements Initializable{
    private Stage stage;
    private Subject subject = UserSession.getSubject();
    private Lesson lesson = UserSession.getLesson();
    private Word word = new Word();
    private Topic topicToBind;

    private LessonDao lessonDao = new LessonDaoImpl();
    private TopicDao topicDao = new TopicDaoImpl();
    private WordDao wordDao = new WordDaoImpl();

    @FXML private TableView tableVocabulary;
    @FXML private TableColumn colWord;
    @FXML private TableColumn colTranslation;
    @FXML private TableColumn colTopics;
    @FXML private TableColumn colEdit;
    @FXML private TableColumn colDelete;
    @FXML private TextField txtWord;
    @FXML private TextField txtTranslation;
    @FXML private Hyperlink lnkTopic;
    @FXML private Button btnAdd;
    @FXML private FlowPane cloud;


    public void delete(ActionEvent event) {
        Long id = Long.parseLong(((Hyperlink) event.getSource()).getId());
        wordDao.delete(id);
        update();
    }

    public void edit(ActionEvent event) {
        Long id = Long.parseLong(((Hyperlink) event.getSource()).getId());
        word = wordDao.getWordById(id);
        txtWord.setText(word.getWord());
        txtTranslation.setText(word.getTranslation());
        Topic topic = word.getTopic();
        if (topic != null) {
            lnkTopic.setVisible(true);
            lnkTopic.setText(topic.getName());
            lnkTopic.setBackground(new Background(
                    new BackgroundFill(Color.web(topic.getColor()), null, null)));
            lnkTopic.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    unbindTopic(event);
                }
            });
        } else {
            lnkTopic.setVisible(false);
        }
        btnAdd.setText("Изменить слово");
        final EventHandler eventHandler = btnAdd.getOnAction();
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                word.setWord(txtWord.getText());
                word.setTranslation(txtTranslation.getText());
                wordDao.edit(word);
                txtWord.setText("");
                txtTranslation.setText("");
                lnkTopic.setVisible(false);
                btnAdd.setText("Добавить слово");
                btnAdd.setOnAction(eventHandler);
                update();
                word = new Word();
            }
        });
    }

    private void unbindTopic(ActionEvent event) {
        word.setTopic(null);
        lnkTopic.setVisible(false);
    }

    public void add(ActionEvent event) {
        word.setLesson(lesson);
        word.setWord(txtWord.getText());
        word.setTranslation(txtTranslation.getText());
        wordDao.add(word);
        txtWord.setText("");
        txtTranslation.setText("");
        lnkTopic.setVisible(false);
        word = new Word();
        update();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
        initCloud();
    }

    private void initCloud() {
        List<Topic> topics = topicDao.listTopics(subject);
        for (final Topic topic : topics) {
            Hyperlink topicLink = new Hyperlink(topic.getName());
            topicLink.setBackground(new Background(
                    new BackgroundFill(Color.web(topic.getColor()), null, null)));
            topicLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    word.setTopic(topic);
                    lnkTopic.setVisible(true);
                    lnkTopic.setText(topic.getName());
                    lnkTopic.setBackground(new Background(new BackgroundFill(Color.web(topic.getColor()), null, null)));
                    lnkTopic.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            unbindTopic(event);
                        }
                    });
                }
            });
            cloud.getChildren().add(topicLink);
        }
    }

    protected void update() {
        List<WordV> wordVs = new ArrayList<>();
        List<Word> words = wordDao.listLessonWords(lesson);
        for (Word word : words) {
            wordVs.add(new WordV(word));
        }

        ObservableList<WordV> data = FXCollections.observableArrayList(wordVs);
        tableVocabulary.setItems(data);
        colWord.setCellValueFactory(new PropertyValueFactory<WordV, String>("word"));
        colTranslation.setCellValueFactory(new PropertyValueFactory<WordV, String>("translation"));
        colTopics.setCellValueFactory(new PropertyValueFactory<WordV, Label>("topic"));
        colEdit.setCellValueFactory(new PropertyValueFactory<WordV, Hyperlink>("edit"));
        colDelete.setCellValueFactory(new PropertyValueFactory<WordV, Hyperlink>("delete"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public class WordV {
        private String word;
        private String translation;
        private Label topic;
        private Hyperlink edit;
        private Hyperlink delete;

        public WordV(Word word) {
            setWord(word);
            setTranslation(word);
            setTopic(word);
            setEdit(word);
            setDelete(word);
        }

        public String getWord() {
            return word;
        }

        public void setWord(Word word) {
            this.word = word.getWord();
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(Word word) {
            this.translation = word.getTranslation();
        }

        public Label getTopic() {
            return topic;
        }

        public void setTopic(Word word) {
            Label label = new Label();
            Topic topic = word.getTopic();
            if (topic != null) {
                label.setText(topic.getName());
                label.setBackground(new Background(
                        new BackgroundFill(Color.web(topic.getColor()), null, null)));
                label.setId(String.valueOf(topic.getId()));
            }
            this.topic = label;
        }

        public Hyperlink getEdit() {
            return edit;
        }

        public void setEdit(Word word) {
            this.edit = new Hyperlink("Изменить");
            this.edit.setId(String.valueOf(word.getId()));
            this.edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    edit(event);
                }
            });
        }

        public Hyperlink getDelete() {
            return delete;
        }

        public void setDelete(Word word) {
            this.delete = new Hyperlink("Удалить");
            this.delete.setId(String.valueOf(word.getId()));
            this.delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    delete(event);
                }
            });
        }
    }
}
