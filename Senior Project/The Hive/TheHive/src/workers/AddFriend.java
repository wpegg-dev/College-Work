package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;

import dbconnection.LocalDatabaseConnection;

public class AddFriend implements Runnable, Serializable {

	private static final long serialVersionUID = -4679707274779007555L;
	
	private String personId;
	private String friendId;

	public AddFriend(String personId, String friendId) {
		super();
		this.personId = personId;
		this.friendId = friendId;
	}

	@Override
	public void run() {
		this.addToFriends();
	}

	public void addToFriends(){
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.ADD_PERSON_AS_FRIEND);
			
			try
			{
				
				ps.setString(1, personId);
				ps.setString(2, friendId);
				
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
