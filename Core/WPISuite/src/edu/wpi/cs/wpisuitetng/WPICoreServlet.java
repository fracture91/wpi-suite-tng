package edu.wpi.cs.wpisuitetng;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * Primary servlet for the WPISuite service
 * 
 * This class handles incoming API requests over HTTP
 * 
 * @author Mike Della Donna
 *
 */
public class WPICoreServlet extends HttpServlet 
{
	private static final long serialVersionUID = -7156601241025735047L;
	
	/**
	 * Empty Constructor
	 */
	public WPICoreServlet()
	{
	}
	
	/**
	 * Forwards get requests and restful parameters to the ManagerLayer singleton
	 */
	public void doGet (HttpServletRequest req,
                       HttpServletResponse res) throws ServletException, IOException
	{
        PrintWriter out = res.getWriter();
        String delims = "[/]+";
        String[] path = req.getPathInfo().split(delims);
        
        System.arraycopy(path, 1, path, 0, path.length-1);
        path[path.length-1] = null;
        
        out.println(ManagerLayer.getInstance().read(path));
       
        out.close();
	}
	
	/**
	 * Forwards put requests and restful parameters to the ManagerLayer singleton
	 */
	public void doPut (HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException
    {
		BufferedReader in = req.getReader();
		PrintWriter out = res.getWriter();
		String delims = "[/]+";
        String[] path = req.getPathInfo().split(delims);
        
        System.arraycopy(path, 1, path, 0, path.length-1);
        path[path.length-1] = null;
        
        out.println(ManagerLayer.getInstance().create(path,in.readLine()));
        
        out.close();
    }
	
	/**
	 * Forwards post requests and restful parameters to the ManagerLayer singleton
	 */
	public void doPost (HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException
    {
		BufferedReader in = req.getReader();
		PrintWriter out = res.getWriter();
		String delims = "[/]+";
        String[] path = req.getPathInfo().split(delims);
        
        System.arraycopy(path, 1, path, 0, path.length-1);
        path[path.length-1] = null;
        
        out.println(ManagerLayer.getInstance().update(path,in.readLine()));
        
        out.close();
    }
	
	/**
	 * Forwards delete requests and restful parameters to the ManagerLayer singleton
	 */
	public void doDelete (HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException
    {
		PrintWriter out = res.getWriter();
		String delims = "[/]+";
        String[] path = req.getPathInfo().split(delims);
        
        System.arraycopy(path, 1, path, 0, path.length-1);
        path[path.length-1] = null;
        
        out.println(ManagerLayer.getInstance().delete(path));
        
        out.close();
    }
}