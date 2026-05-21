package dao;

import database.DatabaseConnection;
import models.PrevEmpTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for prevemptable.
 * One member can have multiple previous employment records.
 */
public class PrevEmpDAO {

    // ─── CREATE ──────────────────────────────────────────────────────────────────

    public boolean insertPrevEmp(PrevEmpTable prevEmp) {
        // Prev_Emp_Code is AUTO_INCREMENT — do not include it in INSERT
        String sql = "INSERT INTO prevemptable "
                + "(PagIbig_MID_No, Company_Code, To_Date, From_Date) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, prevEmp.getPagIbigMIDNo());
            ps.setString(2, prevEmp.getCompanyCode());
            ps.setDate(3,   prevEmp.getToDate());
            ps.setDate(4,   prevEmp.getFromDate());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PrevEmpDAO] insertPrevEmp error: " + e.getMessage());
            return false;
        }
    }

    // ─── READ (all prev jobs of one member) ──────────────────────────────────────

    public List<PrevEmpTable> getPrevEmpByMID(String pagIbigMIDNo) {
        List<PrevEmpTable> list = new ArrayList<>();
        String sql = "SELECT * FROM prevemptable WHERE PagIbig_MID_No = ? ORDER BY From_Date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[PrevEmpDAO] getPrevEmpByMID error: " + e.getMessage());
        }
        return list;
    }

    // ─── READ (single record by Prev_Emp_Code) ───────────────────────────────────

    public PrevEmpTable getPrevEmpByCode(int prevEmpCode) {
        String sql = "SELECT * FROM prevemptable WHERE Prev_Emp_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prevEmpCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            System.err.println("[PrevEmpDAO] getPrevEmpByCode error: " + e.getMessage());
        }
        return null;
    }

    // ─── READ (all) ───────────────────────────────────────────────────────────────

    public List<PrevEmpTable> getAllPrevEmp() {
        List<PrevEmpTable> list = new ArrayList<>();
        String sql = "SELECT * FROM prevemptable";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[PrevEmpDAO] getAllPrevEmp error: " + e.getMessage());
        }
        return list;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    public boolean updatePrevEmp(PrevEmpTable prevEmp) {
        String sql = "UPDATE prevemptable SET "
                + "Company_Code = ?, To_Date = ?, From_Date = ? "
                + "WHERE Prev_Emp_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, prevEmp.getCompanyCode());
            ps.setDate(2,   prevEmp.getToDate());
            ps.setDate(3,   prevEmp.getFromDate());
            ps.setInt(4,    prevEmp.getPrevEmpCode());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PrevEmpDAO] updatePrevEmp error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE (single record) ──────────────────────────────────────────────────

    public boolean deletePrevEmp(int prevEmpCode) {
        String sql = "DELETE FROM prevemptable WHERE Prev_Emp_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prevEmpCode);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PrevEmpDAO] deletePrevEmp error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE (all prev emp of a member) ───────────────────────────────────────

    public boolean deleteAllPrevEmpByMID(String pagIbigMIDNo) {
        String sql = "DELETE FROM prevemptable WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PrevEmpDAO] deleteAllPrevEmpByMID error: " + e.getMessage());
            return false;
        }
    }

    // ─── Helper ───────────────────────────────────────────────────────────────────

    private PrevEmpTable mapRow(ResultSet rs) throws SQLException {
        return new PrevEmpTable(
                rs.getString("PagIbig_MID_No"),
                rs.getInt("Prev_Emp_Code"),
                rs.getString("Company_Code"),
                rs.getDate("To_Date"),
                rs.getDate("From_Date")
        );
    }
}