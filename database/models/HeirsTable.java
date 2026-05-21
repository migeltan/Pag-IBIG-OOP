package models;

import java.sql.Date;

public class HeirsTable {

    private String pagIbigMIDNo;
    private int    heirCode;           // AUTO_INCREMENT
    private String heirsName;
    private String heirsRelationship;
    private Date   heirsBirthdate;

    // ─── Constructors ────────────────────────────────────────────────────────────

    public HeirsTable() {}

    public HeirsTable(String pagIbigMIDNo, int heirCode, String heirsName,
                      String heirsRelationship, Date heirsBirthdate) {
        this.pagIbigMIDNo      = pagIbigMIDNo;
        this.heirCode          = heirCode;
        this.heirsName         = heirsName;
        this.heirsRelationship = heirsRelationship;
        this.heirsBirthdate    = heirsBirthdate;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String getPagIbigMIDNo()                         { return pagIbigMIDNo; }
    public void setPagIbigMIDNo(String pagIbigMIDNo)        { this.pagIbigMIDNo = pagIbigMIDNo; }

    public int getHeirCode()                                { return heirCode; }
    public void setHeirCode(int heirCode)                   { this.heirCode = heirCode; }

    public String getHeirsName()                            { return heirsName; }
    public void setHeirsName(String heirsName)              { this.heirsName = heirsName; }

    public String getHeirsRelationship()                    { return heirsRelationship; }
    public void setHeirsRelationship(String heirsRelationship){ this.heirsRelationship = heirsRelationship; }

    public Date getHeirsBirthdate()                         { return heirsBirthdate; }
    public void setHeirsBirthdate(Date heirsBirthdate)      { this.heirsBirthdate = heirsBirthdate; }

    @Override
    public String toString() {
        return "HeirsTable{" +
                "pagIbigMIDNo='" + pagIbigMIDNo + '\'' +
                ", heirCode=" + heirCode +
                ", heirsName='" + heirsName + '\'' +
                ", heirsRelationship='" + heirsRelationship + '\'' +
                ", heirsBirthdate=" + heirsBirthdate +
                '}';
    }
}
