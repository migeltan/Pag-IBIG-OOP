package models;

public class CompanyDetailsTable {

    private String companyCode;
    private String companyName;
    private String companyAddress;
    private String officeAssignment;
    private String branchLocation;

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

    public String getCompanyCode()                     { return companyCode; }
    public void   setCompanyCode(String v)             { this.companyCode = v; }

    public String getCompanyName()                     { return companyName; }
    public void   setCompanyName(String v)             { this.companyName = v; }

    public String getCompanyAddress()                  { return companyAddress; }
    public void   setCompanyAddress(String v)          { this.companyAddress = v; }

    public String getOfficeAssignment()                { return officeAssignment; }
    public void   setOfficeAssignment(String v)        { this.officeAssignment = v; }

    public String getBranchLocation()                  { return branchLocation; }
    public void   setBranchLocation(String v)          { this.branchLocation = v; }

    @Override
    public String toString() {
        return "CompanyDetailsTable{code='" + companyCode + "', name='" + companyName + "'}";
    }
}