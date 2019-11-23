package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;

import ca.ubc.cs304.model.*;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	
	private Connection connection = null;
	
	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public ArrayList<Vehicle> viewVehicle(String vtname, String location, String time) {
		// TODo
		ArrayList<Vehicle> result = new ArrayList<>();
		String sql = "SELECT * FROM vehicles WHERE status = 'Available' ";;

		if (!vtname.isEmpty()) {
			sql = sql + "AND vtname = " + "'" + vtname + "'";
		}
		if (!location.isEmpty() )  {
			sql = sql+ "AND location = " + "'" +location + "'";
		}
		if (!time.isEmpty())  {
			sql = sql + "AND time = " + "'" + time + "'";
		}
		System.out.println(sql);

		try {
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				Vehicle vehicle = new Vehicle(rs.getInt("vid"),
						rs.getString("vlicense"),
						rs.getString("odometer"),
						rs.getString("status"),
						rs.getString("vtname"),
						rs.getString("location"));
				result.add(vehicle);
			}
			connection.commit();

			ps.close();
			return result;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		return result;


	}

	public Boolean existingCustomer(int dLicense) {

		try {
			System.out.println("in existing customers");
			PreparedStatement ps = connection.prepareStatement(" SELECT * FROM customer WHERE dLicense = ?");
			ps.setInt(1, dLicense);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				System.out.println("Customer exists and is verified");
				return true;
			}
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		return false;
	}

	public void insertCustomer(Customer customer){

	try{
		System.out.println("in insert customers");
		PreparedStatement ps = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?)");

		ps.setInt(1, customer.getPhoneNumber());
		ps.setString(2, customer.getName());
		ps.setString(3, customer.getAddress());
		ps.setInt(4, customer.getdLicense());

		ps.executeUpdate();
		System.out.println("created new customer");

		connection.commit();

		ps.close();

	} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();

		}
	}

	public void reserveVehicle (Reservation res) {
		try {
			// TODO
			PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation VALUES (?,?,?,to_date(?,'YYYY/MM/DD'),?,?,?)");
			// check ordering
			ps.setInt(1, res.getConfNo());
			ps.setString(2, res.getVtname());
			ps.setInt(3, res.getdLicense());
			ps.setString(4, res.getFromDate());
			ps.setInt(5, res.getFromTime());
			ps.setString(6, res.getToDate());
			ps.setInt(7, res.getToTime());

			ps.executeUpdate();

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

	}

	public Boolean validVlicense(String id) {
		String sql = "SELECT * FROM vehicles WHERE status = 'Available' AND vlicense = " + "'"+ id + "'";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				System.out.println("available vehicle");
				return true;
			}
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

		return false;

	}



	public void rentVehicle(Rental rental) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO rentals VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, rental.getRid());
			ps.setString(2, rental.getvLicense());
			ps.setInt(3, rental.getdLicense());
			ps.setString(4, rental.getFromDate());
			ps.setInt(5, rental.getFromTime());
			ps.setString(6, rental.getToDate());
			ps.setInt(7, rental.getToTime());
			ps.setInt(8, rental.getOdometer());
			//TODO set to null
			if(rental.getConfNo() == 0) {
				ps.setNull(9, Types.INTEGER);
			}else {
				ps.setNull(9, rental.getConfNo());
			}

			PreparedStatement ps2 = connection.prepareStatement("UPDATE vehicles SET status = 'Rented' WHERE vlicense = ?");
			ps2.setString(1,rental.getvLicense());

			ps.executeUpdate();
			ps2.executeUpdate();

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}


	}

	public void returnVehicle() {

	}

	public void deleteBranch(int branchId) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
			ps.setInt(1, branchId);
			
			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
			}
			
			connection.commit();
	
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public void insertBranch(BranchModel model) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
			ps.setInt(1, model.getId());
			ps.setString(2, model.getName());
			ps.setString(3, model.getAddress());
			ps.setString(4, model.getCity());
			if (model.getPhoneNumber() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getPhoneNumber());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public BranchModel[] getBranchInfo() {
		ArrayList<BranchModel> result = new ArrayList<BranchModel>();
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
		
//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}
			
			while(rs.next()) {
				BranchModel model = new BranchModel(rs.getString("branch_addr"),
													rs.getString("branch_city"),
													rs.getInt("branch_id"),
													rs.getString("branch_name"),
													rs.getInt("branch_phone"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}	
		
		return result.toArray(new BranchModel[result.size()]);
	}
	
	public void updateBranch(int id, String name) {
		try {
		  PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
		  ps.setString(1, name);
		  ps.setInt(2, id);
		
		  int rowCount = ps.executeUpdate();
		  if (rowCount == 0) {
		      System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
		  }
	
		  connection.commit();
		  
		  ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}	
	}
	
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}
	
			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);
	
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();	
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
}
