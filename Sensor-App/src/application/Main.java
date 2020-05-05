package application;
	
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


public class Main extends Application {

	//all the UI elements
	@FXML
	private Label status;
	@FXML
	private Label co2Level;
	@FXML
	private Label smokeLevel;
	@FXML
	private TextField sensorIdField;
	@FXML
	private Button registerButton;
	@FXML
	private Label response;
	@FXML
	private Label countLabel;

	//keeping a count to send the state of a sensor
	private static int count = 10;
	//storing the valid sensor id
	private String sensorId;
	//create an object of API class
	API api = new API();
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		//loading the UI
		Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
		Scene scene = new Scene(root,500,500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	public void registerSensor(ActionEvent event) throws Exception
	{
	    //this method will be triggered when the register button is clicked
	    //check whether the sensorIdField is entered or not
		if(sensorIdField.getText().equals("") || sensorIdField.getText() == null)
		{
		    //if not showing an error
			response.setText("Enter a valid sensor ID");
			return;
		}
		
		//if the sensorIdField is successfully entered
		//checking the entered id valid or not by calling a method in the API class
		//if valid it will return true
		boolean isValidId = api.checkSensorById(sensorIdField.getText().toString());
		//checking the boolean value
		if(isValidId)
		{
		    //if valid the registration is success
		    //and it is ready to send its state
			sensorIdField.setDisable(true);
			registerButton.setText("Registered");
			registerButton.setDisable(true);
			sensorId = sensorIdField.getText().toString();
			response.setText("App was registered as a sensor!");
			updateSensorDetails();
		}
		else
		{
		    //else showing the error response
			response.setText("Entered ID is not mapped to a sensor!");
		}
		
		
	}
	//this method is reposnsible to update the sensor every 10 seconds
	public void updateSensorDetails()
	{
		//send sensor status every 10 seconds
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() 
            {
                //decreasing the count variable
            	count--;
            	//if the count == 0
            	//it is the time to send to state of a sensor to thr API
            	if(count == 0)
            	{
            	    //we make the count varibale 10 again to show the remaining time in the app
            		count = 10;
            		//and this method will generate the sensor state
            		sendUpdates();
            	}
            	 Platform.runLater(() -> {
            		countLabel.setText("Sensor updates will be sent in " +count + " seconds");
                 });
            
            	
                
            }
        }, 0, 1000);
	}
	public void sendUpdates()
	{
	    //generating random values for active state,co2 level and smoke level
		Random rand = new Random();
		int smoke = rand.nextInt((10 - 1) + 1) + 1;
		int co2 = rand.nextInt((10 - 1) + 1) + 1;
		
		Platform.runLater(() -> 
		{
			//setting the UI elements with the randomly generated values
			boolean isActive = false;
			if((smoke + co2) > 10)
			{
				isActive = true;
			}
     		
			if(isActive)
			{
				status.setText("Status : Active");
			}
			else
			{
				status.setText("Status : Inactive");
			}
			
			co2Level.setText("CO2 Level : " + co2);
			smokeLevel.setText("Smoke Level : " + smoke);

			//sending the values to the API with the randomly generated values
			sendToAPI(isActive,smoke,co2);
			
          });
		
	}

	private void sendToAPI(boolean isActive, int smoke, int co2)
	{
	    //calling the API class updateSensor method to update the sensor with the parameters
		boolean isUpdated = api.updateSensor(sensorId, isActive, co2, smoke);
		Platform.runLater(() -> 
		{
		    //check whether it is updated or not
			if(isUpdated)
			{
				response.setText("Updates were successfully sent!");
			}
			else
			{
				response.setText("Something went wrong!");
			}
			
		});
	}
}
