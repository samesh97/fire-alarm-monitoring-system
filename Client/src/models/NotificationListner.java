package models;

import java.util.ArrayList;
//this interaface is responsible to send the notification by the client and RMI server
public interface NotificationListner 
{
    //inimplemented method with the list of SensorModel
	public void notifyWarning(ArrayList<SensorModel> item);
}
