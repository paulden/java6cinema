package com.googleplaces;

import java.io.IOException; 
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
	
	private static String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
	public static double lat = 48.7648573;
	public static double lng = 2.2885256;
	public static double radius = 5000;

	public static void main(String[] args) throws ClientProtocolException, IOException, JSONException {
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
		
		for (int i=0;i<resultats.length();++i) {
			System.out.println(resultats.getJSONObject(i).getString("name"));
			}

	}

}
