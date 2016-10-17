package com.googleplaces;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;


public class MyAddress {
	
	private String myIP;
	private double myLat;
	private double myLng;
	
	public MyAddress(){
	}
	
	public MyAddress(String myIP, double lat, double lng){
		this.myIP = myIP;
		this.myLat = lat;
		this.myLng = lng;
	}
	
	public void setMyIP() throws ClientProtocolException, IOException, JSONException{
		HttpClient httpClient = HttpClientBuilder.create().build();
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
	
	public void setMyLat()throws ClientProtocolException, IOException, JSONException{
		HttpClient httpClient = HttpClientBuilder.create().build();
		String requete = "http://ip-api.com/json/"+getMyIP();
		HttpGet getRequest = new HttpGet(requete);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		String responseString = new BasicResponseHandler().handleResponse(res);	
		JSONObject obj = new JSONObject(responseString);
		this.myLat = obj.getDouble("lat");
	}

	
	public void setMyLng()throws ClientProtocolException, IOException, JSONException{
		HttpClient httpClient = HttpClientBuilder.create().build();
		String requete = "http://ip-api.com/json/"+getMyIP();
		HttpGet getRequest = new HttpGet(requete);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		String responseString = new BasicResponseHandler().handleResponse(res);	
		JSONObject obj = new JSONObject(responseString);
		this.myLng = obj.getDouble("lon");;
	}
	
	public double getMyLat(){
		return myLat;
	}
	
	public double getMyLng(){
		return myLng;
	}

}