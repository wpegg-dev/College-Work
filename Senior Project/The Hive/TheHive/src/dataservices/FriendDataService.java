package dataservices;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.util.StringHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import resources.UserData;
import workers.AddFriend;
import workers.GatherFriendsList;

/**
 * Servlet implementation class FriendDataService
 */
@WebServlet("/FriendDataService")
public class FriendDataService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendDataService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		
		if(("checkFriends").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("personId")))
			{
				String searchPersonId = request.getParameter("personId");
				
				GatherFriendsList gfl = new GatherFriendsList(searchPersonId);
				
				Thread gflt = new Thread(gfl);
				
				gflt.run();
				
				waitForThread(gflt);
				
				JSONObject output = new JSONObject();
				JSONArray FriendsList = new JSONArray();
				
				Iterator<UserData> friendIterator = gfl.getFriends().iterator();
				while(friendIterator.hasNext())
				{
					FriendsList.add(friendIterator.next());
				}
				
				output.put("friendsList", FriendsList);
				pw.println(output.toJSONString());
			}
		}
		if(("addFriend").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("personId")) &&
					StringHelper.isNotEmpty(request.getParameter("friendId")))
			{
				String personId = request.getParameter("personId");
				String friendId = request.getParameter("friendId");
				
				AddFriend af = new AddFriend(personId, friendId);
				
				Thread aft = new Thread(af);
				
				aft.run();
				
				waitForThread(aft);
				
				JSONObject output = new JSONObject();
				JSONArray message = new JSONArray();

				message.add("success");
				
				output.put("friendMessage", message);
				pw.println(output.toJSONString());
			}
		}
		
	}
	
	private void waitForThread(Thread thread)
	{
		if(thread.isAlive())
		{
			try
			{
				thread.join();
			}
			catch(InterruptedException e)
			{
				System.out.println("Thread Interrupted: "+e.getMessage());
			}
		}
	}

}
