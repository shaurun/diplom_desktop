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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

    SubjectDao subjectDao = new SubjectDaoImpl();

    public void editSubject(ActionEvent event) {
        Long id = Long.parseLong(((Button) event.getSource()).getId());
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
        Long id = Long.parseLong(((Button) event.getSource()).getId());
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
        colTitle.setCellValueFactory(new PropertyValueFactory<SubjectV,String>("name"));
        colEdit.setCellValueFactory(new PropertyValueFactory<SubjectV, Button>("edit"));
        colDelete.setCellValueFactory(new PropertyValueFactory<SubjectV, Button>("delete"));
    }

    public class SubjectV extends Subject {
        private Button edit;
        private Button delete;

        public SubjectV() {
            super();
        }

        public SubjectV(Subject subject) {
            setId(subject.getId());
            setUser(subject.getUser());
            setLessons(subject.getLessons());
            setName(subject.getName());
            setTopics(subject.getTopics());
            Button btn = new Button("Изменить");
            btn.setId(String.valueOf(subject.getId()));
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    editSubject(event);
                }
            });
            setEdit(btn);
            btn = new Button("Удалить");
            btn.setId(String.valueOf(subject.getId()));
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                   deleteSubject(event);
                }
            });
            setDelete(btn);
        }

        public Button getEdit() {
            return edit;
        }

        public void setEdit(Button edit) {
            this.edit = edit;
        }

        public Button getDelete() {
            return delete;
        }

        public void setDelete(Button delete) {
            this.delete = delete;
        }
    }
}
