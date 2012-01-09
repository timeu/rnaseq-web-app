package com.gmi.rnaseqwebapp.client.mvp.main;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gmi.rnaseqwebapp.client.event.LoadingIndicatorEvent;
import com.gmi.rnaseqwebapp.client.testutil.PresenterTestBase;
import com.gmi.rnaseqwebapp.client.testutil.PresenterTestModule;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.testing.CountingEventBus;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;


@RunWith(JukitoRunner.class)
public class MainPagePresenterTest extends PresenterTestBase {
	
	public static class Module extends PresenterTestModule {

		@Override
		protected void configurePresenterTest() {
		}
	}
	
	@Inject
	MainPagePresenter presenter;
	
	@Inject
	MainPagePresenter.MyView view;
	
	
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void showActiveNavigationItemWhenPlaceRequest() {
		PlaceRequest request = new PlaceRequest("home");
	    placeManager.revealPlace(request);
	    when(placeManager.getCurrentPlaceRequest()).thenReturn(request);
	    presenter.onReset();
	    verify(view).setActiveNavigationItem(request.getNameToken());
	}
	
	@Test
	public void showLoadingIndicatorOnEvent() {
		presenter.onBind();
		presenter.onReset();
		LoadingIndicatorEvent.fire(eventBus, true);
		verify(view).showLoadingIndicator(true);
	}
	
	
	

}
