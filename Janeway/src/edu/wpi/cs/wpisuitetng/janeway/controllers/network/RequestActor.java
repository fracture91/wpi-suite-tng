package edu.wpi.cs.wpisuitetng.janeway.controllers.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * This class makes a request asynchronously.
 */
public class RequestActor extends Thread {
	private List<ResponseListener> listeners;
	private URL url;
	private String method;
	private String requestBody;
	
	/**
	 * Constructor.
	 * 
	 * @param listeners		A List of ResponseListeners which will be called when a Response is received.
	 * @param url			The full URL to connect to.
	 * @param method		A String representing the HTTP request method. Ex: "GET", "POST", "PUT", "DELETE"
	 */
	public RequestActor(List<ResponseListener> listeners, URL url, String method) {
		this.url = url;
		this.method = method;
		this.requestBody = null;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param listeners		A List of ResponseListeners which will be called when a Response is received.
	 * @param url			The full URL to connect to.
	 * @param method		A String representing the HTTP request method. Ex: "GET", "POST", "PUT", "DELETE"
	 * @param requestBody	A String containing data to be sent to the server in the body of the request.
	 */
	public RequestActor(List<ResponseListener> listeners, URL url, String method, String requestBody) {
		this.url = url;
		this.method = method;
		this.requestBody = requestBody;
	}
	
	/**
	 * Overrides Thread's run method. This will be called when the thread is started.
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		HttpURLConnection connection = null;
		
		try {
			// setup connection
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoInput(true);
			
			// if there is a body to send, send it
			if (requestBody != null) {
				connection.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.writeBytes(requestBody);
				out.flush();
				out.close();
			}

			// get the response body
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			String responseBody = ""; 
			while((line = reader.readLine()) != null) {
				responseBody += line + "\n";
			}
			reader.close();
			
			// get the response headers
			Map<String, List<String>> responseHeaders = connection.getHeaderFields();
			
			// get the response code
			int responseCode = connection.getResponseCode();
			
			// get the response message
			String responseMessage = connection.getResponseMessage();
			
			// create Response
			Response response = new Response(responseCode, responseMessage, responseHeaders, responseBody);
			
			// TODO pass response to handlers
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect(); 
			}
		}
	}
}
