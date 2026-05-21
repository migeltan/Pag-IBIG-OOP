package models;

public class CompanyDetailsTable {

    private String companyCode;
    private String companyName;
    private String companyAddress;
    private String officeAssignment;   // 'HEAD OFFICE' | 'BRANCH'
    private String branchLocation;     // nullable

    // ─── Constructors ────────────────────────────────────────────────────────────

    public CompanyDetailsTable() {}

    public CompanyDetailsTable(String companyCode, String companyName,
                               String companyAddress, String officeAssignment,
                               String branchLocation) {
        this.companyCode      = companyCode;
        this.companyName      = companyName;
        this.companyAddress   = companyAddress;
        this.officeAssignment = officeAssignment;
        this.branchLocation   = branchLocation;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String getCompanyCode()                     { return companyCode; }
    public void setCompanyCode(String companyCode)     { this.companyCode = companyCode; }

    public String getCompanyName()                     { return companyName; }
    public void setCompanyName(String companyName)     { this.companyName = companyName; }

    public String getCompanyAddress()                  { return companyAddress; }
    public void setCompanyAddress(String companyAddress){ this.companyAddress = companyAddress; }

    public String getOfficeAssignment()                { return officeAssignment; }
    public void setOfficeAssignment(String v)          { this.officeAssignment = v; }

    public String getBranchLocation()                  { return branchLocation; }
    public void setBranchLocation(String branchLocation){ this.branchLocation = branchLocation; }

    @Override
    public String toString() {
        return "CompanyDetailsTable{" +
                "companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", officeAssignment='" + officeAssignment + '\'' +
                ", branchLocation='" + branchLocation + '\'' +
                '}';
    }
}
