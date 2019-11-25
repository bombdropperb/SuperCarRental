package ca.ubc.cs304.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.*;

/**
 * The class is only responsible for handling terminal text inputs. 
 */
public class TerminalTransactions {
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final int INVALID_INPUT = Integer.MIN_VALUE;
	private static final int EMPTY_INPUT = 0;
	
	private BufferedReader bufferedReader = null;
	private TerminalTransactionsDelegate delegate = null;

	public TerminalTransactions() {
	}

	/**
	 * Displays simple text interface
	 */ 
	public void showMainMenu(TerminalTransactionsDelegate delegate) {
		this.delegate = delegate;
		
	    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;
		
		while (choice != 10) {
			System.out.println();
			System.out.println("1. View Vehicle");
			System.out.println("2. Reserve Vehicle");
			System.out.println("3. Rent a vehicle");
			System.out.println("4. Return a vehicle");
			System.out.println("5. Create new customer");
			System.out.println("6. daily rental log");
			System.out.println("7. daily rental log for branch");
			System.out.println("8. daily return log ");
			System.out.println("9. daily return log for branch");
			System.out.println("10. quit");
			System.out.print("Please choose one of the above 5 options: ");

			choice = readInteger(false);

			System.out.println(" ");

			if (choice != INVALID_INPUT) {
				switch (choice) {
					case 1:
						handleView();
						break;
					case 2:
						handleReservation();
						break;
					case 3:
						handleRental();
						break;
					case 4:
						handleReturn();
						break;
					case 5:
						handleInsertCustomer();
						break;
					case 6:
						handleDailyRental();
						break;
					case 7:
						handleDailyBranchRental();
						break;
					case 8 :
						handleDailyReturn();
						break;
					case 9 :
						handleDailyBranchReturn();
						break;
					case 10:
						handleQuitOption();
						break;
					default:
						System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
						break;
				}
			}
		}		
	}

	private void handleView() {
		String vtname = null;
		String location = null;
		Date time = null;

		System.out.println("Enter type");
		vtname = readLine().trim();

		System.out.println("Enter location");
		location = readLine().trim();

		System.out.println("Do you wish to Enter time that you need the car");
		String s = readLine().trim();
		if(s == "yes"){

			System.out.println("Year: ");
			int year = readInteger(false);

			System.out.println("Month: ");
			int month = readInteger(false);

			System.out.println("Date: ");
			int date = readInteger(false);

			time = new Date(year, month, date);
		}

		ArrayList<Vehicle> arr = delegate.viewVehicle(vtname, location, time);
		for (Vehicle v: arr) {
			System.out.println("vid: " + v.getVid()+ "vlicense: " + v.getVlicense()+ "odometer: " +v.getOdometer() + "status: "
					+ v.getStatus()+ "vtname: "+ v.getVtname()+  "location: " +v.getLocation());
		}

	}

	private void handleInsertCustomer() {
		int dLicense;
		int phoneNumber;
		String name;
		String address;

		System.out.println("Enter the details to dlicense");
		dLicense = readInteger(false);

		System.out.print("Enter the details to the Phone number ");
		phoneNumber = readInteger(false);

		System.out.print("Enter the details to the  name");
		name = readLine().trim();

		System.out.print("Enter the details to the address");
		address = readLine().trim();

		Customer customer = new Customer(phoneNumber, name, address,dLicense);
		delegate.insertCustomer(customer);

	}

	private void handleReservation() {
		int dLicense = 0;

		System.out.print("Please enter your license: ");
		dLicense = readInteger(false);

		Boolean exist =  delegate.existingCustomer(dLicense);
		if (!exist){
			System.out.println("Customer does not exist");
			return;
		}

		String vtname = null;
		String fromDate = null;
		int fromTime;
		String toDate = null;
		int toTime;
		int confNo = INVALID_INPUT;

		while (confNo == INVALID_INPUT) {
			System.out.print("Enter the details to the confNo of the reservation: ");
			confNo = readInteger(false);
		}
		while (vtname == null || vtname.length() <= 0) {
			System.out.print("Please enter the vehicle type name: ");
			vtname = readLine().trim();
		}

		if(!delegate.existVehicleType(vtname)) {
			System.out.println("vehicle of this type is not available");
			return;
		}

		while (fromDate == null || fromDate.length() <= 0) {
			System.out.print("Please enter your fromDate: ");
			fromDate = readLine().trim();
		}

		System.out.print("Please enter your fromTime: ");
		fromTime = readInteger(false);

		while (toDate == null || toDate.length() <= 0) {
			System.out.print("Please enter your toDate: ");
			toDate = readLine().trim();
		}

		System.out.print("Please enter your toTime: ");
		toTime = readInteger(false);

		Reservation reservation = new Reservation(confNo, vtname, dLicense, fromDate,fromTime, toDate,toTime);
		Boolean success  = delegate.reserveVehicle(reservation);
		if (success) {
			System.out.println("this is the confno: "+ reservation.getConfNo() + " use this to print out the needed info");
		}

	}

