package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import common.Queries;

import dbconnection.LocalDatabaseConnection;
import resources.UserData;

public class GatherPublicProfile implements Runnable, Serializable {

	private static final long serialVersionUID = -3877562932388111961L;
	
	private String personId;
	private Vector<UserData> usersData;

	public GatherPublicProfile(String personId) {
		super();
		this.personId = personId;
		this.usersData = new Vector<UserData>();
	}

	@Override
	public void run() {
		this.gatherPublicProfile();
	}
	
	public void gatherPublicProfile()
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.GATHER_PUBLIC_PROFILE_USER_DATA);
			
			try
			{
				ps.setString(1, personId);
				
				ResultSet rs = ps.executeQuery();
				
				try
				{
					while(rs.next())
					{
						String personId = rs.getString("personId");
						String fName = rs.getString("FirstName");
						String lName = rs.getString("LastName");
						String emailAdd = rs.getString("EmailAddress");
						String repo = "";
						String profilePic = rs.getString("ProfilePic");
						
						this.usersData.add(new UserData(personId, fName, lName, emailAdd,repo,profilePic));
						
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

	public Vector<UserData> getUsersData() {
		return usersData;
	}

}
