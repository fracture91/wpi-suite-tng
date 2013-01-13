package edu.wpi.cs.wpisuitetng.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	 * TODO handle 400 and 500 error exceptions and whatnot
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		HttpURLConnection connection = null;
		
		try {
			// setup connection
			connection = (HttpURLConnection) request.getURL().openConnection();
			connection.setConnectTimeout(20*1000);
			connection.setReadTimeout(5*1000);
			connection.setRequestMethod(request.getRequestMethod());
			connection.setDoInput(true);
			connection.setRequestProperty("Connection", "close");
			
			// set request headers
			Iterator<String> requestHeaderKeysI = request.getRequestHeaders().keySet().iterator();
			while (requestHeaderKeysI.hasNext()) {
				String requestHeaderKey = requestHeaderKeysI.next();
				Iterator<String> requestHeaderValuesI = request.getRequestHeaders().get(requestHeaderKey).iterator();
				
				while (requestHeaderValuesI.hasNext()) {
					connection.setRequestProperty(requestHeaderKey, requestHeaderValuesI.next());
				}
			}
			
			// if there is a body to send, send it
			if (request.getRequestBody() != null) {
				connection.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.writeBytes(request.getRequestBody());
				out.flush();
				out.close();
			}
			// otherwise, just connect
			else {
				connection.connect();
			}

			// get the response body
			String responseBody = "";
			try {
				InputStream in = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in), 1);
				String line;
				try {
					while((line = reader.readLine()) != null) {
						responseBody += line + "\n";
					}
				} catch (SocketTimeoutException e) {
					// Note: this will be thrown if a read takes longer than 5 seconds
					// TODO Auto-generated catch block
					e.printStackTrace();
					request.notifyObserversFail(); // TODO think about this
				}
				finally {
					if (reader != null) {
						reader.close();
					}
				}
			}
			catch (IOException e) {
				// do nothing, received a 400, or 500 status code
				System.out.println("Received a 400 or 500 status code.");
				request.notifyObserversError(); // TODO think about this
			}
			
			// get the response headers
			Map<String, List<String>> responseHeaders = connection.getHeaderFields();
			
			// get the response code
			int responseCode = connection.getResponseCode();
			
			// get the response message
			String responseMessage = connection.getResponseMessage();
			
			// create Response
			Response response = new Response(responseCode, responseMessage, responseHeaders, responseBody);
			
			// set the Request's response to the newly created response
			request.setResponse(response);
			
			request.notifyObserversDone();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close the connection
			if (connection != null) {
				connection.disconnect(); 
			}
		}
	}
}
