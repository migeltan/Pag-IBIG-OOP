package dao;

import database.DatabaseConnection;
import models.MemberTable;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for membertable.
 * Handles all CRUD operations for Member records.
 */
public class MemberDAO {

    // ─── CREATE ──────────────────────────────────────────────────────────────────

    public boolean insertMember(MemberTable member) {
        String sql = "INSERT INTO membertable ("
                + "PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Type_Others, "
                + "Membership_Category, Membership_Category_Others, Member_Name, Father_Name, "
                + "Mother_Name, Spouse_Name, Birthdate, Marital_Status, Birthplace, Citizenship, "
                + "Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS, Employee_Number, "
                + "Present_Home_Address, Permanent_Home_Address, Preferred_Mailing_Address, "
                + "Home_TelNum, Cellphone_Num, Bus_DirectLine, Bus_TrunkLine, Local, "
                + "Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,  member.getPagIbigMIDNo());
            ps.setString(2,  member.getOccupationalStatus());
            ps.setString(3,  member.getMembershipType());
            ps.setString(4,  member.getMembershipTypeOthers());
            ps.setString(5,  member.getMembershipCategory());
            ps.setString(6,  member.getMembershipCategoryOthers());
            ps.setString(7,  member.getMemberName());
            ps.setString(8,  member.getFatherName());
            ps.setString(9,  member.getMotherName());
            ps.setString(10, member.getSpouseName());
            ps.setDate(11,   member.getBirthdate());
            ps.setString(12, member.getMaritalStatus());
            ps.setString(13, member.getBirthplace());
            ps.setString(14, member.getCitizenship());
            ps.setString(15, member.getSex());
            ps.setString(16, member.getCrn());
            ps.setString(17, member.getFrequencyOfMembershipSavings());
            ps.setString(18, member.getTin());
            ps.setString(19, member.getSss());
            ps.setObject(20, member.getEmployeeNumber());    // nullable int
            ps.setString(21, member.getPresentHomeAddress());
            ps.setString(22, member.getPermanentHomeAddress());
            ps.setString(23, member.getPreferredMailingAddress());
            ps.setString(24, member.getHomeTelNum());
            ps.setString(25, member.getCellphoneNum());
            ps.setString(26, member.getBusDirectLine());
            ps.setString(27, member.getBusTrunkLine());
            ps.setString(28, member.getLocal());
            ps.setString(29, member.getEmailAddress());
            ps.setBigDecimal(30, member.getAllowBasic());
            ps.setBigDecimal(31, member.getAllowOtherSources());
            ps.setBigDecimal(32, member.getTotalMoIncome());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[MemberDAO] insertMember error: " + e.getMessage());
            return false;
        }
    }

    // ─── READ (single) ────────────────────────────────────────────────────────────

    public MemberTable getMemberById(String pagIbigMIDNo) {
        String sql = "SELECT * FROM membertable WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            System.err.println("[MemberDAO] getMemberById error: " + e.getMessage());
        }
        return null;
    }

    // ─── READ (all) ───────────────────────────────────────────────────────────────

    public List<MemberTable> getAllMembers() {
        List<MemberTable> members = new ArrayList<>();
        String sql = "SELECT * FROM membertable";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                members.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[MemberDAO] getAllMembers error: " + e.getMessage());
        }
        return members;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    public boolean updateMember(MemberTable member) {
        String sql = "UPDATE membertable SET "
                + "Occupational_Status = ?, Membership_Type = ?, Membership_Type_Others = ?, "
                + "Membership_Category = ?, Membership_Category_Others = ?, Member_Name = ?, "
                + "Father_Name = ?, Mother_Name = ?, Spouse_Name = ?, Birthdate = ?, "
                + "Marital_Status = ?, Birthplace = ?, Citizenship = ?, Sex = ?, CRN = ?, "
                + "Frequency_Of_Membership_Savings = ?, TIN = ?, SSS = ?, Employee_Number = ?, "
                + "Present_Home_Address = ?, Permanent_Home_Address = ?, "
                + "Preferred_Mailing_Address = ?, Home_TelNum = ?, Cellphone_Num = ?, "
                + "Bus_DirectLine = ?, Bus_TrunkLine = ?, Local = ?, Email_Address = ?, "
                + "Allow_Basic = ?, Allow_Other_Sources = ?, Total_Mo_Income = ? "
                + "WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,  member.getOccupationalStatus());
            ps.setString(2,  member.getMembershipType());
            ps.setString(3,  member.getMembershipTypeOthers());
            ps.setString(4,  member.getMembershipCategory());
            ps.setString(5,  member.getMembershipCategoryOthers());
            ps.setString(6,  member.getMemberName());
            ps.setString(7,  member.getFatherName());
            ps.setString(8,  member.getMotherName());
            ps.setString(9,  member.getSpouseName());
            ps.setDate(10,   member.getBirthdate());
            ps.setString(11, member.getMaritalStatus());
            ps.setString(12, member.getBirthplace());
            ps.setString(13, member.getCitizenship());
            ps.setString(14, member.getSex());
            ps.setString(15, member.getCrn());
            ps.setString(16, member.getFrequencyOfMembershipSavings());
            ps.setString(17, member.getTin());
            ps.setString(18, member.getSss());
            ps.setObject(19, member.getEmployeeNumber());
            ps.setString(20, member.getPresentHomeAddress());
            ps.setString(21, member.getPermanentHomeAddress());
            ps.setString(22, member.getPreferredMailingAddress());
            ps.setString(23, member.getHomeTelNum());
            ps.setString(24, member.getCellphoneNum());
            ps.setString(25, member.getBusDirectLine());
            ps.setString(26, member.getBusTrunkLine());
            ps.setString(27, member.getLocal());
            ps.setString(28, member.getEmailAddress());
            ps.setBigDecimal(29, member.getAllowBasic());
            ps.setBigDecimal(30, member.getAllowOtherSources());
            ps.setBigDecimal(31, member.getTotalMoIncome());
            ps.setString(32, member.getPagIbigMIDNo());   // WHERE clause

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[MemberDAO] updateMember error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    public boolean deleteMember(String pagIbigMIDNo) {
        String sql = "DELETE FROM membertable WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[MemberDAO] deleteMember error: " + e.getMessage());
            return false;
        }
    }

    // ─── Helper: map ResultSet row → MemberTable ──────────────────────────────────

    private MemberTable mapRow(ResultSet rs) throws SQLException {
        MemberTable m = new MemberTable();
        m.setPagIbigMIDNo(rs.getString("PagIbig_MID_No"));
        m.setOccupationalStatus(rs.getString("Occupational_Status"));
        m.setMembershipType(rs.getString("Membership_Type"));
        m.setMembershipTypeOthers(rs.getString("Membership_Type_Others"));
        m.setMembershipCategory(rs.getString("Membership_Category"));
        m.setMembershipCategoryOthers(rs.getString("Membership_Category_Others"));
        m.setMemberName(rs.getString("Member_Name"));
        m.setFatherName(rs.getString("Father_Name"));
        m.setMotherName(rs.getString("Mother_Name"));
        m.setSpouseName(rs.getString("Spouse_Name"));
        m.setBirthdate(rs.getDate("Birthdate"));
        m.setMaritalStatus(rs.getString("Marital_Status"));
        m.setBirthplace(rs.getString("Birthplace"));
        m.setCitizenship(rs.getString("Citizenship"));
        m.setSex(rs.getString("Sex"));
        m.setCrn(rs.getString("CRN"));
        m.setFrequencyOfMembershipSavings(rs.getString("Frequency_Of_Membership_Savings"));
        m.setTin(rs.getString("TIN"));
        m.setSss(rs.getString("SSS"));
        m.setEmployeeNumber((Integer) rs.getObject("Employee_Number"));
        m.setPresentHomeAddress(rs.getString("Present_Home_Address"));
        m.setPermanentHomeAddress(rs.getString("Permanent_Home_Address"));
        m.setPreferredMailingAddress(rs.getString("Preferred_Mailing_Address"));
        m.setHomeTelNum(rs.getString("Home_TelNum"));
        m.setCellphoneNum(rs.getString("Cellphone_Num"));
        m.setBusDirectLine(rs.getString("Bus_DirectLine"));
        m.setBusTrunkLine(rs.getString("Bus_TrunkLine"));
        m.setLocal(rs.getString("Local"));
        m.setEmailAddress(rs.getString("Email_Address"));
        m.setAllowBasic(rs.getBigDecimal("Allow_Basic"));
        m.setAllowOtherSources(rs.getBigDecimal("Allow_Other_Sources"));
        m.setTotalMoIncome(rs.getBigDecimal("Total_Mo_Income"));
        return m;
    }
    
 // ─── GENERATE NEXT MID ───────────────────────────────────────────────────────
    // Add this method inside MemberDAO.java alongside the other CRUD methods.

    /**
     * Reads the highest existing PagIbig_MID_No from membertable,
     * increments the last 4-digit segment by 1, and returns the new MID.
     *
     * Format: XXXX-XXXX-XXXX  (only the last 4 digits are incremented)
     * Example: 1212-3434-5659  →  1212-3434-5660
     *
     * Returns null if the query fails.
     */
    public String generateNextMID() {
        String sql = "SELECT PagIbig_MID_No FROM membertable "
                   + "ORDER BY PagIbig_MID_No DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastMID = rs.getString("PagIbig_MID_No"); // e.g. "1212-3434-5659"
                String[] parts = lastMID.split("-");              // ["1212","3434","5659"]

                int lastSegment = Integer.parseInt(parts[2]);     // 5659
                lastSegment++;                                    // 5660

                // Pad back to 4 digits (e.g. 0001 stays 0001)
                return parts[0] + "-" + parts[1] + "-" + String.format("%04d", lastSegment);
            }

        } catch (SQLException e) {
            System.err.println("[MemberDAO] generateNextMID error: " + e.getMessage());
        }
        return null;
    }
    
}

