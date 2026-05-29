package dao;

import database.DatabaseConnection;
import models.UserCredentials;

import java.sql.*;

/**
 * Data Access Object for usercredentials table.
 * Handles insert and verification for login + password recovery.
 */
public class UserCredentialsDAO {

    // ─── CREATE ───────────────────────────────────────────────────────────────

    public boolean insertCredentials(UserCredentials uc) {
        String sql = "INSERT INTO usercredentials ("
                + "PagIbig_MID_No, Password, "
                + "Security_Q1, Security_A1, "
                + "Security_Q2, Security_A2, "
                + "Security_Q3, Security_A3"
                + ") VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uc.getPagIbigMIDNo());
            ps.setString(2, uc.getPassword());       // hash before passing in
            ps.setString(3, uc.getSecurityQ1());
            ps.setString(4, uc.getSecurityA1());
            ps.setString(5, uc.getSecurityQ2());
            ps.setString(6, uc.getSecurityA2());
            ps.setString(7, uc.getSecurityQ3());
            ps.setString(8, uc.getSecurityA3());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[UserCredentialsDAO] insertCredentials error: " + e.getMessage());
            return false;
        }
    }

    // ─── VERIFY LOGIN (MID + password) ────────────────────────────────────────

    /**
     * Returns true if the MID exists and the password matches.
     * Pass in the already-hashed password to compare.
     */
    public boolean verifyLogin(String pagIbigMIDNo, String hashedPassword) {
        String sql = "SELECT 1 FROM usercredentials "
                   + "WHERE PagIbig_MID_No = ? AND Password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("[UserCredentialsDAO] verifyLogin error: " + e.getMessage());
            return false;
        }
    }

    // ─── VERIFY SECURITY QUESTIONS (for forgot password) ─────────────────────

    /**
     * Returns true if all 3 questions AND answers match for the given MID.
     * Answers are compared case-insensitively.
     */
    public boolean verifySecurityQuestions(String pagIbigMIDNo,
                                           String q1, String a1,
                                           String q2, String a2,
                                           String q3, String a3) {
        String sql = "SELECT Security_A1, Security_A2, Security_A3, "
                   + "Security_Q1, Security_Q2, Security_Q3 "
                   + "FROM usercredentials WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return false;   // MID not found

            boolean q1Match = rs.getString("Security_Q1").equals(q1)
                           && rs.getString("Security_A1").equalsIgnoreCase(a1);
            boolean q2Match = rs.getString("Security_Q2").equals(q2)
                           && rs.getString("Security_A2").equalsIgnoreCase(a2);
            boolean q3Match = rs.getString("Security_Q3").equals(q3)
                           && rs.getString("Security_A3").equalsIgnoreCase(a3);

            return q1Match && q2Match && q3Match;

        } catch (SQLException e) {
            System.err.println("[UserCredentialsDAO] verifySecurityQuestions error: " + e.getMessage());
            return false;
        }
    }

    // ─── UPDATE PASSWORD ──────────────────────────────────────────────────────

    public boolean updatePassword(String pagIbigMIDNo, String newHashedPassword) {
        String sql = "UPDATE usercredentials SET Password = ? WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHashedPassword);
            ps.setString(2, pagIbigMIDNo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[UserCredentialsDAO] updatePassword error: " + e.getMessage());
            return false;
        }
    }
}