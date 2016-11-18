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
	private static MyAddress myAddress = new MyAddress();

	private List<Cinema> closestCinemas;
	
	// Default constructor
	public ClosestCinemas(){}

	/**
	 * Build the closest cinemas list by calling Google Places API
	 * @param radius The maximum distance the user is willing to go to
	 * @throws IOException If there is a failure while connecting to the API
	 * @throws JSONException If there is something unexpected in the received JSON
	 */
	//Cette méthode permet de construire la liste des cinémas proches en appelant l'API Google Places
	public void setClosestCinemas(String departureAdress, double radius) throws IOException, JSONException{
		
		MyAddress myAddress;
		if(departureAdress==null) {
			myAddress = new MyAddress();
		} else {
			myAddress = new MyAddress(departureAdress);
		}

		
		double lat = myAddress.getMyLat();
		double lng = myAddress.getMyLng();
		String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
		
		// Build the API request with the user's location, the radius and the API Key.
		HttpClient httpClient = HttpClientBuilder.create().build();
		String request = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
				+ String.valueOf(lat) + "," + String.valueOf(lng) + "&radius=" + String.valueOf(radius)
				+ "&types=movie_theater&key=" + API_key;		
		HttpGet getRequest = new HttpGet(request);
		getRequest.addHeader("accept", "application/json");		
		HttpResponse res = httpClient.execute(getRequest);
		
		if (res.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failure: HTTP err code: " + res.getStatusLine().getStatusCode());
			}
		
		// Handling the JSON response
		String responseString = new BasicResponseHandler().handleResponse(res);		
		JSONObject obj = new JSONObject(responseString);		
		JSONArray results = obj.getJSONArray("results");
		
		// The Cinema List to receive the retrieved cinemas
		List<Cinema> list = new ArrayList<>();
		
		// Retrieval of all the cinemas found into the JSON response
		for (int i=0;i<results.length();++i) {
							
			JSONObject obj2 = results.getJSONObject(i);
			String name = results.getJSONObject(i).getString("name");
			String address = results.getJSONObject(i).getString("vicinity");
			JSONObject geometry = obj2.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			double latitude = location.getDouble("lat");
			double longitude = location.getDouble("lng");
			
			// We create the Cinema objects
			Cinema newCinema = new Cinema(name, address, latitude, longitude);
			
			list.add(newCinema);
			
			}
		
		// Finally setting the list
		this.closestCinemas = list;
		
		
	}
	
	
	
	public static MyAddress getMyAddress() {
		return myAddress;
	}

	public List<Cinema> getClosestCinemas(){
		return closestCinemas;
	}
			
			
			
			
		
		
	

}