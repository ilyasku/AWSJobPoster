package model;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceReadFromS3 {
    
    private AmazonS3 s3;
    private String bucketName;
    private String jsonFileKey;
    
    public void setS3(AmazonS3 s3) {
        this.s3 = s3;
    }
    
    public Map<String, Job> getAllJobs(){
        Map<String, Job> jobs = new HashMap<>();
        ObjectListing objectListing = s3.listObjects( new ListObjectsRequest()
                .withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()){
            String key = objectSummary.getKey();
            if (isHtml(key)){
                Job job = new Job();
                job.setHtmlFileKey(key);                                                
                jobs.put(key, job);
            }
        }
        return jobs;
    }
    
    public List<String> getVisibleJobs() throws IOException{
        String visibilityJsonString = getJobStatusJsonFile();
        return Mapper.extractVisibleJobsFromJson(visibilityJsonString);
    }
    
    private String getJobStatusJsonFile() throws IOException{
        S3Object jsonFileAsS3Object = s3.getObject(new GetObjectRequest(getBucketName(), getJsonFileKey()));
        return s3ObjectContentToString(jsonFileAsS3Object.getObjectContent());
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getJsonFileKey() {
        return jsonFileKey;
    }

    public void setJsonFileKey(String jsonFileKey) {
        this.jsonFileKey = jsonFileKey;
    }

    private String s3ObjectContentToString(S3ObjectInputStream objectContent) throws IOException {
        String returnString = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(objectContent));
        while (true){
            String line = reader.readLine();
            if (line == null) break;
            returnString += line + "\n";
        }
        return returnString;
    }  

    private boolean isHtml(String key) {
        if (key.contains(".html")){
            return true;
        }
        return false;
    }
    
}
