package com.gmi.rnaseqwebapp.client.testutil;

import org.jukito.JukitoModule;
import org.jukito.TestSingleton;

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.testing.CountingEventBus;
import com.google.gwt.junit.GWTMockUtilities;
import com.gwtplatform.mvp.client.AutobindDisable;


public abstract class PresenterTestModule extends JukitoModule {

	@Override
	protected void configureTest() {
		GWTMockUtilities.disarm();
	    bind(EventBus.class).to(CountingEventBus.class).in(TestSingleton.class);
	    
	    configurePresenterTest();
	    
	    bind(AutobindDisable.class).toInstance(new AutobindDisable(true));
	}
	
	abstract protected void configurePresenterTest();

}
