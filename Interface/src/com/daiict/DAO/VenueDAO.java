package com.daiict.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.daiict.DBConnection; // Adjust package as needed
import com.daiict.Model.Venue;

public class VenueDAO {

    /**
     * Maps a ResultSet row to a Venue object.
     */
    private Venue mapResultSetToVenue(ResultSet rs) throws SQLException {
        Venue venue = new Venue();
        // Use column names exactly as they appear in the table/query result
        venue.setVenueName(rs.getString("VenueName")); 
        venue.setCapacity(rs.getInt("Capacity"));
        venue.setLocation(rs.getString("Location"));
        venue.setBookingStatus(rs.getString("BookingStatus"));
        return venue;
    }
    
    /**
     * Adds a new venue record to the database.
     */
    public boolean addVenue(Venue venue) {
        String sql = "INSERT INTO \"Venue\" (\"venueName\", \"Capacity\", \"Location\", \"BookingStatus\") VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, venue.getVenueName());
            pstmt.setInt(2, venue.getCapacity());
            pstmt.setString(3, venue.getLocation());
            pstmt.setString(4, venue.getBookingStatus());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error adding venue: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Venue> getAllVenues() {
        List<Venue> venueList = new ArrayList<>();
        String sql = "SELECT * FROM \"Venue\" ORDER BY \"venuename\"";
        
        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                venueList.add(mapResultSetToVenue(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all venues: " + e.getMessage());
            e.printStackTrace();
        }
        return venueList;
    }
    
    public Venue getVenueByName(String venueName) {
        Venue venue = null;
        String sql = "SELECT * FROM \"Venue\" WHERE \"VenueName\" = ?";
        
        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, venueName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    venue = mapResultSetToVenue(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving venue by name: " + e.getMessage());
            e.printStackTrace();
        }
        return venue;
    }
}