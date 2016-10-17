package com.googleplaces;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyAddress {
	
	private String myIP;
	private double myLat;
	private double myLng;

	private static String MY_API_KEY = "AIzaSyCm77ySoeF1Kzu7BaxaW7wlUfx0heV9mj4";

	// First constructor without any arguments : generates the GPS coordinates from the user's IP address.
	public MyAddress(){
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			setMyIP(httpClient);
			String requete = "http://ip-api.com/json/"+getMyIP();
			HttpGet getRequest = new HttpGet(requete);
			getRequest.addHeader("accept", "application/json");
			HttpResponse res = httpClient.execute(getRequest);
			String responseString = new BasicResponseHandler().handleResponse(res);
			JSONObject obj = new JSONObject(responseString);
			this.myLat = obj.getDouble("lat");
			this.myLng = obj.getDouble("lon");
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	// The second constructor sets the GPS coordinates using the user's location as input.
	public MyAddress(String address) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			setCoordinates(address, httpClient);
		} catch (IOException | JSONException | AddressNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Basic constructor using the 3 arguments defining the class.
	public MyAddress(String myIP, double lat, double lng){
		this.myIP = myIP;
		this.myLat = lat;
		this.myLng = lng;
	}

	// Uses an API to get the user's IP
	public void setMyIP(HttpClient httpClient) throws IOException, JSONException{
		String requete = "https://api.ipify.org/?format=json";
		HttpGet getRequest = new HttpGet(requete);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		String responseString = new BasicResponseHandler().handleResponse(res);	
		JSONObject obj = new JSONObject(responseString);
		this.myIP = obj.getString("ip");
	}
	
	public String getMyIP(){
		return myIP;
	}

	// SetMyLat and SetMyLng get the user's location from his IP address
	public void setMyLat()throws IOException, JSONException{
		HttpClient httpClient = HttpClientBuilder.create().build();
		String requete = "http://ip-api.com/json/"+getMyIP();
		HttpGet getRequest = new HttpGet(requete);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		String responseString = new BasicResponseHandler().handleResponse(res);	
		JSONObject obj = new JSONObject(responseString);
		this.myLat = obj.getDouble("lat");
	}

	
	public void setMyLng()throws IOException, JSONException{
		HttpClient httpClient = HttpClientBuilder.create().build();
		String requete = "http://ip-api.com/json/"+getMyIP();
		HttpGet getRequest = new HttpGet(requete);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		String responseString = new BasicResponseHandler().handleResponse(res);	
		JSONObject obj = new JSONObject(responseString);
		this.myLng = obj.getDouble("lon");
	}

	public void setCoordinates(String address, HttpClient httpClient) throws IOException, AddressNotFoundException {
		// the request to google maps api
		String requete = generateLocationRequest(address);

		// handling the request
		HttpGet getRequest = new HttpGet(requete);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		String responseString = new BasicResponseHandler().handleResponse(res);
		JSONObject obj = new JSONObject(responseString);

		// Looking for the right field in Json object
		JSONArray results = obj.getJSONArray("results");
		if (results == null || results.length() == 0) throw new AddressNotFoundException("No location found for this address.");
		JSONObject location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

		// setting the coordinates
		this.myLat = location.getDouble("lat");
		this.myLng = location.getDouble("lng");

	}

	private String generateLocationRequest(String address) {
		String request = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		request += address.replace(" ", "+");
		request += "&key=" + MY_API_KEY;
		return request;
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