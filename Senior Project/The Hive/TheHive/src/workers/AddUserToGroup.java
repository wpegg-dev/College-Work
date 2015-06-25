package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;

import dbconnection.LocalDatabaseConnection;

public class AddUserToGroup implements Runnable, Serializable {

	private static final long serialVersionUID = 7878188403664601004L;
	
	private String groupName;
	private String personId;

	public AddUserToGroup(String personId, String groupName) {
		super();
		this.groupName = groupName;
		this.personId = personId;
	}

	@Override
	public void run() {
		this.addUserToGroup();
	}
	
	public void addUserToGroup()
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.CREATE_GROUP_ADD_USER_TO_GROUP);
			
			try
			{
				
				ps.setString(1, personId);
				ps.setString(2, groupName);
				
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
