package models;

import java.io.Serializable;

public class SensorModel implements Serializable{

    //this class is to store a sensor which is taken by the API

    //sensor attributes
	private Object _id;
	private boolean isActive;
	private String floorNo;
	private String roomNo;
	private int smokeLevel;
	private int CO2Level;

	//SensorModel getters and setters
	public Object get_id() {
		return _id;
	}
	public void set_id(Object _id) {
		this._id = _id;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getFloorNo() {
		return floorNo;
	}
	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public int getSmokeLevel() {
		return smokeLevel;
	}
	public void setSmokeLevel(int smokeLevel) {
		this.smokeLevel = smokeLevel;
	}
	public int getCO2Level() {
		return CO2Level;
	}
	public void setCO2Level(int cO2Level) {
		CO2Level = cO2Level;
	}
	
	

}
