package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;

import dbconnection.LocalDatabaseConnection;

public class UpdateGroupSettings implements Runnable, Serializable {

	private static final long serialVersionUID = 4515682802443163458L;
	
	private String groupId;
	private String groupName;
	private String groupDescr;
	private String courseName;
	
	public UpdateGroupSettings(String groupId, String groupName,
			String groupDescr, String courseName) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupDescr = groupDescr;
		this.courseName = courseName;
	}

	@Override
	public void run() {
		this.updateGroupInfo();
	}
	
	public void updateGroupInfo()
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.UPDATE_GROUP_SETTINGS);
			
			try
			{
				
				ps.setString(1, groupName);
				ps.setString(2, courseName);
				ps.setString(3, groupDescr);
				ps.setString(4, groupId);
				
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
