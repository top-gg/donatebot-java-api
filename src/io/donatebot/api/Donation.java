package io.donatebot.api;

import java.util.Date;

import java.text.SimpleDateFormat;
import org.json.JSONObject;

public class Donation {
	private String txn_id = "";
	private Status status;
	private String buyer_email = "";
	private String buyer_id = "";
	private Boolean recurring = false;
	private String role_id = "";
	private String product_id = "";
	private String price = "";
	private String currency = "";
	private String timestamp = "";
	private String[] seller_customs;
	
	public Donation(JSONObject obj) {
		try {
			this.txn_id = obj.getString("txn_id");
			this.status = Status.valueOf(obj.getString("status").toUpperCase());
			this.buyer_email = obj.getString("buyer_email");
			this.buyer_id = obj.getString("buyer_id");
			this.recurring = obj.getBoolean("recurring");
			this.role_id = obj.optString("role_id");
			this.product_id = obj.optString("product_id");
			this.price = obj.getString("price");
			this.currency = obj.getString("currency");
			
			//long unixSeconds = obj.getLong("timestamp");

			//Date date = new java.util.Date(unixSeconds*1000L); 
			//SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); 
			//sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4")); 
			//String timestamp = sdf.format(date);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	public String getTransactionID() {
		return this.txn_id;
	}
	
	public Enum<Status> getStatus() {
		return this.status;
	}
	
	public String getBuyerEmail() {
		return this.buyer_email;
	}
	
	public String getBuyerID() {
		return this.buyer_id;
	}
	
	public Boolean getRecurring() {
		return this.recurring;
	}
	
	public String getRoleID() {
		return this.role_id;
	}
	
	public String getProductID() {
		return this.product_id;
	}
	
	public String getPrice() {
		return this.price;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public String getTime() {
		return this.timestamp;
	}


}
