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

public class ApiService implements ApiServiceInterface {

	@Override
	public ArrayList<SensorModel> getAllSensors() throws Exception
	{
		String url = "http://localhost:5000/api/sensors/";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int respondCode = con.getResponseCode();
		
		BufferedReader  in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}
		in.close();
		
		
		JSONArray array = new JSONArray(response.toString());
		ArrayList<SensorModel> list = new ArrayList<SensorModel>();
		for(int i = 0; i < array.length(); i++)
		{
			JSONObject jObject = array.getJSONObject(i);
			SensorModel model = new SensorModel();
			model.set_id(jObject.get("_id"));
			model.setActive(jObject.getBoolean("isActive"));
			model.setFloorNo(jObject.getString("floorNo"));
			model.setRoomNo(jObject.getString("roomNo"));
			model.setSmokeLevel(jObject.getInt("smokeLevel"));
			model.setCO2Level(jObject.getInt("CO2Level"));
			
			list.add(model);
		}
		
		return list;
	}

	@Override
	public boolean createNewSensor(boolean isActive, String floorNo, String roomNo, int smokeLevel, int co2Level)
			throws Exception {
		
		String burl = "http://localhost:5000/api/sensors/createNewSensor/?isActive="+isActive+"&floorNo="+floorNo+"&roomNo="+roomNo+"&smokeLevel="+smokeLevel+"&CO2Level="+ co2Level;
		String url = burl.replaceAll(" ","%20");
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("POST");
		int respondCode = con.getResponseCode();
		System.out.println("POST" + respondCode);
		
		if(respondCode == 200)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean checkLogin(String username, String password) throws Exception {
		
		String burl = "http://localhost:5000/api/users/login?username=" + username + "&password=" + password;
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

	@Override
	public boolean RegisterUser(String floorNo, String roomNo) throws Exception {
		
		String burl = "http://localhost:5000/api/users/register?floorNo=" + floorNo + "&roomNo=" + roomNo;
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

	@Override
	public boolean updateSensor(String id, String floorNo, String roomNo) throws Exception
	{
		String burl = "http://localhost:5000/api/sensors/updateSensor?id=" + id + "&floorNo=" + floorNo + "&roomNo=" + roomNo;
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

}
