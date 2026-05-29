package models;

import java.sql.Date;

public class CurrentEmpRecordTable {

    private String pagIbigMIDNo;
    private String companyCode;
    private String occupation;
    private String employmentStatus;
    private String typeOfWork;
    private String countryOfAssignment;
    private Date   dateEmployed;

    public CurrentEmpRecordTable() {}

    public CurrentEmpRecordTable(String pagIbigMIDNo, String companyCode,
                                  String occupation, String employmentStatus,
                                  String typeOfWork, String countryOfAssignment,
                                  Date dateEmployed) {
        this.pagIbigMIDNo        = pagIbigMIDNo;
        this.companyCode         = companyCode;
        this.occupation          = occupation;
        this.employmentStatus    = employmentStatus;
        this.typeOfWork          = typeOfWork;
        this.countryOfAssignment = countryOfAssignment;
        this.dateEmployed        = dateEmployed;
    }

    public String getPagIbigMIDNo()                    { return pagIbigMIDNo; }
    public void   setPagIbigMIDNo(String v)            { this.pagIbigMIDNo = v; }

    public String getCompanyCode()                     { return companyCode; }
    public void   setCompanyCode(String v)             { this.companyCode = v; }

    public String getOccupation()                      { return occupation; }
    public void   setOccupation(String v)              { this.occupation = v; }

    public String getEmploymentStatus()                { return employmentStatus; }
    public void   setEmploymentStatus(String v)        { this.employmentStatus = v; }

    public String getTypeOfWork()                      { return typeOfWork; }
    public void   setTypeOfWork(String v)              { this.typeOfWork = v; }

    public String getCountryOfAssignment()             { return countryOfAssignment; }
    public void   setCountryOfAssignment(String v)     { this.countryOfAssignment = v; }

    public Date   getDateEmployed()                    { return dateEmployed; }
    public void   setDateEmployed(Date v)              { this.dateEmployed = v; }

    @Override
    public String toString() {
        return "CurrentEmpRecordTable{mid='" + pagIbigMIDNo + "', company='" + companyCode + "'}";
    }
}