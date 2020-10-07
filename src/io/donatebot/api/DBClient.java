package io.donatebot.api;

import static org.asynchttpclient.Dsl.*;
import java.util.concurrent.CompletableFuture;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
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
	
	AsyncHttpClient asyncHttpClient = asyncHttpClient();
	
	private final String userAgent = "Donate-Bot-Java-API/1.0.1";
	private final String baseUrl = "https://donatebot.io/api/v1";
	
	/**
	 * A new Donate Bot API Client
	 * @param serverId Your Discord Server ID
	 * @param apiKey Your Donate Bot API Key from the panel
	 */
	public DBClient(String serverId, String apiKey) {
		this.serverId = serverId;
		this.apiKey = apiKey;
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
	    
	    return asyncHttpClient
	            .prepareGet(baseUrl + "/donations/" + serverId + "/new?find=" + findQuery)
	            .setHeader("authorization", this.apiKey)
	            .setHeader("user-agent", userAgent)
	            .execute()
	            .toCompletableFuture()
	            .thenApply(x -> HandleDonations(x));
	}
	
	/**
	 * Handle donations from the API
	 * @param x The body string retrieved from the API
	 * @return An array of Donation Objects
	 */
	private Donation[] HandleDonations(final Response response) {
		Donation[] objectArray = new Donation[0];
		
		try {
			JSONObject donationsJSON = new JSONObject(response.getResponseBody());
			
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
	 * @return Response
	 */
	public CompletableFuture<Response> markDonation(String txnId, Boolean isEndedSubscription, Boolean markProcessed) {
		
		JSONObject requestBody = new JSONObject();
		
		requestBody.put("isEndedSubscription", isEndedSubscription);
		requestBody.put("markProcessed", markProcessed);
		
	    return asyncHttpClient
	            .preparePost(baseUrl + "/donations/" + serverId + "/" + txnId + "/mark")
	            .setBody(requestBody.toString())
	            .setHeader("authorization", this.apiKey)
	            .setHeader("user-agent", userAgent)
	            .setHeader("content-type", "application/json")
	            .execute()
	            .toCompletableFuture();
	}
	
	/**
	 * Fetch new donations from the API
	 * @param Statuses An array of statuses to search for
	 * @return An array of Donation objects
	 */
	public CompletableFuture<Donation[]> getEndedSubscriptions() {
		
	    return asyncHttpClient
	            .prepareGet(baseUrl + "/donations/" + serverId + "/endedsubscriptions")
	            .setHeader("authorization", this.apiKey)
	            .setHeader("user-agent", userAgent)
	            .execute()
	            .toCompletableFuture()
	            .thenApply(x -> HandleDonations(x));
	}

}
