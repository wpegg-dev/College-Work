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

import resources.CommentData;
import resources.DiscussionData;
import workers.CreateComment;
import workers.CreateDiscussion;
import workers.GatherComments;
import workers.GatherDiscssionTopics;
import workers.GenerateEmail;

/**
 * Servlet implementation class DiscussionDataService
 */
@WebServlet("/DiscussionDataService")
public class DiscussionDataService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DiscussionDataService() {
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
		
		if(("addTopic").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("personId")) &&
					StringHelper.isNotEmpty(request.getParameter("groupId")) &&
					StringHelper.isNotEmpty(request.getParameter("topic")))
			{
				String personId = request.getParameter("personId");
				String groupId = request.getParameter("groupId");
				String topic = request.getParameter("topic");
				
				CreateDiscussion cd = new CreateDiscussion(personId, groupId, topic);
				
				Thread cdt = new Thread(cd);
				
				cdt.run();
				
				waitForThread(cdt);
				
				JSONObject output = new JSONObject();
				JSONArray message = new JSONArray();

				message.add("success");
				
				output.put("discussionMessage", message);
				pw.println(output.toJSONString());
				
			}
		}
		if(("gatherTopics").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupId")))
			{
				String groupId = request.getParameter("groupId");
				
				GatherDiscssionTopics gdt = new GatherDiscssionTopics(groupId);
				
				Thread gdtt = new Thread(gdt);
				
				gdtt.run();
				
				waitForThread(gdtt);
				
				JSONObject output = new JSONObject();
				JSONArray discTopics = new JSONArray();

				Iterator<DiscussionData> topicsIterator = gdt.getTopics().iterator();
				while(topicsIterator.hasNext())
				{
					discTopics.add(topicsIterator.next());
				}
				
				
				output.put("discussionTopics", discTopics);
				pw.println(output.toJSONString());
				
			}
		}
		if(("gatherComments").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("discussionId")))
			{
				String discussionId = request.getParameter("discussionId");
				
				GatherComments gc = new GatherComments(discussionId);
				
				Thread gct = new Thread(gc);
				
				gct.run();
				
				waitForThread(gct);
				
				JSONObject output = new JSONObject();
				JSONArray discComments = new JSONArray();

				Iterator<CommentData> commentIterator = gc.getComments().iterator();
				while(commentIterator.hasNext())
				{
					discComments.add(commentIterator.next());
				}
				
				
				output.put("discussionComments", discComments);
				pw.println(output.toJSONString());
				
			}
		}
		if(("createCommentFromRoot").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("discussionId")) &&
					StringHelper.isNotEmpty(request.getParameter("commentText")) &&
					StringHelper.isNotEmpty(request.getParameter("personId")))
			{
				String discussionId = request.getParameter("discussionId");
				String personId = request.getParameter("personId");
				String commentText = request.getParameter("commentText");
				String method = request.getParameter("method");
				
				CreateComment cc = new CreateComment(personId, commentText, discussionId,method);
				
				Thread cct = new Thread(cc);
				
				cct.run();
				
				waitForThread(cct);
				
				JSONObject output = new JSONObject();
				JSONArray commentMessage = new JSONArray();

				commentMessage.add("success");
				
				output.put("commentMessage", commentMessage);
				pw.println(output.toJSONString());
				
			}
		}
		if(("createCommentFromComment").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("discussionId")) &&
					StringHelper.isNotEmpty(request.getParameter("commentText")) &&
					StringHelper.isNotEmpty(request.getParameter("personId")) &&
					StringHelper.isNotEmpty(request.getParameter("associateCommentId")))
			{
				String discussionId = request.getParameter("discussionId");
				String personId = request.getParameter("personId");
				String commentText = request.getParameter("commentText");
				String method = request.getParameter("method");
				String associateCommentId = request.getParameter("associateCommentId");
				
				CreateComment ccc = new CreateComment(personId, commentText, discussionId,associateCommentId,method);
				
				Thread ccct = new Thread(ccc);
				
				ccct.run();
				
				waitForThread(ccct);
				
				JSONObject output = new JSONObject();
				JSONArray commentMessage = new JSONArray();

				commentMessage.add("success");
				
				output.put("commentMessage", commentMessage);
				pw.println(output.toJSONString());
				
			}
		}
		if(("sendEmails").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("groupId")) &&
					StringHelper.isNotEmpty(request.getParameter("groupName")) &&
					StringHelper.isNotEmpty(request.getParameter("discussionName")))
			{
				String discussionName = request.getParameter("discussionName");
				String groupId = request.getParameter("groupId");
				String groupName = request.getParameter("groupName");
				
				GenerateEmail ge = new GenerateEmail(groupId, groupName, discussionName);
				
				Thread get = new Thread(ge);
				
				get.run();
				
				waitForThread(get);
				
				JSONObject output = new JSONObject();
				JSONArray commentMessage = new JSONArray();

				commentMessage.add("success");
				
				output.put("commentMessage", commentMessage);
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
