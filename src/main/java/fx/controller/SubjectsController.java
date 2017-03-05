package fx.controller;

import fx.dao.SubjectDao;
import fx.dao.SubjectDaoImpl;
import fx.model.Subject;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubjectsController implements Initializable {
    @FXML private TableView tableSubjects;
    @FXML private TableColumn colTitle;
    @FXML private TableColumn colEdit;
    @FXML private TableColumn colDelete;
    @FXML private TextField txtSubjectName;
    @FXML private Button btnAddSubject;

    private Subject subject;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    SubjectDao subjectDao = new SubjectDaoImpl();

    public void editSubject(ActionEvent event) {
        Long id = Long.parseLong(((Hyperlink) event.getSource()).getId());
        subject = subjectDao.getSubjectById(id);
        txtSubjectName.setText(subject.getName());
        btnAddSubject.setText("Изменить дисциплину");
        final EventHandler eventHandler = btnAddSubject.getOnAction();
        btnAddSubject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                subject.setName(txtSubjectName.getText());
                subjectDao.edit(subject);
                txtSubjectName.setText("");
                btnAddSubject.setText("Добавить дисциплину");
                btnAddSubject.setOnAction(eventHandler);
                update();
            }
        });
    }

    public void deleteSubject(ActionEvent event) {
        Long id = Long.parseLong(((Hyperlink) event.getSource()).getId());
        subjectDao.delete(id);
        update();
    }

    public void addSubject(ActionEvent event) {
        Subject subject = new Subject();
        subject.setUser(UserSession.getUser());
        subject.setName(txtSubjectName.getText());
        subjectDao.addSubject(subject);
        txtSubjectName.setText("");
        update();
    }

    private void openSubject(ActionEvent event) throws IOException {
        UserSession.setSubject(subjectDao.getSubjectById(Long.parseLong(((Hyperlink)(event.getSource())).getId())));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/subject.fxml"));
        Parent panel = loader.load();
        SubjectController controller = (SubjectController) loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(panel, 600, 763);
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    protected void update() {
        List<SubjectV> subjectVs = new ArrayList<>();
        List<Subject> subjects = subjectDao.listSubjects();
        for (Subject subject : subjects) {
            subjectVs.add(new SubjectV(subject));
        }

        ObservableList<SubjectV> data =
                FXCollections.observableArrayList(subjectVs);
        tableSubjects.setItems(data);
        colTitle.setCellValueFactory(new PropertyValueFactory<SubjectV,Hyperlink>("subj"));
        colEdit.setCellValueFactory(new PropertyValueFactory<SubjectV, Hyperlink>("edit"));
        colDelete.setCellValueFactory(new PropertyValueFactory<SubjectV, Hyperlink>("delete"));
    }

    public class SubjectV extends Subject {
        private Hyperlink subj;
        private Hyperlink edit;
        private Hyperlink delete;

        public SubjectV() {
            super();
        }

        public SubjectV(Subject subject) {
            setId(subject.getId());
            setUser(subject.getUser());
            setLessons(subject.getLessons());
            setName(subject.getName());
            setTopics(subject.getTopics());
            Hyperlink link = new Hyperlink(subject.getName());
            link.setId(String.valueOf(subject.getId()));
            link.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        openSubject(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            setSubj(link);
            link = new Hyperlink("Изменить");
            link.setId(String.valueOf(subject.getId()));
            link.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editSubject(event);
                }
            });
            setEdit(link);
            link = new Hyperlink("Удалить");
            link.setId(String.valueOf(subject.getId()));
            link.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                   deleteSubject(event);
                }
            });
            setDelete(link);
        }

        public Hyperlink getEdit() {
            return edit;
        }

        public void setEdit(Hyperlink edit) {
            this.edit = edit;
        }

        public Hyperlink getDelete() {
            return delete;
        }

        public void setDelete(Hyperlink delete) {
            this.delete = delete;
        }

        public Hyperlink getSubj() {
            return subj;
        }

        public void setSubj(Hyperlink subj) {
            this.subj = subj;
        }
    }


}
