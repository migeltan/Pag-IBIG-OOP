package main;

import models.MemberTable;
import models.UserCredentials;

/**
 * Singleton that holds all registration data IN MEMORY during the signup flow.
 * Nothing is written to the DB until finalizeRegistration() is called after
 * all 4 forms are completed.
 *
 * Usage:
 *   RegistrationSession session = RegistrationSession.getInstance();
 *   session.setTempMID("1212-3434-5660");
 *   session.setCredentials(...);
 *   // ... after all forms done:
 *   session.finalizeRegistration();
 */
public class RegistrationSession {

    // ─── Singleton ────────────────────────────────────────────────────────────
    private static RegistrationSession instance;

    public static RegistrationSession getInstance() {
        if (instance == null) instance = new RegistrationSession();
        return instance;
    }

    /** Call this if the user quits halfway — wipes the session clean. */
    public static void reset() { instance = new RegistrationSession(); }

    // ─── Session State ────────────────────────────────────────────────────────
    private String          tempMID;
    private UserCredentials credentials;
    private MemberTable     memberData;

    // Track which forms are done
    private boolean memberInfoDone    = false;
    private boolean currentEmpDone    = false;
    private boolean prevEmpDone       = false;
    private boolean heirsDone         = false;

    private RegistrationSession() {}

    // ─── MID ──────────────────────────────────────────────────────────────────
    public String getTempMID()              { return tempMID; }
    public void   setTempMID(String mid)    { this.tempMID = mid; }

    // ─── Credentials ──────────────────────────────────────────────────────────
    public UserCredentials getCredentials()              { return credentials; }
    public void setCredentials(UserCredentials creds)    { this.credentials = creds; }

    // ─── Member Data ──────────────────────────────────────────────────────────
    public MemberTable getMemberData()                   { return memberData; }
    public void setMemberData(MemberTable data)          { this.memberData = data; }

    // ─── Completion Flags ─────────────────────────────────────────────────────
    public boolean isMemberInfoDone()                    { return memberInfoDone; }
    public void setMemberInfoDone(boolean v)             { this.memberInfoDone = v; }

    public boolean isCurrentEmpDone()                    { return currentEmpDone; }
    public void setCurrentEmpDone(boolean v)             { this.currentEmpDone = v; }

    public boolean isPrevEmpDone()                       { return prevEmpDone; }
    public void setPrevEmpDone(boolean v)                { this.prevEmpDone = v; }

    public boolean isHeirsDone()                         { return heirsDone; }
    public void setHeirsDone(boolean v)                  { this.heirsDone = v; }

    /** Returns true only when all 4 forms are complete. */
    public boolean isComplete() {
        return memberInfoDone && currentEmpDone && prevEmpDone && heirsDone;
    }

    /** How many forms are done (for the progress bar in SignUpFrame). */
    public int completedCount() {
        int count = 0;
        if (memberInfoDone) count++;
        if (currentEmpDone) count++;
        if (prevEmpDone)    count++;
        if (heirsDone)      count++;
        return count;
    }
}