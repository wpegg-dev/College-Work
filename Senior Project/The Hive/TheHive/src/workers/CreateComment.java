package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;
import dbconnection.LocalDatabaseConnection;

public class CreateComment implements Runnable, Serializable {

	private static final long serialVersionUID = 7179543065456801853L;
	
	private String personId;
	private String commentText;
	private String discussionId;
	private String associateCommentId;
	private String method;

	public CreateComment(String personId, String commentText, String discussionId, String method) {
		super();
		this.personId = personId;
		this.commentText = commentText;
		this.discussionId = discussionId;
		this.method = method;
	}
	
	public CreateComment(String personId, String commentText, String discussionId,
			String associateCommentId, String method) {
		super();
		this.personId = personId;
		this.commentText = commentText;
		this.discussionId = discussionId;
		this.associateCommentId = associateCommentId;
		this.method = method;
	}

	@Override
	public void run() {
		this.createCommentEntry();
	}
	
	public void createCommentEntry()
	{
		
		if((method).equals("createCommentFromRoot"))
		{
			LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
			
			Connection conn = lDbCon.getConnection();
			try 
			{
				PreparedStatement ps = conn.prepareStatement(Queries.CREATE_USER_COMMENT_FROM_ROOT_DISCUSSION);
				
				try
				{
					
					ps.setString(1, commentText);
					ps.setInt(2, Integer.parseInt(discussionId));
					ps.setInt(3, Integer.parseInt(personId));
					
					ps.executeUpdate();
					
				}
				finally
				{
					ps.close();
				}
			} 
			catch (SQLException e) 
			{
				lDbCon.closeConnection();
			}
			finally
			{
				lDbCon.closeConnection();
			}
		}
		else
		{
			LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
			
			Connection conn = lDbCon.getConnection();
			try 
			{
				PreparedStatement ps = conn.prepareStatement(Queries.CREATE_USER_COMMENT_FROM_COMMENT);
				
				try
				{
					
					ps.setString(1, commentText);
					ps.setInt(2, Integer.parseInt(discussionId));
					ps.setInt(3, Integer.parseInt(personId));
					ps.setInt(4, Integer.parseInt(associateCommentId));
					
					ps.executeUpdate();
					
				}
				finally
				{
					ps.close();
				}
				
			} 
			catch (SQLException e) 
			{
				lDbCon.closeConnection();
			}
			finally
			{
				lDbCon.closeConnection();
			}
		}
		
	}

}
