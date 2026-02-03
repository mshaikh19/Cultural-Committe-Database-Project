package com.daiict.Model;

import java.sql.Date;

public class Participant {
    private String userEmail;
    private String participantName;
    private String eventName;
    private Date registrationDate;
    private String confirmationStatus;
    
    
    public Participant() {}

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
    public String getConfirmationStatus() { return confirmationStatus; }
    public void setConfirmationStatus(String confirmationStatus) { this.confirmationStatus = confirmationStatus; }

    public String toJson() {
        String regDateStr = this.registrationDate != null ? this.registrationDate.toString() : "null";
        return String.format(
            "{ \"userEmail\": \"%s\", \"participantName\": \"%s\", \"eventName\": \"%s\", \"registrationDate\": \"%s\", \"confirmationStatus\": \"%s\" }",
            this.userEmail,
            this.participantName,
            this.eventName,
            regDateStr,
            this.confirmationStatus
        );
    }
}