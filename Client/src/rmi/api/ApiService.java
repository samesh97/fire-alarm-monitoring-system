package rmi.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import models.SensorModel;

//ApiService class implements the ApiServiceInterface
public class ApiService implements ApiServiceInterface {

    //overriden methods of the ApiServiceInterface

    //this method is to get all the sensors from the api by sending a request
    //and returning a list of sensors
	@Override
	public ArrayList<SensorModel> getAllSensors() throws Exception
	{
	    //url to send the request
		String url = "http://localhost:5000/api/sensors/";
		//creating an URL object with url we created
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		//setting the reuest type to GET
		con.setRequestMethod("GET");
		//catching the response code
		int respondCode = con.getResponseCode();

		//reading the response
		BufferedReader  in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}
		in.close();
		
		//the response string converts to an JSONArray
		JSONArray array = new JSONArray(response.toString());
		//create a list of SensorModel
		ArrayList<SensorModel> list = new ArrayList<SensorModel>();
		//running a for loop to create to SensorModel list with JSONArray
		for(int i = 0; i < array.length(); i++)
		{
		    //getting the sensors from the JSONArray and created new SensorModel object
			JSONObject jObject = array.getJSONObject(i);
			SensorModel model = new SensorModel();
			model.set_id(jObject.get("_id"));
			model.setActive(jObject.getBoolean("isActive"));
			model.setFloorNo(jObject.getString("floorNo"));
			model.setRoomNo(jObject.getString("roomNo"));
			model.setSmokeLevel(jObject.getInt("smokeLevel"));
			model.setCO2Level(jObject.getInt("CO2Level"));

		    //adding it to the list
			list.add(model);
		}

		//returning finally added list
		return list;
	}

    //this is an overriden method from the ApiServiceInterface
    //this method will be called to create a new sensor
	@Override
	public boolean createNewSensor(boolean isActive, String floorNo, String roomNo, int smokeLevel, int co2Level)
			throws Exception {

	    //setting up the url to send to HTTP request
		
		String burl = "http://localhost:5000/api/sensors/createNewSensor/?isActive="+isActive+"&floorNo="+floorNo+"&roomNo="+roomNo+"&smokeLevel="+smokeLevel+"&CO2Level="+ co2Level;
		//removing spaces of the url by replacing "%20"
		String url = burl.replaceAll(" ","%20");
		//creating an URL object with the url we created
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//setting up the request type to POST
		con.setRequestMethod("POST");
		//catching the response code
		int respondCode = con.getResponseCode();
		System.out.println("POST" + respondCode);

		//checking the response code to turn a boolean to make sure the sensor is created or not
		if(respondCode == 200)
		{
			return true;
		}
		return false;
	}

    //this method is overriden by the ApiServiceInterface
    //this method will be called to check the login with usersname and password
	@Override
	public boolean checkLogin(String username, String password) throws Exception {

		//setting up the url
		String burl = "http://localhost:5000/api/users/login?username=" + username + "&password=" + password;
		//replacing the spaces
		String url = burl.replaceAll(" ","%20");
		//creating the URL object with url we created
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//setting the request type
		con.setRequestMethod("GET");
		//holding the response code
		int respondCode = con.getResponseCode();

		//reading the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}
		in.close();
		
		//converting response to an JSONArray
		JSONArray array = new JSONArray(response.toString());

		//check whether the login is success or not to return
		if(respondCode == 200 && array.length() > 0)
		{
			return true;
		}
		return false;
	}

    //this method is responsible to register an user
	@Override
	public boolean RegisterUser(String floorNo, String roomNo) throws Exception {

		//setting up the url with parameters
		String burl = "http://localhost:5000/api/users/register?floorNo=" + floorNo + "&roomNo=" + roomNo;
		//replacing the spaces url conatins
		String url = burl.replaceAll(" ","%20");
		//creating an URL object
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//setting the request type to POST
		con.setRequestMethod("POST");
		//getting the response code
		int respondCode = con.getResponseCode();

		//check whether the user successfully registered or not with response code
		//if 200 retured its success
		if(respondCode == 200)
		{
			return true;
		}
		//retuning
		return false;
	}

    //this method will be called to update a sensor
	@Override
	public boolean updateSensor(String id, String floorNo, String roomNo) throws Exception
	{
	    //setting up the url
		String burl = "http://localhost:5000/api/sensors/updateSensor?id=" + id + "&floorNo=" + floorNo + "&roomNo=" + roomNo;
		//removing spaces
		String url = burl.replaceAll(" ","%20");
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//setting the request type to POST
		con.setRequestMethod("POST");
		//get the response code
		int respondCode = con.getResponseCode();

		//checking whether the respondCode is 200 to return the update state
		if(respondCode == 200)
		{
			return true;
		}
		return false;
	}

}