	private void handleRental() {
		int rid;
		String vLicense = null;
		int dLicense;
		String fromDate = null;
		int fromTime;
		String toDate = null;
		int toTime;
		int odometer;
		int confNo = INVALID_INPUT;

		System.out.print("Please enter your rid: ");
		rid = readInteger(false);

		while (vLicense == null || vLicense.length() <= 0) {
			System.out.print("Please enter the vehicle license: ");
			vLicense = readLine().trim();
		}

		if (!delegate.validVlicense(vLicense)) {
			System.out.println("Vehicle unavailable!");
			return;
		}

		System.out.print("Please enter your dLicense: ");
		dLicense = readInteger(false);

		if (!delegate.existingCustomer(dLicense)){
			System.out.println("Customer does not exist");
			return;
		}

		while (fromDate == null || fromDate.length() <= 0) {
			System.out.print("Please enter your fromDate: ");
			fromDate = readLine().trim();
		}

		System.out.print("Please enter your fromTime: ");
		fromTime = readInteger(false);

		while (toDate == null || toDate.length() <= 0) {
			System.out.print("Please enter your toDate: ");
			toDate = readLine().trim();
		}

		System.out.print("Please enter your toTime: ");
		toTime = readInteger(false);

		System.out.print("Please enter your odometer: ");
		odometer = readInteger(false);

		while (confNo == INVALID_INPUT) {
			System.out.print("Enter the details to the confNo of the reservation, if none enter 0: ");
			confNo = readInteger(false);
		}

		Rental rent = new Rental(rid,vLicense,dLicense,fromDate,fromTime,toDate,toTime,odometer,confNo);
		Boolean success = delegate.rentVehicle(rent);

		if (success) {
			System.out.println("output the details needed here -> : "+ rent.getConfNo());
		}
	}

	private void handleReturn() {
		int rid;
		String date;
		int time;
		int odometer;
		String fullTank;
		int value;

		System.out.println("Enter the rid: ");
		rid = readInteger(false);

		System.out.print("Enter the date: ");
		date = readLine().trim();

		System.out.print("Enter the details to time: ");
		time = readInteger(false);

		System.out.print("Enter the details to odometer: ");
		odometer = readInteger(false);

		System.out.print("Enter the detail to fulltank: ");
		fullTank = readLine().trim();

		System.out.print("Enter the details to value: ");
		value = readInteger(false);

		Return r = new Return(rid, date, time, odometer, fullTank, value);

		delegate.returnVehicle(r);

	}

	private void viewTable() {
		delegate.viewAll();
	}

