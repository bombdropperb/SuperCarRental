package ca.ubc.cs304.model;

public class Vehicle {
    private final int vid;
    private final String vlicense;
    private final String odometer;
    private final String status;
    private final String vtname;
    private final String location;

    public Vehicle(int vid, String vlicense, String odometer, String status, String vtname, String location ) {
        this.vid = vid;
        this.vlicense = vlicense;
        this.odometer = odometer;
        this.status = status;
        this.vtname = vtname;
        this.location = location;
    }
    public int getVid() {
        return vid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getOdometer() {
        return odometer;
    }

    public String getStatus() {
        return status;
    }

    public String getVtname() {
        return vtname;
    }

    public String getLocation() { return location; }
}
