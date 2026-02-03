package com.daiict.Model;

import java.util.ArrayList;
import java.util.List;

public class Organizer {
    private String organizerName;
    private String userEmail;
    private List<String> phoneNumbers = new ArrayList<>();
    private List<EventAssignment> assignments = new ArrayList<>(); 

    public Organizer() {
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<EventAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<EventAssignment> assignments) {
        this.assignments = assignments;
    }

    public String toJson() {
        StringBuilder phonesArray = new StringBuilder();
        phonesArray.append("[");
        
        List<String> phoneNumbers = this.getPhoneNumbers();
        for (int i = 0; i < phoneNumbers.size(); i++) {
            phonesArray.append("\"").append(phoneNumbers.get(i)).append("\"");
            
            if (i < phoneNumbers.size() - 1) {
                phonesArray.append(", ");
            }
        }
        phonesArray.append("]");

        StringBuilder assignmentsArray = new StringBuilder();
        assignmentsArray.append("[");
        
        List<EventAssignment> assignments = this.getAssignments();
        for (int i = 0; i < assignments.size(); i++) {
            EventAssignment assignment = assignments.get(i);
            assignmentsArray.append(assignment.toJson()); 
            
            if (i < assignments.size() - 1) {
                assignmentsArray.append(", ");
            }
        }
        assignmentsArray.append("]");

        return String.format(
            "{ \"organizerName\": \"%s\", \"userEmail\": \"%s\", \"phoneNumbers\": %s, \"assignments\": %s }", 
            this.organizerName,
            this.userEmail,
            phonesArray.toString(),
            assignmentsArray.toString()
        );
    }

}