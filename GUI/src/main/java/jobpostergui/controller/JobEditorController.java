package jobpostergui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.web.HTMLEditor;
import jobpostergui.MainApp;
import jobpostergui.model.JobForTableView;

public class JobEditorController {
    
    @FXML
    private TableView<JobForTableView> jobTable;
    @FXML
    private TableColumn<JobForTableView, String> htmlFileNameColumn;
    @FXML
    private TableColumn<JobForTableView, Boolean> visibleColumn;
    
    @FXML
    private CheckBox visibleCheckBox;
    @FXML
    private TextField fileNameField;
    @FXML
    private HTMLEditor contentHtmlEditor;
    
    private MainApp mainApp;
        
    
    public JobEditorController() {        
    }
    
    @FXML
    private void initialize() {
        htmlFileNameColumn.setCellValueFactory(cellData -> cellData.getValue().htmlFileKeyProperty());
        visibleColumn.setCellValueFactory(param -> param.getValue().visibleProperty());
        visibleColumn.setCellFactory(CheckBoxTableCell.forTableColumn(visibleColumn));
        
        showJobDetails(null);
        
        jobTable.getSelectionModel().selectedItemProperty().addListener(
                (obervable, oldValue, newValue) -> showJobDetails(newValue));  
        
        contentHtmlEditor.setStyle("-fx-font-family: Times New Roman");
        
    }
    
    private void showJobDetails(JobForTableView jobForTableView) {
        if (jobForTableView != null) {
            visibleCheckBox.setSelected(jobForTableView.isVisible());
            fileNameField.setText(jobForTableView.getHtmlFileKey());
            contentHtmlEditor.setHtmlText(jobForTableView.getHtmlContent());
        }
        else {
            visibleCheckBox.setSelected(false);
            fileNameField.setText("");
            //contentHtmlEditor.setHtmlText("");
        }
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        jobTable.setItems(mainApp.getJobsForTableView());
    }
    
}
