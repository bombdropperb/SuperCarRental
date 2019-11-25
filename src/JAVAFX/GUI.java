package JAVAFX;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import ca.ubc.cs304.controller.SuperCar;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.Customer;
import ca.ubc.cs304.model.Rental;
import ca.ubc.cs304.model.Reservation;
import ca.ubc.cs304.model.Return;
import ca.ubc.cs304.model.Vehicle;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.Customer;
import ca.ubc.cs304.model.Reservation;
import ca.ubc.cs304.model.Vehicle;

public class GUI extends Application {
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final int INVALID_INPUT = Integer.MIN_VALUE;
	private static final int EMPTY_INPUT = 0;
	
	private BufferedReader bufferedReader = null;
	private TerminalTransactionsDelegate delegate = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	// create a tile pane 
    	//Creating a GridPane container
    	GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);
        Label label1 = new Label("1. View Vehicle\n"
        		+ "2. Reserve Vehicle\n"
        		+ "3. Rent a Vehicle\n"
        		+ "4. Return a Vehicle\n"
        		+ "5. Create new customer\n"
        		+ "6. daily rental log\n"
        		+ "7. daily rental log for branch\n"
        		+ "8. daily return log\n"
        		+ "9. daily return log for branch\n"
        		+ "10.Quit\n");
        GridPane.setConstraints(label1, 0, 0);
        label1.setFont(new Font("Arial", 20));
        // create a label to show the input in text dialog 
        Label label2 = new Label("Please enter your choice:"); 
        GridPane.setConstraints(label2, 0, 1);
        label2.setFont(new Font("Arial", 20));
        TextField tf = new TextField ();
        GridPane.setConstraints(tf, 1, 1);
     // create a button 
        Button d = new Button("confirm");
        GridPane.setConstraints(d, 2, 1);
        Label label3 = new Label("Result:");
        label3.setFont(new Font("Arial", 20));
        GridPane.setConstraints(label3, 0, 2);
        
        //TextInputDialog td = new TextInputDialog();
     
     // create a event handler 
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	// show the text input dialog 
                //td.showAndWait(); 
                int choice = Integer.parseInt(tf.getText());
                // set the text of the label 
                //l.setText(td.getEditor().getText()); 
                if (choice != INVALID_INPUT) {
    				switch (choice) {
    					case 1:
    						GUIhandleView();
    						break;
    					case 2:
    						GUIhandleReservation();
    						break;
    					case 3:
    						GUIhandleRental();
    						break;
    					case 4:
    						GUIhandleReturn();
    						break;
    					case 5:
    						GUIhandleInsertCustomer();
    						break;
    					case 6:
    						GUIhandleDRental();
    						break;
    					case 7:
    						GUIhandleDBRental();
    						break;
    					case 8:
    						GUIhandleDReturn();
    						break;
    					case 9:
    						GUIhandleDBReturn();
    						break;
    					case 10:
    						GUIhandleQuitOption();
    						break;
    					default:
    						GUIInvalidInputHandler();
    						break;
    				}
                } 
            } 
       };
     // set on action of event 
        d.setOnAction(event); 
        
        grid.getChildren().add(label1); 
     // add button and label 
        grid.getChildren().add(label2); 
        grid.getChildren().add(tf); 
        grid.getChildren().add(d); 
        grid.getChildren().add(label3); 
        
     // create a scene 
        Scene sc = new Scene(grid, 500, 500); 
  
        // set the scene 
        primaryStage.setTitle("SuperCar GUI Interface");
        primaryStage.setScene(sc); 
        primaryStage.show();
    }

    
    public void go(String[] args,SuperCar supercar) {
    	this.delegate=supercar;
    	launch(args);
    }
    
    private void GUIhandleView() {

		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("Type:"); 
        GridPane.setConstraints(label1, 0, 0 );
        
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("Location:"); 
        GridPane.setConstraints(label2, 0, 1 );
        
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("Year:"); 
        GridPane.setConstraints(label3, 0, 2 );
        
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        Label label4 = new Label("Month:"); 
        GridPane.setConstraints(label4, 0, 3 );
        
        TextField tf4 = new TextField ();
        GridPane.setConstraints(tf4, 1, 3);
        
        Label label5 = new Label("Date:"); 
        GridPane.setConstraints(label5, 0, 4 );
        
        TextField tf5 = new TextField ();
        GridPane.setConstraints(tf5, 1, 4);
        
        
     // create a button 
        Button d = new Button("submit");
        GridPane.setConstraints(d, 2, 5);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	String vtname = tf1.getText();
        		String location = tf2.getText();
        		int year = Integer.parseInt(tf3.getText());
        		int month = Integer.parseInt(tf4.getText());
        		int date = Integer.parseInt(tf5.getText());
        		Date time = new Date(year, month, date);
        		String ret="";
        		
        		ArrayList<Vehicle> arr = delegate.viewVehicle(vtname, location, time);
        		
        		for (Vehicle v: arr) {
        			ret+="vid: " + v.getVid()+ "vlicense: " + v.getVlicense()+ "odometer: " +v.getOdometer() + "status: "
        					+ v.getStatus()+ "vtname: "+ v.getVtname()+  "location: " +v.getLocation();
        		}
        		
        		Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Result");
        		alert.setHeaderText("Result Information:");
        		alert.setContentText(ret);
        		alert.show();
            }
        };
        
        
        d.setOnAction(event); 
        
        grid.getChildren().add(label1);
        grid.getChildren().add(label2);
        grid.getChildren().add(label3);
        grid.getChildren().add(label4);
        grid.getChildren().add(label5);
        grid.getChildren().add(tf1);
        grid.getChildren().add(tf2);
        grid.getChildren().add(tf3);
        grid.getChildren().add(tf4);
        grid.getChildren().add(tf5);
        grid.getChildren().add(d);
		
		Scene sc = new Scene(grid, 500, 500); 
		  
        // set the scene 
		Stage stage = new Stage();
        stage.setTitle("View Vehicle");
        stage.setScene(sc); 
        stage.show();
		

	}
    
    private void GUIhandleInsertCustomer() {
    	GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("dlicense:"); 
        GridPane.setConstraints(label1, 0, 0 );
        
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("phone number:"); 
        GridPane.setConstraints(label2, 0, 1 );
        
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("name:"); 
        GridPane.setConstraints(label3, 0, 2 );
        
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        Label label4 = new Label("address:"); 
        GridPane.setConstraints(label4, 0, 3 );
        
        TextField tf4 = new TextField ();
        GridPane.setConstraints(tf4, 1, 3);
        
        
     // create a button 
        Button d = new Button("submit");
        GridPane.setConstraints(d, 3, 3);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	int dLicense;
        		int phoneNumber;
        		String name;
        		String address;

        		dLicense = Integer.parseInt(tf1.getText());

        		phoneNumber= Integer.parseInt(tf2.getText());

        		name = tf3.getText();

        		address = tf4.getText();

        		Customer customer = new Customer(phoneNumber, name, address,dLicense);
        		delegate.insertCustomer(customer);
        		
            }
        };
        
        
        d.setOnAction(event); 
        
        grid.getChildren().add(label1);
        grid.getChildren().add(label2);
        grid.getChildren().add(label3);
        grid.getChildren().add(label4);
        grid.getChildren().add(tf1);
        grid.getChildren().add(tf2);
        grid.getChildren().add(tf3);
        grid.getChildren().add(tf4);
        grid.getChildren().add(d);
		
		Scene sc = new Scene(grid, 500, 500); 
		  
        // set the scene 
		Stage stage = new Stage();
        stage.setTitle("Insert Customer");
        stage.setScene(sc); 
        stage.show();
	}

	private void GUIhandleReservation() {
		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("dlicense:"); 
        GridPane.setConstraints(label1, 0, 0 );
        
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("conf number:"); 
        GridPane.setConstraints(label2, 0, 1 );
        
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("vehicle type name:"); 
        GridPane.setConstraints(label3, 0, 2 );
        
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        Label label4 = new Label("From Date:"); 
        GridPane.setConstraints(label4, 0, 3 );
        
        TextField tf4 = new TextField ();
        GridPane.setConstraints(tf4, 1, 3);
        
        Label label5 = new Label("From Time:"); 
        GridPane.setConstraints(label5, 0, 4 );
        
        TextField tf5 = new TextField ();
        GridPane.setConstraints(tf5, 1, 4);
        
        Label label6 = new Label("To Date:"); 
        GridPane.setConstraints(label6, 0, 5 );
        
        TextField tf6 = new TextField ();
        GridPane.setConstraints(tf6, 1, 5);
        
        Label label7 = new Label("To time:"); 
        GridPane.setConstraints(label7, 0, 6 );
        
        TextField tf7 = new TextField ();
        GridPane.setConstraints(tf7, 1, 6);
        
        
     // create a button 
        Button d = new Button("submit");
        GridPane.setConstraints(d, 3, 6);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	int dLicense = 0;
        		dLicense = Integer.parseInt(tf1.getText());

        		Boolean exist =  delegate.existingCustomer(dLicense);
        		if (!exist){
        			Alert alert = new Alert(AlertType.INFORMATION);
            		alert.setTitle("Result");
            		alert.setHeaderText("Result Information:");
            		alert.setContentText("Customer does not exist");
            		alert.show();
        			return;
        		}


        		String vtname = null;
        		String fromDate = null;
        		String toDate = null;
        		int confNo = INVALID_INPUT;
        		
        		confNo = Integer.parseInt(tf2.getText());
        		
        		vtname = tf3.getText();
        		
        		fromDate = tf4.getText();

        		int fromTime = Integer.parseInt(tf5.getText());

        		toDate = tf6.getText();
        		
        		int toTime = Integer.parseInt(tf7.getText());
        		
        		Reservation reservation = new Reservation(confNo, vtname, dLicense, fromDate,fromTime, toDate,toTime);
        		delegate.reserveVehicle(reservation);
        		
            }
        };
        
        
        d.setOnAction(event); 
        
        grid.getChildren().add(label1);
        grid.getChildren().add(label2);
        grid.getChildren().add(label3);
        grid.getChildren().add(label4);
        grid.getChildren().add(label5);
        grid.getChildren().add(label6);
        grid.getChildren().add(label7);
        grid.getChildren().add(tf1);
        grid.getChildren().add(tf2);
        grid.getChildren().add(tf3);
        grid.getChildren().add(tf4);
        grid.getChildren().add(tf5);
        grid.getChildren().add(tf6);
        grid.getChildren().add(tf7);
        grid.getChildren().add(d);
		
		Scene sc = new Scene(grid, 500, 500); 
		  
        // set the scene 
		Stage stage = new Stage();
        stage.setTitle("Reservation");
        stage.setScene(sc); 
        stage.show();

	}

	private void GUIhandleRental() {
		
		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("rid:"); 
        GridPane.setConstraints(label1, 0, 0 );
        
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("vehicle license:"); 
        GridPane.setConstraints(label2, 0, 1 );
        
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("driver license:"); 
        GridPane.setConstraints(label3, 0, 2 );
        
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        Label label4 = new Label("From Date:"); 
        GridPane.setConstraints(label4, 0, 3 );
        
        TextField tf4 = new TextField ();
        GridPane.setConstraints(tf4, 1, 3);
        
        Label label5 = new Label("From Time:"); 
        GridPane.setConstraints(label5, 0, 4 );
        
        TextField tf5 = new TextField ();
        GridPane.setConstraints(tf5, 1, 4);
        
        Label label6 = new Label("To Date:"); 
        GridPane.setConstraints(label6, 0, 5 );
        
        TextField tf6 = new TextField ();
        GridPane.setConstraints(tf6, 1, 5);
        
        Label label7 = new Label("To time:"); 
        GridPane.setConstraints(label7, 0, 6 );
        
        TextField tf7 = new TextField ();
        GridPane.setConstraints(tf7, 1, 6);
        
        Label label8 = new Label("odometer:"); 
        GridPane.setConstraints(label8, 0, 7 );
        
        TextField tf8 = new TextField ();
        GridPane.setConstraints(tf8, 1, 7);
        
        Label label9 = new Label("confo no:"); 
        GridPane.setConstraints(label9, 0, 8 );
        
        TextField tf9 = new TextField ();
        GridPane.setConstraints(tf9, 1, 8);
        
        
     // create a button 
        Button d = new Button("submit");
        GridPane.setConstraints(d, 3, 9);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	int rid = Integer.parseInt(tf1.getText());
            	String vLicense = tf2.getText();
            	int dLicense = Integer.parseInt(tf3.getText());
            	String fromDate = tf4.getText();
            	int fromTime = Integer.parseInt(tf5.getText());
            	String toDate = tf6.getText();
            	int toTime = Integer.parseInt(tf7.getText());
            	int odometer = Integer.parseInt(tf8.getText());
            	int confNo = Integer.parseInt(tf9.getText());
        		
        		
        		Rental rent = new Rental(rid,vLicense,dLicense,fromDate,fromTime,toDate,toTime,odometer,confNo);
        		Boolean success = delegate.rentVehicle(rent);	
            }
        };
        
        
        d.setOnAction(event); 
        
        grid.getChildren().add(label1);
        grid.getChildren().add(label2);
        grid.getChildren().add(label3);
        grid.getChildren().add(label4);
        grid.getChildren().add(label5);
        grid.getChildren().add(label6);
        grid.getChildren().add(label7);
        grid.getChildren().add(label8);
        grid.getChildren().add(label9);
        grid.getChildren().add(tf1);
        grid.getChildren().add(tf2);
        grid.getChildren().add(tf3);
        grid.getChildren().add(tf4);
        grid.getChildren().add(tf5);
        grid.getChildren().add(tf6);
        grid.getChildren().add(tf7);
        grid.getChildren().add(tf8);
        grid.getChildren().add(tf9);
        grid.getChildren().add(d);
		
		Scene sc = new Scene(grid, 500, 500); 
		  
        // set the scene 
		Stage stage = new Stage();
        stage.setTitle("Reservation");
        stage.setScene(sc); 
        stage.show();
	}
	
	private void GUIhandleDBRental() {
		
		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("Location:"); 
        GridPane.setConstraints(label1, 0, 0 );
        
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("Date in form of YYYY-MM-DD:"); 
        GridPane.setConstraints(label2, 0, 1 );
        
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
     // create a button 
        Button d = new Button("submit");
        GridPane.setConstraints(d, 2, 2);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	String location = tf1.getText();
        		String time = tf2.getText();
        		String ret="";
        		
        		ArrayList<Vehicle> arr = delegate.dailyRental(time, location);

        		for (Vehicle v : arr ) {
        			ret+="VID: "+ v.getVid() + " Vtype: " + v.getVtname() + " Location: " + v.getLocation()+"\n";
        		}
        		ret+=countType(arr);
        		
        		
        		Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Daily Rental");
        		alert.setHeaderText("Result Information:");
        		alert.setContentText(ret);
        		alert.show();
            }
        };
        
        
        d.setOnAction(event); 
        
        grid.getChildren().add(label1);
        grid.getChildren().add(label2);
        grid.getChildren().add(tf1);
        grid.getChildren().add(tf2);
        grid.getChildren().add(d);
		
		Scene sc = new Scene(grid, 500, 500); 
		  
        // set the scene 
		Stage stage = new Stage();
        stage.setTitle("Daily Branch Rental");
        stage.setScene(sc); 
        stage.show();
	}
	
	private void GUIhandleQuitOption() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setContentText("Goodbye!");
		alert.show();
		delegate.terminalTransactionsFinished();
	}
	
	private void GUIhandleReturn() {
		
		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("rid:"); 
        GridPane.setConstraints(label1, 0, 0 );
        
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("Date:"); 
        GridPane.setConstraints(label2, 0, 1 );
        
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("Time:"); 
        GridPane.setConstraints(label3, 0, 2 );
        
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        Label label4 = new Label("Odometer:"); 
        GridPane.setConstraints(label4, 0, 3 );
        
        TextField tf4 = new TextField ();
        GridPane.setConstraints(tf4, 1, 3);
        
        Label label5 = new Label("fulltank:"); 
        GridPane.setConstraints(label5, 0, 4 );
        
        TextField tf5 = new TextField ();
        GridPane.setConstraints(tf5, 1, 4);
       
        Label label6 = new Label("value:"); 
        GridPane.setConstraints(label6, 0, 5);
        
        TextField tf6 = new TextField ();
        GridPane.setConstraints(tf6, 1, 5);
        
        Button d = new Button("submit");
        GridPane.setConstraints(d, 3, 6);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	int rid =Integer.parseInt(tf1.getText());
            	String date=tf2.getText();
            	int time =Integer.parseInt(tf3.getText());
            	int odometer =Integer.parseInt(tf4.getText());
            	String fullTank =tf5.getText();
            	int value =Integer.parseInt(tf6.getText());
        		Return r = new Return(rid, date, time, odometer, fullTank, value);

        		delegate.returnVehicle(r);
            }
        };
        d.setOnAction(event); 
        
        grid.getChildren().add(label1);
		grid.getChildren().add(tf1); 
		grid.getChildren().add(label2);
		grid.getChildren().add(tf2);
		grid.getChildren().add(label3);
		grid.getChildren().add(tf3);
		grid.getChildren().add(label4);
		grid.getChildren().add(tf4);
		grid.getChildren().add(label5);
		grid.getChildren().add(tf5);
		grid.getChildren().add(label6);
		grid.getChildren().add(tf6);
        grid.getChildren().add(d);
        // set the scene 
        Scene sc = new Scene(grid, 500, 500);
		Stage stage = new Stage();
        stage.setTitle("Return a vehicle");
        stage.setScene(sc); 
        stage.show();
        
	}
	
	
	private void GUIInvalidInputHandler() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Result");
		alert.setHeaderText("Result Information:");
		alert.setContentText("Invalid Choice");
		alert.show();
		
	}
	
	private String countType(ArrayList<Vehicle> arr) {
		int truck = 0;
		int suv = 0;
		int fullsize = 0;
		int economy = 0;
		int compact = 0;
		int midsize = 0;
		int standard = 0;
		String ret="";
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
		ret=
		"Truck: " + truck+"\n"+
		"Suv: " + suv+"\n"+
		"Fullsize: " + fullsize+"\n"+
		"Economy: " + economy+"\n"+
		"Compact: " + compact+"\n"+
		"Midsize: " + midsize+"\n"+
		"Standard: " + standard+"\n";
		
		return ret;
	}
	
