package app;

import java.sql.Timestamp;

public class ApplianceEmail {

    static final int CREATED = 0;
    static final int UPDATED = 1;
    static final int DELETED = -1;
    private String applianceIP;
    private String version;
    private String updater;
    private String current;
    private String previous;
    private Timestamp timestamp;
    private int status;

    public ApplianceEmail() {

    }

    public ApplianceEmail(String applianceIP) {
        this.applianceIP = applianceIP;
    }

    public void setApplianceStatus(int status) {
        this.status = status;
    }

    public int getApplianceStatus() {
        return this.status;
    }

    public String getApplianceStatusString() {
        if (status == CREATED) {
            return "Appliance created";
        } else if (status == UPDATED) {
            return "Appliance updated";
        } else if (status == DELETED) {
            return "Appliance deleted";
        } else {
            return "Unknown modification made on appliance.";
        }
    }

    public String getApplianceIP() {
        return applianceIP;
    }

    public void setApplianceIP(String applianceIP) {
        this.applianceIP = applianceIP;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }
}
