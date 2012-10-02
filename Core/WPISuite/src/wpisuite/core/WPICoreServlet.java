package wpisuite.core;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import com.google.gson.Gson;

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
        
        System.arraycopy(path, 1, path, 0, path.length-1);
        path[path.length-1] = null;
        
        Model[] m = data.getModel(path);
        
        Gson gson = new Gson();
        
        out.println(gson.toJson(m, m.getClass()));
       

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