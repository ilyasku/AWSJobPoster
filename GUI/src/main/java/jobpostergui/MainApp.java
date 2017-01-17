package jobpostergui;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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
import jobposter.model.WrapperAmazonS3;
import jobpostergui.controller.JobEditorController;
import jobpostergui.model.JobForTableView;


public class MainApp extends Application {

    private static final String BUCKET_NAME = "job-poster-gui-test-bucket";
    private static final String JOB_JSON_FILE_KEY = "jobs.json";
    
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    private ObservableList<JobForTableView> jobsForTableView = FXCollections.observableArrayList();
    
    private Map<String, Job> jobs;
    
    private WrapperAmazonS3 wrapperAmazonS3;    

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
        
        
        
        wrapperAmazonS3 = new WrapperAmazonS3();
        wrapperAmazonS3.setBucketName(BUCKET_NAME);
        wrapperAmazonS3.setJsonFileKey(JOB_JSON_FILE_KEY);
        wrapperAmazonS3.setAmazonS3(createAmazonS3Object());
        
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
            //scene.getStylesheets().add("/styles/styles.css");
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

    private AmazonS3 createAmazonS3Object() {
        AWSCredentials credentials = null;
        try{
            credentials = new ProfileCredentialsProvider().getCredentials();
        }
        catch (Exception e){
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }                
        AmazonS3 s3 = new AmazonS3Client(credentials);
        Region region = Region.getRegion(Regions.EU_CENTRAL_1);
        s3.setRegion(region);
        return s3;
    }
}