private void GUIhandleDBReturn() {
		
		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("Location:"); 
        GridPane.setConstraints(label1, 0, 0 );
        
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("Date in form of YYYY-MM-DD:"); 
        GridPane.setConstraints(label2, 0, 1 );
        
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
     // create a button 
        Button d = new Button("submit");
        GridPane.setConstraints(d, 2, 2);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	String location = tf1.getText();
        		String time = tf2.getText();
        		String ret="";
        		
        		ArrayList<Vehicle> arr = delegate.dailyReturn(time, location);

        		for (Vehicle v : arr ) {
        			ret+="VID: "+ v.getVid() + " Vtype: " + v.getVtname() + " Location: " + v.getLocation()+"\n";
        		}
        		ret+=countType(arr);
        		
        		
        		Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Daily Return");
        		alert.setHeaderText("Result Information:");
        		alert.setContentText(ret);
        		alert.show();
            }
        };
        
        
        d.setOnAction(event); 
        
        grid.getChildren().add(label1);
        grid.getChildren().add(label2);
        grid.getChildren().add(tf1);
        grid.getChildren().add(tf2);
        grid.getChildren().add(d);
		
		Scene sc = new Scene(grid, 500, 500); 
		  
        // set the scene 
		Stage stage = new Stage();
        stage.setTitle("Daily Branch Return");
        stage.setScene(sc); 
        stage.show();
	}

