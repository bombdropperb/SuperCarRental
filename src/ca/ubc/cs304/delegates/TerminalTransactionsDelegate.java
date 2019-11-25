package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the 
 * controller class (in this case Bank).
 * 
 * TerminalTransactions calls the methods that we have listed below but 
 * Bank is the actual class that will implement the methods.
 */
public interface TerminalTransactionsDelegate {
	public Boolean existingCustomer(int dLicense);
	public void insertCustomer(Customer customer);
	public Boolean reserveVehicle(Reservation reserve);
	public ArrayList<Vehicle> viewVehicle(String vtname, String location, Date time);
	public Boolean rentVehicle(Rental rent);
	public Boolean validVlicense(String id);
	public void returnVehicle(Return r);
	public void viewAll();
	public Boolean existVehicleType(String vtname);
	public void terminalTransactionsFinished();
	public ArrayList<Vehicle> dailyRental(String d, String location);
	public ArrayList<Vehicle> dailyReturn(String d, String location);
	public HashMap<String, Integer> getMapOfTotalRentedByVType (String typ, String date, String specifiedBranch);
    public HashMap<String, Integer> getMapOfTotalRentedByBranch (String typ, String date, String specifiedBranch);
    public Integer getTotalOfRented (String typ, String date, String specifiedBranch);
    public HashMap<String, Integer> revByType (String typ, String date, String specifiedBranch);
    public HashMap<String, Integer> revByBranch (String typ, String date, String specifiedBranch);
    public Integer getTotalRev (String typ, String date, String specifiedBranch);
}
