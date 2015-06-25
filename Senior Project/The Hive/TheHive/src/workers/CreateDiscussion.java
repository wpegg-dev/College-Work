package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;
import dbconnection.LocalDatabaseConnection;

public class CreateDiscussion implements Runnable, Serializable {

	private static final long serialVersionUID = -2375138544675320699L;
	
	private String personId;
	private String groupId;
	private String discussionText;

	public CreateDiscussion(String personId, String groupId,
			String discussionText) {
		super();
		this.personId = personId;
		this.groupId = groupId;
		this.discussionText = discussionText;
	}

	@Override
	public void run() {
		this.createDiscussionTopic();
	}
	
	public void createDiscussionTopic()
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.CREATE_DISCUSSION_TOPIC);
			
			try
			{
				
				ps.setString(1, discussionText);
				ps.setInt(2, Integer.parseInt(groupId));
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

}
