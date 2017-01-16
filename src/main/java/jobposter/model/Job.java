package jobposter.model;

public class Job {
    
    private Boolean visibilityEdited = false;
    private Boolean contentEdited = false;
    
    private Boolean visible;
    private String htmlFileKey;
    private JobType jobType;
    private String htmlContent;
    private String title;

    public String getHtmlFileKey() {
        return htmlFileKey;
    }

    public void setHtmlFileKey(String htmlFileKey) {
        this.htmlFileKey = htmlFileKey;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public boolean VisibilityEdited() {
        return visibilityEdited;
    }

    public void setVisibilityEdited(Boolean visibilityEdited) {
        this.visibilityEdited = visibilityEdited;
    }

    public Boolean ContentEdited() {
        return contentEdited;
    }

    public void setContentEdited(Boolean contentEdited) {
        this.contentEdited = contentEdited;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
        
    
}
