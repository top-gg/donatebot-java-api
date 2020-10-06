package io.donatebot.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.text.SimpleDateFormat;
import org.json.JSONObject;

public class Donation {
	private String txn_id;
	private TransactionStatus status;
	private String buyer_email;
	private String buyer_id;
	private Boolean recurring;
	private String role_id;
	private String product_id;
	private String price;
	private String currency;
	private Date timestamp;
	private Map<String, String> seller_customs;
	
	public Donation() {
		
	}
	
	public Donation deserialize(JSONObject obj) {
		try {
			this.txn_id = obj.getString("txn_id");
			this.status = TransactionStatus.valueOf(obj.getString("status").toUpperCase());
			this.buyer_email = obj.getString("buyer_email");
			this.buyer_id = obj.getString("buyer_id");
			this.recurring = obj.getBoolean("recurring");
			this.role_id = obj.optString("role_id");
			this.product_id = obj.optString("product_id");
			this.price = obj.getString("price");
			this.currency = obj.getString("currency");
			long unixSeconds = obj.getLong("timestamp");
			this.timestamp = new Date(unixSeconds *1000);
			this.seller_customs = convertSellerCustoms(obj.optJSONObject("seller_customs"));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return this;
	}
	
	/**
	 * Take JSON object and convert to a Map of seller custom values
	 * @return
	 */
	private Map<String, String> convertSellerCustoms(JSONObject sellerCustoms) {
		Map<String, String> map = new HashMap<String, String>();
		
		if (sellerCustoms == null) {
			return map;
		}
		
		Iterator<String> keys = sellerCustoms.keys();
		
		while(keys.hasNext()) {
		    String key = keys.next();
		    map.put(key, sellerCustoms.getString(key));
		}
		
		return map;
	}

	public String getTransactionID() {
		return this.txn_id;
	}
	
	public Enum<TransactionStatus> getStatus() {
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
	
	public Date getTime() {
		return this.timestamp;
	}
	
	public Map<String, String> getSellerCustoms() {
		return this.seller_customs;
	}

}
