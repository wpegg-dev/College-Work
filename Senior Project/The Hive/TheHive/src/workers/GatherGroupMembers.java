package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import common.Queries;
import dbconnection.LocalDatabaseConnection;
import resources.GroupMemberData;

public class GatherGroupMembers implements Runnable, Serializable {

	private static final long serialVersionUID = -3450131552655795888L;
	
	private String groupId;
	private Vector<GroupMemberData> members;

	public GatherGroupMembers(String groupId) {
		super();
		this.groupId = groupId;
		this.members = new Vector<GroupMemberData>();
	}

	@Override
	public void run() {
		this.gatherMembers();
	}
	
	public void gatherMembers(){
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.GATHER_GROUP_MEMBERS);
			
			try
			{
				ps.setString(1, groupId);
				
				ResultSet rs = ps.executeQuery();
				
				try
				{
					while(rs.next())
					{
						String personId = rs.getString("ID");
						String personName = rs.getString("PersonName");
						String emailAddress = rs.getString("EmailAddress");
						
						this.members.add(new GroupMemberData(personId, personName, emailAddress));
						
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
	};

	public Vector<GroupMemberData> getMembers() {
		return members;
	}

}
