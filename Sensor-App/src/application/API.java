package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;

public class API 
{
    //check whether a sensor is avaiable by the id which is passed as a parameter
    //and returning a boolean.
 	public boolean checkSensorById(String id)
	{
		try
		{
		    //this is the url to check whther a sensor is avaiable or not
			String burl = "http://localhost:5000/api/sensors/findSensorById?id=" +id;
			//and replacing it with spaces with "%20" to make sure no spaces are in the url
			String url = burl.replaceAll(" ","%20");
			//creating URL object with the url we created
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//the request method is GET
			con.setRequestMethod("GET");
			//getting the response code
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
			
			//store the response in to a JSONArray
			JSONArray array = new JSONArray(response.toString());

			//check the response code
			//if it is 200, there is no issue with the connection or anything
			//and checking the JSONArray size to make sure there is a sensor by the specified id.
			//if avaiable we are returning true
			//else rturning false which means there are no sensors by the id
			if(respondCode == 200 && array.length() > 0)
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
		    //if an exception occured we send the boolean false
			return false;
		}
		
	}
	//update a sensor method
	public boolean updateSensor(String id,boolean isActive,int co2Level,int smokeLevel)
	{
	    //getting id,active state,co2Level,smokeLevel as parameters
		try
		{
		    //creating the url to send the request
			String burl = "http://localhost:5000/api/sensors/updateSensorBySensorApp?id=" + id + "&co2Level=" + co2Level + "&smokeLevel=" + smokeLevel + "&isActive="+ isActive;
			//replacing spaces with the "%20" to eleminate spaces in the url
			String url = burl.replaceAll(" ","%20");
			//creating a URl object with the url we created
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//setting the request type (Here it is post)
			con.setRequestMethod("POST");
			int respondCode = con.getResponseCode();

			//if the response code is true
			//the updates has been made successfully
			//so we return true
			if(respondCode == 200)
			{
				return true;
			}
			//else return false
			return false;
		}
		catch(Exception e)
		{
		    //if an exception occured we return false as well
			return false;
		}
		
	}
}
