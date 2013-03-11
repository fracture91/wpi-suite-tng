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

package edu.wpi.cs.wpisuitetng.network.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class TestResponseModel {
	private final String headers[][] = {
			{"header1", "value1a"},
			{"header1", "value1b"},
			{"header2", "value2"},
			{"header3", "value3"}
		};
	private ResponseModel rm;
	
	@Before
	public void setUp() {
		rm = new ResponseModel();
	}
	
	/**
	 * Test the addHeader method.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddHeader() {
		assertTrue(rm.getHeaders().isEmpty());
		for (int i = 0; i < headers.length; i++) {
			rm.addHeader(headers[i][0], headers[i][1]);
			assertTrue(rm.getHeaders().get(headers[i][0]).contains(headers[i][1]));
		}
		assertEquals(headers.length-1, rm.getHeaders().size());
		assertEquals(2, rm.getHeaders().get(headers[0][0]).size());
		
		rm.addHeader(null, "");
	}
	
	/**
	 * Test the addHeader method for a NullPointerException if key is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddHeaderKeyNullException() {
		rm.addHeader(null, "");
	}
	
	/**
	 * Test the setBody method.
	 */
	@Test
	public void testSetBody() {
		rm.setBody("A body");
		assertTrue("A body".equals(rm.getBody()));
		
		rm.setBody("Another body");
		assertTrue("Another body".equals(rm.getBody()));
	}
	
	/**
	 * Test the setBody method for a NullPointerException.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetBodyException() {
		rm.setBody(null);
	}
	
	/**
	 * Test the setStatusCode method.
	 */
	@Test
	public void testSetStatusCode() {
		rm.setStatusCode(200);
		assertEquals(200, rm.getStatusCode());

		rm.setStatusCode(500);
		assertEquals(500, rm.getStatusCode());
	}
	
	/**
	 * Test the setStatusMessage method.
	 */
	@Test
	public void testSetStatusMessage() throws MalformedURLException {
		rm.setStatusMessage("A message");
		assertTrue("A message".equals(rm.getStatusMessage()));
		
		rm.setStatusMessage("Another message");
		assertTrue("Another message".equals(rm.getStatusMessage()));
	}
	
	/**
	 * Test the setStatusMessage method for a NullPointerException.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetStatusMessageException() {
		rm.setStatusMessage(null);
	}
}
