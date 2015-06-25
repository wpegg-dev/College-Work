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

import resources.FullGroupData;
import resources.GroupMemberData;
import workers.AddUserToGroup;
import workers.CreateGroup;
import workers.CreateGroupAdmin;
import workers.GatherGroupDetails;
import workers.GatherGroupMembers;
import workers.RemoveGroupMember;
import workers.UpdateGroupPicture;
import workers.UpdateGroupSettings;



/**
 * Servlet implementation class GroupDataService
 */
@WebServlet("/GroupDataService")
public class GroupDataService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupDataService() {
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
		
		if(("getDetails").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupId")))
			{
				String groupId = request.getParameter("groupId");
				
				GatherGroupDetails ggd = new GatherGroupDetails(groupId);
				
				Thread ggdt = new Thread(ggd);
				
				ggdt.run();
				
				waitForThread(ggdt);
				
				JSONObject output = new JSONObject();
				JSONArray GroupDetails = new JSONArray();
				
				Iterator<FullGroupData> GroupIterator = ggd.getGroupDetails().iterator();
				while(GroupIterator.hasNext())
				{
					GroupDetails.add(GroupIterator.next());
				}
				
				output.put("GroupDetails", GroupDetails);
				pw.println(output.toJSONString());
			}
		}
		if(("createGroup").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupName")) &&
					StringHelper.isNotEmpty(request.getParameter("courseTitle")) &&
					StringHelper.isNotEmpty(request.getParameter("gDescr")) &&
					StringHelper.isNotEmpty(request.getParameter("personId")))
			{
				String groupName = request.getParameter("groupName");
				String courseName = request.getParameter("courseTitle");
				String groupDescription = request.getParameter("gDescr");
				String personId = request.getParameter("personId");
				
				CreateGroup cg = new CreateGroup(groupName, courseName, groupDescription);
				CreateGroupAdmin cga = new CreateGroupAdmin(personId,groupName);
				AddUserToGroup autg = new AddUserToGroup(personId, groupName);
				
				Thread cgt = new Thread(cg);
				Thread cgat = new Thread(cga);
				Thread autgt = new Thread(autg);
				
				cgt.run();
				cgat.run();
				autgt.run();
				
				waitForThread(cgt);
				waitForThread(cgat);
				waitForThread(autgt);
				
				JSONObject output = new JSONObject();
				JSONArray create = new JSONArray();
				
				create.add("Success");
				
				output.put("message", create);
				pw.println(output.toJSONString());
				
			}
		}
		if(("updateGroup").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupId")) &&
					StringHelper.isNotEmpty(request.getParameter("groupName")) &&
					StringHelper.isNotEmpty(request.getParameter("groupDescr")) && 
					StringHelper.isNotEmpty(request.getParameter("courseName")))
			{
				String groupId = request.getParameter("groupId");
				String groupName = request.getParameter("groupName");
				String groupDescr = request.getParameter("groupDescr");
				String courseName = request.getParameter("courseName");
				
				UpdateGroupSettings ugs = new UpdateGroupSettings(groupId, groupName, groupDescr, courseName);
				GatherGroupDetails ggd = new GatherGroupDetails(groupId);
				
				Thread ugst = new Thread(ugs);
				Thread ggdt = new Thread(ggd);
				
				ugst.run();
				ggdt.run();
				
				waitForThread(ugst);
				waitForThread(ggdt);
				
				JSONObject output = new JSONObject();
				JSONArray GroupDetails = new JSONArray();
				
				Iterator<FullGroupData> GroupIterator = ggd.getGroupDetails().iterator();
				while(GroupIterator.hasNext())
				{
					GroupDetails.add(GroupIterator.next());
				}
				
				output.put("GroupDetails", GroupDetails);
				pw.println(output.toJSONString());
			}
		}
		if(("updateGroupPicture").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupId")) &&
					StringHelper.isNotEmpty(request.getParameter("filePath")))
			{
				String groupId = request.getParameter("groupId");
				String filePath = request.getParameter("filePath");
				
				UpdateGroupPicture ugp = new UpdateGroupPicture(groupId, filePath);
				GatherGroupDetails ggd = new GatherGroupDetails(groupId);
				
				Thread ugpt = new Thread(ugp);
				Thread ggdt = new Thread(ggd);
				
				ugpt.run();
				ggdt.run();
				
				waitForThread(ugpt);
				waitForThread(ggdt);
				
				JSONObject output = new JSONObject();
				JSONArray GroupDetails = new JSONArray();
				
				Iterator<FullGroupData> GroupIterator = ggd.getGroupDetails().iterator();
				while(GroupIterator.hasNext())
				{
					GroupDetails.add(GroupIterator.next());
				}
				
				output.put("GroupDetails", GroupDetails);
				pw.println(output.toJSONString());
			}
		}
		if(("getMembers").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupId")))
			{
				String groupId = request.getParameter("groupId");
				
				GatherGroupMembers ggm = new GatherGroupMembers(groupId);
				
				Thread ggmt = new Thread(ggm);
				
				ggmt.run();
				
				waitForThread(ggmt);
				
				JSONObject output = new JSONObject();
				JSONArray GroupMembers = new JSONArray();
				
				Iterator<GroupMemberData> GroupMemberIterator = ggm.getMembers().iterator();
				while(GroupMemberIterator.hasNext())
				{
					GroupMembers.add(GroupMemberIterator.next());
				}
				
				output.put("GroupMembers", GroupMembers);
				pw.println(output.toJSONString());
			}
		}
		if(("removeMember").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupId")) &&
					StringHelper.isNotEmpty(request.getParameter("personId")))
			{
				String groupId = request.getParameter("groupId");
				String personId = request.getParameter("personId");
				
				RemoveGroupMember rgm = new RemoveGroupMember(personId, groupId);
				GatherGroupMembers ggm = new GatherGroupMembers(groupId);
				
				Thread rgmt = new Thread(rgm);
				Thread ggmt = new Thread(ggm);
				
				rgmt.run();
				ggmt.run();
				
				waitForThread(rgmt);
				waitForThread(ggmt);
				
				JSONObject output = new JSONObject();
				JSONArray GroupMembers = new JSONArray();
				
				Iterator<GroupMemberData> GroupMemberIterator = ggm.getMembers().iterator();
				while(GroupMemberIterator.hasNext())
				{
					GroupMembers.add(GroupMemberIterator.next());
				}
				
				output.put("GroupMembers", GroupMembers);
				pw.println(output.toJSONString());
			}
		}
		if(("addNewMember").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupName")) &&
					StringHelper.isNotEmpty(request.getParameter("groupId")) &&
					StringHelper.isNotEmpty(request.getParameter("personId")))
			{
				String groupName = request.getParameter("groupName");
				String personId = request.getParameter("personId");
				String groupId = request.getParameter("groupId");
				
				AddUserToGroup autg = new AddUserToGroup(personId, groupName);
				GatherGroupMembers ggm = new GatherGroupMembers(groupId);
				
				Thread autgt = new Thread(autg);
				Thread ggmt = new Thread(ggm);
				
				autgt.run();
				ggmt.run();
				
				waitForThread(autgt);
				waitForThread(ggmt);
				
				JSONObject output = new JSONObject();
				JSONArray GroupMembers = new JSONArray();
				
				Iterator<GroupMemberData> GroupMemberIterator = ggm.getMembers().iterator();
				while(GroupMemberIterator.hasNext())
				{
					GroupMembers.add(GroupMemberIterator.next());
				}
				
				output.put("GroupMembers", GroupMembers);
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
