package jobpostergui.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
    private Button saveJobsButton;
    @FXML
    private Button loadJobsButton;
    @FXML
    private Button addJobButton;
    
    
    @FXML
    private CheckBox visibleCheckBox;
    @FXML
    private TextField fileNameField;
    @FXML
    private HTMLEditor contentHtmlEditor;
            
    private MainApp mainApp;
        
    private JobForTableView currentlySelectedJob;
    
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
        
        setFontOfHtmlEditor();
        
        fileNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldPropertyValue, Boolean newPropertyValue) {                
                if (!newPropertyValue) {
                    handleFileNameEdited();                    
                }                
            }
        });
        
        
    }
    
    private void showJobDetails(JobForTableView jobForTableView) {
        if (currentlySelectedJob != null) {
            updateHtmlContentOfCurrentlySelectedJob(); 
        }
        currentlySelectedJob = jobForTableView;
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

    
    @FXML
    private void handleFileNameEdited() {
        if (currentlySelectedJob != null){
            currentlySelectedJob.setHtmlFileKey(fileNameField.getText());
        }
    }

    
    @FXML
    private void handleVisibleCheckBoxClicked() {
        currentlySelectedJob.setVisible(visibleCheckBox.isSelected());
    }
    
        
    @FXML
    private void handleSaveJobsButtonClicked() {
        System.out.println("save button clicked!");
    }
    
    @FXML
    private void handleLoadJobsButtonClicked() {
        System.out.println("load button clicked!");
    }
    
    @FXML
    private void handleAddJobButtonClicked() {
        System.out.println("add job button clicked!");
    }
    
    private void updateHtmlContentOfCurrentlySelectedJob() {
        currentlySelectedJob.setHtmlContent(contentHtmlEditor.getHtmlText());
    }
    
    /**
     * Trying to set the stupid font of the Editor!
     * Not working so far.
     */
    private void setFontOfHtmlEditor() {
        String feedback = "";
        int i = 0;
        List<String> cssIdentifier = Arrays.asList(".radio-button", ".top-toolbar", 
                ".combo-box", "combo-box", ".combo-box-base", ".combo-box-popup",
                ".bottom-toolbar", "ChoiceBox", ".choice-box");
        for (String identifier: cssIdentifier){
            Set<Node> lookupAll = contentHtmlEditor.lookupAll(identifier);
            feedback += identifier + ": " +Integer.toString(lookupAll.size()) + "<br>";
            if (".bottom-toolbar".equals(identifier) || ".top-toolbar".equals(identifier)){
                feedback += "==================================<br>";
                for (Node node: lookupAll){
                    if (node instanceof Parent){
                        ObservableList<Node> childrenUnmodifiable = ((Parent) node).getChildrenUnmodifiable();
                        feedback += "Children: " + childrenUnmodifiable.size() + "<br>";
                    }
                }
                feedback += "==================================<br>";
            }

        }
        
        
        /*
        for (Node candidate: (contentHtmlEditor.lookupAll("MenuButton"))){
            System.err.println(i);
            if(candidate instanceof MenuButton && i == 1) {
                MenuButton fontSelectionButton = (MenuButton) candidate;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {                        
                        List<MenuItem> fontSelections = fontSelectionButton.getItems();
                        System.err.println(fontSelections.size());
                        if (!fontSelections.isEmpty()) {
                            MenuItem item = fontSelections.get(0);
                            if (item instanceof RadioMenuItem) {
                                System.out.println(item.getText());
                                ((RadioMenuItem) item).setSelected(true);
                            }
                        }
                    }
                    
                });                
            }            
            i++;
            contentHtmlEditor.setHtmlText("tried to set " + i + "!");
        }
        */        
        contentHtmlEditor.setHtmlText(feedback);
    }
    
}
