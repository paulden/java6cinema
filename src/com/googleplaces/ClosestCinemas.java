package com.googleplaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cinema.Cinema;

public class ClosestCinemas {
	
	public static MyAddress myAddress = new MyAddress();
	public static double lat;
	public static double lng;
	public static String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
	private List<Cinema> closestCinemas;
	
	
	public ClosestCinemas(){}
	
	public ClosestCinemas(List<Cinema> closestCinemas) {
		this.closestCinemas = closestCinemas;
	}
	
	public void setClosestCinemas(double radius) throws ClientProtocolException, IOException, JSONException{
		
		lat = myAddress.getMyLat();
		lng = myAddress.getMyLng();
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		String requete = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
				+ String.valueOf(lat) + "," + String.valueOf(lng) + "&radius=" + String.valueOf(radius)
				+ "&types=movie_theater&key=" + API_key;		
		HttpGet getRequest = new HttpGet(requete);		
		getRequest.addHeader("accept", "application/json");		
		HttpResponse res = httpClient.execute(getRequest);
		
		if (res.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Echec: HTTP err code : " + res.getStatusLine().getStatusCode());
			}
		
		String responseString = new BasicResponseHandler().handleResponse(res);		
		JSONObject obj = new JSONObject(responseString);		
		JSONArray resultats = obj.getJSONArray("results");
		
		List<Cinema> list = new ArrayList<Cinema>();
		
		for (int i=0;i<resultats.length();++i) {
							
			JSONObject obj2 = resultats.getJSONObject(i);
			String name = resultats.getJSONObject(i).getString("name");
			String adresse = resultats.getJSONObject(i).getString("vicinity");
			JSONObject geometry = obj2.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			double lat = location.getDouble("lat");
			double lng = location.getDouble("lng");
			
			Cinema newCinema = new Cinema(name, adresse, lat, lng);
			
			list.add(newCinema);
			
			}
		
		this.closestCinemas = list;
		
		
	}
	
	public List<Cinema> getClosestCinemas(){
		return closestCinemas;
	}
			
			
			
			
		
		
	

}