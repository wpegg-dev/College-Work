package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import common.Queries;

import dbconnection.LocalDatabaseConnection;
import resources.FullGroupData;

public class GatherGroupDetails implements Runnable, Serializable {

	private static final long serialVersionUID = 5175785053220565269L;
	
	private String groupId;
	private Vector<FullGroupData> groupDetails;

	public GatherGroupDetails(String groupId) {
		super();
		this.groupId = groupId;
		this.groupDetails = new Vector<FullGroupData>();
	}

	public Vector<FullGroupData> getGroupDetails() {
		return groupDetails;
	}

	@Override
	public void run() {
		this.getGroupDetailList();
	}
	
	public void getGroupDetailList(){
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.GATHER_GROUP_DETAILS);
			
			try
			{
				ps.setString(1, groupId);
				
				ResultSet rs = ps.executeQuery();
				
				try
				{
					while(rs.next())
					{
						String groupId = rs.getString("ID");
						String groupName = rs.getString("GroupName");
						String classTitle = rs.getString("ClassTitle");
						String groupRepo = rs.getString("Repository");
						String admin = rs.getString("AdminName");
						String groupDescr = rs.getString("Descr");
						String profilePic = rs.getString("ProfilePic");
						
						this.groupDetails.add(new FullGroupData(groupId, groupName, classTitle, groupDescr, groupRepo, admin,profilePic));
						
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

}
