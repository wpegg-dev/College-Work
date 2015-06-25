package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;

import dbconnection.LocalDatabaseConnection;

public class RemoveGroupMember implements Runnable, Serializable {

	private static final long serialVersionUID = 7288496517077572146L;
	
	private String personId;
	private String groupId;

	public RemoveGroupMember(String personId, String groupId) {
		super();
		this.personId = personId;
		this.groupId = groupId;
	}

	@Override
	public void run() {
		this.removeMember();
	}
	
	public void removeMember(){
		
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.REMOVE_MEMBER_FROM_GROUP);
			
			try
			{
				
				ps.setString(1, groupId);
				ps.setString(2, personId);
				
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
