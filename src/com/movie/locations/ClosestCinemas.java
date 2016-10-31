package com.movie.locations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.movie.cinema.Cinema;

/**
 * This class gathers the cinemas that are closest to the user in a {@link Cinema} list.<br>
 * They can be used in search of close cinemas or paths.
 */

public class ClosestCinemas {
	
	/**
	 *  Initialize a MyAddress object that will be used to represent the user's position <br>
	 *  and get a list of cinemas close to him.
	 */
	public static MyAddress myAddress = new MyAddress();
	public static double lat;
	public static double lng;
	
	// Google developer API Key
	public static String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
	private List<Cinema> closestCinemas;
	
	// Default constructor
	public ClosestCinemas(){}
	
	public ClosestCinemas(List<Cinema> closestCinemas) {
		this.closestCinemas = closestCinemas;
	}


	/**
	 * Build the closest cinemas list by calling Google Places API
	 * @param radius The maximum distance the user is willing to go to
	 * @throws IOException If there is a failure while connecting to the API
	 * @throws JSONException If there is something unexpected in the received JSON
	 */
	public void setClosestCinemas(double radius) throws IOException, JSONException{
		
		lat = myAddress.getMyLat();
		lng = myAddress.getMyLng();
		
		// Build the API request with the user's location, the radius and the API Key.
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
		
		// Handling the JSON response
		String responseString = new BasicResponseHandler().handleResponse(res);		
		JSONObject obj = new JSONObject(responseString);		
		JSONArray resultats = obj.getJSONArray("results");
		
		// The Cinema List to receive the rtrieved cinemas
		List<Cinema> list = new ArrayList<Cinema>();
		
		// Retrieval of all the cinemas found into the JSON response
		for (int i=0;i<resultats.length();++i) {
							
			JSONObject obj2 = resultats.getJSONObject(i);
			String name = resultats.getJSONObject(i).getString("name");
			String adresse = resultats.getJSONObject(i).getString("vicinity");
			JSONObject geometry = obj2.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			double lat = location.getDouble("lat");
			double lng = location.getDouble("lng");
			
			// We create the Cinema objects
			Cinema newCinema = new Cinema(name, adresse, lat, lng);
			
			list.add(newCinema);
			
			}
		
		// Finally setting the list
		this.closestCinemas = list;
		
		
	}
	
	public List<Cinema> getClosestCinemas(){
		return closestCinemas;
	}
			
			
			
			
		
		
	

}