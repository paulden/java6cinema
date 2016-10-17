package com.movie.locations;

import java.io.IOException;

import com.movie.exceptions.AddressNotFoundException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class creates an object containing the user's location. Its multiple constructors allow the user to specify what he wants : <br>
 *     - either he doesn't give any information and his location is retrieved via his IP address (which is retrieved using an API) <br>
 *     - or he provides an address and we deduce his coordinates from it <br>
 *     - he can also directly give his IP address and GPS coordinates.
 * @author Paul & Renaud
 */

public class MyAddress {
	
	private String myIP;
	private double myLat;
	private double myLng;

	private static String MY_API_KEY = "AIzaSyCm77ySoeF1Kzu7BaxaW7wlUfx0heV9mj4";

	/**
	 * First constructor without any arguments : generates the GPS coordinates from the user's IP address.
	 */
	public MyAddress(){
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			setMyIP(httpClient);
			String requete = "http://ip-api.com/json/"+getMyIP();
			JSONObject obj = getJsonResponse(requete, httpClient);
			this.myLat = obj.getDouble("lat");
			this.myLng = obj.getDouble("lon");
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The second constructor sets the GPS coordinates using the user's location as input.
	 * @param address : the user's address as used by the post services.
	 */
	public MyAddress(String address) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			setCoordinates(address, httpClient);
		} catch (IOException | JSONException | AddressNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Basic constructor using the 3 arguments defining the class.
	 * @param myIP : user's IP address
	 * @param lat : user's latitude
	 * @param lng : user's longitude
	 */
	public MyAddress(String myIP, double lat, double lng){
		this.myIP = myIP;
		this.myLat = lat;
		this.myLng = lng;
	}

	/**
	 * Uses an API to get the user's IP
 	 */
	public void setMyIP(HttpClient httpClient) throws IOException, JSONException{
		String requete = "https://api.ipify.org/?format=json";
		JSONObject obj = getJsonResponse(requete, httpClient);
		this.myIP = obj.getString("ip");
	}
	
	public String getMyIP(){
		return myIP;
	}

	/**
	 * SetMyLat and SetMyLng get the user's location from his IP address
 	 */
	public void setMyLat()throws IOException, JSONException{
		HttpClient httpClient = HttpClientBuilder.create().build();
		String requete = "http://ip-api.com/json/"+getMyIP();
		JSONObject obj = getJsonResponse(requete, httpClient);
		this.myLat = obj.getDouble("lat");
	}

	
	public void setMyLng()throws IOException, JSONException{
		HttpClient httpClient = HttpClientBuilder.create().build();
		String requete = "http://ip-api.com/json/"+getMyIP();
		JSONObject obj = getJsonResponse(requete, httpClient);
		this.myLng = obj.getDouble("lon");
	}

	/**
	 * Sets the user's GPS coordinates from a postal address
	 * @param address : the user's postal address in String form.
	 * @param httpClient : the client used to connect to the API.
	 * @throws IOException if the httpClient can't execute the request
	 * @throws AddressNotFoundException if the address given by the user doesn't match with anything in google maps database
	 */
	public void setCoordinates(String address, HttpClient httpClient) throws IOException, AddressNotFoundException {
		// the request to google maps api
		String requete = generateLocationRequest(address);
		JSONObject obj = getJsonResponse(requete, httpClient);

		// Looking for the right field in Json object
		JSONArray results = obj.getJSONArray("results");
		if (results == null || results.length() == 0) throw new AddressNotFoundException("No location found for this address.");
		JSONObject location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

		// setting the coordinates
		this.myLat = location.getDouble("lat");
		this.myLng = location.getDouble("lng");

	}

	// Generate the request string from the address in postal form.
	private String generateLocationRequest(String address) {
		String request = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		request += address.replace(" ", "+");
		request += "&key=" + MY_API_KEY;
		return request;
	}

	// Separate the client "business" for clarity purposes
	public JSONObject getJsonResponse(String request, HttpClient httpClient) throws IOException{
		HttpGet getRequest = new HttpGet(request);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		String responseString = new BasicResponseHandler().handleResponse(res);
		return new JSONObject(responseString);
	}
	
	public double getMyLat(){
		return myLat;
	}
	
	public double getMyLng(){
		return myLng;
	}

	public String toString() {
		String myAddress = "IP address : " + this.getMyIP() + "\n";
		myAddress += "Lat : " + this.getMyLat() + "\n";
		myAddress += "Lng : " + this.getMyLng();
		return myAddress;
	}

}