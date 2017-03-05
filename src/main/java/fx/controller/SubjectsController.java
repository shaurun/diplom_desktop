package fx.controller;

import fx.dao.SubjectDao;
import fx.dao.SubjectDaoImpl;
import fx.model.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SubjectsController {
    @FXML TableView tableSubjects;
    @FXML TableColumn colTitle;
    @FXML TableColumn colEdit;
    @FXML TableColumn colDelete;
    @FXML TextField txtSubjectName;
    @FXML Button btnAddSubject;

    SubjectDao subjectDao = new SubjectDaoImpl();

    public void addSubject(ActionEvent event) {
        ObservableList<Subject> data =
                FXCollections.observableArrayList(subjectDao.listSubjects());
        tableSubjects.setItems(data);
        colTitle.setCellValueFactory(new PropertyValueFactory<Subject,String>("name"));
        //colTitle.setCellValueFactory();


    }
}
