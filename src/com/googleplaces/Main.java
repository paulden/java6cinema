package com.googleplaces;

import java.io.IOException;
import org.json.JSONException;

public class Main {
	
	private static String API_key = "AIzaSyAAgLaPXaGmpAC_oaJqjoFZt8A2aQfftTw";
	public static double lat = 48.7648573;
	public static double lng = 2.2885256;
	public static double radius = 5000;
	public static ClosestCinemas closestCinemas = new ClosestCinemas();

	public static void main(String[] args) throws IOException, JSONException {

		closestCinemas.setClosestCinemas(10000);
		
		int n = closestCinemas.getClosestCinemas().size();
		
		for(int i = 0; i<n;i++){
			System.out.println(closestCinemas.getClosestCinemas().get(i).getNom());
			System.out.println(closestCinemas.getClosestCinemas().get(i).getAdresse());
			System.out.println(closestCinemas.getClosestCinemas().get(i).getLat());
			System.out.println(closestCinemas.getClosestCinemas().get(i).getLng());
		}


		String goodAddress = new MyAddress("Grande Voie des Vignes 92295 CHÂTENAY-MALABRY Cedex").toString();
		System.out.println(goodAddress);


	}

}
