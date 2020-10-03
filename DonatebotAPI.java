import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DonatebotAPI {
	
	private static String serverID;
	private static String apiKey;
	private static String rawApiURL;
	private static String apiURL;
	public static final String USER_AGENT = "DonateBot Java Client";

	public static final void init(String id, String key) {
		System.out.println(id);
		System.out.println(key);
		serverID = id;
		apiKey = key;
		rawApiURL = "https://donatebot.io/api/v1/donations/"+serverID;
	}
	
	// Get new Donations from the API
	public static final StringBuilder getNewDonations() {
		if(serverID.isEmpty() || apiKey.isEmpty()) {
			System.out.println("[ERROR] Missing ServerID or API-Key!");
			return null;
		}
		try {
			apiURL = rawApiURL+"/new?find=Reversed,Refunded,Completed";
            java.net.URL obj = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Authorization", apiKey);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + apiURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Get ended subscriptions from the API
	public static final StringBuilder getEndedSubscriptions() {
		if(serverID.isEmpty() || apiKey.isEmpty()) {
			System.out.println("[ERROR] Missing ServerID or API-Key!");
			return null;
		}
		try {
			apiURL = rawApiURL+"/endedsubscriptions";
            java.net.URL obj = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accent-Language", "en-US,en,q=0.5");
			con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", apiKey);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + apiURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Mark a subscription as Done or Undone
	public static final StringBuilder markDonationDone(String txn_id, String status) {
		if(serverID.isEmpty() || apiKey.isEmpty()) {
			System.out.println("[ERROR] Missing ServerID or API-Key!");
			return null;
		}
		if(txn_id.isEmpty() || status.isEmpty()) {
			System.out.println("[ERROR] Missing TXN-ID or Status!");
			return null;
		}
		try {
			apiURL = rawApiURL+"/"+txn_id+"/mark";
			java.net.URL obj = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accent-Language", "en-US,en,q=0.5");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", apiKey);
			
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(status);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			if(responseCode != 204) {
				System.out.println("\nSending 'POST' request to URL : " + apiURL);
				System.out.println("POST parameters : " + status);
				System.out.println("Response Code : " + responseCode);
			};
			
			BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream())
			);
			String inputLine;
			StringBuilder response = new StringBuilder();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
