package resources;

import java.io.Serializable;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class SimpleGroupData implements JSONAware, Serializable {

	private static final long serialVersionUID = -828694648952449125L;
	
	private String groupId;
	private String groupName;

	public SimpleGroupData(String groupId, String groupName) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
	}
	
	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject obj = new  JSONObject();
		
		obj.put("Group ID", this.groupId);
		obj.put("Group Name", this.groupName);
		
		return obj.toJSONString();
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
