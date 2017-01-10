package model;

public enum JobType {
    IT("it"), OFFICE("office");
    
    String stringIdentifier;
    
    JobType(String stringIdentifier){
        this.stringIdentifier = stringIdentifier;
    }
}