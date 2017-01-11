package model;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;

public class InterfaceReadFromS3Test {
    
    public InterfaceReadFromS3Test() {
    }

    @Test
    public void testGetAllJobs() {
        
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
        
        AmazonS3 mockS3 = mock(AmazonS3.class);
        when(mockS3.listObjects(Matchers.anyString())).thenReturn(mockObjectListing);
        
        // ===================================================
        
        InterfaceReadFromS3 interfaceRead = new InterfaceReadFromS3();
        interfaceRead.setS3(mockS3);
        
        Map<String, Job> jobs = interfaceRead.getAllJobs();
        
        assertTrue(jobs.size() == 2);
        assertTrue(jobs.containsKey("test-job-1.html"));
        assertTrue(jobs.containsKey("test-job-2.html"));
        assertTrue("test-job-1.html".equals(jobs.get("test-job-1.html").getHtmlFileKey()));
        assertTrue("test-job-2.html".equals(jobs.get("test-job-2.html").getHtmlFileKey()));                
    }

    @Ignore
    @Test
    public void testGetVisibleJobs(){
        
        S3ObjectInputStream mockContentInputStream = mock(S3ObjectInputStream.class);
        // @TODO How to mock `new InputStreamReader(mockContentInputStream)` inside
        // InterfaceReadFromS3.s3ObjectContentToString() ????
        
        S3Object mockJsonFileObject = mock(S3Object.class);
        when(mockJsonFileObject.getObjectContent()).thenReturn(mockContentInputStream);
        
        AmazonS3 mockS3 = mock(AmazonS3.class);
        when(mockS3.getObject(Matchers.anyString(), Matchers.anyString())).thenReturn(mockJsonFileObject);
    }

    @Test
    public void testUpdateJobByHtmlContent() throws Exception {
    }
    
}
