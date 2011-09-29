package com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import org.apache.commons.collections.map.HashedMap;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.dto.Accession;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate;
import com.gmi.rnaseqwebapp.client.testutil.PresenterTestBase;
import com.gmi.rnaseqwebapp.client.testutil.PresenterTestModule;
import com.gmi.rnaseqwebapp.client.ui.HasSearchHandlers;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

@RunWith(JukitoRunner.class)
public class ResultsListPresenterTest extends PresenterTestBase {

	public static class Module extends PresenterTestModule {

		@Override
		protected void configurePresenterTest() {
			HasData<Phenotype> hasData = mock(HasData.class);
			ResultsListPresenter.MyView view = mock(ResultsListPresenter.MyView.class);
			given(view.getDisplay()).willReturn(hasData);
			bind(ResultsListPresenter.MyView.class).toInstance(view);
		}
	}
	
	
	@Inject
	ResultsListPresenter presenter;
	
	@Inject
	ResultsListPresenter.MyView view;
	
	@Inject
	HasSelectionChangedHandlers hasSelectionChangeHandlers;
	
	@Inject
	HasSearchHandlers hasSearchHandlers;
	
	@Inject
	HandlerRegistration handlerRegistration;
	
	@Inject
	DispatchAsync dispatch;
	
	@Before 
	public void setUp() {
		HasData<Phenotype> hasData = view.getDisplay();
		given(view.getDisplay()).willReturn(hasData);
		given(hasData.getVisibleRange()).willReturn(new Range(0,50));
		
		given(view.getSearchNameHandlers()).willReturn(hasSearchHandlers);
		given(view.getSearchStartHandlers()).willReturn(hasSearchHandlers);
		given(view.getSearchEndHandlers()).willReturn(hasSearchHandlers);
		given(hasSearchHandlers.addKeyUpHandler(any(KeyUpHandler.class))).willReturn(handlerRegistration);
	}
	
	@Test
	public void onBindInitDataGridAndHandlers() {
		presenter.bind();
		verify(view).initDataGrid();
		verify(view).setSearchTerms(any(HashMap.class));
		
		verify(hasSearchHandlers, times(3)).addKeyUpHandler(any(SeachKeyUpHandler.class));
		
		presenter.unbind();
		verify(handlerRegistration,times(3)).removeHandler();
	}
}
