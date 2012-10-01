package wpisuite.core;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import wpisuite.models.*;
public class WPICoreServlet extends HttpServlet 
{

	MockDataStore data;
	
	public WPICoreServlet()
	{
		data = MockDataStore.getMockDataStore();
	}
	
	public void doGet (HttpServletRequest req,
                       HttpServletResponse res) throws ServletException, IOException
	{
        PrintWriter out = res.getWriter();
        String delims = "[/]+";
        String[] path = req.getPathInfo().split(delims);
        if(path[1].equalsIgnoreCase("user"))
        {
        	if(path.length < 3)
        	{
        		User[] u = data.getUser("");
            	out.println(User.toJSON(u));
        	}
        	else
        	{
	        	User[] u = data.getUser(path[2]);
	        	out.println(User.toJSON(u));
        	}
        }
        else
        {
        	if(path.length < 3)
        	{
        		Project[] u = data.getProject(-1);
            	out.println(Project.toJSON(u));
        	}
        	else
        	{
	        	Project[] u = data.getProject(Integer.parseInt(path[2]));
	        	out.println(Project.toJSON(u));
        	}

        }
        out.close();
	}
	
	public void doPut (HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException
    {
		BufferedReader in = req.getReader();
		String delims = "[/]+";
        String[] path = req.getPathInfo().split(delims);
        if(path[1].equalsIgnoreCase("user"))
        {
    		data.addUser(in.readLine());

        }
        else
        {
    		data.addProject(in.readLine());

        }
    }
}