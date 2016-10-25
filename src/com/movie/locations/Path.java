package com.movie.locations;

import java.io.IOException;

import com.movie.exceptions.NoPathException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class creates a readable object from origin and destination coordinates, and an optional transportation mode. <br>
 * If the transportation mode is not "driving", "walking", "bicycling" or "transit" (for public transportation) the default mode is driving.<br><br>
 * 
 * The 2 attributes represent the time it takes to go from origin to destination.<br>
 * Value is an int that represents the value in seconds ; readable is a String which gives human-readable information in hours, minutes and seconds.
 * @author Renaud
 *
 */

public class Path {
	
	private static String API_key = "AIzaSyAXKXL1u5kkztbN3LOSBOvhGPNpK2kYD5E";
	
	protected int value;
	protected String readable;
	
	public enum ModeTrajet {
		WALKING("walking"),
		DRIVING("driving"),
		BICYCLING("bicycling"),
		TRANSIT("transit");
	
		private String mode;
		
		ModeTrajet(String mode) {
			this.mode = mode;
		}
		
		/**
		 * Permet de récupérer le mode du trajet.
		 */
		@Override
		public String toString() {
			return this.mode;
		}
	
	};
	
	public Path(double origin_lat, double origin_lng, double dest_lat, double dest_lng, String mode) 
			throws ClientProtocolException, IOException, NoPathException {
		super();
		JSONObject queryResult = getTimeToDestination(origin_lat, origin_lng, dest_lat, dest_lng, mode);
		this.value = queryResult.getInt("value");
		this.readable = queryResult.getString("text");
	}


	public int getValue() {
		return value;
	}


	public String getReadable() {
		return readable;
	}


	protected JSONObject getTimeToDestination(double origin_lat, double origin_lng, double dest_lat, double dest_lng, String mode) 
			throws ClientProtocolException, IOException, NoPathException {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		String requete = generatePathRequest(origin_lat, origin_lng, dest_lat, dest_lng, mode);
		
		// Sending the request 
		HttpGet getRequest = new HttpGet(requete);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		
		if (res.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Echec: HTTP error code : " + res.getStatusLine().getStatusCode());
		}
		
		// Handling the response
		String responseString = new BasicResponseHandler().handleResponse(res);
	
		// Run a lot of tests to make sure we recieved correct results
		JSONObject obj = new JSONObject(responseString);
		JSONArray routes = obj.getJSONArray("routes");
		if (routes == null) throw new NoPathException("No path found to go to this destination.");
		
		JSONObject firstItinerary = routes.getJSONObject(0);
		if (firstItinerary == null) throw new NoPathException("No path found to go to this destination.");
		
		JSONArray legs = firstItinerary.getJSONArray("legs");
		if (legs == null) throw new NoPathException("No path found to go to this destination.");
		
		JSONObject leg = legs.getJSONObject(0);
		if (leg == null) throw new NoPathException("No path found to go to this destination.");
		
		JSONObject duration = leg.getJSONObject("duration");
		if (duration == null) throw new NoPathException("No path found to go to this destination.");
		
		// Return usable object 
		return duration;
	}
	
	
	protected String generatePathRequest(double origin_lat, double origin_lng, double dest_lat, double dest_lng, String mode) {
		
		String requete = "https://maps.googleapis.com/maps/api/directions/json?origin="
				+ String.valueOf(origin_lat) + "," + String.valueOf(origin_lng)
				+ "&destination="
				+ String.valueOf(dest_lat) + "," + String.valueOf(dest_lng);
		
		if(mode.equals("driving") || mode.equals("walking") || mode.equals("bicycling") || mode.equals("transit")) {
			requete += "&mode=" + mode;
		}
		
		requete += "&key=" + API_key;
		return requete;
		
	}

}
