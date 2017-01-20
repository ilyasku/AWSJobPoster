package jobposter.model;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.when;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;
import org.apache.commons.io.IOUtils;

public class WrapperAmazonS3Test {
    
    public WrapperAmazonS3Test() {
    }

    @Ignore
    @Test
    public void testGetAllJobs() throws IOException {
        
        // ===================================================
        //            mock dependencies
        // ===================================================
        
        S3ObjectSummary mockObjectSummaryJob1 = mock(S3ObjectSummary.class);
        S3ObjectSummary mockObjectSummaryJob2 = mock(S3ObjectSummary.class);
        S3ObjectSummary mockObjectSummaryJson = mock(S3ObjectSummary.class);
        
        when(mockObjectSummaryJob1.getKey()).thenReturn("test-job-1.html");
        when(mockObjectSummaryJob2.getKey()).thenReturn("test-job-2.html");
        when(mockObjectSummaryJson.getKey()).thenReturn("jobs.json");
        
        List<S3ObjectSummary> mockSummaries = new ArrayList<>();
        
        mockSummaries.add(mockObjectSummaryJob1);
        mockSummaries.add(mockObjectSummaryJob2);
        mockSummaries.add(mockObjectSummaryJson);
        
        ObjectListing mockObjectListing = mock(ObjectListing.class);
        when(mockObjectListing.getObjectSummaries()).thenReturn(mockSummaries);
        

        InputStream stubJob1InputStream = IOUtils.toInputStream("", "UTF-8");
        InputStream stubJob2InputStream = IOUtils.toInputStream("", "UTF-8");
                
        S3Object mockTestJob1 = mock(S3Object.class);
        //when(mockTestJob1.getObjectContent()).thenReturn((S3ObjectInputStream) stubJob1InputStream);
        S3Object mockTestJob2 = mock(S3Object.class);
        //when(mockTestJob1.getObjectContent()).thenReturn((S3ObjectInputStream) stubJob2InputStream);
        
        
        AmazonS3 mockS3 = mock(AmazonS3.class);
        when(mockS3.listObjects(Matchers.anyString())).thenReturn(mockObjectListing);
        when(mockS3.getObject(Matchers.anyString(), Matchers.anyString())).thenReturn(mockTestJob1).thenReturn(mockTestJob2);
        
        // ===================================================
        
        WrapperAmazonS3 wrapperAmazonS3 = new WrapperAmazonS3();
        wrapperAmazonS3.setAmazonS3(mockS3);
        
        /*
        List<String> htmlFileNames = wrapperAmazonS3.getHtmlFileNames();
        Map<String, Job> jobs = new HashMap<>();
        for (String htmlFileName: htmlFileNames) {
            jobs.put(htmlFileName, wrapperAmazonS3.getJob(htmlFileName));
        }        
        
        assertTrue(jobs.size() == 2);
        assertTrue(jobs.containsKey("test-job-1.html"));
        assertTrue(jobs.containsKey("test-job-2.html"));
        assertTrue("test-job-1.html".equals(jobs.get("test-job-1.html").getHtmlFileKey()));
        assertTrue("test-job-2.html".equals(jobs.get("test-job-2.html").getHtmlFileKey()));                
        */
    }

    @Ignore
    @Test
    public void testGetVisibleJobs(){
        
        S3ObjectInputStream mockContentInputStream = mock(S3ObjectInputStream.class);
        // @TODO How to mock `new InputStreamReader(mockContentInputStream)` inside
        // WrapperAmazonS3.s3ObjectContentToString() ????
        
        S3Object mockJsonFileObject = mock(S3Object.class);
        when(mockJsonFileObject.getObjectContent()).thenReturn(mockContentInputStream);
        
        AmazonS3 mockS3 = mock(AmazonS3.class);
        when(mockS3.getObject(Matchers.anyString(), Matchers.anyString())).thenReturn(mockJsonFileObject);
    }
    
}
