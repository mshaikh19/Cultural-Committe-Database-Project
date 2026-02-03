package com.daiict.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.daiict.DBConnection;
import com.daiict.Model.Event;

public class EventDAO {

    private static final String SELECT_ALL_EVENTS = "SELECT * FROM \"Event\"";
    private static final String SELECT_ALL_UPCOMING_EVENTS = "SELECT * FROM \"Event\" WHERE \"startdate\" > NOW() AND status != 'Completed'"; 
    
    public List<Event> getAllEvents() throws SQLException {
        List<Event> eventList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EVENTS);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Event event = new Event();
                event.setEventName(rs.getString("eventname"));
                
                Date startDate = rs.getDate("startdate");
                Date endDate = rs.getDate("enddate");

                event.setStartDate(startDate); 
                event.setEndDate(endDate);
                event.setEventDesc(rs.getString("eventdesc"));
                event.setStatus(rs.getString("status"));
                event.setVenueName(rs.getString("venuename"));
                eventList.add(event);
            }

        } 
        return eventList;
    }
    public List<Event> getAllUpcomingEvents() throws SQLException {
        List<Event> eventList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_UPCOMING_EVENTS);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                
                Event event = new Event();
                event.setEventName(rs.getString("eventname"));
                
                Date startDate = rs.getDate("startdate");
                Date endDate = rs.getDate("enddate");

                event.setStartDate(startDate); 
                event.setEndDate(endDate);
                event.setEventDesc(rs.getString("eventdesc"));
                event.setStatus(rs.getString("status"));
                event.setVenueName(rs.getString("venuename"));
                eventList.add(event);
            }

        } 
        return eventList;
    }

    public boolean updateEvent(Event event, String originalEventName) {
        String sql = "UPDATE \"Event\" SET " +
                    "\"eventname\" = ?,\"startdate\" = ?, \"enddate\" = ?, \"eventdesc\" = ?, \"venuename\" = ?, \"status\" = ? " +
                    "WHERE \"eventname\" = ?";
        
        try (Connection con = DBConnection.connectDB();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
                
            pstmt.setString(1, event.getEventName());
            pstmt.setDate(2, event.getStartDate());
            pstmt.setDate(3, event.getEndDate());
            pstmt.setString(4, event.getEventDesc());
            pstmt.setString(5, event.getVenueName());
            pstmt.setString(6, event.getStatus());
            pstmt.setString(7, originalEventName);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating event: " + event.getEventName() + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEvent(String eventName) {
        String sql = "DELETE FROM \"Event\" WHERE \"eventname\" = ?"; 
        
        try (Connection con = DBConnection.connectDB();
            PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, eventName); 

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting event '" + eventName + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean addEvent(Event event) {
        String sql = "INSERT INTO \"Event\" (\"eventname\", \"startdate\", \"enddate\", \"eventdesc\", \"status\" , \"venuename\") " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.connectDB();
            PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, event.getEventName());
            pstmt.setDate(2, event.getStartDate());
            pstmt.setDate(3, event.getEndDate());
            pstmt.setString(4, event.getEventDesc());
            pstmt.setString(5, event.getStatus());
            pstmt.setString(6, event.getVenueName());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error adding new event: " + event.getEventName() + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}