package dataservices;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.util.StringHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import resources.SimpleGroupData;
import resources.UserData;
import workers.CreateUserAccount;
import workers.GatherPublicProfile;
import workers.GatherSimpleGroupData;
import workers.GatherUserData;



/**
 * Servlet implementation class UserLoginService
 */
@WebServlet("/UserLoginService")
public class UserLoginService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserLoginService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		
		
		if(("signIn").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("emailAddress"))&&
					StringHelper.isNotEmpty(request.getParameter("password")))
			{
				
				String userEmailAddress = request.getParameter("emailAddress");
				String pass = request.getParameter("password");
				
				GatherUserData gud = new GatherUserData(userEmailAddress,pass);
				
				Thread gudt = new Thread(gud);
				
				gudt.run();
				
				waitForThread(gudt);
				
				Vector<UserData> accountCheck = gud.getUserInfo();
				Integer AccountCheckInt = accountCheck.size();
				
				if(AccountCheckInt < 1)
				{/* do nothing because account was not found  */}
				else
				{
					JSONObject output = new JSONObject();
					JSONArray userData = new JSONArray();
					
					Iterator<UserData> userIterator = gud.getUserInfo().iterator();
					while(userIterator.hasNext())
					{
						userData.add(userIterator.next());

					}
					
					output.put("userData", userData);
					pw.println(output.toJSONString());
				}
			}
		}
		if(("getPublic").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("personId")))
			{
				String pId = request.getParameter("personId");
				
				GatherPublicProfile gpp = new GatherPublicProfile(pId);
				
				Thread gppt = new Thread(gpp);
				
				gppt.run();
				
				waitForThread(gppt);
				
				JSONObject output1 = new JSONObject();
				JSONArray publicProfile = new JSONArray();
				
				Iterator<UserData> publicProfileIterator = gpp.getUsersData().iterator();
				while(publicProfileIterator.hasNext())
				{
					publicProfile.add(publicProfileIterator.next());

				}
				
				output1.put("publicProfileData", publicProfile);
				pw.println(output1.toJSONString());
			}
		}
		if(("getGroups").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("personId")))
			{
				String pId = request.getParameter("personId");
				
				GatherSimpleGroupData gsgd = new GatherSimpleGroupData(pId);
				
				Thread gsgdt = new Thread(gsgd);
				
				gsgdt.run();
				
				waitForThread(gsgdt);
				
				JSONObject output1 = new JSONObject();
				JSONArray sGroupData = new JSONArray();
				
				Iterator<SimpleGroupData> sGroupIterator = gsgd.getSimpleGroups().iterator();
				while(sGroupIterator.hasNext())
				{
					sGroupData.add(sGroupIterator.next());

				}
				
				output1.put("simpleGroupData", sGroupData);
				pw.println(output1.toJSONString());
			}
		}
		if(("signUp").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("firstName"))&&
					StringHelper.isNotEmpty(request.getParameter("lastName")) &&
					StringHelper.isNotEmpty(request.getParameter("emailAddress")) &&
					StringHelper.isNotEmpty(request.getParameter("password")))
			{
				
				String userEmailAddress = request.getParameter("emailAddress");
				String pass = request.getParameter("password");
				String fName = request.getParameter("firstName");
				String lName = request.getParameter("lastName");
				
				CreateUserAccount cua = new CreateUserAccount(fName,lName,userEmailAddress,pass);
				
				Thread cuat = new Thread(cua);
				
				cuat.run();
				
				waitForThread(cuat);
				
				Integer checkAccountExists = cua.getAccountExists();
				
				if(checkAccountExists < 1)
				{
					// do nothing because account exists
				}
				else
				{
					GatherUserData gud = new GatherUserData(userEmailAddress,pass);
					
					Thread gudt = new Thread(gud);
					
					gudt.run();
					
					waitForThread(gudt);
					
					JSONObject output = new JSONObject();
					JSONArray userData = new JSONArray();
					
					Iterator<UserData> userIterator = gud.getUserInfo().iterator();
					while(userIterator.hasNext())
					{
						userData.add(userIterator.next());
					}
					
					output.put("userData", userData);
					pw.println(output.toJSONString());
				}
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
