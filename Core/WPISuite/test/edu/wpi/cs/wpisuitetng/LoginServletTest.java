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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.authentication.BasicAuth;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.entitymanagers.UserManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
/**
 * Performs acceptence tests against the LoginSerlvet.
 * 	Uses Apache HTTPClient to facilitate the HTTP interactions.
 * @author twack
 *
 */
public class LoginServletTest {	
	
	private DefaultHttpClient httpclient;
	
	@Before
	/**
	 * Initialize the http client being used to hit the Login Servlet
	 * @throws WPISuiteException
	 */
	public void setUp() throws WPISuiteException
	{
		this.httpclient = new DefaultHttpClient();		
	}
	
	@Test
	@Ignore
	/**
	 * Acceptance Test 1: User with valid username but incorrect login credentials.
	 * 	Expected Results: 	(1) 403 Forbidden error response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void testLoginAttemptBadPassword() throws ClientProtocolException, IOException
	{
		HttpPost httpPost = new HttpPost("http://localhost:8080/WPISuite/API/login/");
		String token = BasicAuth.generateBasicAuth("twack", "abde");
		httpPost.setHeader("Authorization", token);
		HttpResponse response = httpclient.execute(httpPost);

		try {
		    //System.out.println(response.getStatusLine());
			StatusLine responseStatus = response.getStatusLine();
		    String forbidden = "HTTP/1.1 403 Forbidden";
		    assertEquals(403, responseStatus.getStatusCode());
		    // TODO: test return messages
		} finally {
		    httpPost.releaseConnection();
		}
	}
	
	@Test
	@Ignore
	/**
	 * Acceptance Test 2: User with an invalid username (no User).
	 * 	Expected Results: 	(1) 403 Forbidden error response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void testLoginAttemptBadUsername() throws ClientProtocolException, IOException
	{
		HttpPost httpPost = new HttpPost("http://localhost:8080/WPISuite/API/login/");
		String token = BasicAuth.generateBasicAuth("jable", "abde");
		httpPost.setHeader("Authorization", token);
		HttpResponse response = httpclient.execute(httpPost);

		try {
		    //System.out.println(response.getStatusLine());
			StatusLine responseStatus = response.getStatusLine();
		    String forbidden = "HTTP/1.1 403 Forbidden";
		    assertEquals(403, responseStatus.getStatusCode());
		    // TODO: test return message
		} finally {
		    httpPost.releaseConnection();
		}
	}
	
	
	
	@Test
	@Ignore
	/**
	 * Acceptence Test 3: User with valid credentials attempts to login.
	 *  Preconditions:		(1) There must be a user with the username "twack" and 
	 *  						a password "abcde" in the database
	 *  						
	 * 	Expected Results: 	(1) 200 OK Response Code
	 * 						(2) WPI Suite Cookie Returned
	 * 						(3) Cookie header match
	 * 						(4) Cookie body contains loginTime & valid username (will be tightened soon)
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void testLoginSuccess() throws ClientProtocolException, IOException
	{

		HttpPost httpPost = new HttpPost("http://localhost:8080/WPISuite/API/login/");
		String token = BasicAuth.generateBasicAuth("twack", "abcde");
		httpPost.setHeader("Authorization", token);
		HttpResponse response = httpclient.execute(httpPost);

		try {
			// Stat Code is OK
		    StatusLine responseStatus = response.getStatusLine();
		    assertEquals(200, responseStatus.getStatusCode());

		    // Cookie exists?
		    List<Cookie> cookies = this.httpclient.getCookieStore().getCookies();
		    assertEquals(1, cookies.size());
		    
		    // Cookie Header validation
		    Cookie wpiSuite = cookies.get(0);
		    String expectedHeader = "WPISUITE-twack";
		    assertTrue(expectedHeader.equals(wpiSuite.getName()));
		    
		    // Cookie Body validation
		    String cookieBody = wpiSuite.getValue();
		    
		    assertTrue(cookieBody.contains("loginTime"));
		    assertTrue(cookieBody.contains("twack"));
		    // TODO: compare against actual serialized Session
		} finally {
		    httpPost.releaseConnection();
		}
	}
}
