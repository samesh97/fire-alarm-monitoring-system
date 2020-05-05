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
	private static NotificationListner listner;
	private static int port;
	
	//constructor
	public RmiServer(int port,NotificationListner listner)
	{
		this.listner = listner;
		this.port = port;
	}
	
	public static void main(String[] args) throws RemoteException 
	{
		
		Registry registry = LocateRegistry.createRegistry(port);
		
		ApiService apiService = new ApiService();
		ApiServiceInterface apiServiceInterface =  (ApiServiceInterface) UnicastRemoteObject.exportObject(apiService,0);
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
		            			ArrayList<SensorModel> list = apiService.getAllSensors();
		            			ArrayList<SensorModel> warningList = new ArrayList<>();
		            			
		            			
		            			for(int i = 0; i < list.size(); i++)
		            			{
		            				if(list.get(i).getCO2Level() > 5 || list.get(i).getSmokeLevel() > 5)
		            				{
		            					warningList.add(list.get(i));
		            				}
		            				if(i == list.size() - 1)
		            				{
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