	private void handleDailyRental(){
		String location = null;
		String date;

		System.out.println("Please enter year in form of YYYY-MM-DD");
		date = readLine().trim();

		ArrayList<Vehicle> arr = delegate.dailyRental(date, location);

		for (Vehicle v : arr ) {
			System.out.println("VID: "+ v.getVid() + " Vtype: " + v.getVtname() + " Location: " + v.getLocation());
		}

        HashMap<String, Integer> rentedByVehicles = delegate.getMapOfTotalRentedByVType("rent", date, location);
        Set<String> keys = rentedByVehicles.keySet();
        System.out.println("Vehicles by Category:");
        for (String key: keys) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + rentedByVehicles.get(key));
        }

        HashMap<String, Integer> rentedByBranch = delegate.getMapOfTotalRentedByBranch("rent", date, location);
        Set<String> keysB = rentedByBranch.keySet();
        System.out.println("Vehicles by Branch:");
        for (String key: keysB) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + rentedByBranch.get(key));
        }

        Integer totalRent = delegate.getTotalOfRented("rent", date, location);
        System.out.println("Total Daily Rented: " + totalRent);
        // countType(arr);
	}

	private void handleDailyBranchRental(){
		String location = null;
		String date;

		System.out.println("enter branch location: ");
		location = readLine().trim();

		System.out.println("Please enter date");
		date = readLine().trim();

		ArrayList<Vehicle> arr = delegate.dailyRental(date, location);

		for (Vehicle v : arr ) {
			System.out.println("VID: "+ v.getVid() + " Vtype: " + v.getVtname() + "Location: " + v.getLocation());
		}

        HashMap<String, Integer> rentedByVehicles = delegate.getMapOfTotalRentedByVType("rent", date, location);
        Set<String> keys = rentedByVehicles.keySet();
        System.out.println("Vehicles by Category:");
        for (String key: keys) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + rentedByVehicles.get(key));
        }

        Integer totalRent = delegate.getTotalOfRented("rent", date, location);
        System.out.println("Total Daily Rented: " + totalRent);

		// countType(arr);



	}

	private void handleDailyReturn(){
		String location = null;
		String date;

		System.out.println("Please enter date");
		date = readLine().trim();

		ArrayList<Vehicle> arr = delegate.dailyReturn(date, location);

		for (Vehicle v : arr ) {
			System.out.println("VID: "+ v.getVid() + " Vtype: " + v.getVtname() + "Location: " + v.getLocation());
		}

        HashMap<String, Integer> rentedByVehicles = delegate.getMapOfTotalRentedByVType("return", date, location);
        Set<String> keys = rentedByVehicles.keySet();
        System.out.println("Vehicles by Category:");
        for (String key: keys) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + rentedByVehicles.get(key));
        }

        HashMap<String, Integer> rentedByBranch = delegate.getMapOfTotalRentedByBranch("return", date, location);
        Set<String> keysB = rentedByBranch.keySet();
        System.out.println("Vehicles by Branch:");
        for (String key: keysB) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + rentedByBranch.get(key));
        }

        HashMap<String, Integer> revByType = delegate.revByType("return", date, location);
        Set<String> keysRBT = revByType.keySet();
        System.out.println("Revenue by Type:");
        for (String key: keysRBT) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + revByType.get(key));
        }

        HashMap<String, Integer> revByBranch = delegate.revByBranch("return", date, location);
        Set<String> keysRBB = revByBranch.keySet();
        System.out.println("Revenue by Branch");
        for (String key: keysRBB) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + revByBranch.get(key));
        }

        Integer totalRent = delegate.getTotalOfRented("return", date, location);
        System.out.println("Total Daily Returned: " + totalRent);

        Integer totalRev = delegate.getTotalRev("return", date, location);
        System.out.println("Total Daily Revenue: " + totalRev);


        // countType(arr);

	}

	private void handleDailyBranchReturn(){
		String location = null;
		String date;

		System.out.println("enter branch location: ");
		location = readLine().trim();

		System.out.println("Please enter date");
		date = readLine().trim();

		ArrayList<Vehicle> arr = delegate.dailyReturn(date, location);

		for (Vehicle v : arr ) {
			System.out.println("VID: "+ v.getVid() + " Vtype: " + v.getVtname() + "Location: " + v.getLocation());
		}

        HashMap<String, Integer> rentedByVehicles = delegate.getMapOfTotalRentedByVType("return", date, location);
        Set<String> keys = rentedByVehicles.keySet();
        System.out.println("Vehicles by Category:");
        for (String key: keys) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + rentedByVehicles.get(key));
        }

        HashMap<String, Integer> revByType = delegate.revByType("return", date, location);
        Set<String> keysRBT = revByType.keySet();
        System.out.println("Revenue by Type:");
        for (String key: keysRBT) {
            // System.out.println("Key: " + key + ", Val: " + rentedByVehicles.get(key));
            System.out.println(key + ": " + revByType.get(key));
        }

        Integer totalRent = delegate.getTotalOfRented("return", date, location);
        System.out.println("Total Daily Returned: " + totalRent);

        Integer totalRev = delegate.getTotalRev("return", date, location);
        System.out.println("Total Daily Revenue: " + totalRev);

		// countType(arr);
	}

	
	private void handleQuitOption() {
		System.out.println("Good Bye!");
		
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("IOException!");
			}
		}
		
		delegate.terminalTransactionsFinished();
	}

	public void countType(ArrayList<Vehicle> arr) {
		int truck = 0;
		int suv = 0;
		int fullsize = 0;
		int economy = 0;
		int compact = 0;
		int midsize = 0;
		int standard = 0;
		for (Vehicle rs: arr) {
			System.out.println(rs.getVtname());
			String r = rs.getVtname();
			if (r.equals("Truck")) {
				truck++;
			} else if (r.equals("Suv")) {
				suv++;
			} else if (r.equals("Full-size")) {
				fullsize++;
			} else if (r.equals("Economy")) {
				economy++;
			} else if (r.equals("Compact")) {
				compact++;
			} else if (r.equals("Mid-size")) {
				midsize++;
			} else if (r.equals("Standard")) {
				standard++;
		}
		}

		System.out.println("Truck: " + truck);
		System.out.println("Suv: " + suv);
		System.out.println("Fullsize: " + fullsize);
		System.out.println("Economy: " + economy);
		System.out.println("Compact: " + compact);
		System.out.println("Midsize: " + midsize);
		System.out.println("Standard: " + standard);
	}

	
	private int readInteger(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = EMPTY_INPUT;
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return input;
	}
	
	private String readLine() {
		String result = null;
		try {
			result = bufferedReader.readLine();
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}
}
