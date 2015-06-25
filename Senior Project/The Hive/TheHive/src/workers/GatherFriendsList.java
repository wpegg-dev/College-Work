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

public class GatherFriendsList implements Runnable, Serializable {

	private static final long serialVersionUID = -4469197751294710878L;
	
	private String personId;
	private Vector<UserData> friends;

	public GatherFriendsList(String personId) {
		super();
		this.personId = personId;
		this.friends = new Vector<UserData>();
	}

	@Override
	public void run() {
		this.gatherFriends();
	}
	
	public void gatherFriends()
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.GATHER_FRIENDS_LIST);
			
			try
			{
				ps.setString(1, personId);
				
				ResultSet rs = ps.executeQuery();
				
				try
				{
					while(rs.next())
					{
						String friendId = rs.getString("FriendId");
						String friendFirstName = rs.getString("FirstName");
						String friendLastName = rs.getString("LastName");
						String friendEmail = rs.getString("EmailAddress");
						String friendProfilePic = rs.getString("ProfilePic");
						
						this.friends.add(new UserData(friendId, friendFirstName, friendLastName, friendEmail, "",friendProfilePic));
					 	
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

	public Vector<UserData> getFriends() {
		return friends;
	}

}
