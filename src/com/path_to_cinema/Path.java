package com.path_to_cinema;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class Path {
	
	private static String API_key = "AIzaSyAXKXL1u5kkztbN3LOSBOvhGPNpK2kYD5E";
	
	public JSONObject getTimeToDestination(double origin_lat, double origin_lng, double dest_lat, double dest_lng) throws ClientProtocolException, IOException, NoPathException {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		// Generate request according to specs
		String requete = "https://maps.googleapis.com/maps/api/directions/json?origin="
				+ String.valueOf(origin_lat) + "," + String.valueOf(origin_lng)
				+ "&destination="
				+ String.valueOf(dest_lat) + "," + String.valueOf(dest_lng)
				+ "&key=" + API_key;
		
		// Sending the request 
		HttpGet getRequest = new HttpGet(requete);
		getRequest.addHeader("accept", "application/json");
		HttpResponse res = httpClient.execute(getRequest);
		
		if (res.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Echec: HTTP error code : " + res.getStatusLine().getStatusCode());
		}
		
		// Handling the response
		String responseString = new BasicResponseHandler().handleResponse(res);
	
		// Run a lot of tests to make sure we recieved results
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
		
		// Print out the time to go to the cinema
		System.out.println("Time in seconds : " + duration.getInt("value"));
		System.out.println("Human-readable time : " + duration.getString("text"));
		return duration;
		
	}

}
