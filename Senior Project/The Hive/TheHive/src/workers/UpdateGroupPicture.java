package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;

import dbconnection.LocalDatabaseConnection;

public class UpdateGroupPicture implements Runnable, Serializable {

	private static final long serialVersionUID = 4508169795305785496L;
	
	private String groupId;
	private String filePath;

	public UpdateGroupPicture(String groupId, String filePath) {
		super();
		this.groupId = groupId;
		this.filePath = filePath;
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
			PreparedStatement ps = conn.prepareStatement(Queries.UPDATE_GROUP_PROFILE_PICTURE);
			
			try
			{
				
				ps.setString(1, filePath);
				ps.setString(2, groupId);
				
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
