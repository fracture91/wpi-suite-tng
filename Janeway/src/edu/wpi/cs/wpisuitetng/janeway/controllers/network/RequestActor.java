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
	private String inputbody;
	
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
		this.inputbody = null;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param listeners		A List of ResponseListeners which will be called when a Response is received.
	 * @param url			The full URL to connect to.
	 * @param method		A String representing the HTTP request method. Ex: "GET", "POST", "PUT", "DELETE"
	 * @param inputbody		A String containing data to be sent to the server in the body of the request.
	 */
	public RequestActor(List<ResponseListener> listeners, URL url, String method, String inputbody) {
		this.url = url;
		this.method = method;
		this.inputbody = inputbody;
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
			if (inputbody != null) {
				connection.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.writeBytes(inputbody);
				out.flush();
				out.close();
			}

			// read the response
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			String responsebody = ""; 
			while((line = reader.readLine()) != null) {
				responsebody += line + "\n";
			}
			reader.close();
			
			// get the response headers
			Map<String, List<String>> headers = connection.getHeaderFields();
			
			// get the response code
			int responsecode = connection.getResponseCode();
			
			// get the response message
			String responsemsg = connection.getResponseMessage();
			
			// TODO create response and pass to handlers
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
