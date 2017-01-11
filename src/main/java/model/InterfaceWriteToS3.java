package model;

import com.amazonaws.services.s3.AmazonS3;
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
            if (job.VisibilityEdited()){
                needToUpdateJson = true;                
            }
            if (job.isVisible()){
                visibleJobs.add(job.getHtmlFileKey());
            }
            if (job.ContentEdited()){
                File tmpHtmlFile = createTmpFile(job.getHtmlContent(), job.getHtmlFileKey());
                writeFileToS3(job.getHtmlFileKey(), tmpHtmlFile);
            }
        }
        // build json of visible jobs ...
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

    private File createTmpFile(String fileContent, String fileName) throws IOException {
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
}
