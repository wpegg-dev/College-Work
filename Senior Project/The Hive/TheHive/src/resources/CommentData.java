package resources;

import java.io.Serializable;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class CommentData implements JSONAware, Serializable {

	private static final long serialVersionUID = -1102350581660779291L;
	
	private String commentId;
	private String description;
	private String discussionId;
	private String associatedCommentId;
	private String createdBy;
	private String createDate;

	public CommentData(String commentId, String description,
			String discussionId, String associatedCommentId, String createdBy,
			String createDate) {
		super();
		this.commentId = commentId;
		this.description = description;
		this.discussionId = discussionId;
		this.associatedCommentId = associatedCommentId;
		this.createdBy = createdBy;
		this.createDate = createDate;
	}

	@SuppressWarnings("unchecked")
	public String toJSONString() {
		JSONObject obj = new  JSONObject();
		
		obj.put("Comment ID", this.commentId);
		obj.put("Comment Text", this.description);
		obj.put("Discussion ID", this.discussionId);
		obj.put("Associated Comment", this.associatedCommentId);
		obj.put("Created By", this.createdBy);
		obj.put("Created Date", this.createDate);
		
		return obj.toJSONString();
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiscussionId() {
		return discussionId;
	}

	public void setDiscussionId(String discussionId) {
		this.discussionId = discussionId;
	}

	public String getAssociatedCommentId() {
		return associatedCommentId;
	}

	public void setAssociatedCommentId(String associatedCommentId) {
		this.associatedCommentId = associatedCommentId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
