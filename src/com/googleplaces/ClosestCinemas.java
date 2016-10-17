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

/* Cette classe permet d'obtenir les cinémas les plus proches sous forme
d'une collection List d'objets Cinema(nom, adresse, lat, lng) pouvant être
utilisés dans la recherche de cinémas proches et d'itinéraires

Elle demande en input un rayon radius en mètres */

public class ClosestCinemas {
	
	//On initialise un objet myAddress qui sera utilisé pour localiser l'utilisateur
	//et récupérer une liste de cinémas proche de sa position
	public static MyAddress myAddress = new MyAddress();
	public static double lat;
	public static double lng;
	
	//Il s'agit de la clé API Google Developer utilisé dans le programme
	public static String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
	private List<Cinema> closestCinemas;
	
	//Constructeur par défaut
	public ClosestCinemas(){}
	
	public ClosestCinemas(List<Cinema> closestCinemas) {
		this.closestCinemas = closestCinemas;
	}
	
	//Cette méthode permet de construire la liste des cinémas proches en appelant l'API Google Places
	public void setClosestCinemas(double radius) throws ClientProtocolException, IOException, JSONException{
		
		//On récupère l'adresse de l'utilisateur via la classe MyAddress
		myAddress.setMyIP();
		myAddress.setMyLat();
		myAddress.setMyLng();
		
		lat = myAddress.getMyLat();
		lng = myAddress.getMyLng();
		
		//Requête à l'API Google Places avec en input la localisation (obtenue précédemment),
		//le rayon et la clé API définie au départ
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
		
		//Traitement du fichier JSON obtenu en réponse
		String responseString = new BasicResponseHandler().handleResponse(res);		
		JSONObject obj = new JSONObject(responseString);		
		JSONArray resultats = obj.getJSONArray("results");
		
		//On initialise une List qui recevra les cinémas récupérés
		List<Cinema> list = new ArrayList<Cinema>();
		
		//Récupération de tous les cinémas présents dans le fichier JSON
		for (int i=0;i<resultats.length();++i) {
							
			JSONObject obj2 = resultats.getJSONObject(i);
			String name = resultats.getJSONObject(i).getString("name");
			String adresse = resultats.getJSONObject(i).getString("vicinity");
			JSONObject geometry = obj2.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			double lat = location.getDouble("lat");
			double lng = location.getDouble("lng");
			
			//Les cinémas sont sous forme d'objets Cinema(name, adresse, lat, lng)
			//pour pouvoir utiliser les get associés
			Cinema newCinema = new Cinema(name, adresse, lat, lng);
			
			list.add(newCinema);
			
			}
		
		//Récupération finale de la liste
		this.closestCinemas = list;
		
		
	}
	
	public List<Cinema> getClosestCinemas(){
		return closestCinemas;
	}
			
			
			
			
		
		
	

}