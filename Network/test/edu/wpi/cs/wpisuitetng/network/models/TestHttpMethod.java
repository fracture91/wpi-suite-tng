package edu.wpi.cs.wpisuitetng.network.models;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test HttpMethod enum
 */
public class TestHttpMethod {
	/**
	 * Test that the toString method returns the correct String representation of a given HttpMethod value.
	 */
	@Test
	public void testToString() {
		assertTrue("GET".equals(HttpMethod.GET.toString()));
		assertTrue("POST".equals(HttpMethod.POST.toString()));
		assertTrue("PUT".equals(HttpMethod.PUT.toString()));
		assertTrue("DELETE".equals(HttpMethod.DELETE.toString()));
	}
}
