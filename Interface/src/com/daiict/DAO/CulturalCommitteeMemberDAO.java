package com.daiict.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.daiict.DBConnection;
import com.daiict.Model.CulturalCommitteeMember;

public class CulturalCommitteeMemberDAO {

    private CulturalCommitteeMember mapResultSetToMember(ResultSet rs) throws SQLException {
        CulturalCommitteeMember member = new CulturalCommitteeMember();
        member.setStudentId(rs.getString("StudentID"));
        member.setUserEmail(rs.getString("UserEmail"));
        member.setContribution(rs.getString("Contribution"));
        member.setJoinDate(rs.getDate("JoinDate"));
        member.setPosition(rs.getString("Position"));
        member.setStatus(rs.getString("Status"));
        return member;
    }

    public void addMember(CulturalCommitteeMember member) {
        String sql = "INSERT INTO \"CulturalCommitteeMember\" (StudentID, UserEmail, Contribution, JoinDate, Position, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, member.getStudentId());
            pstmt.setString(2, member.getUserEmail());
            pstmt.setString(3, member.getContribution());
            pstmt.setDate(4, member.getJoinDate()); 
            pstmt.setString(5, member.getPosition());
            pstmt.setString(6, member.getStatus());

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " cultural committee member added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CulturalCommitteeMember getMemberByStudentId(String studentId) {
        String sql = "SELECT * FROM \"CulturalCommitteeMember\" WHERE StudentID = ?";
        CulturalCommitteeMember member = null;
        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    member = mapResultSetToMember(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    public List<CulturalCommitteeMember> getAllMembers() {
        List<CulturalCommitteeMember> members = new ArrayList<>();
        String sql = "SELECT * FROM \"CulturalCommitteeMember\"";
        try (Connection con = DBConnection.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    // private static final String SELECT_ACTIVE_MEMBERS = 
    //     "SELECT C.*, U.\"UserName\" " + 
    //     "FROM \"CulturalCommitteeMember\" C " + 
    //     "INNER JOIN \"User\" U ON C.\"UserEmail\" = U.\"UserEmail\" " + 
    //     "WHERE C.\"Status\" = 'Active'";

    public List<CulturalCommitteeMember> getActiveMembers() {
        List<CulturalCommitteeMember> members = new ArrayList<>();
        
        // Use the optimized query:
        // String sql = "SELECT C.*, U.\"username\" " + 
        //             "FROM \"CulturalCommitteeMember\" C " + 
        //             "INNER JOIN \"User\" U ON C.\"useremail\" = U.\"useremail\" " + 
        //             "WHERE C.\"status\" = 'Active'";

        String sql = "SELECT C.*, U.\"username\" " + 
                 "FROM \"CulturalCommitteeMember\" C " + 
                 "INNER JOIN \"User\" U ON C.\"useremail\" = U.\"useremail\" " + 
                 "WHERE C.\"status\" = 'Active' " +
                 // --- START OF NEW ORDERING CLAUSE ---
                 "ORDER BY " +
                 // Assign a numerical value to each position for sorting
                 "CASE C.\"position\" " +
                 "  WHEN 'Convener' THEN 1 " +
                 "  WHEN 'Co-Convener' THEN 2 " +
                 "  ELSE 3 " + // All other roles (Members) come last
                 "END, " +
                 // Secondary sort: Alphabetical by UserName for those with the same position
                 "U.\"username\" ASC";
                    
        try (Connection con = DBConnection.connectDB();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Retrieve data using the modified mapping helper
                CulturalCommitteeMember member = mapResultSetToMember(rs);
                
                // Explicitly set the UserName fetched from the joined table
                member.setUserName(rs.getString("userName")); 
                
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    /**
     * Updates an existing committee member's details based on Student ID.
     */
    public void updateMember(CulturalCommitteeMember member) {
        String sql = "UPDATE \"CulturalCommitteeMember\" SET UserEmail = ?, Contribution = ?, JoinDate = ?, Position = ?, Status = ? WHERE StudentID = ?";
        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, member.getUserEmail());
            pstmt.setString(2, member.getContribution());
            pstmt.setDate(3, member.getJoinDate());
            pstmt.setString(4, member.getPosition());
            pstmt.setString(5, member.getStatus());
            pstmt.setString(6, member.getStudentId()); // WHERE clause

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " cultural committee member updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the status of a committee member by Student ID.
     */
    public void updateMemberStatus(String studentId, String newStatus) {
        String sql = "UPDATE \"CulturalCommitteeMember\" SET Status = ? WHERE StudentID = ?";
        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setString(2, studentId);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " member status updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a committee member by Student ID.
     */
    public void deleteMember(String studentId) {
        String sql = "DELETE FROM \"CulturalCommitteeMember\" WHERE StudentID = ?";
        try (Connection con = DBConnection.connectDB();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, studentId);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " cultural committee member deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}