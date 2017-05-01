package team6.iot.uiowa.edu.iotandroid;

/**
 * Created by lukeb on 4/30/2017.
 */

public class Log {
    String hasAccess;
    String lockId;
    Double time;
    String user;
    public Log(String hasAccess, String lockId, Double time, String user){
        this.hasAccess = hasAccess;
        this.lockId = lockId;
        this.time = time;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(String hasAccess) {
        this.hasAccess = hasAccess;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

}
