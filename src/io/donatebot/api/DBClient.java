package io.donatebot.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import org.json.JSONArray;
import org.json.JSONObject;


public class DBClient {
	
	private String apiURL = "https://webhook.site/404c63d5-baab-43fc-a529-d87d9906c6e2";
	private String apiKey = "";
	
	private HttpClient client = HttpClient.newHttpClient();
	
	private final String userAgent = "Donate-Bot-Java-API/1.0.0";
	
	
	public DBClient(String serverID, String apiKey) {
		this.apiKey = apiKey;
	}

	public static void main(String[] args) throws Exception {
		// Example of Fetching donations
		
		DBClient donatebotClient = new DBClient("", "myAPIKey");
		
		String[] statuses = {"Completed", "Reversed", "Refunded"};
		
		CompletableFuture<Donation[]> donations = donatebotClient.getNewDonations(statuses);
		
		Donation[] myDonations = donations.get();
		
		Donation myFirstDonation = myDonations[0];
		
		if (myFirstDonation.getStatus() == Status.COMPLETED) {
			System.out.println("This donation is completed");
			// Print the buyer's email
			System.out.println(myFirstDonation.getBuyerEmail());
		}
	}
	
	private Donation[] HandleNewDonations(final String x) {
		Donation[] objectArray = new Donation[0];
		
		try {
			JSONObject donationsJSON = new JSONObject(x);
			
			JSONArray donationsArray = donationsJSON.getJSONArray("donations");
			
			objectArray = new Donation[donationsArray.length()];
			
			for (int i = 0; i < donationsArray.length(); i++)
			{

				JSONObject donation = donationsArray.getJSONObject(i);
				objectArray[i] = new Donation(donation);
			}
			
			return objectArray;
		} catch(Exception e) {
			// Unhandled Exception
		}
		
		return objectArray;

	}
	
	/**
	 * Fetch new donations from the API
	 * @param Statuses An array of statuses to search for
	 * @return An array of Donation objects
	 */
	public CompletableFuture<Donation[]> getNewDonations(String[] Statuses) {
		// Find query which is sent to the API
		String findQuery = "";
		
		for (int i = 0; i < Statuses.length; i++) {
			if (i == Statuses.length - 1) {
				findQuery += Statuses[i];
			} else {
				findQuery += Statuses[i] + ",";
			}
		}
		
	    HttpRequest request = HttpRequest.newBuilder()
	          .uri(URI.create(apiURL + "?find=" + findQuery))
	          .setHeader("authorization", this.apiKey)
	          .setHeader("user-agent", userAgent)
	          .build();

	    return client.sendAsync(request, BodyHandlers.ofString())
	    		.thenApply(HttpResponse::body)
	    		.thenApply(x -> HandleNewDonations(x));
	}

}
