package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.*;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import JAVAFX.GUI;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class SuperCar implements LoginWindowDelegate, TerminalTransactionsDelegate {
	public DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;

	public SuperCar() {
		dbHandler = new DatabaseConnectionHandler();
	}
	
	public void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);
	}
	
	/**
	 * LoginWindowDelegate Implementation
	 * 
     * connects to Oracle database with supplied username and password
     */ 
	public void login(String username, String password) {
		boolean didConnect = dbHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();

			TerminalTransactions transaction = new TerminalTransactions();
			transaction.showMainMenu(this);
		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}
	
	/**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Insert a branch with the given info
	 */
    public void insertBranch(BranchModel model) {
    	dbHandler.insertBranch(model);
    }

    /**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Delete branch with given branch ID.
	 */ 
    public void deleteBranch(int branchId) {
    	dbHandler.deleteBranch(branchId);
    }
    
    /**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Update the branch name for a specific ID
	 */

    public void updateBranch(int branchId, String name) {
    	dbHandler.updateBranch(branchId, name);
    }

	@Override
		public Boolean existingCustomer(int dLicense) {
		return dbHandler.existingCustomer(dLicense);
	}

	@Override
	public void insertCustomer(Customer customer) {
		dbHandler.insertCustomer(customer);
	}

	@Override
	public Boolean reserveVehicle(Reservation reserve) {
		return dbHandler.reserveVehicle(reserve);
	}

	@Override
	public ArrayList<Vehicle> viewVehicle(String vtname, String location, Date time) {
		return dbHandler.viewVehicle(vtname, location, time);
	}

	@Override
	public Boolean rentVehicle(Rental rent) {
		return dbHandler.rentVehicle(rent);
	}

	@Override
	public Boolean validVlicense(String id) {
		return dbHandler.validVlicense(id);
	}

	@Override
	public void returnVehicle(Return r) {
		dbHandler.returnVehicle(r);
	}

	@Override
	public void viewAll() {
		dbHandler.viewAll();
	}

	@Override
	public Boolean existVehicleType(String vtname) {
		return dbHandler.existVehicleType(vtname);
	}


	/**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Displays information about varies bank branches.
	 */
    public void showBranch() {
    	BranchModel[] models = dbHandler.getBranchInfo();
    	
    	for (int i = 0; i < models.length; i++) {
    		BranchModel model = models[i];
    		
    		// simplified output formatting; truncation may occur
    		System.out.printf("%-10.10s", model.getId());
    		System.out.printf("%-20.20s", model.getName());
    		if (model.getAddress() == null) {
    			System.out.printf("%-20.20s", " ");
    		} else {
    			System.out.printf("%-20.20s", model.getAddress());
    		}
    		System.out.printf("%-15.15s", model.getCity());
    		if (model.getPhoneNumber() == 0) {
    			System.out.printf("%-15.15s", " ");
    		} else {
    			System.out.printf("%-15.15s", model.getPhoneNumber());
    		}
    		
    		System.out.println();
    	}
    }
	
    /**
	 * TerminalTransactionsDelegate Implementation
	 * 
     * The TerminalTransaction instance tells us that it is done with what it's 
     * doing so we are cleaning up the connection since it's no longer needed.
     */ 
    public void terminalTransactionsFinished() {
    	dbHandler.close();
    	dbHandler = null;
    	
    	System.exit(0);
    }

	@Override
	public ArrayList<Vehicle> dailyRental(String d, String location) {
		return dbHandler.dailyRentalBranch( d, location);
	}

	@Override
	public ArrayList<Vehicle> dailyReturn(String  d, String location) {
    	return dbHandler.dailyReturnBranch(d, location);

	}

    @Override
    public HashMap<String, Integer> getMapOfTotalRentedByVType (String typ, String date, String specifiedBranch) {
        return dbHandler.getMapOfTotalRentedByVType(typ, date, specifiedBranch);
    }

    @Override
    public HashMap<String, Integer> getMapOfTotalRentedByBranch (String typ, String date, String specifiedBranch) {
        return dbHandler.getMapOfTotalRentedByBranch(typ, date, specifiedBranch);
    }

    @Override
    public Integer getTotalOfRented (String typ, String date, String specifiedBranch) {
        return dbHandler.getTotalOfRented(typ, date, specifiedBranch);
    }

    @Override
    public HashMap<String, Integer> revByType (String typ, String date, String specifiedBranch) {
        return dbHandler.revByType(typ, date, specifiedBranch);
    }

    @Override
    public HashMap<String, Integer> revByBranch (String typ, String date, String specifiedBranch) {
        return dbHandler.revByBranch(typ, date, specifiedBranch);
    }

    @Override
    public Integer getTotalRev (String typ, String date, String specifiedBranch) {
        return dbHandler.getTotalRev(typ, date, specifiedBranch);
    }


    /**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		SuperCar superCar = new SuperCar();
		superCar.start();
		GUI gui = new GUI();
		gui.go(args,superCar);
	}
}
