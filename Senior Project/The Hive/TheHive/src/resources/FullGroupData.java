package resources;

import java.io.Serializable;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class FullGroupData implements JSONAware, Serializable {

	private static final long serialVersionUID = 3402292939144886231L;
	
	private String groupId;
	private String groupName;
	private String classTitle;
	private String groupDescription;
	private String repositoryRoot;
	private String personName;
	private String profilePic;

	public FullGroupData(String groupId, String groupName, String classTitle, String groupDescription,
			String repositoryRoot, String personName, String profilePic) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.classTitle = classTitle;
		this.repositoryRoot = repositoryRoot;
		this.personName = personName;
		this.groupDescription = groupDescription;
		this.profilePic = profilePic;
	}

	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject obj = new  JSONObject();
		
		obj.put("Group ID", this.groupId);
		obj.put("Group Name", this.groupName);
		obj.put("Class Title", this.classTitle);
		obj.put("Group Description", this.groupDescription);
		obj.put("Repository Root", this.repositoryRoot);
		obj.put("Person ID", this.personName);
		obj.put("Profile Picture", this.profilePic);
		
		return obj.toJSONString();
	}

	public String getgroupId() {
		return groupId;
	}

	public void setgroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getClassTitle() {
		return classTitle;
	}

	public void setClassTitle(String classTitle) {
		this.classTitle = classTitle;
	}

	public String getRepositoryRoot() {
		return repositoryRoot;
	}

	public void setRepositoryRoot(String repositoryRoot) {
		this.repositoryRoot = repositoryRoot;
	}

	public String getPersonId() {
		return personName;
	}

	public void setPersonId(String personName) {
		this.personName = personName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

}
