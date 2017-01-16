package jobposter.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

class Mapper {
    
    public static List<String> extractVisibleJobsFromJson(String visibleJobsJsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();        
        JsonNode jobsOuterJsonNode = mapper.readTree(visibleJobsJsonString);
        
        List<String> visibleJobs = new ArrayList<>();
        for (int i = 0; i < jobsOuterJsonNode.size(); i++){            
            JsonNode jobNode = jobsOuterJsonNode.get(i);
            visibleJobs.add(jobNode.get("path").asText());
        }
        return visibleJobs;
    }
    
    public static void updateJobByHtmlContent(Job job, String htmlContent){
        Document htmlDocument = Jsoup.parse(htmlContent);
        // select the first heading
        Element head = htmlDocument.select("h0, h1, h2, h3, h4, h5, h6").first();
        String jobTitle = head.text();        
        JobType jobType;
        if (head.attr("data-job-type").equals("office")){
            jobType = JobType.OFFICE;
        }
        else{
            jobType = JobType.IT;
        }
        
        job.setHtmlContent(htmlContent);
        job.setJobType(jobType);
        job.setTitle(jobTitle);
    }
    
}
