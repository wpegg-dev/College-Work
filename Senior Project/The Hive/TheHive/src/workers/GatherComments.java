package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import common.Queries;

import dbconnection.LocalDatabaseConnection;
import resources.CommentData;

public class GatherComments implements Runnable, Serializable {

	private static final long serialVersionUID = 4649160293598729243L;

	private String discussionId;
	private Vector<CommentData> comments;
	
	public GatherComments(String discussionId) {
		super();
		this.discussionId = discussionId;
		this.comments = new Vector<CommentData>();
	}

	@Override
	public void run() {
		this.gatherComments();
	}
	
	public void gatherComments(){
LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.GATHER_COMMENTS);
			
			try
			{
				ps.setString(1, discussionId);
				
				ResultSet rs = ps.executeQuery();
				
				try
				{
					while(rs.next())
					{
						String commentId = rs.getString("ID");
						String description = rs.getString("Description");
						String discussionId = rs.getString("DiscussionID");
						String createdDate = rs.getString("CreatedDate");
						String associatedComment = rs.getString("AssociatedComment");
						String person = rs.getString("PersonName");
						
						this.comments.add(new CommentData(commentId, description, discussionId, associatedComment, person, createdDate));
						
					}
				}
				finally
				{
					rs.close();
				}
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

	public Vector<CommentData> getComments() {
		return comments;
	}

}
