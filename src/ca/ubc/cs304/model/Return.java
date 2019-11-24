package ca.ubc.cs304.model;

public class Return {
    private final int rid;
    private final String date;
    private final int time;
    private final int odometer;
    private final String fullTank;
    private final int value;


    public Return(int rid, String fromDate, int fromTime, int odometer, String fullTank,  int value){
        this.rid = rid;
        this.date = fromDate;
        this.time = fromTime;
        this.odometer = odometer;
        this.fullTank = fullTank;
        this.value = value;
    }
    public int getRid() {return rid;}

    public String getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public int getOdometer() {return odometer;}

    public String getFullTank() { return fullTank;}

    public int getValue() {return value;}
}
