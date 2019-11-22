package ca.ubc.cs304.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.Customer;
import ca.ubc.cs304.model.Reservation;
import ca.ubc.cs304.model.Vehicle;

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
			System.out.println("3. Update branch name");
			System.out.println("4. Show branch");
			System.out.println("5. Quit");
			System.out.println("6. Quit");
			System.out.println("7. Insert Customer");
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
						handleDeleteOption();
						break;
					case 4:
						handleUpdateOption();
						break;
					case 5:
						delegate.showBranch();
						break;
					case 6:
						handleQuitOption();
						break;
					case 7:
						handleInsertCustomer();
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
		String time = null;

		System.out.println("Enter type");
		vtname = readLine().trim();

		System.out.println("Enter location");
		location = readLine().trim();

		System.out.println("Enter time");
		time = readLine().trim();

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
		String fromTime = null;
		String toDate = null;
		String toTime = null;
		int confNo = INVALID_INPUT;
		while (confNo == INVALID_INPUT) {
			System.out.print("Enter the details to the reservation ");
			confNo = readInteger(false);
		}
		while (vtname == null || vtname.length() <= 0) {
			System.out.print("Please enter the vehicle type name: ");
			vtname = readLine().trim();
		}

		while (fromDate == null || fromDate.length() <= 0) {
			System.out.print("Please enter your fromDate: ");
			fromDate = readLine().trim();
		}

		while (fromTime == null || fromTime.length() <= 0) {
			System.out.print("Please enter your fromTime: ");
			fromTime = readLine().trim();
		}

		while (toDate == null || toDate.length() <= 0) {
			System.out.print("Please enter your toDate: ");
			toDate = readLine().trim();
		}
		while (toTime == null || toTime.length() <= 0) {
			System.out.print("Please enter your toTime: ");
			toTime = readLine().trim();
		}
		Reservation reservation = new Reservation(confNo, vtname, dLicense, fromDate,fromTime, toDate,toTime);
		delegate.reserveVehicle(reservation);

	}

	private void handleDeleteOption() {
		int branchId = INVALID_INPUT;
		while (branchId == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to delete: ");
			branchId = readInteger(false);
			if (branchId != INVALID_INPUT) {
				delegate.deleteBranch(branchId);
			}
		}
	}
	
	private void handleInsertOption() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to insert: ");
			id = readInteger(false);
		}
		
		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the branch name you wish to insert: ");
			name = readLine().trim();
		}
		
		// branch address is allowed to be null so we don't need to repeatedly ask for the address
		System.out.print("Please enter the branch address you wish to insert: ");
		String address = readLine().trim();
		if (address.length() == 0) {
			address = null;
		}
		
		String city = null;
		while (city == null || city.length() <= 0) {
			System.out.print("Please enter the branch city you wish to insert: ");
			city = readLine().trim();
		}
		
		int phoneNumber = INVALID_INPUT;
		while (phoneNumber == INVALID_INPUT) {
			System.out.print("Please enter the branch phone number you wish to insert: ");
			phoneNumber = readInteger(true);
		}
		
		BranchModel model = new BranchModel(address,
											city,
											id,
											name,
											phoneNumber);
		delegate.insertBranch(model);
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
	
	private void handleUpdateOption() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to update: ");
			id = readInteger(false);
		}
		
		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the branch name you wish to update: ");
			name = readLine().trim();
		}

		delegate.updateBranch(id, name);
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
