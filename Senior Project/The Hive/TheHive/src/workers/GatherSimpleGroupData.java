package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import common.Queries;

import dbconnection.LocalDatabaseConnection;
import resources.SimpleGroupData;

public class GatherSimpleGroupData implements Runnable, Serializable {

	private static final long serialVersionUID = 2961652013425755670L;
	
	private String personId;
	private Vector<SimpleGroupData> simpleGroups;

	public GatherSimpleGroupData(String personId) {
		super();
		this.personId = personId;
		this.simpleGroups = new Vector<SimpleGroupData>();
	}

	@Override
	public void run() {
		this.gatherSimpleGroupData();
	}
	
	public void gatherSimpleGroupData()
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement ps = conn.prepareStatement(Queries.GATHER_SIMPLE_GROUP_DATA);
			
			try
			{
				ps.setString(1, personId);
				
				ResultSet rs = ps.executeQuery();
				
				try
				{
					while(rs.next())
					{
						String groupId = rs.getString("GroupId");
						String gName = rs.getString("Name");
						
						this.simpleGroups.add(new SimpleGroupData(groupId,gName));
						
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

	public Vector<SimpleGroupData> getSimpleGroups() {
		return simpleGroups;
	}

}
