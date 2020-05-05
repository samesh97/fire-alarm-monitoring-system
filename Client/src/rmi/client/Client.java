package rmi.client;
import java.net.ServerSocket;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.controlsfx.control.Notifications;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import models.NotificationListner;
import models.SensorModel;
import rmi.api.ApiServiceInterface;
import rmi.server.RmiServer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

//this class implements NotificationListner
public class Client extends Application implements Initializable,NotificationListner
{
    //all the UI elements
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private Label response;
	@FXML
	private Pane loginPane;
	@FXML
	private Pane loggedOnPane;
	@FXML
	private ListView sensorListView;
	@FXML
	private TextField floorNo;
	@FXML
	private TextField roomNo;
	@FXML
	private Label addNewOrUpdateSensorLabel;
	@FXML
	private Pane addNewSensorPane;
	@FXML
	private Label createSensorResponse;
	@FXML
	private Button createOrUpdateSensorButton;
	
	
	@FXML
	private Pane sensorFullDetailsPane;
	@FXML
	private Label sensorID;
	@FXML
	private Label sensorStatus;
	@FXML
	private Label sensorFloorNumber;
	@FXML
	private Label sensorRoomNumber;
	@FXML
	private Label sensorCo2Level;
	@FXML
	private Label sensorSmokeLevel;
	@FXML
	private Button addNewSensorButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Button updateSensorButton;

	//attribues
	//ApiServiceInterface object
	private ApiServiceInterface apiServiceInterface;
	//listview ObservableList
	private ObservableList<String> list = FXCollections.observableArrayList();
	//sensor list
	private ArrayList<SensorModel> sensList;
	//updating sensor id
	private String updatingSensorId = null;
	//updating sensor floor number
	private String updatingSensorFloorNumber = null;
	//updating sensor room number
	private String updatingSensorRoomNumber = null;
	private boolean isCreating;
	//last clicked position of the list
	private int lastListClickedPosition = -99;

