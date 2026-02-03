package com.daiict.Model;

public class Venue {
    private String venueName;
    private int capacity;
    private String location;
    private String bookingStatus;

    public Venue() {}

    public Venue(String venueName, int capacity, String location, String bookingStatus) {
        this.venueName = venueName;
        this.capacity = capacity;
        this.location = location;
        this.bookingStatus = bookingStatus;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "venueName='" + venueName + '\'' +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }


    public String toJson() {
        return String.format(
            "{ \"venueName\": \"%s\", \"capacity\": %d, \"location\": \"%s\", \"bookingStatus\": \"%s\" }",
            this.venueName,
            this.capacity,
            this.location,
            this.bookingStatus
        );
    }
}