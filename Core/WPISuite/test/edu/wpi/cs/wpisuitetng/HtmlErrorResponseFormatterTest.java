/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    twack
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import org.junit.*;

import edu.wpi.cs.wpisuitetng.authentication.Authenticator;
import edu.wpi.cs.wpisuitetng.authentication.BasicAuth;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;

import static org.junit.Assert.*;

/**
 * Testing the abtract class Authentictor. Uses the BasicAuth 
 * implementation to test the non-abstract methods.
 * @author twack
 *
 */
public class HtmlErrorResponseFormatterTest {
	HtmlErrorResponseFormatter formatter;
	
	@Before
	public void setUp()
	{
		this.formatter = new HtmlErrorResponseFormatter();
	}
	
	
	@Test
	public void testEmptyException()
	{
		String expected = "<html> <head> <title> 500 </title> </head> <body> <h1> Error 500 </h1> <h2> Info </h2> <p>  </p> </body> </html>";
		
		WPISuiteException e = new WPISuiteException();
		String error = this.formatter.formatContent(e);
		String statusCode = String.valueOf(e.getStatus());

		
		assertTrue(expected.contains(statusCode));
		assertTrue(expected.contains("Error " + statusCode));
		assertTrue(expected.equals(error));
	}
	
	@Test
	public void testTitle()
	{
		String expectedHeader = "<html> <head> <title> 403 </title> </head>";
		Authenticator auth = new BasicAuth();
		try {
			auth.login("this shoud break it!");
		}
		catch(WPISuiteException e)
		{
			String error = this.formatter.formatContent(e);
			String statusCode = String.valueOf(e.getStatus());

			assertTrue(error.contains(statusCode));
			assertTrue(error.startsWith(expectedHeader));
		}
	}
	
	@Test
	public void testBody()
	{
		Authenticator auth = new BasicAuth();
		try {
			auth.login("this shoud break it!");
		}
		catch(WPISuiteException e)
		{
			String error = this.formatter.formatContent(e);
			String statusCode = String.valueOf(e.getStatus());
			
			assertTrue(error.contains("Error " + statusCode));
			assertTrue(error.contains(e.getMessage()));
		}
	}
	
	@Test
	public void testHTML()
	{
		Authenticator auth = new BasicAuth();
		try {
			auth.login("this shoud break it!");
		}
		catch(WPISuiteException e)
		{
			String error = this.formatter.formatContent(e);
			
			assertTrue(error.startsWith("<html>"));
			assertTrue(error.endsWith("</html>"));
		}
	}
}