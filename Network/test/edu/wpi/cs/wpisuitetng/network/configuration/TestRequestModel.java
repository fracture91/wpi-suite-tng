package edu.wpi.cs.wpisuitetng.network.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.RequestModel;

public class TestRequestModel {
	private final String headers[][] = {
			{"header1", "value1a"},
			{"header1", "value1b"},
			{"header2", "value2"},
			{"header3", "value3"}
		};
	private final String queryData[][] = {
			{"key1", "val1"},
			{"key2", "val2a"},
			{"key2", "val2b"},
			{"key3", "val3"}
		};
	private RequestModel rm;
	
	@Before
	public void setUp() {
		rm = new RequestModel();
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
	 * Test the addQueryData method.
	 */
	@Test
	public void testAddQueryData() {
		assertTrue(rm.getQueryData().isEmpty());
		for (int i = 0; i < queryData.length; i++) {
			rm.addQueryData(queryData[i][0], queryData[i][1]);
		}
		assertEquals(queryData.length-1, rm.getQueryData().size());
	}
	
	/**
	 * Test the addQueryData method for a NullPointerException if key is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddQueryDataKeyNullException() {
		rm.addQueryData(null, "");
	}
	
	/**
	 * Test the addQueryData method for a NullPointerException if value is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddQueryDataValueNullException() {
		rm.addQueryData("", null);
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
	 * Test the testSetHttpMethod method.
	 */
	@Test
	public void testSetHttpMethod() {
		rm.setHttpMethod(HttpMethod.GET);
		assertEquals(HttpMethod.GET, rm.getHttpMethod());

		rm.setHttpMethod(HttpMethod.POST);
		assertEquals(HttpMethod.POST, rm.getHttpMethod());
	}
	
	/**
	 * Test the testSetHttpMethod method for a NullPointerException.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetHttpMethodException() {
		rm.setHttpMethod(null);
	}
	
	/**
	 * Test the testSetUrl method.
	 */
	@Test
	public void testSetUrl() throws MalformedURLException {
		URL url = new URL("http://www.wpi.edu");
		rm.setUrl(url);
		assertTrue(url.equals(rm.getUrl()));
	}
	
	/**
	 * Test the testSetUrl method for a NullPointerException.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetUrlException() {
		rm.setUrl(null);
	}
}
