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
	
	private static int count = 10;
	private String sensorId;
	API api = new API();
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
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
		if(sensorIdField.getText().equals("") || sensorIdField.getText() == null)
		{
			response.setText("Enter a valid sensor ID");
			return;
		}
		
		
		boolean isValidId = api.checkSensorById(sensorIdField.getText().toString());
		if(isValidId)
		{
			sensorIdField.setDisable(true);
			registerButton.setText("Registered");
			registerButton.setDisable(true);
			sensorId = sensorIdField.getText().toString();
			response.setText("App was registered as a sensor!");
			updateSensorDetails();
		}
		else
		{
			response.setText("Entered ID is not mapped to a sensor!");
		}
		
		
	}
	public void updateSensorDetails()
	{
		//send sensor status every 5 seconds
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() 
            {
            	count--;
            	if(count == 0)
            	{
            		count = 10;
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
		Random rand = new Random();
		int smoke = rand.nextInt((10 - 1) + 1) + 1;
		int co2 = rand.nextInt((10 - 1) + 1) + 1;
		
		Platform.runLater(() -> 
		{
			
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
			
			sendToAPI(isActive,smoke,co2);
			
          });
		
	}

	private void sendToAPI(boolean isActive, int smoke, int co2)
	{
		boolean isUpdated = api.updateSensor(sensorId, isActive, co2, smoke);
		Platform.runLater(() -> 
		{
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
