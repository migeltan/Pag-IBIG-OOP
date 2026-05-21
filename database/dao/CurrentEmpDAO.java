package dao;

import database.DatabaseConnection;
import models.CurrentEmpRecordTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for currentemprecordtable.
 * PagIbig_MID_No is both PK and FK to membertable (one member = one current job).
 */
public class CurrentEmpDAO {

    // ─── CREATE ──────────────────────────────────────────────────────────────────

    public boolean insertCurrentEmp(CurrentEmpRecordTable emp) {
        String sql = "INSERT INTO currentemprecordtable "
                + "(PagIbig_MID_No, Company_Code, Occupation, Employment_Status, "
                + "TypeOfWork, Country_Of_Assignment, Date_Employed) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getPagIbigMIDNo());
            ps.setString(2, emp.getCompanyCode());
            ps.setString(3, emp.getOccupation());
            ps.setString(4, emp.getEmploymentStatus());
            ps.setString(5, emp.getTypeOfWork());           // nullable
            ps.setString(6, emp.getCountryOfAssignment());
            ps.setDate(7,   emp.getDateEmployed());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] insertCurrentEmp error: " + e.getMessage());
            return false;
        }
    }

    // ─── READ (single) ────────────────────────────────────────────────────────────

    public CurrentEmpRecordTable getCurrentEmpByMID(String pagIbigMIDNo) {
        String sql = "SELECT * FROM currentemprecordtable WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] getCurrentEmpByMID error: " + e.getMessage());
        }
        return null;
    }

    // ─── READ (all) ───────────────────────────────────────────────────────────────

    public List<CurrentEmpRecordTable> getAllCurrentEmployees() {
        List<CurrentEmpRecordTable> list = new ArrayList<>();
        String sql = "SELECT * FROM currentemprecordtable";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] getAllCurrentEmployees error: " + e.getMessage());
        }
        return list;
    }

    // ─── READ (by company) ────────────────────────────────────────────────────────

    public List<CurrentEmpRecordTable> getEmployeesByCompany(String companyCode) {
        List<CurrentEmpRecordTable> list = new ArrayList<>();
        String sql = "SELECT * FROM currentemprecordtable WHERE Company_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, companyCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] getEmployeesByCompany error: " + e.getMessage());
        }
        return list;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    public boolean updateCurrentEmp(CurrentEmpRecordTable emp) {
        String sql = "UPDATE currentemprecordtable SET "
                + "Company_Code = ?, Occupation = ?, Employment_Status = ?, "
                + "TypeOfWork = ?, Country_Of_Assignment = ?, Date_Employed = ? "
                + "WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getCompanyCode());
            ps.setString(2, emp.getOccupation());
            ps.setString(3, emp.getEmploymentStatus());
            ps.setString(4, emp.getTypeOfWork());
            ps.setString(5, emp.getCountryOfAssignment());
            ps.setDate(6,   emp.getDateEmployed());
            ps.setString(7, emp.getPagIbigMIDNo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] updateCurrentEmp error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    public boolean deleteCurrentEmp(String pagIbigMIDNo) {
        String sql = "DELETE FROM currentemprecordtable WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] deleteCurrentEmp error: " + e.getMessage());
            return false;
        }
    }

    // ─── Helper ───────────────────────────────────────────────────────────────────

    private CurrentEmpRecordTable mapRow(ResultSet rs) throws SQLException {
        return new CurrentEmpRecordTable(
                rs.getString("PagIbig_MID_No"),
                rs.getString("Company_Code"),
                rs.getString("Occupation"),
                rs.getString("Employment_Status"),
                rs.getString("TypeOfWork"),
                rs.getString("Country_Of_Assignment"),
                rs.getDate("Date_Employed")
        );
    }
}