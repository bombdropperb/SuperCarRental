package ca.ubc.cs304.model;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import oracle.sql.DATE;

import java.util.ArrayList;
import java.util.HashMap;

public class DailyReturnsBranch {

    // For each HashMap, String is the vehicleType and the name describes the values
   /* private final HashMap<String, ArrayList<Vehicle>> vehicles;
    private final HashMap<String, Integer> numVehicles;
    private final HashMap<String, Integer> revByCat;
    private final int newReturns;
    private final int totRev;

    public DailyReturnsBranch(String specifiedBranch, DATE date) {
        DatabaseConnectionHandler dbHandler = new DatabaseConnectionHandler();
        vehicles = dbHandler.dailyReportBranchRtn(specifiedBranch, date);
        numVehicles = dbHandler.dailyReportBranchNumRtn(specifiedBranch, date);
        newReturns = dbHandler.dailyReportBranchTotalRtn(specifiedBranch, date);
        revByCat = dbHandler.dailyReportBranchRevByCat(specifiedBranch, date);
        totRev = dbHandler.dailyReportBranchTotalRev(specifiedBranch, date);
    }

    public HashMap<String, ArrayList<Vehicle>> getVehicles() {
        return vehicles;
    }

    public HashMap<String, Integer> getNumVehicles() {
        return numVehicles;
    }

    public HashMap<String, Integer> getRevByCat() {
        return revByCat;
    }

    public int getNewReturns() {
        return newReturns;
    }

    public int getTotRev() {
        return totRev;
    }*/
}
