package jobposter.model;

import jobposter.model.JobType;
import jobposter.model.Mapper;
import jobposter.model.Job;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapperTest {
    
    public MapperTest() {
    }

    @Test
    public void testExtractVisibleJobsFromJson() throws Exception {
        String jsonString = "[\n"
                + "  {\n"
                + "    \"title\": \"Pizzabäcker*in (m/w)\",\n"
                + "    \"path\": \"test-job-2.html\",\n"
                + "    \"vacancyType\": \"office\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"title\": \"Überdieschultergucker (m/w)\",\n"
                + "    \"path\": \"test-job-3.html\",\n"
                + "    \"vacancyType\": \"it\"\n"
                + "  }\n"
                + "]\n";
        
        List<String> visibleJobs = Mapper.extractVisibleJobsFromJson(jsonString);
        
        assertTrue(visibleJobs.size() == 2);
        assertTrue(visibleJobs.contains("test-job-2.html"));
        assertTrue(visibleJobs.contains("test-job-3.html"));        
    }
    
    @Test
    public void testUpdateJobByHtmlContent() {
        System.out.println("=================================================");
        System.out.println("test method: testUpdateJobByHtmlContent");
        String htmlContent = "<span>\n"
                + "<h2 data-job-type=\"office\">Pizzabäcker*in (m/w)</h2>"
                + "<p>Du suchst einen kreativen Job? Du willst handwerklich arbeiten und\n"
                + "gleichzeitig mit neuester Technologie. Dann komm zu uns!\n"
                + "Wir suchen einen aufgeschlossenen Senior Pizzaentwickler.\n"
                + "Als erstes müsstest du allerdings dein eigenes hausinternes\n"
                + " Pizzabestellsystem entwickeln.</p>\n"
                + "</span>";
        
        
        Job job = new Job();
        
        Mapper.updateJobByHtmlContent(job, htmlContent);
        
        System.out.println(job.getTitle());
        
        assertTrue(job.getTitle().equals("Pizzabäcker*in (m/w)"));
        assertTrue(job.getJobType() == JobType.OFFICE);
        assertTrue(job.getHtmlContent().equals(htmlContent));
        
    }    
    
}
