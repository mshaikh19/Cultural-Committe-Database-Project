package com.daiict.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.daiict.DBConnection;
import com.daiict.Model.EventAssignment;
import com.daiict.Model.Organizer;

public class OrganizerDAO {

    public List<Organizer> getAllOrganizers() {
        Map<String, Organizer> organizerMap = new HashMap<>(); 
        
        Map<String, Set<String>> phoneNumbersMap = new HashMap<>();

        String sql = "SELECT " +
                     "OD.organizername, " +
                     "OD.useremail, " +
                     "OA.eventname, " +
                     "OA.assignmentdate, " +
                     "UPN.phonenumber " + 
                     "FROM \"OrganizerDetails\" OD " +
                     "LEFT JOIN \"OrganizerAssignment\" OA ON OD.OrganizerName = OA.OrganizerName " +
                     "INNER JOIN \"Event\" E ON OA.eventname = E.eventname " +
                     "LEFT JOIN \"UserPhoneNumber\" UPN ON OD.useremail = UPN.useremail " + 
                     "WHERE E.\"status\" NOT IN ('Completed', 'Cancelled') " + 
                     "AND E.\"startdate\" > NOW() " + 
                     "ORDER BY OD.organizername, OA.assignmentdate DESC";

        try (Connection con = DBConnection.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String organizerName = rs.getString("OrganizerName");
                String userEmail = rs.getString("UserEmail");
                String phoneNumber = rs.getString("PhoneNumber"); 
                String eventName = rs.getString("EventName");
                Date assignmentDate = rs.getDate("AssignmentDate");

                Organizer organizer = organizerMap.get(organizerName);
                if (organizer == null) {
                    organizer = new Organizer();
                    organizer.setOrganizerName(organizerName);
                    organizer.setUserEmail(userEmail);
                    organizerMap.put(organizerName, organizer);
                    phoneNumbersMap.put(organizerName, new HashSet<>());
                }
                
                if (eventName != null) {
                    EventAssignment assignment = new EventAssignment();
                    assignment.setEventName(eventName);
                    assignment.setAssignmentDate(assignmentDate);
                    
                    boolean assignmentExists = organizer.getAssignments().stream()
                         .anyMatch(a -> a.getEventName().equals(eventName));
                    
                    if (!assignmentExists) {
                        organizer.getAssignments().add(assignment);
                    }
                }
                
                if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                    phoneNumbersMap.get(organizerName).add(phoneNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing getAllOrganizer query: " + e.getMessage());
            e.printStackTrace();
        }
        
        for (Organizer organizer : organizerMap.values()) {
            Set<String> uniquePhones = phoneNumbersMap.get(organizer.getOrganizerName());
            if (uniquePhones != null) {
                organizer.setPhoneNumbers(new ArrayList<>(uniquePhones));
            }
        }
        
        return new ArrayList<>(organizerMap.values());
    }
}