package com.daiict.Model;

import java.sql.Date;

public class EventAssignment {
    private String eventName;
    private Date assignmentDate;

    public EventAssignment() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }
    
    @Override
    public String toString() {
        return "EventAssignment{" +
                "eventName='" + eventName + '\'' +
                ", assignmentDate=" + assignmentDate +
                '}';
    }

    public String toJson() {
        String assignmentDateStr = this.assignmentDate != null ? this.assignmentDate.toString() : "";
        
        return String.format(
            "{ \"eventName\": \"%s\", \"assignmentDate\": \"%s\" }",
            this.eventName,
            assignmentDateStr
        );
    }
}