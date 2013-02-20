package edu.wpi.cs.wpisuitetng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;

/**
 * Servlet implementation class WPIAdvancedServlet
 */
@WebServlet("/AdvancedAPI")
public class WPIAdvancedServlet extends HttpServlet {
	

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WPIAdvancedServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
        PrintWriter out = response.getWriter();
        String delims = "[/]+";
        String[] path = request.getPathInfo().split(delims);
        
        System.arraycopy(path, 1, path, 0, path.length-1);
        path[path.length-1] = null;
        response.setStatus(HttpServletResponse.SC_OK);
        try {
			out.println(ManagerLayer.getInstance().advancedGet(path,request.getCookies()));
		} catch (WPISuiteException e) {
			response.setStatus(e.getStatus());
		}
       
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	
		BufferedReader in = req.getReader();
		PrintWriter out = res.getWriter();
		String delims = "[/]+";
        String[] path = req.getPathInfo().split(delims);
        
        System.arraycopy(path, 1, path, 0, path.length-1);
        path[path.length-1] = null;
        
        res.setStatus(HttpServletResponse.SC_OK);
        try {
        	out.println(ManagerLayer.getInstance().advancedPost(path,in.readLine(),req.getCookies()));
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
		}
        
        out.close();
	}
	
	/**
	 * A note on advanced PUT.  The content body cannot contain and line breaks.  Only the first line willbe passed.
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		BufferedReader in = req.getReader();
		PrintWriter out = res.getWriter();
		String delims = "[/]+";
        String[] path = req.getPathInfo().split(delims);
        
        System.arraycopy(path, 1, path, 0, path.length-1);
        path[path.length-1] = null;
        
        res.setStatus(HttpServletResponse.SC_OK);
        try {
        	out.println(ManagerLayer.getInstance().advancedPut(path,in.readLine(),req.getCookies()));
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
		}
        
        out.close();
	}

}
