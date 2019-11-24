package ca.ubc.cs304.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;

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
			System.out.println("4. Create new customer");
			System.out.println("5. Return a vehicle");
			System.out.println("7. View all table");
			System.out.println("10. Quit");
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
						handleInsertCustomer();
						break;
					case 5:
						handleReturn();
						break;
					case 10:
						handleQuitOption();
						break;
					case 7:
						viewTable();
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
