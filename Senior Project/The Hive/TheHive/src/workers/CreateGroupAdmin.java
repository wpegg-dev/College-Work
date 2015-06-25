package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;
import dbconnection.LocalDatabaseConnection;

public class CreateGroupAdmin implements Runnable, Serializable {

	private static final long serialVersionUID = 3106553359436045906L;
	
	private String groupName;
	private String personId;

	public CreateGroupAdmin(String personId, String groupName) {
		super();
		this.groupName = groupName;
		this.personId = personId;
	}

	@Override
	public void run() {
		this.createGroupsAdmin();
	}
	
	public void createGroupsAdmin()
		{
			LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
			
			Connection conn = lDbCon.getConnection();
			try 
			{
				PreparedStatement ps = conn.prepareStatement(Queries.CREATE_GROUP_ADMIN);
				
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
