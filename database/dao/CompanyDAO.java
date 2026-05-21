package dao;

import database.DatabaseConnection;
import models.CompanyDetailsTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for companydetailstable.
 */
public class CompanyDAO {

    // ─── CREATE ──────────────────────────────────────────────────────────────────

    public boolean insertCompany(CompanyDetailsTable company) {
        String sql = "INSERT INTO companydetailstable "
                + "(Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, company.getCompanyCode());
            ps.setString(2, company.getCompanyName());
            ps.setString(3, company.getCompanyAddress());
            ps.setString(4, company.getOfficeAssignment());
            ps.setString(5, company.getBranchLocation());   // nullable

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CompanyDAO] insertCompany error: " + e.getMessage());
            return false;
        }
    }

    // ─── READ (single) ────────────────────────────────────────────────────────────

    public CompanyDetailsTable getCompanyByCode(String companyCode) {
        String sql = "SELECT * FROM companydetailstable WHERE Company_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, companyCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            System.err.println("[CompanyDAO] getCompanyByCode error: " + e.getMessage());
        }
        return null;
    }

    // ─── READ (all) ───────────────────────────────────────────────────────────────

    public List<CompanyDetailsTable> getAllCompanies() {
        List<CompanyDetailsTable> list = new ArrayList<>();
        String sql = "SELECT * FROM companydetailstable";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[CompanyDAO] getAllCompanies error: " + e.getMessage());
        }
        return list;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    public boolean updateCompany(CompanyDetailsTable company) {
        String sql = "UPDATE companydetailstable SET "
                + "Company_Name = ?, Company_Address = ?, "
                + "Office_Assignment = ?, Branch_Location = ? "
                + "WHERE Company_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, company.getCompanyName());
            ps.setString(2, company.getCompanyAddress());
            ps.setString(3, company.getOfficeAssignment());
            ps.setString(4, company.getBranchLocation());
            ps.setString(5, company.getCompanyCode());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CompanyDAO] updateCompany error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    public boolean deleteCompany(String companyCode) {
        String sql = "DELETE FROM companydetailstable WHERE Company_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, companyCode);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CompanyDAO] deleteCompany error: " + e.getMessage());
            return false;
        }
    }

    // ─── Helper ───────────────────────────────────────────────────────────────────

    private CompanyDetailsTable mapRow(ResultSet rs) throws SQLException {
        return new CompanyDetailsTable(
                rs.getString("Company_Code"),
                rs.getString("Company_Name"),
                rs.getString("Company_Address"),
                rs.getString("Office_Assignment"),
                rs.getString("Branch_Location")
        );
    }
}