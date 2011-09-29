package com.gmi.rnaseqwebapp.client.testutil;

import org.junit.AfterClass;

import com.google.gwt.junit.GWTMockUtilities;


public class ViewTestBase {
	
	@AfterClass
	public static void tearDown() {
		GWTMockUtilities.restore();
	}

}
