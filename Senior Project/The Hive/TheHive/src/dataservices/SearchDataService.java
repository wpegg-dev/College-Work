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

import resources.SearchItemData;
import workers.GatherSearchResults;

/**
 * Servlet implementation class SearchDataService
 */
@WebServlet("/SearchDataService")
public class SearchDataService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchDataService() {
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
		
		if(("search").equals(request.getParameter("method")))
		{
			if(StringHelper.isNotEmpty(request.getParameter("keyword")))
			{
				String searchKeyword = request.getParameter("keyword");
				
				GatherSearchResults gsr = new GatherSearchResults(searchKeyword);
				
				Thread gsrt = new Thread(gsr);
				
				gsrt.run();
				
				waitForThread(gsrt);
				
				JSONObject output = new JSONObject();
				JSONArray SearchDetails = new JSONArray();
				
				Iterator<SearchItemData> searchIterator = gsr.getSearchItems().iterator();
				while(searchIterator.hasNext())
				{
					SearchDetails.add(searchIterator.next());
				}
				
				output.put("searchResults", SearchDetails);
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
