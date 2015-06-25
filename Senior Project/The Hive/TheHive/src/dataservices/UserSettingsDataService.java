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
import workers.GatherUpdatedUserData;
import workers.UpdateProfilePicture;
import workers.UpdateUserSettings;

/**
 * Servlet implementation class UserSettingsDataService
 */
@WebServlet("/UserSettingsDataService")
public class UserSettingsDataService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserSettingsDataService() {
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
		
		if(("updateNoPass").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("personId")) &&
					StringHelper.isNotEmpty(request.getParameter("firstName")) &&
					StringHelper.isNotEmpty(request.getParameter("lastName")) &&
					StringHelper.isNotEmpty(request.getParameter("emailAddress")))
			{
				String personId = request.getParameter("personId");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String email = request.getParameter("emailAddress");
				String method = request.getParameter("method");
				
				UpdateUserSettings uus = new UpdateUserSettings(personId, firstName, lastName, email, method);
				GatherUpdatedUserData guud = new GatherUpdatedUserData(personId);
				
				Thread uust = new Thread(uus);
				Thread guudt = new Thread(guud);
				
				uust.run();
				guudt.run();
				
				waitForThread(uust);
				waitForThread(guudt);
				
				JSONObject output = new JSONObject();
				JSONArray UserData = new JSONArray();
				
				Iterator<UserData> userDataIterator = guud.getUserData().iterator();
				while(userDataIterator.hasNext())
				{
					UserData.add(userDataIterator.next());
				}
				
				output.put("userData", UserData);
				pw.println(output.toJSONString());
			}
		}
		if(("updateWithPass").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("personId")) &&
					StringHelper.isNotEmpty(request.getParameter("firstName")) &&
					StringHelper.isNotEmpty(request.getParameter("lastName")) &&
					StringHelper.isNotEmpty(request.getParameter("emailAddress")) &&
					StringHelper.isNotEmpty(request.getParameter("password")))
			{
				String personId = request.getParameter("personId");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String email = request.getParameter("emailAddress");
				String password = request.getParameter("password");
				String method = request.getParameter("method");
				
				UpdateUserSettings uus = new UpdateUserSettings(personId, firstName, lastName, email, method, password);
				GatherUpdatedUserData guud = new GatherUpdatedUserData(personId);
				
				Thread uust = new Thread(uus);
				Thread guudt = new Thread(guud);
				
				uust.run();
				guudt.run();
				
				waitForThread(uust);
				waitForThread(guudt);
				
				JSONObject output = new JSONObject();
				JSONArray UserData = new JSONArray();
				
				Iterator<UserData> userDataIterator = guud.getUserData().iterator();
				while(userDataIterator.hasNext())
				{
					UserData.add(userDataIterator.next());
				}
				
				output.put("userData", UserData);
				pw.println(output.toJSONString());
			}
		}
		if(("updateProfilePic").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("personId")) &&
					StringHelper.isNotEmpty(request.getParameter("picturePath")))
			{
				String personId = request.getParameter("personId");
				String pathName = request.getParameter("picturePath");
				
				UpdateProfilePicture upp = new UpdateProfilePicture(personId, pathName);
				GatherUpdatedUserData guud = new GatherUpdatedUserData(personId);
				
				Thread uppt = new Thread(upp);
				Thread guudt = new Thread(guud);
				
				uppt.run();
				guudt.run();
				
				waitForThread(uppt);
				waitForThread(guudt);
				
				JSONObject output = new JSONObject();
				JSONArray UserData = new JSONArray();
				
				Iterator<UserData> userDataIterator = guud.getUserData().iterator();
				while(userDataIterator.hasNext())
				{
					UserData.add(userDataIterator.next());
				}
				
				output.put("userData", UserData);
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
