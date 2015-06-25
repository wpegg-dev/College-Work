package resources;

import java.io.Serializable;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class UserData implements JSONAware, Serializable {

	private static final long serialVersionUID = 7529713409485025612L;

	private String personId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String userRepository;
	private String profilePicture;
	
	
	
	public UserData(String personId, String firstName, String lastName, String emailAddress, String userRepository, String profilePicture) {
		super();
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.userRepository = userRepository;
		this.profilePicture = profilePicture;
	}


	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject obj = new  JSONObject();
		
		obj.put("Person ID", this.personId);
		obj.put("First Name", this.firstName);
		obj.put("Last Name", this.lastName);
		obj.put("Email Address", this.emailAddress);
		obj.put("User Repository", this.userRepository);
		obj.put("Profile Picture", this.profilePicture);
		
		return obj.toJSONString();
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public String getUserRepository() {
		return userRepository;
	}


	public void setUserRepository(String userRepository) {
		this.userRepository = userRepository;
	}


	public String getPersonId() {
		return personId;
	}


	public void setPersonId(String personId) {
		this.personId = personId;
	}


	public String getProfilePicture() {
		return profilePicture;
	}


	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

}
