package jobposter.model;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InterfaceWriteToS3 {
    private AmazonS3 s3;
    private String bucketName;
    private String jsonFileKey;

    public void writeJobsToS3(Map<String, Job> jobs) throws IOException{
        List<String> visibleJobs = new ArrayList<>();
        boolean needToUpdateJson = false;
        for (Job job : jobs.values()){
            if (job.isVisible()){
                visibleJobs.add(job.getHtmlFileKey());
            }
            if (job.ContentEdited()){
                needToUpdateJson = true; // in case the jobType or title was edited ...
                File temporaryHtmlFile = createTemporaryFile(job.getHtmlContent(), job.getHtmlFileKey());
                writeFileToS3(job.getHtmlFileKey(), temporaryHtmlFile);
            }
            else if (job.VisibilityEdited()){
                needToUpdateJson = true;                
            }
        }
        if (needToUpdateJson) {
            ArrayNode jsonOfVisibleJobs = buildJsonOfVisibleJobs(visibleJobs, jobs);
            File temporaryJsonFile = createTemporaryFile(jsonOfVisibleJobs.toString(), jsonFileKey);
            writeFileToS3(jsonFileKey, temporaryJsonFile);
        }
    }   

    private File createTemporaryFile(String fileContent, String fileName) throws IOException {
        String[] fileNameAndExtension = fileName.split("\\.");
        File file = File.createTempFile(fileNameAndExtension[0], fileNameAndExtension[1]);
        file.deleteOnExit();
        
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(fileContent);
        writer.close();
        
        return file;
    }

    private void writeFileToS3(String fileKey, File file) {
        s3.putObject(bucketName, fileKey, file);
    }

    private ArrayNode buildJsonOfVisibleJobs(List<String> visibleJobs, Map<String, Job> jobs) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode rootNode = factory.arrayNode();
        
        for (String jobFileName: visibleJobs){
            Job currentJobObject = jobs.get(jobFileName);
            
            ObjectNode singleJobNode = factory.objectNode();            
            singleJobNode.put("title", currentJobObject.getTitle());
            singleJobNode.put("path", currentJobObject.getHtmlFileKey());
            singleJobNode.put("vacancyType", currentJobObject.getJobType().getStringIdentifier());
            
            rootNode.add(singleJobNode);
        }
        
        return rootNode;
    }
    
    public void setS3(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setJsonFileKey(String jsonFileKey) {
        this.jsonFileKey = jsonFileKey;
    }
}
