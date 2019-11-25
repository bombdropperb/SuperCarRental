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
import ca.ubc.cs304.model.Reservation;
import ca.ubc.cs304.model.Vehicle;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;

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
        		+ "3. Delete Branch\n"
        		+ "4. Update Branch name\n"
        		+ "5. Show Branch\n"
        		+ "6. Quit\n"
        		+ "7. Insert Customer\n"
        		+ "8. Insert Branch");
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
    						GUIhandleDeleteOption();
    						break;
    					case 4:
    						GUIhandleUpdateOption();
    						break;
    					case 5:
    						delegate.showBranch();
    						break;
    					case 6:
    						GUIhandleQuitOption();
    						break;
    					case 7:
    						GUIhandleInsertCustomer();
    						break;
    					case 8:
    						GUIhandleInsertOption();
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
        //label1.setFont(new Font("Arial", 20));
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("Location:"); 
        GridPane.setConstraints(label2, 0, 1 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("Time:"); 
        GridPane.setConstraints(label3, 0, 2 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        
     // create a button 
        Button d = new Button("submit");
        GridPane.setConstraints(d, 2, 2);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	String vtname = tf1.getText();
        		String location = tf2.getText();
        		String time = tf3.getText();
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
        grid.getChildren().add(tf1);
        grid.getChildren().add(tf2);
        grid.getChildren().add(tf3);
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
        //label1.setFont(new Font("Arial", 20));
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("phone number:"); 
        GridPane.setConstraints(label2, 0, 1 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("name:"); 
        GridPane.setConstraints(label3, 0, 2 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        Label label4 = new Label("address:"); 
        GridPane.setConstraints(label4, 0, 3 );
        //label1.setFont(new Font("Arial", 20));
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
        //label1.setFont(new Font("Arial", 20));
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("conf number:"); 
        GridPane.setConstraints(label2, 0, 1 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("vehicle type name:"); 
        GridPane.setConstraints(label3, 0, 2 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        Label label4 = new Label("From Date:"); 
        GridPane.setConstraints(label4, 0, 3 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf4 = new TextField ();
        GridPane.setConstraints(tf4, 1, 3);
        
        Label label5 = new Label("From Time:"); 
        GridPane.setConstraints(label5, 0, 4 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf5 = new TextField ();
        GridPane.setConstraints(tf5, 1, 4);
        
        Label label6 = new Label("To Date:"); 
        GridPane.setConstraints(label6, 0, 5 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf6 = new TextField ();
        GridPane.setConstraints(tf6, 1, 5);
        
        Label label7 = new Label("To time:"); 
        GridPane.setConstraints(label7, 0, 6 );
        //label1.setFont(new Font("Arial", 20));
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
        		String fromTime = null;
        		String toDate = null;
        		String toTime = null;
        		int confNo = INVALID_INPUT;
        		
        		confNo = Integer.parseInt(tf2.getText());
        		
        		vtname = tf3.getText();
        		
        		fromDate = tf4.getText();

        		fromTime = tf5.getText();

        		toDate = tf6.getText();
        		
        		toTime = tf7.getText();
        		
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

	private void GUIhandleDeleteOption() {
		
		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("Branch ID to delete:"); 
        GridPane.setConstraints(label1, 0, 0 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Button d = new Button("submit");
        GridPane.setConstraints(d, 3, 0);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	int branchId = Integer.parseInt(tf1.getText());
        		delegate.deleteBranch(branchId);
            }
        };
        d.setOnAction(event); 
		Scene sc = new Scene(grid, 500, 500); 
		grid.getChildren().add(label1);
		grid.getChildren().add(tf1);
        grid.getChildren().add(d);
        // set the scene 
		Stage stage = new Stage();
        stage.setTitle("Delete a branch");
        stage.setScene(sc); 
        stage.show();
	}
	
	private void GUIhandleInsertOption() {
		
		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("Branch ID:"); 
        GridPane.setConstraints(label1, 0, 0 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("Branch Name:"); 
        GridPane.setConstraints(label2, 0, 1 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Label label3 = new Label("Branch Address"); 
        GridPane.setConstraints(label3, 0, 2 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf3 = new TextField ();
        GridPane.setConstraints(tf3, 1, 2);
        
        Label label4 = new Label("Branch City:"); 
        GridPane.setConstraints(label4, 0, 3 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf4 = new TextField ();
        GridPane.setConstraints(tf4, 1, 3);
        
        Label label5 = new Label("Branch Phone Number:"); 
        GridPane.setConstraints(label5, 0, 4 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf5 = new TextField ();
        GridPane.setConstraints(tf5, 1, 4);
        
        Button d = new Button("submit");
        GridPane.setConstraints(d, 3, 4);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	int id = Integer.parseInt(tf1.getText());
        		
        		String name = tf2.getText();
        		
        		String address = tf3.getText();
        		
        		String city = tf4.getText();
        		
        		int phoneNumber = Integer.parseInt(tf5.getText());
        		
        		BranchModel model = new BranchModel(address,
        											city,
        											id,
        											name,
        											phoneNumber);
        		delegate.insertBranch(model);
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
		
        grid.getChildren().add(d);
        // set the scene 
        Scene sc = new Scene(grid, 500, 500);
		Stage stage = new Stage();
        stage.setTitle("Insert a branch");
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
	
	private void GUIhandleUpdateOption() {
		
		GridPane grid = new GridPane();
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(5);
    	grid.setHgap(5);

        Label label1 = new Label("Branch ID:"); 
        GridPane.setConstraints(label1, 0, 0 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf1 = new TextField ();
        GridPane.setConstraints(tf1, 1, 0);
        
        Label label2 = new Label("Branch Name:"); 
        GridPane.setConstraints(label2, 0, 1 );
        //label1.setFont(new Font("Arial", 20));
        TextField tf2 = new TextField ();
        GridPane.setConstraints(tf2, 1, 1);
        
        Button d = new Button("submit");
        GridPane.setConstraints(d, 3, 4);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	int id = Integer.parseInt(tf1.getText());
        		
        		String name = tf2.getText();
        		
        		delegate.updateBranch(id, name);
            }
        };
		
		d.setOnAction(event); 
		Scene sc = new Scene(grid, 500, 500); 
		grid.getChildren().add(label1);
		grid.getChildren().add(tf1);
		grid.getChildren().add(label2);
		grid.getChildren().add(tf2);
        grid.getChildren().add(d);
        // set the scene 
		Stage stage = new Stage();
        stage.setTitle("Update a branch");
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
}

