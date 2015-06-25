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

public class GatherUpdatedUserData implements Runnable, Serializable {

	private static final long serialVersionUID = -1859673298795829956L;
	
	private String personId;
	private Vector<UserData> userData;

	public GatherUpdatedUserData(String personId) {
		super();
		this.personId = personId;
		this.userData = new Vector<UserData>();
	}

	@Override
	public void run() {
		this.gatherUserData();
	}
	
	public void gatherUserData()
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.GATHER_UPDATED_USER_DATA);
			
			try
			{
				ps.setString(1, personId);
				
				ResultSet rs = ps.executeQuery();
				
				try
				{
					while(rs.next())
					{
						String personId = rs.getString("PersonID");
						String fName = rs.getString("FirstName");
						String lName = rs.getString("LastName");
						String emailAdd = rs.getString("EmailAdd");
						String repo = rs.getString("UserRepo");
						String profilePic = rs.getString("ProfilePic");
						
						this.userData.add(new UserData(personId, fName, lName, emailAdd,repo,profilePic));
						
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

	public Vector<UserData> getUserData() {
		return userData;
	}

}
