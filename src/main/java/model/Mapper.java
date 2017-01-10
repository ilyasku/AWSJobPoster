package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    
}
