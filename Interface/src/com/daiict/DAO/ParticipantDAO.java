package com.daiict.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.daiict.DBConnection;
import com.daiict.Model.Participant;

public class ParticipantDAO {

    public List<Participant> getParticipantsByEvent(String eventName) {
        List<Participant> participants = new ArrayList<>();
        
        String sql = "SELECT " +
                     "P.\"useremail\", P.\"participantname\", " +
                     "R.\"registrationdate\", R.\"confirmationstatus\" " +
                     "FROM \"ParticipantInfo\" P " +
                     "INNER JOIN \"ParticipantRegistration\" R ON P.\"useremail\" = R.\"useremail\" " +
                     "WHERE R.\"eventname\" = ?"; 

        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, eventName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Participant p = new Participant();
                    
                    p.setUserEmail(rs.getString("useremail"));
                    p.setParticipantName(rs.getString("participantname"));
                    p.setRegistrationDate(rs.getDate("registrationdate"));
                    p.setConfirmationStatus(rs.getString("confirmationstatus"));
                    
                    p.setEventName(eventName); 
                    participants.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching participants for event " + eventName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return participants;
    }
}