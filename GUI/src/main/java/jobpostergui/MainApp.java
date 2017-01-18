package jobpostergui;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jobposter.model.Job;
import jobposter.model.WrapperAmazonS3;
import jobpostergui.controller.JobEditorController;
import jobpostergui.controller.NewJobController;
import jobpostergui.model.JobForTableView;


public class MainApp extends Application {

    private static final String BUCKET_NAME = "job-poster-gui-test-bucket";
    private static final String JOB_JSON_FILE_KEY = "jobs.json";
        
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    private ObservableList<JobForTableView> jobsForTableView;
    
    private Map<String, Job> jobs;    
    private Boolean jobsWereLoaded = false;        
    
    private WrapperAmazonS3 wrapperAmazonS3;   
    
    private List<String> htmlFileNames = new ArrayList<>();
    private List<String> jobsToBeDeleted = new ArrayList<>();

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
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            
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
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/JobEditor.fxml"));
            AnchorPane jobEditor = (AnchorPane) loader.load();

            
            rootLayout.setCenter(jobEditor);
            
            JobEditorController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadJobsFromAmazonS3() throws IOException {
        htmlFileNames = wrapperAmazonS3.getHtmlFileNames();
        jobsToBeDeleted = new ArrayList<>();
        jobs = new HashMap<>();
        for (String htmlFileName: htmlFileNames) {
            Job job = wrapperAmazonS3.getJob(htmlFileName);
            job.setVisible(false);
            jobs.put(htmlFileName, job);
        }        
        
        List<String> visibleJobs = wrapperAmazonS3.getVisibleJobs();
        for (String fileNameOfVisibleJob: visibleJobs) {
            Job jobObject = jobs.get(fileNameOfVisibleJob);
            jobObject.setVisible(true);
        }
        
        createJobsForTableView(jobs.values());
        jobsWereLoaded = true;        
    }
    
    public void updateFileNameInJobsMap(String oldFileName, String newFileName) {
        Job jobObject = jobs.get(oldFileName);
        jobs.remove(oldFileName);
        jobs.put(newFileName, jobObject);
    }
    
    public void writeJobsToAmazonS3() {
        System.out.println("writeJobs called!");
        if (jobsWereLoaded) {
            // @TODO write jobs ...
            
            
            deleteJobsFromAmazonS3();
        }
        
    }
    
    public void addJobToDeleteList(String fileName) {
        if (htmlFileNames.contains(fileName)) {
            jobsToBeDeleted.add(fileName);
        }
    }
    
    
    public void addJobForTableView(Job job) {
        JobForTableView jobForTableView = new JobForTableView(job);
        jobsForTableView.add(jobForTableView);
    }
    
    private void deleteJobsFromAmazonS3() {
        System.out.println("deleteJobs called!");
        for (String fileName: jobsToBeDeleted) {
            System.out.println("deleting " + fileName + "?");
        }
        jobsToBeDeleted = new ArrayList<>();
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public Set<String> getHtmlFileNames() {
        return jobs.keySet();
    }
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    public ObservableList<JobForTableView> getJobsForTableView(){
        return jobsForTableView;
    }
    
    public boolean showNewJobDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/NewJob.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add new job");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            NewJobController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            
            dialogStage.showAndWait();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        
    }
    
    public void addJob(Job job){
        jobs.put(job.getHtmlFileKey(), job);
    }

    private void createJobsForTableView(Collection<Job> listOfJobs) {
        jobsForTableView = FXCollections.observableArrayList();
        for (Job job: listOfJobs){            
            addJobForTableView(job);
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
