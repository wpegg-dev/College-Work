package workers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import common.Queries;

import dbconnection.LocalDatabaseConnection;
import resources.SearchItemData;

public class GatherSearchResults implements Runnable, Serializable {

	private static final long serialVersionUID = -7616601297485067944L;
	
	private String searchKeyword;
	private Vector<SearchItemData> searchItems;

	public GatherSearchResults(String searchKeyword) {
		super();
		this.searchKeyword = searchKeyword;
		this.searchItems = new Vector<SearchItemData>();
	}

	@Override
	public void run() {
		this.gatherSearchResults();

	}

	public void gatherSearchResults()
	{
		LocalDatabaseConnection lDbCon = new LocalDatabaseConnection();
		
		Connection conn = lDbCon.getConnection();
		try 
		{
			PreparedStatement getPeople = conn.prepareStatement(Queries.SEARCH_PEOPLE);
			//TODO: update groups to include profile pic
			PreparedStatement getGroups = conn.prepareStatement(Queries.SEARCH_GROUPS);
			
			try
			{
				
				getPeople.setString(1, "%"+searchKeyword+"%");
				//getPeople.setString(2, "%"+searchKeyword+"%");
				
				getGroups.setString(1, "%"+searchKeyword+"%");
				
				
				
				ResultSet rsP = getPeople.executeQuery();
				ResultSet rsG = getGroups.executeQuery();
				
				try
				{
					while(rsP.next())
					{
						String id = rsP.getString("ItemId");
						String name = rsP.getString("ItemName");
						String type = rsP.getString("ItemType");
						String profilePic = rsP.getString("ProfilePic");
						
						this.searchItems.add(new SearchItemData(id, name, type,profilePic));
						
					}
					while(rsG.next())
					{
						String id = rsG.getString("ItemId");
						String name = rsG.getString("ItemName");
						String type = rsG.getString("ItemType");
						String profilePic = rsG.getString("ProfilePic");
						
						this.searchItems.add(new SearchItemData(id, name, type,profilePic));
						
					}
					
				}
				finally
				{
					rsP.close();
					rsG.close();
				}
				

			}
			finally
			{
				getPeople.close();
				getGroups.close();
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
	
	public Vector<SearchItemData> getSearchItems() {
		return searchItems;
	}

}