	//is the admin is logged in or not
	private boolean isLoggedIn = false;

	

	
	@Override
	public void initialize(URL url,ResourceBundle rb)
	{
		 
		try {
			//getting a free port to run the RMI server
			ServerSocket serverSocket = new ServerSocket(0);
			int port = serverSocket.getLocalPort();
			serverSocket.close();

			//create server object with the port number and the notification lister
			RmiServer server = new RmiServer(port,this);
			//run the main method
			server.main(null);
			

			//accessing the rmi server
			Registry registry;
			registry = LocateRegistry.getRegistry("localhost",port);
			//get the apiServiceInterface object
			apiServiceInterface = (ApiServiceInterface) registry.lookup("RmiServer");
			
		} 
		catch (Exception e1) 
		{
		
			e1.printStackTrace();
		}
		
	
        //making the login screen visible at the very first time
		loginPane.setVisible(true);
		sensorListView.setItems(list);
		refreshingList();
		

	    //on item click listner of sensor listview
		sensorListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) 
	        {
	            //storing the last clicked position to show the full detailed view of the sensor
	        	lastListClickedPosition = sensorListView.getSelectionModel().getSelectedIndex();
	        	//setting the full detailed view of the currently clicked position
	        	setupItemUI(lastListClickedPosition);
	        }
	    });
		
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//showing the login UI
			setLoginUI(primaryStage);
			setLoggedOnUI(true);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setSensorList()
	{
		//setting up the sensor list
		 Platform.runLater(() -> 
		 {
		    //clearing the list to eleminate duplicates
			 list.clear();
			 try {
			        //getting all the sensors with the apiServiceInterface object
					sensList = apiServiceInterface.getAllSensors();
					//running a for each loop to create the listview items
					for(SensorModel model : sensList)
					{
					    //adding to the observableArrayList
						list.add("Floor : " + model.getFloorNo() + " Room : " + model.getRoomNo());
					}
					//check whether the last position item is not out of bound of the array to handle runtime exceptions
					if(lastListClickedPosition >=0 && lastListClickedPosition < sensList.size())
					{
					    //setting up full detailed view of a sensor
						setupItemUI(lastListClickedPosition);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			 
         });	
	}
    //setting the login UI
	private void setLoginUI(Stage primaryStage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
		Scene scene = new Scene(root,500,500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//setting the UI after the admin logged into the system
	private void setLoggedOnUI(boolean isLogged)
	{
		try
		{
			if(isLogged)
			{
				loginPane.setVisible(false);
				addNewSensorPane.setVisible(false);
				loggedOnPane.setVisible(true);
				isLoggedIn = true;
				addNewSensorButton.setVisible(true);
				logoutButton.setText("Logout");
				updateSensorButton.setVisible(true);
				
				
			}
			else
			{
				loginPane.setVisible(false);
				addNewSensorPane.setVisible(false);
				loggedOnPane.setVisible(true);
				isLoggedIn = false;
				addNewSensorButton.setVisible(false);
				logoutButton.setText("Login");
				updateSensorButton.setVisible(false);
			
			}
			
		}
		catch(Exception e)
		{
			
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	//this method will be called when the login button is clicked
	public void Login(ActionEvent event)
	{
		try {
			//check whether the username and password is valid
			if((username.getText().equals("") || username.getText() == null) || password.getText().equals("") || password.getText() == null)
			{
			    //if not valid show the error
				response.setText("Enter Something Valid!");
				return;
			}
			//if there's something entered as the username and password.password
			//check the username and password is valid or not
			boolean isLoginSuccess = apiServiceInterface.checkLogin(username.getText(),password.getText());
			//if isLoginSuccess is true. which means the entered username and password is valid
			if(isLoginSuccess)
			{
			    //setting the response in the UI
				response.setText("Login Success!");
				setLoggedOnUI(true);
			}
			else
			{
			    //setting the response in the UI
				response.setText("Authentication failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//setting the full detailed view of a sensor
	public void setupItemUI(int position)
	{
		 Platform.runLater(() -> 
		 {
		    //showing the sensorFullDetailsPane
			 sensorFullDetailsPane.setVisible(true);
			 
			 
			 //setting up css for the pane and textfields based on the co2 level and smoke level
			 //if the smoke level or co2 level is greater than 5
			 //the background will be shown as red
			 //else as white
			 if(sensList.get(position).getCO2Level() > 5 || sensList.get(position).getSmokeLevel() > 5)
			 {
				 sensorFullDetailsPane.setStyle("-fx-background-color: #FF0000");
				 sensorFloorNumber.setStyle("-fx-text-fill: white");
				 sensorRoomNumber.setStyle("-fx-text-fill: white");
				 sensorCo2Level.setStyle("-fx-text-fill: white");
				 sensorSmokeLevel.setStyle("-fx-text-fill: white");
				 sensorStatus.setStyle("-fx-text-fill: white");
				 sensorID.setStyle("-fx-text-fill: white");
			 }
			 else
			 {
			    //setting up css for the pane and textfields based on the co2 level and smoke level
				 sensorFullDetailsPane.setStyle("-fx-background-color: #FFFFFF");
				 sensorFloorNumber.setStyle("-fx-text-fill: black");
				 sensorRoomNumber.setStyle("-fx-text-fill: black");
				 sensorCo2Level.setStyle("-fx-text-fill: black");
				 sensorSmokeLevel.setStyle("-fx-text-fill: black");
				 sensorStatus.setStyle("-fx-text-fill: black");
				 sensorID.setStyle("-fx-text-fill: black");
			 }
			 
			 
				//checking the active state of the sensor
				if(sensList.get(position).isActive())
				{
					sensorStatus.setText("Sensor Status : Active");
				}
				else
				{
					sensorStatus.setText("Sensor Status : Inactive");
				}

				//setting the UI with the currently clicked sensor details
				sensorID.setText("Sensor ID : " + sensList.get(position).get_id());
				sensorFloorNumber.setText("Floor Number : " + sensList.get(position).getFloorNo());
				sensorRoomNumber.setText("Room Number : " + sensList.get(position).getRoomNo());
				sensorCo2Level.setText("CO2 Level : " + sensList.get(position).getCO2Level());
				sensorSmokeLevel.setText("Smoke Level : " + sensList.get(position).getSmokeLevel());
				
				updatingSensorId = sensList.get(position).get_id().toString();
				updatingSensorFloorNumber = sensList.get(position).getFloorNo();
				updatingSensorRoomNumber = sensList.get(position).getRoomNo();
				
		 });
		
		
		
	}
	//this method will be called when adding a new sensor or updating sensor
	public void AddNewSensor(ActionEvent event)
	{
	    //setting the UI for updating or creating
		isCreating = true;
		loggedOnPane.setVisible(false);
		addNewSensorPane.setVisible(true);
		addNewOrUpdateSensorLabel.setText("Add New Sensor");
		createOrUpdateSensorButton.setText("Create Sensor");
		floorNo.setText("");
		roomNo.setText("");
	}
	public void skipLogin(ActionEvent event)
	{
	    //skiping the login
		setLoggedOnUI(false);
	}
	//this method will be called when an sensor is about to update
	public void UpdateSensor(ActionEvent event) throws Exception
	{
	    //setting up the UI
		isCreating = false;
		loggedOnPane.setVisible(false);
		addNewSensorPane.setVisible(true);
		addNewOrUpdateSensorLabel.setText("Update Existing Sensor");
		createOrUpdateSensorButton.setText("Update Sensor");
		floorNo.setText(updatingSensorFloorNumber);
		roomNo.setText(updatingSensorRoomNumber);

	}
	//this method will be called when the CreateNewSensor button is clicked
	public void CreateNewSensor(ActionEvent event) throws Exception
	{
		createOrUpdateSensor(isCreating);
	}
	//updating or creating a sensor method
	public void createOrUpdateSensor(boolean isCreateNew) throws Exception
	{
	    //check whether the everything is okay to proceed
		if(floorNo.getText().equals("") || floorNo.getText() == null && roomNo.getText().equals("") || roomNo.getText() == null)
		{
			createSensorResponse.setText("Enter valid details");
			return;
		}

		//if a new sensor is creating
		if(isCreateNew)
		{
		    //calling createNewSensor method to create a sensor with the floor number and room number
			boolean isAdded = apiServiceInterface.createNewSensor(true, floorNo.getText(), roomNo.getText(), 0, 0);
			//check whether the sensor is properly created or not to show the response in the UI
			if(isAdded)
			{
				floorNo.setText("");
				roomNo.setText("");
				createSensorResponse.setText("Sensor was created!");
				updateUIAfterAddingOrUpdatingASensor();
			}
			else
			{
				createSensorResponse.setText("Something went wrong!");
			}
		}
		//or a sensor is updating
		else
		{
		    //calling the update sensor method
		    //with the id,floor number and room number
			boolean isUpdated = apiServiceInterface.updateSensor(updatingSensorId, floorNo.getText(), roomNo.getText());
			//check whether it is properly updated or not to show in the UI
			if(isUpdated)
			{
				createSensorResponse.setText("Sensor was updated!");
				updateUIAfterAddingOrUpdatingASensor();
			}
			else
			{
				createSensorResponse.setText("Something went wrong!");
			}
		}
		
	}
	//this method will be called when the go back button is clicked
	public void goBackToLoggedOnScreen(ActionEvent event)
	{
		loggedOnPane.setVisible(true);
		addNewSensorPane.setVisible(false);
		updatingSensorId = null;
		updatingSensorFloorNumber = null;
		updatingSensorRoomNumber = null;
		sensorFullDetailsPane.setVisible(false);
	}
	//this method will be called when the logout button is clicked
	public void goBackToLoginScreen(ActionEvent event)
	{
		response.setText("");
		username.setText("");
		password.setText("");
		loginPane.setVisible(true);
		addNewSensorPane.setVisible(false);
		loggedOnPane.setVisible(false);
		
		isLoggedIn = false;
	}
	//updating ui after a sensor is created or updated
	public void updateUIAfterAddingOrUpdatingASensor()
	{
		setSensorList();
		loggedOnPane.setVisible(true);
		addNewSensorPane.setVisible(false);
		createSensorResponse.setText("");
		updatingSensorId = null;
		updatingSensorFloorNumber = null;
		updatingSensorRoomNumber = null;
		sensorFullDetailsPane.setVisible(false);
	}
	//updating the sensorList every 30 seconds
	public void refreshingList()
	{
		//updating sensor list every 30 seconds
		 int timePeriod = 30000;
		 Platform.runLater(() -> 
		 {
			 Timer timer = new Timer();
		        timer.schedule(new TimerTask() {
		            @Override
		            public void run() 
		            {
		                //setting up the list
		            	setSensorList();
		            	if(isLoggedIn)
		            	{
		            		 String body = "";

		                        //showing the warning notification if any of sensor's smoke level or co2 level is graeter than 5
		    				 for(int i = 0; i < sensList.size(); i++)
		    				 {
		    				 //setting up the notification body
		    					 if(sensList.get(i).getCO2Level() > 5 || sensList.get(i).getSmokeLevel() > 5)
		    					 {
		    						 body = body + "Floor no " + sensList.get(i).getFloorNo() + ", Room no " + sensList.get(i).getRoomNo() + " sensor shows an unusual behaviour. Please check!\n";
		    					 }
		    					 if(i == sensList.size() - 1)
		    					 {
		    						 showNotification("Warning!",body);
		    					 }
		    				 }
		            	}
		        
		            }
		        }, 0, timePeriod);
		 });
		
	}
	//showing a notification method with the title and body
	public void showNotification(String title,String text)
	{
		Platform.runLater(() -> 
		 {
		 //show notification
			 Notifications.create()
				.title(title)
				.text(text)
				.showWarning();
		 });
		
	}
    //this method will be called by the rmi server when it needs to send an email or sms
	@Override
	public void notifyWarning(ArrayList<SensorModel> items) 
	{
		 Platform.runLater(() -> 
		 {
		    //the sending email or sms notification will be shown only if the admin has logged into the system
			 if(isLoggedIn)
			 {
			 //setting up the notification body
				 String body = "";
				 for(int i = 0; i < items.size(); i++)
				 {
					 body = body + "Floor no " + items.get(i).getFloorNo() + ", Room no " + items.get(i).getRoomNo() + " sensor shows an unusual behaviour. Please check!\n";
					 
					 if(i == items.size() - 1)
					 {
						 showNotification("Sending an email & SMS",body);
					 }
				 }
			 }
			
		 });
		
	}
}
