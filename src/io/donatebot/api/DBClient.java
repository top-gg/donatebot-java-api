package io.donatebot.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Julian#7797
 * The main Donate Bot API client class, allows you to communicate with the Donate Bot
 * API.
 */
public class DBClient {
	
	private String apiKey = "";
	private String serverId = "";
	
	private final HttpClient client = HttpClient.newHttpClient();
	
	private final String userAgent = "Donate-Bot-Java-API/1.0.0";
	private final String baseUrl = "https://webhook.site/404c63d5-baab-43fc-a529-d87d9906c6e2/api/v1";
	
	/**
	 * A new Donate Bot API Client
	 * @param serverId Your Discord Server ID
	 * @param apiKey Your Donate Bot API Key from the panel
	 */
	public DBClient(String serverId, String apiKey) {
		this.serverId = serverId;
		this.apiKey = apiKey;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException  {
		// Example of Fetching donations
		
		DBClient dbClient = new DBClient("404394509917487105", "My API Key");
		
		//CompletableFuture<Void> cf1 = dbClient.markDonation("123", false, false);
		
		String[] statuses = {"Completed", "Reversed", "Refunded"};
		
		CompletableFuture<Donation[]> cf1 = dbClient.getNewDonations(statuses);
		
		Donation[] donations = cf1.get();
		
		String test = donations[0].getCurrency();
		
		System.out.println(test);
		
	}
	
	/**
	 * Fetch new donations from the API
	 * @param Statuses An array of strings statuses to search for
	 * @return  An array of Donation objects
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
	          .uri(URI.create(baseUrl + "/donations/" + serverId + "/new?find=" + findQuery))
	          .setHeader("authorization", this.apiKey)
	          .setHeader("user-agent", userAgent)
	          .build();

	    return client.sendAsync(request, BodyHandlers.ofString())
	    		.thenApply(HttpResponse::body)
	    		.thenApply(x -> HandleDonations(x));
	}
	
	/**
	 * Handle donations from the API
	 * @param x The body string retrieved from the API
	 * @return An array of Donation Objects
	 */
	private Donation[] HandleDonations(final String x) {
		Donation[] objectArray = new Donation[0];
		
		try {
			JSONObject donationsJSON = new JSONObject(x);
			
			JSONArray donationsArray = donationsJSON.getJSONArray("donations");
			
			objectArray = new Donation[donationsArray.length()];
			
			for (int i = 0; i < donationsArray.length(); i++)
			{

				JSONObject donation = donationsArray.getJSONObject(i);
				objectArray[i] = new Donation().deserialize(donation);
			}
			
			return objectArray;
		} catch(Exception e) {
			// Unhandled Exception
		}
		
		return objectArray;
	}
	
	/**
	 * Mark a donation if you have done something with it
	 * @param txnId The transaction ID to mark
	 * @param isEndedSubscription If the transaction is an ended subscription
	 * @param markProcessed If the transaction has been processed
	 * @return Nothing
	 */
	public CompletableFuture<Void> markDonation(String txnId, Boolean isEndedSubscription, Boolean markProcessed) {
		
		JSONObject requestBody = new JSONObject();
		
		requestBody.put("isEndedSubscription", isEndedSubscription);
		requestBody.put("markProcessed", markProcessed);
		
	    HttpRequest request = HttpRequest.newBuilder()
	          .uri(URI.create(baseUrl + "/donations/" + serverId + "/" + txnId + "/mark"))
	          .setHeader("authorization", this.apiKey)
	          .setHeader("user-agent", userAgent)
	          .POST(BodyPublishers.ofString(requestBody.toString()))
	          .build();

	    return client.sendAsync(request, BodyHandlers.ofString())
			.thenAccept((x) -> {
				return;
			});
	}
	
	/**
	 * Fetch new donations from the API
	 * @param Statuses An array of statuses to search for
	 * @return An array of Donation objects
	 */
	public CompletableFuture<Donation[]> getEndedSubscriptions() {
	    HttpRequest request = HttpRequest.newBuilder()
	          .uri(URI.create(baseUrl + "/donations/" + serverId + "/endedsubscriptions"))
	          .setHeader("authorization", this.apiKey)
	          .setHeader("user-agent", userAgent)
	          .build();

	    return client.sendAsync(request, BodyHandlers.ofString())
	    		.thenApply(HttpResponse::body)
	    		.thenApply(x -> HandleDonations(x));
	}

}
