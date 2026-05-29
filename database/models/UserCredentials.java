package models;

public class UserCredentials {

    private String pagIbigMIDNo;
    private String password;
    private String securityQ1;
    private String securityA1;
    private String securityQ2;
    private String securityA2;
    private String securityQ3;
    private String securityA3;

    // ─── Constructors ─────────────────────────────────────────────────────────

    public UserCredentials() {}

    public UserCredentials(String pagIbigMIDNo, String password,
                           String securityQ1, String securityA1,
                           String securityQ2, String securityA2,
                           String securityQ3, String securityA3) {
        this.pagIbigMIDNo = pagIbigMIDNo;
        this.password     = password;
        this.securityQ1   = securityQ1;
        this.securityA1   = securityA1;
        this.securityQ2   = securityQ2;
        this.securityA2   = securityA2;
        this.securityQ3   = securityQ3;
        this.securityA3   = securityA3;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public String getPagIbigMIDNo()              { return pagIbigMIDNo; }
    public void setPagIbigMIDNo(String v)        { this.pagIbigMIDNo = v; }

    public String getPassword()                  { return password; }
    public void setPassword(String v)            { this.password = v; }

    public String getSecurityQ1()                { return securityQ1; }
    public void setSecurityQ1(String v)          { this.securityQ1 = v; }

    public String getSecurityA1()                { return securityA1; }
    public void setSecurityA1(String v)          { this.securityA1 = v; }

    public String getSecurityQ2()                { return securityQ2; }
    public void setSecurityQ2(String v)          { this.securityQ2 = v; }

    public String getSecurityA2()                { return securityA2; }
    public void setSecurityA2(String v)          { this.securityA2 = v; }

    public String getSecurityQ3()                { return securityQ3; }
    public void setSecurityQ3(String v)          { this.securityQ3 = v; }

    public String getSecurityA3()                { return securityA3; }
    public void setSecurityA3(String v)          { this.securityA3 = v; }

    @Override
    public String toString() {
        return "UserCredentials{pagIbigMIDNo='" + pagIbigMIDNo + "'}";
    }
}