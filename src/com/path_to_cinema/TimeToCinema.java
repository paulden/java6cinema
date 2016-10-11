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

/**
 * Classe contenant de quoi calculer le chemin vers un cinéma
 * @author Renaud
 *
 */
public class TimeToCinema {
	
	private static String API_key = "AIzaSyAXKXL1u5kkztbN3LOSBOvhGPNpK2kYD5E";
	public static double origin_lat = 48.7648573;
	public static double origin_lng = 2.2885256;
	public static double dest_lat = 48.7659532;
	public static double dest_lng = 2.2599667;

	public static void main(String[] args) throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		String requete = "https://maps.googleapis.com/maps/api/directions/json?origin="
				+ String.valueOf(origin_lat) + "," + String.valueOf(origin_lng)
				+ "&destination="
				+ String.valueOf(dest_lat) + "," + String.valueOf(dest_lng)
				+ "&key=" + API_key;
		
		
		HttpGet getRequest = new HttpGet(requete);
		
		getRequest.addHeader("accept", "application/json");
		
		HttpResponse res = httpClient.execute(getRequest);
		
		if (res.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Echec: HTTP error code : " + res.getStatusLine().getStatusCode());
		}
		
		String responseString = new BasicResponseHandler().handleResponse(res);
	
		
		JSONObject obj = new JSONObject(responseString);
		
		JSONArray routes = obj.getJSONArray("routes");
		if (routes == null) {
			System.out.println("Pas d'itin�raire");
			return;
		}
		JSONObject firstItinerary = routes.getJSONObject(0);
		if (firstItinerary == null) {
			System.out.println("Pas d'itinéraire");
			return;
		}
		JSONArray legs = firstItinerary.getJSONArray("legs");
		if (legs == null) {
			System.out.println("Pas d'itinéraire");
			return;
		}
		JSONObject leg = legs.getJSONObject(0);
		if (leg == null) {
			System.out.println("Pas d'itinéraire");
			return;
		}
		JSONObject duration = leg.getJSONObject("duration");
		if (duration == null) {
			System.out.println("Pas d'itinéraire");
			return;
		}
		
		System.out.println("Time in seconds : " + duration.getInt("value"));
		System.out.println("Human-readable time : " + duration.getString("text"));
		

	}

}
