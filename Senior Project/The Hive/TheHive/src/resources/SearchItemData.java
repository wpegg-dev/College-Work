package resources;

import java.io.Serializable;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class SearchItemData implements JSONAware, Serializable {

	private static final long serialVersionUID = 7627722326889203435L;
	
	private String itemId;
	private String itemName;
	private String itemType;
	private String itemProfilePicture;
	

	public SearchItemData(String itemId, String itemName, String itemType, String itemProfilePicture) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemType = itemType;
		this.itemProfilePicture = itemProfilePicture;
	}


	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject obj = new  JSONObject();
		
		obj.put("Searched Item ID", this.itemId);
		obj.put("Searched Item Name", this.itemName);
		obj.put("Searched Item Type", this.itemType);
		obj.put("Searched Item Profile Picture", this.itemProfilePicture);
		
		return obj.toJSONString();
	}


	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getItemType() {
		return itemType;
	}


	public void setItemType(String itemType) {
		this.itemType = itemType;
	}


	public String getItemProfilePicture() {
		return itemProfilePicture;
	}


	public void setItemProfilePicture(String itemProfilePicture) {
		this.itemProfilePicture = itemProfilePicture;
	}

}
