package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ca.ubc.cs304.model.*;
import oracle.sql.DATE;

import javax.sound.midi.Soundbank;
import javax.xml.transform.Result;

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

    public Integer getTotalRev (String typ, String date, String specifiedBranch) {

        String sql = "";
        Integer number = 0;

        if (specifiedBranch == null){
            sql = "SELECT SUM(re.valueR) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                    "re.dateR = '" + date + "'";
        }else {
            sql = "SELECT SUM(re.valueR) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                    "re.dateR = '" + date +"' AND v.location = '"+ specifiedBranch + "'";
        }

        // sql = "SELECT * FROM vehicles v, rentals r";
        System.out.println(sql);

        try {

            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            ResultSetMetaData rmd = rs.getMetaData();
            // System.out.println(rmd.getColumnName(2));

            while (rs.next()) {
                // System.out.println("running rs");
                number = rs.getInt("ADR");
                // System.out.println(number);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return number;
    }

    public HashMap<String, Integer> revByBranch (String typ, String date, String specifiedBranch) {

        HashMap<String, Integer> res = new HashMap<>();
        String sql = "";

        if (specifiedBranch == null) {
            sql = "SELECT v.location, SUM(re.valueR) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                    "re.dateR = '" + date + "' GROUP BY v.location";
        } else {
            sql = "SELECT v.location, SUM(re.valueR) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                    "re.dateR = '" + date + "' AND v.location = '" + specifiedBranch + "' GROUP BY v.location";
        }

        // sql = "SELECT * FROM vehicles v, rentals r";
        System.out.println(sql);

        try {

            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            ResultSetMetaData rmd = rs.getMetaData();
            // System.out.println(rmd.getColumnName(2));

            while (rs.next()) {
                // System.out.println("running rs");
                String key = rs.getString("location");
                Integer number = rs.getInt("ADR");
                // System.out.println(key);
                // System.out.println(number);
                res.put(key, number);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return res;
    }

    public HashMap<String, Integer> revByType (String typ, String date, String specifiedBranch) {

        HashMap<String, Integer> res = new HashMap<>();
        String sql = "";

        if (specifiedBranch == null) {
            sql = "SELECT vtname, SUM(re.valueR) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                    "re.dateR = '" + date + "' GROUP BY v.vtname";
        } else {
            sql = "SELECT vtname, SUM(re.valueR) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                    "re.dateR = '" + date + "' AND v.location = '" + specifiedBranch + "' GROUP BY v.vtname";

            // sql = "SELECT * FROM vehicles v, rentals r";
            System.out.println(sql);
        }

        try {

            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            ResultSetMetaData rmd = rs.getMetaData();
            // System.out.println(rmd.getColumnName(2));

            while (rs.next()) {
                // System.out.println("running rs");
                String key = rs.getString("vtname");
                Integer number = rs.getInt("ADR");
                // System.out.println(number);
                res.put(key, number);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return res;
	}

    public Integer getTotalOfRented (String typ, String date, String specifiedBranch) {

        String sql = "";
        Integer number = 0;

        if (typ.equals("rent")) {
            if (specifiedBranch == null) {
                sql = "SELECT COUNT(v.vlicense) AS Adr FROM vehicles v, rentals r WHERE v.vlicense = r.vlicense AND r.fromDate = '" +
                        date + "'";
            } else {
                sql = "SELECT COUNT(v.vlicense) AS Adr FROM vehicles v, rentals r WHERE v.vlicense = r.vlicense AND r.fromDate = '" +
                        date + "' AND v.location = '" + specifiedBranch + "'";
            }
        }
        else if (typ.equals("return")) {
            if (specifiedBranch == null){
                sql = "SELECT COUNT(v.vlicense) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                        "re.dateR = '" + date + "'";
            }else {
                sql = "SELECT COUNT(v.vlicense) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                        "re.dateR = '" + date +"' AND v.location = '"+ specifiedBranch + "'";
            }
        }

        // sql = "SELECT * FROM vehicles v, rentals r";
        System.out.println(sql);

        try {

            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            ResultSetMetaData rmd = rs.getMetaData();
            // System.out.println(rmd.getColumnName(2));

            while (rs.next()) {
                // System.out.println("running rs");
                number = rs.getInt("ADR");
                // System.out.println(number);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return number;
    }

    public HashMap<String, Integer> getMapOfTotalRentedByBranch (String typ, String date, String specifiedBranch) {

        HashMap<String, Integer> res = new HashMap<>();
        String sql = "";

        if (typ.equals("rent")) {
            if (specifiedBranch == null) {
                sql = "SELECT v.location, COUNT(v.vlicense) AS Adr FROM vehicles v, rentals r WHERE v.vlicense = r.vlicense AND r.fromDate = '" +
                        date + "' GROUP BY v.location";
            }
        }
        else if (typ.equals("return")) {
            if (specifiedBranch == null){
                sql = "SELECT v.location, COUNT(v.vlicense) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                        "re.dateR = '" + date + "' GROUP BY v.location";
            }
        }

        // sql = "SELECT * FROM vehicles v, rentals r";
        System.out.println(sql);

        try {

            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            ResultSetMetaData rmd = rs.getMetaData();
            // System.out.println(rmd.getColumnName(2));

            while (rs.next()) {
                // System.out.println("running rs");
                String key = rs.getString("location");
                Integer number = rs.getInt("ADR");
                // System.out.println(key);
                // System.out.println(number);
                res.put(key, number);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return res;
    }

    public HashMap<String, Integer> getMapOfTotalRentedByVType (String typ, String date, String specifiedBranch) {

        HashMap<String, Integer> res = new HashMap<>();
        String sql = "";

        if (typ.equals("rent")) {
            if (specifiedBranch == null) {
                sql = "SELECT vtname, COUNT(v.vlicense) AS Adr FROM vehicles v, rentals r WHERE v.vlicense = r.vlicense AND r.fromDate = '" +
                        date + "' GROUP BY v.vtname";
            } else {
                sql = "SELECT vtname, COUNT(v.vlicense) AS Adr FROM vehicles v, rentals r WHERE v.vlicense = r.vlicense AND r.fromDate = '" +
                        date + "' AND v.location = '" + specifiedBranch + "' GROUP by v.vtname";
            }
        }
        else if (typ.equals("return")) {
            if (specifiedBranch == null){
                sql = "SELECT vtname, COUNT(v.vlicense) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                        "re.dateR = '" + date + "' GROUP BY v.vtname";
            }else {
                sql = "SELECT vtname, COUNT(v.vlicense) AS Adr FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                        "re.dateR = '" + date +"' AND v.location = '"+ specifiedBranch + "' GROUP BY v.vtname";
            }
        }

        // sql = "SELECT * FROM vehicles v, rentals r";
        System.out.println(sql);

        try {

            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            ResultSetMetaData rmd = rs.getMetaData();
            // System.out.println(rmd.getColumnName(2));

            while (rs.next()) {
                // System.out.println("running rs");
                String key = rs.getString("vtname");
                Integer number = rs.getInt("ADR");
                // System.out.println(number);
                res.put(key, number);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return res;
    }

    public ArrayList<Vehicle> dailyRentalBranch(String date, String specifiedBranch) {

	    ArrayList<Vehicle> cars = new ArrayList<>();
        String sql;

        if (specifiedBranch == null){
            sql = "SELECT * FROM vehicles v, rentals r WHERE v.vlicense = r.vlicense AND r.fromDate = '" +
                    date + "' ORDER BY v.location, v.vtname";
        }else {
            sql = "SELECT * FROM vehicles v, rentals r WHERE v.vlicense = r.vlicense AND r.fromDate = '" +
                    date + "' AND v.location = '" + specifiedBranch + "' ORDER BY v.location, v.vtname";
        }
        // sql = "SELECT * FROM vehicles v, rentals r";
        System.out.println(sql);

        try {

            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while(rs.next()) {
                // System.out.println("running rs");
                Vehicle vehicle = new Vehicle(
                        rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("odometer"),
                        rs.getString("status"),
                        rs.getString("vtname"),
                        rs.getString("location"));
                cars.add(vehicle);
//                System.out.println(rs.getInt("rid"));
//                System.out.println(rs.getString("vlicense"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        System.out.println("Car Size: " + cars.size());
        return cars;
    }

	public ArrayList<Vehicle> dailyReturnBranch(String date, String specifiedBranch) {
        ArrayList<Vehicle> cars = new ArrayList<>();
        String sql;

        if (specifiedBranch == null){
            sql = "SELECT * FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                    "re.dateR = '" + date + "' ORDER BY v.location, v.vtname";
        }else {
            sql = "SELECT * FROM vehicles v , rentals r, return re WHERE v.vlicense = r.vlicense AND r.rid = re.rid AND " +
                    "re.dateR = '" + date +"' AND v.location = '"+ specifiedBranch + "' ORDER BY v.location, v.vtname";
        }
        System.out.println(sql);

        try {

            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while(rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("odometer"),
                        rs.getString("status"),
                        rs.getString("vtname"),
                        rs.getString("location"));
                cars.add(vehicle);
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        System.out.println(cars.size());
        return cars;
    }

    public ArrayList<Vehicle> viewVehicle(String vtname, String location, Date time) {

		ArrayList<Vehicle> result = new ArrayList<>();
		String sql = "SELECT * FROM vehicles WHERE status = 'Available' ";;

		if (!vtname.isEmpty()) {
			sql = sql + "AND vtname = " + "'" + vtname + "'";
		}
		if (!location.isEmpty() )  {
			sql = sql+ "AND location = " + "'" +location + "'";
		}
		if (!(time == null))  {
			sql = sql;
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

	public Boolean reserveVehicle (Reservation res) {
		try {
			// TODO
			PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation VALUES (?,?,?,?,?,?,?)");
			// check ordering
			ps.setInt(1, res.getConfNo());
			ps.setString(2, res.getVtname());
			ps.setInt(3, res.getdLicense());
			ps.setString(4, res.getFromDate());
			ps.setInt(5, res.getFromTime());
			ps.setString(6, res.getToDate());
			ps.setInt(7, res.getToTime());

			int row = ps.executeUpdate();

			if(row!= 0 ) {
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

	public Boolean existVehicleType(String vtname) {

		try {
			String sql  = "SELECT * FROM vehicles WHERE vtname = '" + vtname + "'";
			PreparedStatement ps = connection.prepareStatement(sql);
			// check ordering
			ResultSet rs =  ps.executeQuery();

			if( rs.next() ) {
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



	public Boolean rentVehicle(Rental rental) {
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
			if(rental.getConfNo() == 0) {
				ps.setNull(9, Types.INTEGER);
			}else {
				ps.setInt(9, rental.getConfNo());
			}


			int row = ps.executeUpdate();

			if (row != 0) {
				updateStatus(rental.getvLicense());
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

	public void updateStatus(String vlicense) {

		try {
			String sql = "UPDATE vehicles SET status = 'Rented' WHERE vlicense =" + "'" + vlicense + "'";

			PreparedStatement ps2 = connection.prepareStatement(sql);

			int row = ps2.executeUpdate();

			if (row == 0) {
				System.out.println("Status updated failed");
			}

			connection.commit();

			ps2.close();

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public void returnVehicle(Return r) {

		try {
			if(!existRental(r.getRid())) {
				return;
			}

			PreparedStatement ps = connection.prepareStatement("INSERT INTO return VALUES (?,?,?,?,?,?)");
			ps.setInt(1, r.getRid());
			ps.setString(2, r.getDate());
			ps.setInt(3, r.getTime());
			ps.setInt(4, r.getOdometer());
			ps.setString(5, r.getFullTank());
			ps.setInt(6, r.getValue());

			ps.executeUpdate();

			connection.commit();

			ps.close();

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public Boolean existRental(int id){
		try {
			String sql = "SELECT * FROM rentals WHERE rid = " + "'" +id + "'";
			System.out.println(sql);
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet set = ps.executeQuery();

			if (set.next()) {
				System.out.println("Rental exists");
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
	public void viewAll(){

		try {
			String sql = "SELECT table_name from user_tables" ;

			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet result = ps.executeQuery();

			while (result.next()) {
				System.out.println(result.getString("table_name"));
			}



			connection.commit();

			ps.close();

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

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
