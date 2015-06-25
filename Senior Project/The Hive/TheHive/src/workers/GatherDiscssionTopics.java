package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import common.Queries;

import dbconnection.LocalDatabaseConnection;
import resources.DiscussionData;

public class GatherDiscssionTopics implements Runnable, Serializable {

	private static final long serialVersionUID = -3722493281957835704L;
	
	private String groupId;
	private Vector<DiscussionData> topics;

	public GatherDiscssionTopics(String groupId) {
		super();
		this.groupId = groupId;
		this.topics = new Vector<DiscussionData>();
	}

	@Override
	public void run() {
		this.gatherTopics();
	}
	
	public void gatherTopics(){
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.GATHER_GROUP_DISCUSSION_TOPICS);
			
			try
			{
				ps.setString(1, groupId);
				
				ResultSet rs = ps.executeQuery();
				
				try
				{
					while(rs.next())
					{
						String discussionId = rs.getString("ID");
						String topic = rs.getString("Topic");
						String groupId = rs.getString("GroupId");
						String personName = rs.getString("PersonName");
						String createDate = rs.getString("CreatedDate");
						
						this.topics.add(new DiscussionData(discussionId, topic, groupId, personName, createDate));
						
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
	};

	public Vector<DiscussionData> getTopics() {
		return topics;
	}

}
