package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;

import dbconnection.LocalDatabaseConnection;

public class UpdateProfilePicture implements Runnable, Serializable {

	private static final long serialVersionUID = -7041643846765322348L;
	
	private String personId;
	private String profilePicPath;

	public UpdateProfilePicture(String personId, String profilePicPath) {
		super();
		this.personId = personId;
		this.profilePicPath = profilePicPath;
	}

	@Override
	public void run() {
		this.updatePicture();
	}

	public void updatePicture(){
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.UPDATE_USER_PROFILE_PICTURE);
			
			try
			{
				
				ps.setString(1, profilePicPath);
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
