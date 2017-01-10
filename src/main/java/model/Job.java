package model;

public class Job {
    
    private boolean visibilityEdited = false;
    private boolean contentEdited = false;
    
    private boolean visible;
    private String htmlFileName;
    private String htmlFileKey;
    private JobType jobType;
    private String htmlContent;

    public String getHtmlFileName() {
        return htmlFileName;
    }

    public void setHtmlFileName(String htmlFileName) {
        this.htmlFileName = htmlFileName;
    }

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

    public void setVisibilityEdited(boolean visibilityEdited) {
        this.visibilityEdited = visibilityEdited;
    }

    public boolean ContentEdited() {
        return contentEdited;
    }

    public void setContentEdited(boolean contentEdited) {
        this.contentEdited = contentEdited;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
        
    
}
