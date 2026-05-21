package models;

import java.sql.Date;

public class CurrentEmpRecordTable {

    private String pagIbigMIDNo;
    private String companyCode;
    private String occupation;
    private String employmentStatus;  // PERMANENT/REGULAR | CASUAL | CONTRACTUAL | PROJECT BASED | PART-TIME/TEMPORARY
    private String typeOfWork;        // LAND-BASED | SEA-BASED | nullable
    private String countryOfAssignment;
    private Date   dateEmployed;

    // ─── Constructors ────────────────────────────────────────────────────────────

    public CurrentEmpRecordTable() {}

    public CurrentEmpRecordTable(String pagIbigMIDNo, String companyCode,
                                 String occupation, String employmentStatus,
                                 String typeOfWork, String countryOfAssignment,
                                 Date dateEmployed) {
        this.pagIbigMIDNo       = pagIbigMIDNo;
        this.companyCode        = companyCode;
        this.occupation         = occupation;
        this.employmentStatus   = employmentStatus;
        this.typeOfWork         = typeOfWork;
        this.countryOfAssignment = countryOfAssignment;
        this.dateEmployed       = dateEmployed;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String getPagIbigMIDNo()                        { return pagIbigMIDNo; }
    public void setPagIbigMIDNo(String pagIbigMIDNo)       { this.pagIbigMIDNo = pagIbigMIDNo; }

    public String getCompanyCode()                         { return companyCode; }
    public void setCompanyCode(String companyCode)         { this.companyCode = companyCode; }

    public String getOccupation()                          { return occupation; }
    public void setOccupation(String occupation)           { this.occupation = occupation; }

    public String getEmploymentStatus()                    { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus){ this.employmentStatus = employmentStatus; }

    public String getTypeOfWork()                          { return typeOfWork; }
    public void setTypeOfWork(String typeOfWork)           { this.typeOfWork = typeOfWork; }

    public String getCountryOfAssignment()                 { return countryOfAssignment; }
    public void setCountryOfAssignment(String v)           { this.countryOfAssignment = v; }

    public Date getDateEmployed()                          { return dateEmployed; }
    public void setDateEmployed(Date dateEmployed)         { this.dateEmployed = dateEmployed; }

    @Override
    public String toString() {
        return "CurrentEmpRecordTable{" +
                "pagIbigMIDNo='" + pagIbigMIDNo + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", occupation='" + occupation + '\'' +
                ", employmentStatus='" + employmentStatus + '\'' +
                ", typeOfWork='" + typeOfWork + '\'' +
                ", countryOfAssignment='" + countryOfAssignment + '\'' +
                ", dateEmployed=" + dateEmployed +
                '}';
    }
}
