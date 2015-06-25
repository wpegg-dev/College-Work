package resources;

import java.io.Serializable;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class DiscussionData implements JSONAware, Serializable {

	private static final long serialVersionUID = -3428449175686227339L;
	
	private String discussionId;
	private String discussionTopic;
	private String groupId;
	private String personName;
	private String createDate;

	public DiscussionData(String discussionId, String discussionDesc,
			String groupId, String personName, String createDate) {
		super();
		this.discussionId = discussionId;
		this.discussionTopic = discussionDesc;
		this.groupId = groupId;
		this.personName = personName;
		this.createDate = createDate;
	}

	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject obj = new  JSONObject();
		
		obj.put("Discussion ID", this.discussionId);
		obj.put("Discussion Text", this.discussionTopic);
		obj.put("Group ID", this.groupId);
		obj.put("Person Name", this.personName);
		obj.put("Create Date", this.createDate);
		
		return obj.toJSONString();
	}

	public String getDiscussionId() {
		return discussionId;
	}

	public void setDiscussionId(String discussionId) {
		this.discussionId = discussionId;
	}

	public String getDiscussionDesc() {
		return discussionTopic;
	}

	public void setDiscussionDesc(String discussionDesc) {
		this.discussionTopic = discussionDesc;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getpersonName() {
		return personName;
	}

	public void setpersonName(String personName) {
		this.personName = personName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
