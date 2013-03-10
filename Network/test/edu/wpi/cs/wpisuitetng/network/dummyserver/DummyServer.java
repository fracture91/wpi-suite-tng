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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.network.dummyserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.Cookie;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.RequestModel;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * For testing Requests sent over the network.
 * 
 * TODO finish RequestModel generation and canned response fulfillment.
 */
public class DummyServer {
	private final Handler container;
	private final Server server;
	private final Connection connection;
	private final SocketAddress address;

	public DummyServer(int port) {
		container = new Handler();
		try {
			server = new ContainerServer(container);
			connection = new SocketConnection(server);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		address = new InetSocketAddress(port);
	}

	public void start() {
		try {
			connection.connect(address);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		try {
			connection.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public RequestModel getLastReceived() {
		return container.getLastReceived();
	}
	
	public void setResponse(ResponseModel response) {
		container.setResponse(response);
	}

	private class Handler implements Container {
		private RequestModel lastReceived;
		private ResponseModel cannedResponse;
		
		public Handler() {
			cannedResponse = new ResponseModel();
			cannedResponse.setStatusCode(200);
			cannedResponse.setStatusMessage("OK");
			cannedResponse.setBody("");
		}

		public void handle(Request request, Response response) {
			try {
				RequestModel rm = new RequestModel();
				rm.setBody(request.getContent());
				
				for (String key : request.getQuery().keySet()) {
					rm.addQueryData(key, request.getQuery().get(key));
				}
				
				String cookies = "";
				boolean firstCookie = true;
				for (Cookie cookie : request.getCookies()) {
					if (!firstCookie) {
						cookies += "; ";
					}
					cookies += cookie.getName() + "=" + cookie.getValue();
					firstCookie = false;
				}
				rm.addHeader("Cookie", cookies);
				rm.setHttpMethod(HttpMethod.valueOf(request.getMethod().toUpperCase()));
				
				//TODO finish building RequestModel
				
				lastReceived = rm;
				
				response.setCode(cannedResponse.getStatusCode());
				
				// TODO finish building response from canned response
				
				if (cannedResponse.getBody() != null) {
					PrintStream body = response.getPrintStream();
					body.println(cannedResponse.getBody());
					body.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public RequestModel getLastReceived() {
			return lastReceived;
		}
		
		public void setResponse(ResponseModel response) {
			cannedResponse = response;
		}
	}
}

