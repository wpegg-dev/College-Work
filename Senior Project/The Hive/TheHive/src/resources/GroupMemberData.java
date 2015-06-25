package resources;

import java.io.Serializable;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class GroupMemberData implements JSONAware, Serializable {

	private static final long serialVersionUID = -6889409108867186166L;
	
	private String personId;
	private String personName;
	private String emailAddress;

	public GroupMemberData(String personId, String personName, String emailAddress) {
		super();
		this.personId = personId;
		this.personName = personName;
		this.emailAddress = emailAddress;
	}

	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject obj = new  JSONObject();
		
		obj.put("Person ID", this.personId);
		obj.put("PErson Name", this.personName);
		obj.put("Email Address", this.emailAddress);
		
		
		return obj.toJSONString();
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
