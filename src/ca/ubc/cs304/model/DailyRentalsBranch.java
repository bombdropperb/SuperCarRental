package ca.ubc.cs304.model;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import oracle.sql.DATE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DailyRentalsBranch {

    // HashMap with key being vehicle type and ArrayList containing the list of vehicles of that type which should
    // meet the criteria of being located at the specified branch and rented on the specified date
    //private final HashMap<String, ArrayList<Vehicle>> vehicles;
    //private final HashMap<String, Integer> numVehicles;
   // private final int newRentals;

    public DailyRentalsBranch(String branch, DATE date) {
     /*   DatabaseConnectionHandler dbHandler = new DatabaseConnectionHandler();

        // Calls the DB to get the HashMap for vehicles.
        vehicles = dbHandler.dailyReportBranch(branch, date);
        numVehicles = dbHandler.dailyReportBranchNum(branch, date);
        newRentals = dbHandler.dailyReportBranchTotal(branch, date);
//        HashMap<String, Integer> tempNumVehicles = new HashMap<>();
//
//        Set<String> keys = vehicles.keySet();
//        int totalCount = 0;
//
//        // For each key in the vehicles Map, get the ArrayList of vehicles, count them, and put the count into numVehicles
//        // typeCount = count of vehicles for that type, totalCount = total count of all vehicles
//        for (String key: keys) {
//            int typeCount = 0;
//            ArrayList<Vehicle> vhTypeCount = vehicles.get(key);
//            for (Vehicle vh: vhTypeCount) {
//                totalCount++;
//                typeCount++;
//            }
//            tempNumVehicles.put(key, typeCount);
//        }
//
//        numVehicles = tempNumVehicles;
//        newRentals = totalCount;
    }

    public HashMap<String, ArrayList<Vehicle>> getVehicles() {
        return vehicles;
    }

    public HashMap<String, Integer> getNumVehicles() {
        return numVehicles;
    }

    public int getNewRentals() {
        return newRentals;
    }*/
}
}
