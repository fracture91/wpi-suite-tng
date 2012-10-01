package edu.wpi.cs.wpisuitetng.janeway.controllers.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class makes a request asynchronously.
 */
public class RequestActor extends Thread {
	private List<ResponseListener> responseListeners;
	private URL url;
	private String method;
	private String requestBody;
	private Map<String, List<String>> requestHeaders;
	
	/**
	 * Constructor.
	 * 
	 * @param listeners			A List of ResponseListeners which will be called when a Response is received.
	 * @param url				The full URL to connect to.
	 * @param method			A String representing the HTTP request method. Ex: "GET", "POST", 
	 * 							"PUT", "DELETE"
	 * @param requestBody		A String containing data to be sent to the server in the body of the 
	 * 							request. May be null.
	 * @param requestHeaders	A Map of header keys and Lists of their values to be sent to the 
	 * 							server. May be null.
	 */
	public RequestActor(List<ResponseListener> responseListeners, URL url, String method, String requestBody, Map<String, List<String>> requestHeaders) {
		// null check for listeners
		if (responseListeners == null) {
			// TODO throw Exception
		}
		
		// null check for url
		if (url == null) {
			// TODO throw Exception
		}
		
		// null check for method
		if (method == null) {
			// TODO throw Exception
		}
		
		this.responseListeners = responseListeners;
		this.url = url;
		this.method = method;
		this.requestBody = requestBody;
		this.requestHeaders = requestHeaders;
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
			
			// set request headers
			Iterator<String> requestHeaderKeysI = requestHeaders.keySet().iterator();
			while (requestHeaderKeysI.hasNext()) {
				String requestHeaderKey = requestHeaderKeysI.next();
				Iterator<String> requestHeaderValuesI = requestHeaders.get(requestHeaderKey).iterator();
				
				while (requestHeaderValuesI.hasNext()) {
					connection.setRequestProperty(requestHeaderKey, requestHeaderValuesI.next());
				}
			}
			
			// if there is a body to send, send it
			if (requestBody != null) {
				connection.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.writeBytes(requestBody);
				out.flush();
				out.close();
			}
			// otherwise, just connect
			else {
				connection.connect();
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
