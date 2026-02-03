package com.daiict.Model;

import java.sql.Date;

public class CulturalCommitteeMember {
    private String studentId;
    private String userEmail;
    private String userName;
    private String contribution;
    private Date joinDate;
    private String position;
    private String status;
    

    public CulturalCommitteeMember() {}

    public CulturalCommitteeMember(String studentId, String userEmail, String contribution, Date joinDate, String position, String status) {
        this.studentId = studentId;
        this.userEmail = userEmail;
        this.contribution = contribution;
        this.joinDate = joinDate;
        this.position = position;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Member{" +
               "studentId='" + studentId + '\'' +
               ", userEmail='" + userEmail + '\'' +
               ", contribution='" + contribution + '\'' +
               ", joinDate=" + joinDate +
               ", position='" + position + '\'' +
               ", status='" + status + '\'' +
               '}';
    }

    public String toJson() {
        return String.format(
            "{\"studentId\": \"%s\", \"userEmail\": \"%s\", \"userName\": \"%s\", \"contribution\": \"%s\", \"joinDate\": \"%s\", \"position\": \"%s\", \"status\": \"%s\"}",
            this.studentId,
            this.userEmail,
            // Add the userName field here:
            this.userName != null ? this.userName.replace("\"", "\\\"").replace("\n", "\\n") : "null", 
            // Escape contribution field for valid JSON
            this.contribution != null ? this.contribution.replace("\"", "\\\"").replace("\n", "\\n") : "null", 
            this.joinDate != null ? this.joinDate.toString() : "null", 
            this.position,
            this.status
        );
    }
}