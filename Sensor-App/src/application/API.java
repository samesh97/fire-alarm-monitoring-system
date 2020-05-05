package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;

public class API 
{
	public boolean checkSensorById(String id)
	{
		try
		{
			String burl = "http://localhost:5000/api/sensors/findSensorById?id=" +id;
			String url = burl.replaceAll(" ","%20");
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("GET");
			int respondCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();
			
			
			JSONArray array = new JSONArray(response.toString());
			
			if(respondCode == 200 && array.length() > 0)
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	public boolean updateSensor(String id,boolean isActive,int co2Level,int smokeLevel)
	{
		try
		{
			String burl = "http://localhost:5000/api/sensors/updateSensorBySensorApp?id=" + id + "&co2Level=" + co2Level + "&smokeLevel=" + smokeLevel + "&isActive="+ isActive;
			String url = burl.replaceAll(" ","%20");
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST");
			int respondCode = con.getResponseCode();
			
			if(respondCode == 200)
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
}