private void GUIhandleDReturn() {
	
	GridPane grid = new GridPane();
	grid.setPadding(new Insets(10, 10, 10, 10));
	grid.setVgap(5);
	grid.setHgap(5);

    
    Label label2 = new Label("Date in form of YYYY-MM-DD:"); 
    GridPane.setConstraints(label2, 0, 1 );
    
    TextField tf2 = new TextField ();
    GridPane.setConstraints(tf2, 1, 1);
 // create a button 
    Button d = new Button("submit");
    GridPane.setConstraints(d, 2, 2);
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e) 
        { 
    		String time = tf2.getText();
    		String ret="";
    		
    		ArrayList<Vehicle> arr = delegate.dailyReturn(time, null);

    		for (Vehicle v : arr ) {
    			ret+="VID: "+ v.getVid() + " Vtype: " + v.getVtname() + " Location: " + v.getLocation()+"\n";
    		}
    		ret+=countType(arr);
    		
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Daily Return");
    		alert.setHeaderText("Result Information:");
    		alert.setContentText(ret);
    		alert.show();
        }
    };
    
    
    d.setOnAction(event); 
    
    grid.getChildren().add(label2);
    grid.getChildren().add(tf2);
    grid.getChildren().add(d);
	
	Scene sc = new Scene(grid, 500, 500); 
	  
    // set the scene 
	Stage stage = new Stage();
    stage.setTitle("Daily Return");
    stage.setScene(sc); 
    stage.show();
}
private void GUIhandleDRental() {
	
	GridPane grid = new GridPane();
	grid.setPadding(new Insets(10, 10, 10, 10));
	grid.setVgap(5);
	grid.setHgap(5);

    
    Label label2 = new Label("Date in form of YYYY-MM-DD:"); 
    GridPane.setConstraints(label2, 0, 1 );
    
    TextField tf2 = new TextField ();
    GridPane.setConstraints(tf2, 1, 1);
 // create a button 
    Button d = new Button("submit");
    GridPane.setConstraints(d, 2, 2);
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e) 
        { 
    		String time = tf2.getText();
    		String ret="";
    		
    		ArrayList<Vehicle> arr = delegate.dailyRental(time, null);

    		for (Vehicle v : arr ) {
    			ret+="VID: "+ v.getVid() + " Vtype: " + v.getVtname() + " Location: " + v.getLocation()+"\n";
    		}
    		ret+=countType(arr);
    		
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Daily Return");
    		alert.setHeaderText("Result Information:");
    		alert.setContentText(ret);
    		alert.show();
        }
    };
    
    
    d.setOnAction(event); 
    
    grid.getChildren().add(label2);
    grid.getChildren().add(tf2);
    grid.getChildren().add(d);
	
	Scene sc = new Scene(grid, 500, 500); 
	  
    // set the scene 
	Stage stage = new Stage();
    stage.setTitle("Daily Return");
    stage.setScene(sc); 
    stage.show();
}
}

