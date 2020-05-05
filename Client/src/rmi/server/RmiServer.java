package rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import models.NotificationListner;
import models.SensorModel;
import rmi.api.ApiService;
import rmi.api.ApiServiceInterface;


public class RmiServer 
{
    //notification listner interface
	private static NotificationListner listner;
	//server running port number
	private static int port;
	
	//constructor with the NotificationListner and port
	public RmiServer(int port,NotificationListner listner)
	{
	    //storing the paramaters
		this.listner = listner;
		this.port = port;
	}
	
	public static void main(String[] args) throws RemoteException 
	{
		//creating a registry
		Registry registry = LocateRegistry.createRegistry(port);

		//creating ApiService class object
		ApiService apiService = new ApiService();
		ApiServiceInterface apiServiceInterface =  (ApiServiceInterface) UnicastRemoteObject.exportObject(apiService,0);
		//binding the object
		registry.rebind("RmiServer", apiServiceInterface);
		
		System.out.println("Rmi Server is running!");
		
		
		//this timer will call its run method once in every 15 seconds
		int timePeriod = 15000;
		Platform.runLater(() -> 
		 {
			 Timer timer = new Timer();
		        timer.schedule(new TimerTask() {
		            @Override
		            public void run() 
		            {
		            	try
		            	{
		            	        //get sensors from the api
		            			ArrayList<SensorModel> list = apiService.getAllSensors();
		            			//waring list holds the imformation of the sensors which is either co2 level or smoke level is
		            			//greater than to 5
		            			ArrayList<SensorModel> warningList = new ArrayList<>();
		            			
		            			
		            			for(int i = 0; i < list.size(); i++)
		            			{
		            			    //adding to the warning list
		            				if(list.get(i).getCO2Level() > 5 || list.get(i).getSmokeLevel() > 5)
		            				{
		            					warningList.add(list.get(i));
		            				}
		            				if(i == list.size() - 1)
		            				{
		            				    //calling the notifyWarning with warningList to show the notification
		            					if(warningList.size() > 0)
				            			{
				            				listner.notifyWarning(warningList);
				            			}
		            				}
		            			}
		
						} 
		            	catch (Exception e)
		            	{
							e.printStackTrace();
						}
		                
		            }
		        }, 0, timePeriod);
		 });
		

	}
}
