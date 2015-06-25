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

public class CreateUserAccount implements Runnable, Serializable {

	private static final long serialVersionUID = -6922167758621118889L;

	private String fname;
	private String lname;
	private String emailAddr;
	private String password;
	private Integer accountExists;
	
	public CreateUserAccount(String fname, String lname, String emailAddr,
			String password) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.emailAddr = emailAddr;
		this.password = password;
		this.accountExists = -1;
	}

	@Override
	public void run() 
	{
		try {
			this.CreateAccount();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void CreateAccount() throws IOException
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.CREATE_USER_ACCOUNT);
			
			try
			{
				RandomStringGen rsg = new RandomStringGen();
				
				String tempRepo = fname+lname+rsg.generateRandomString();
				String repo = tempRepo.replaceAll("\\s+","");
				new File("/var/www/UserRepo/"+repo).mkdir();
				
				Runtime.getRuntime().exec("sudo chown www-data:www-data "+"/var/www/UserRepo/"+repo);
				
				String genericProfilePic = "ProfilePictures/generic_user_image.png";
				
				ps.setString(1, fname);
				ps.setString(2, lname);
				ps.setString(3, emailAddr);
				ps.setString(4, password);
				ps.setString(5,repo);
				ps.setString(6,genericProfilePic);
				
				ps.execute();
				accountExists = ps.getUpdateCount();
				
			}
			finally
			{
				ps.close();
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			lDbCon.closeConnection();
		}
		finally
		{
			lDbCon.closeConnection();
		}
	}

	public Integer getAccountExists() {
		return accountExists;
	}

}
