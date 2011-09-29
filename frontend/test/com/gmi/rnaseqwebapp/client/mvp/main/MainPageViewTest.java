package com.gmi.rnaseqwebapp.client.mvp.main;

import org.eclipse.jdt.internal.compiler.ast.AssertStatement;
import org.jukito.All;
import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPageView.Binder;
import com.gmi.rnaseqwebapp.client.testutil.ViewTestBase;
import com.gmi.rnaseqwebapp.client.testutil.ViewTestModule;
import com.gmi.rnaseqwebapp.client.ui.LoadingIndicator;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.gwtplatform.tester.MockFactory;
import com.gwtplatform.tester.MockingBinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class MainPageViewTest extends ViewTestBase{
	
	public static class Module extends ViewTestModule {
		
		static class MyTestBinder extends MockingBinder<Widget, MainPageView> implements Binder {
			@Inject
			public MyTestBinder(final MockFactory mockitoMockFactory) {
				super(Widget.class, mockitoMockFactory);
			}
	    }

		@Override
		protected void configureViewTest() {
			bind(Binder.class).to(MyTestBinder.class);
			bindMock(LoadingIndicator.class).in(TestSingleton.class);
			bindManyInstances(String.class, NameTokens.homepage,NameTokens.accessionspage,NameTokens.helppage);
		}
	}
	
	@Inject 
	MainPageView view;

	
	@Test
	public void testLoadingIndicatorVisibility() {
		boolean isVisible = true;
		given(view.loadingIndicator.isVisible()).willReturn(isVisible);
		view.showLoadingIndicator(isVisible);
		verify(view.loadingIndicator).setVisible(isVisible);
		assertEquals(view.loadingIndicator.isVisible(),isVisible);
	}
	
	@Test
	public void testSetActiveNavigationItem(@All String activeItem) {
		String active_style_name = "ACTIVE_STYLE_NAME";
		given(view.style.current_page_item()).willReturn(active_style_name);
		given(view.homeLink.getTargetHistoryToken()).willReturn(NameTokens.homepage);
		given(view.helpLink.getTargetHistoryToken()).willReturn(NameTokens.helppage);
		given(view.accessionLink.getTargetHistoryToken()).willReturn(NameTokens.accessionspage);
		view.setActiveNavigationItem(activeItem);
		verify(view.homeLink).removeStyleName(active_style_name);
		verify(view.helpLink).removeStyleName(active_style_name);
		verify(view.accessionLink).removeStyleName(active_style_name);
		
		if (activeItem.equals(NameTokens.homepage))
			verify(view.homeLink).addStyleName(active_style_name);
		else if (activeItem.equals(NameTokens.accessionspage))
			verify(view.accessionLink).addStyleName(active_style_name);
		else if (activeItem.equals(NameTokens.helppage))
			verify(view.helpLink).addStyleName(active_style_name);
		else
			fail(activeItem+ " unknown");
	}
	
	
	
}
