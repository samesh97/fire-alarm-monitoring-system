package rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import models.SensorModel;

public interface ApiServiceInterface extends Remote {

	public ArrayList<SensorModel> getAllSensors() throws Exception;
	public boolean createNewSensor(boolean isActive,String floorNo,String roomNo,int smokeLevel,int co2Level) throws Exception;
	public boolean checkLogin(String username,String password) throws Exception;
	public boolean RegisterUser(String floorNo,String roomNo) throws Exception;
	public boolean updateSensor(String id,String floorNo,String roomNo) throws Exception;
}
