/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    JPage
 *    Andrew Hurle
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * This class makes a request asynchronously.
 */
public class RequestActor extends Thread {
	private Request request;
	
	/**
	 * Constructor.
	 * 
	 * @param request
	 */
	public RequestActor(Request request) {
		this.request = request;
	}
	
	/**
	 * Overrides Thread's run method. This will be called when the thread is started.
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		HttpURLConnection connection = null;
		boolean requestSendFail = false;
		boolean responseBodyReadTimeout = false;
		Exception exceptionRecv = null;
		
		try {
			// setup connection
			connection = (HttpURLConnection) request.getUrl().openConnection();
			connection.setConnectTimeout(request.getConnectTimeout());
			connection.setReadTimeout(request.getReadTimeout());
			connection.setRequestMethod(request.getHttpMethod().toString());
			connection.setDoInput(true);
			connection.setRequestProperty("Connection", "close");
			
			// set request headers
			for (String requestHeaderKey : request.getHeaders().keySet()) {
				for (String requestHeaderValue : request.getHeaders().get(requestHeaderKey)) {
					connection.setRequestProperty(requestHeaderKey, requestHeaderValue);
				}
			}
			
			// if there is a body to send, send it
			if (request.getBody() != null) {
				connection.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.writeBytes(request.getBody());
				out.flush();
				out.close();
			}
			// otherwise, just connect
			else {
				connection.connect();
			}
			
			// get the response headers
			Map<String, List<String>> responseHeaders = connection.getHeaderFields();
			
			// get the response code
			int responseCode = connection.getResponseCode();
			
			// get the response message
			String responseMessage = connection.getResponseMessage();

			// get the response body
			String responseBody = "";
			InputStream in;
			
			if (responseCode < 400) {	// if the request succeeds, get the InputStream
				in = connection.getInputStream();
			}
			else {	// if the request fails, get the ErrorStream
				in = connection.getErrorStream();
			}
			
			// read response body
			BufferedReader reader = new BufferedReader(new InputStreamReader(in), 1);
			String line;
			try {
				while((line = reader.readLine()) != null) {
					responseBody += line + "\n";
				}
			} catch (SocketTimeoutException e) {	// if there is a timeout while reading the body
				exceptionRecv = e;
				responseBodyReadTimeout = true;
			} catch (IOException e) {	// if readLine() fails
				exceptionRecv = e;
			}
			finally {	// make sure that the BufferedReader is closed
				if (reader != null) {
					reader.close();
				}
			}
			
			// create Response
			ResponseModel response = new Response(responseCode, responseMessage, responseHeaders, responseBody);
			
			// set the Request's response to the newly created response
			request.setResponse(response);
		} catch (IOException e) {
			exceptionRecv = e;
			requestSendFail = true;
		} finally {
			// close the connection
			if (connection != null) {
				connection.disconnect(); 
			}
			
			if (!request.isAsynchronous) {
				// Do nothing
			}
			else if (requestSendFail) {
				request.notifyObserversFail(exceptionRecv);
			}
			else if (responseBodyReadTimeout) {
				request.notifyObserversFail(exceptionRecv);
			}
			else if (request.getResponse() != null) {
				// On status code 2xx
				if (request.getResponse().getStatusCode() >= 200 && request.getResponse().getStatusCode() < 300) {
					request.notifyObserversResponseSuccess();
				}
				// On status code 4xx or 5xx
				else if (request.getResponse().getStatusCode() >= 400 && request.getResponse().getStatusCode() < 600) {
					request.notifyObserversResponseError();
				}
				// On other status codes
				else {
					request.notifyObserversFail(new Exception());
				}
			}
		}
	}
}
