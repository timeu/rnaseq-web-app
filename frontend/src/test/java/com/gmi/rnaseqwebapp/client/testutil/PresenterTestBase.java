package com.gmi.rnaseqwebapp.client.testutil;

import org.junit.AfterClass;

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.junit.GWTMockUtilities;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class PresenterTestBase {
	
	@Inject
	protected PlaceManager placeManager;
	
	@Inject
	protected EventBus eventBus;
	
	@AfterClass
	public static void tearDown() {
		GWTMockUtilities.restore();
	}

}
