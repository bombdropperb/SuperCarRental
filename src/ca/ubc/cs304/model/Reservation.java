package ca.ubc.cs304.model;

public class Reservation {
    private final int confNo;
    private final String vtname;
    private final int dLicense;
    private final String fromDate;
    private final String fromTime;
    private final String toDate;
    private final String toTime;


    public Reservation(int confNo, String vtname, int dLicense, String fromDate, String fromTime, String toDate, String toTime ) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.dLicense = dLicense;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
    }

    public int getConfNo() {
        return confNo;
    }

    public String getVtname() {
        return vtname;
    }

    public int getdLicense() {
        return dLicense;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToDate() {
        return toDate;
    }
    public String getToTime() {
        return toTime;
    }
}
