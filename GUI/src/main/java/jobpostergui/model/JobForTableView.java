package jobpostergui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import jobposter.model.Job;

public class JobForTableView {
    Job job;
    StringProperty htmlFileKey;
    BooleanProperty visible;
    StringProperty htmlContent;
    
    public JobForTableView(Job job){       
        htmlFileKey = new SimpleStringProperty(job.getHtmlFileKey());
        visible = new SimpleBooleanProperty(job.isVisible());
        htmlContent = new SimpleStringProperty(job.getHtmlContent());
    }
    
    
    
    public void setHtmlFileKey(String htmlFileKey) {
        this.htmlFileKey.set(htmlFileKey);
        job.setHtmlFileKey(htmlFileKey);
    }
    
    public String getHtmlFileKey(){
        return htmlFileKey.get();
    }
    
    public StringProperty htmlFileKeyProperty() {
        return htmlFileKey;
    }
    
    public void setVisible(Boolean status) {
        visible.set(status);
        job.setVisible(status);
    }
    
    public boolean isVisible() {
        return visible.get();
    }
    
    public BooleanProperty visibleProperty() {
        return visible;
    }
    
    //public ObservableBooleanValue _visibleProperty(){
    //return visible;
    //}
    
    public void setHtmlContent(String htmlContent){
        this.htmlContent.set(htmlContent);
        job.setHtmlContent(htmlContent);
    }
    
    public String getHtmlContent() {
        return htmlContent.get();
    }
    
    public StringProperty htmlContentProperty() {
        return htmlContent;
    }
    
    public Job getJob(){
        return job;
    }    
}
