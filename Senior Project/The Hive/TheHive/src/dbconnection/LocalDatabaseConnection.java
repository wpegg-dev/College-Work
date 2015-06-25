package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class LocalDatabaseConnection {

	Connection con;

	public LocalDatabaseConnection()
	{
		try
		{
			String SQLDrive = "com.mysql.jdbc.Driver";
			String SQLURL = "jdbc:mysql://localhost:3306/test";
			String user = "root";
			String password = "root";
			
			
			Class.forName(SQLDrive).newInstance();
			con = DriverManager.getConnection(SQLURL,user,password);
		}
		catch (SQLException e)
		{
			System.out.println("SQL Error:" + e);
		}
		catch (Exception e)
		{
			System.out.println("Failed to load MySQL Driver" + e);
		}
	}
	
	public Connection getConnection()
	{
		return this.con;
	}
	
	public void closeConnection()
	{
		try
		{
			if(this.con != null)
			{
				this.con.close();
			}
		}
		catch (SQLException e)
		{
			SQLException e1 = ((SQLException)e).getNextException();
			if (e1 != null)
				System.out.println("SQL Error:" + e1);
		}
	}
}