package ca.ubc.cs304.model;

public class Rental {
    private final int rid;
    private final String vlicense;
    private final int dLicense;
    private final String fromDate;
    private final int fromTime;
    private final String toDate;
    private final int toTime;
    private final int odometer;
    private final int confNo;

    public Rental(int rid, String vlicense, int dLicense, String fromDate, int fromTime, String toDate, int toTime, int odometer, int confNo){
        this.rid = rid;
        this.vlicense = vlicense;
        this. dLicense = dLicense;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.odometer = odometer;
        this.confNo = confNo;
    }
    public int getConfNo() {
        return confNo;
    }

    public int getRid() {return rid;}

    public String getvLicense() {
        return vlicense;
    }

    public int getdLicense() {
        return dLicense;
    }

    public String getFromDate() {
        return fromDate;
    }

    public int getFromTime() {
        return fromTime;
    }

    public String getToDate() {
        return toDate;
    }

    public int getToTime() {
        return toTime;
    }

    public int getOdometer() {return odometer;}
}
