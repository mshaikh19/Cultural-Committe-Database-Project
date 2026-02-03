package com.daiict.Model;

import java.sql.Date;

public class Event {
    private String eventName;
    private Date startDate;
    private Date endDate;
    private String eventDesc;
    private String status;
    private String venueName;

    public Event() {}

    public Event(String eventName, Date startDate, Date endDate, String eventDesc, String status, String venueName) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventDesc = eventDesc;
        this.status = status;
        this.venueName = venueName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String toJson() {
        return String.format(
            "{ \"eventName\": \"%s\", \"startDate\": \"%s\", \"endDate\": \"%s\", \"eventDesc\": \"%s\", \"status\": \"%s\", \"venueName\": \"%s\" }",
            escapeJson(eventName), startDate, endDate, escapeJson(eventDesc), status, escapeJson(venueName)
        );
    }
    
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
