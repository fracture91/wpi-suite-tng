/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;

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
	private ErrorResponseFormatter reponseFormatter;
	
	/**
	 * Empty Constructor
	 */
	public WPICoreServlet()
	{
		this.reponseFormatter = new JsonErrorResponseFormatter();
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
        
        try {
			out.println(ManagerLayer.getInstance().read(path,req.getCookies()));
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
		}
       
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
        
        
        try {
        	out.println(ManagerLayer.getInstance().create(path,in.readLine(),req.getCookies()));
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
			out.write(this.reponseFormatter.formatContent(e));
			out.flush();
			out.close();
			return;
		}
        res.setStatus(HttpServletResponse.SC_CREATED);
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
        
        try {
			out.println(ManagerLayer.getInstance().update(path,in.readLine(),req.getCookies()));
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
			out.write(this.reponseFormatter.formatContent(e));
			out.flush();
			out.close();
		}
        
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
        
        try {
			out.println(ManagerLayer.getInstance().delete(path,req.getCookies()));
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
			out.write(this.reponseFormatter.formatContent(e));
			out.flush();
			out.close();
		}
        
        out.close();
    }
}