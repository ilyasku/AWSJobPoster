package jobpostergui;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jobposter.model.Job;
import jobpostergui.controller.JobEditorController;
import jobpostergui.model.JobForTableView;


public class MainApp extends Application {


    private Stage primaryStage;
    private BorderPane rootLayout;
    
    private ObservableList<JobForTableView> jobsForTableView = FXCollections.observableArrayList();
    
    private Map<String, Job> jobs;

    public MainApp(){
        // some sample data
        jobs = new HashMap<>();
        
        Job job1 = new Job();
        job1.setHtmlFileKey("job-1-m-w.html");
        job1.setVisible(true);
        job1.setHtmlContent("<h2 data-job-type=\"it\"> Tolle*r Entwickler*in (m/w) </h2><br>"
                + "<p> Bla und Bla sag ich dir!</p>");
        
        Job job2 = new Job();
        job2.setHtmlFileKey("job-2-m-w.html");
        job2.setVisible(false);
        job2.setHtmlContent("<h2 data-job-type=\"it\"> Chef*in vons ganze (m/w) </h2><br>"
                + "<p> Bla und Bla und wer hätte es gedacht!?</p>");
        
        jobs.put("job-1-m-w.html", job1);
        jobs.put("job-2-m-w.html", job2);                        
        
        createJobsForTableView(jobs.values());
        
    }
    
    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Flavia Job Poster");

        initRootLayout();

        showJobEditor();        
    }
    
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the job editor inside the root layout.
     */
    public void showJobEditor() {
        try {
            // Load job editor.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/JobEditor.fxml"));
            AnchorPane jobEditor = (AnchorPane) loader.load();

            // Set job editor into the center of root layout.
            rootLayout.setCenter(jobEditor);
            
            JobEditorController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    public ObservableList<JobForTableView> getJobsForTableView(){
        return jobsForTableView;
    }

    private void createJobsForTableView(Collection<Job> listOfJobs) {
        
        for (Job job: listOfJobs){
            
            JobForTableView jobForTableView = new JobForTableView(job);
            jobsForTableView.add(jobForTableView);
        }        
    }        
}
