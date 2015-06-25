package workers;

import java.io.Serializable;
import java.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.Queries;

import dbconnection.LocalDatabaseConnection;

import resources.UserData;

public class GatherUserData implements Runnable, Serializable {

	private static final long serialVersionUID = 5418664689962260395L;

	private String emailAdd;
	private String password;
	private Vector<UserData> userInfo;
	
	public GatherUserData(String emailAdd, String password) {
		super();
		this.emailAdd = emailAdd;
		this.password = password;
		this.userInfo = new Vector<UserData>();
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
			PreparedStatement ps = conn.prepareStatement(Queries.LOGIN_GATHER_ACCOUNT_DATA);
			
			try
			{
				ps.setString(1, emailAdd);
				ps.setString(2, password);
				
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
						
						this.userInfo.add(new UserData(personId, fName, lName, emailAdd,repo,profilePic));
						
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

	public Vector<UserData> getUserInfo() {
		return userInfo;
	}

}
