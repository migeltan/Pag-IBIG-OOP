package models;

import java.sql.Date;

public class PrevEmpTable {

    private String pagIbigMIDNo;
    private int    prevEmpCode;    // AUTO_INCREMENT
    private String companyCode;
    private Date   toDate;
    private Date   fromDate;

    // ─── Constructors ────────────────────────────────────────────────────────────

    public PrevEmpTable() {}

    public PrevEmpTable(String pagIbigMIDNo, int prevEmpCode,
                        String companyCode, Date toDate, Date fromDate) {
        this.pagIbigMIDNo = pagIbigMIDNo;
        this.prevEmpCode  = prevEmpCode;
        this.companyCode  = companyCode;
        this.toDate       = toDate;
        this.fromDate     = fromDate;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String getPagIbigMIDNo()                    { return pagIbigMIDNo; }
    public void setPagIbigMIDNo(String pagIbigMIDNo)   { this.pagIbigMIDNo = pagIbigMIDNo; }

    public int getPrevEmpCode()                        { return prevEmpCode; }
    public void setPrevEmpCode(int prevEmpCode)         { this.prevEmpCode = prevEmpCode; }

    public String getCompanyCode()                     { return companyCode; }
    public void setCompanyCode(String companyCode)     { this.companyCode = companyCode; }

    public Date getToDate()                            { return toDate; }
    public void setToDate(Date toDate)                 { this.toDate = toDate; }

    public Date getFromDate()                          { return fromDate; }
    public void setFromDate(Date fromDate)             { this.fromDate = fromDate; }

    @Override
    public String toString() {
        return "PrevEmpTable{" +
                "pagIbigMIDNo='" + pagIbigMIDNo + '\'' +
                ", prevEmpCode=" + prevEmpCode +
                ", companyCode='" + companyCode + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}
