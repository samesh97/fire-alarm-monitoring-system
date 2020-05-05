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


public class Client extends Application implements Initializable,NotificationListner
{
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
	
	private ApiServiceInterface apiServiceInterface;
	private ObservableList<String> list = FXCollections.observableArrayList();
	private ArrayList<SensorModel> sensList;
	private String updatingSensorId = null;
	private String updatingSensorFloorNumber = null;
	private String updatingSensorRoomNumber = null;
	private boolean isCreating;
	private int lastListClickedPosition = -99;
	
	private boolean isLoggedIn = false;

	

	
	@Override
	public void initialize(URL url,ResourceBundle rb)
	{
		 
		try {
			//get a free port 
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
		
	

		loginPane.setVisible(true);
		sensorListView.setItems(list);
		refreshingList();
		
	
		sensorListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) 
	        {
	        	lastListClickedPosition = sensorListView.getSelectionModel().getSelectedIndex();
	        	setupItemUI(lastListClickedPosition);
	        }
	    });
		
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			setLoginUI(primaryStage);
			setLoggedOnUI(true);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setSensorList()
	{
		
		 Platform.runLater(() -> 
		 {
			 list.clear();
			 try {
					sensList = apiServiceInterface.getAllSensors();
					for(SensorModel model : sensList)
					{
						list.add("Floor : " + model.getFloorNo() + " Room : " + model.getRoomNo());
					}
					if(lastListClickedPosition >=0 && lastListClickedPosition < sensList.size())
					{
						setupItemUI(lastListClickedPosition);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			 
         });	
	}

	private void setLoginUI(Stage primaryStage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
		Scene scene = new Scene(root,500,500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
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
	public void Login(ActionEvent event)
	{
		try {
			
			if((username.getText().equals("") || username.getText() == null) || password.getText().equals("") || password.getText() == null)
			{
				response.setText("Enter Something Valid!");
				return;
			}
			boolean isLoginSuccess = apiServiceInterface.checkLogin(username.getText(),password.getText());
			if(isLoginSuccess)
			{
				response.setText("Login Success!");
				setLoggedOnUI(true);
			}
			else
			{
				response.setText("Authentication failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setupItemUI(int position)
	{
		 Platform.runLater(() -> 
		 {
			 sensorFullDetailsPane.setVisible(true);
			 
			 
			 //setting up css for the pane and textfields
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
				 sensorFullDetailsPane.setStyle("-fx-background-color: #FFFFFF");
				 sensorFloorNumber.setStyle("-fx-text-fill: black");
				 sensorRoomNumber.setStyle("-fx-text-fill: black");
				 sensorCo2Level.setStyle("-fx-text-fill: black");
				 sensorSmokeLevel.setStyle("-fx-text-fill: black");
				 sensorStatus.setStyle("-fx-text-fill: black");
				 sensorID.setStyle("-fx-text-fill: black");
			 }
			 
			 
				
				if(sensList.get(position).isActive())
				{
					sensorStatus.setText("Sensor Status : Active");
				}
				else
				{
					sensorStatus.setText("Sensor Status : Inactive");
				}
				
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
	public void AddNewSensor(ActionEvent event)
	{
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
		setLoggedOnUI(false);
	}
	public void UpdateSensor(ActionEvent event) throws Exception
	{
		isCreating = false;
		loggedOnPane.setVisible(false);
		addNewSensorPane.setVisible(true);
		addNewOrUpdateSensorLabel.setText("Update Existing Sensor");
		createOrUpdateSensorButton.setText("Update Sensor");
		floorNo.setText(updatingSensorFloorNumber);
		roomNo.setText(updatingSensorRoomNumber);

	}
	public void CreateNewSensor(ActionEvent event) throws Exception
	{
		createOrUpdateSensor(isCreating);
	}
	public void createOrUpdateSensor(boolean isCreateNew) throws Exception
	{
		if(floorNo.getText().equals("") || floorNo.getText() == null && roomNo.getText().equals("") || roomNo.getText() == null)
		{
			createSensorResponse.setText("Enter valid details");
			return;
		}
		
		if(isCreateNew)
		{
			boolean isAdded = apiServiceInterface.createNewSensor(true, floorNo.getText(), roomNo.getText(), 0, 0);
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
		else
		{
			boolean isUpdated = apiServiceInterface.updateSensor(updatingSensorId, floorNo.getText(), roomNo.getText());
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
	public void goBackToLoggedOnScreen(ActionEvent event)
	{
		loggedOnPane.setVisible(true);
		addNewSensorPane.setVisible(false);
		updatingSensorId = null;
		updatingSensorFloorNumber = null;
		updatingSensorRoomNumber = null;
		sensorFullDetailsPane.setVisible(false);
	}
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
		            	setSensorList();
		            	if(isLoggedIn)
		            	{
		            		 String body = "";
		           
		    				 for(int i = 0; i < sensList.size(); i++)
		    				 {
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
	public void showNotification(String title,String text)
	{
		Platform.runLater(() -> 
		 {
			 Notifications.create()
				.title(title)
				.text(text)
				.showWarning();
		 });
		
	}

	@Override
	public void notifyWarning(ArrayList<SensorModel> items) 
	{
		 Platform.runLater(() -> 
		 {
			 if(isLoggedIn)
			 {
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
