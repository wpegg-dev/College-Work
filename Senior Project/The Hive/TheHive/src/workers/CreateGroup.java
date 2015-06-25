package workers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.Queries;
import common.RandomStringGen;
import dbconnection.LocalDatabaseConnection;

public class CreateGroup implements Serializable, Runnable {

	private static final long serialVersionUID = -5995835905434390816L;
	
	private String groupName;
	private String classTitle;
	private String groupDescription;

	public CreateGroup(String groupName, String classTitle, String groupDescription) {
		super();
		this.groupName = groupName;
		this.classTitle = classTitle;
		this.groupDescription = groupDescription;
	}

	@Override
	public void run() {
		try {
			this.CreateGroupEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CreateGroupEntry() throws IOException
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.CREATE_GROUP);
			
			try
			{
				RandomStringGen rsg = new RandomStringGen();
				
				String tempRepo = groupName+rsg.generateRandomString();
				String repo = tempRepo.replaceAll("\\s+","");
				new File("/var/www/GroupRepo/"+repo).mkdir();
				
				Runtime.getRuntime().exec("sudo chown www-data:www-data "+"/var/www/GroupRepo/"+repo);
				
				ps.setString(1, groupName);
				ps.setString(2, classTitle);
				ps.setString(3,groupDescription);
				ps.setString(4, repo);
				
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
