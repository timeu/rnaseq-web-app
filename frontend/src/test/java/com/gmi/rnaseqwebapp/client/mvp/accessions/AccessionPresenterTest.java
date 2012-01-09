package com.gmi.rnaseqwebapp.client.mvp.accessions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.HashedMap;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.dto.Accession;
import com.gmi.rnaseqwebapp.client.dto.Accession.AccessionPredicate;
import com.gmi.rnaseqwebapp.client.dto.Accession.AccessionPredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.mvp.accessions.AccessionPresenter.SearchKeyUpHandler;
import com.gmi.rnaseqwebapp.client.testutil.PresenterTestBase;
import com.gmi.rnaseqwebapp.client.testutil.PresenterTestModule;
import com.gmi.rnaseqwebapp.client.ui.HasSearchHandlers;
import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.maps.client.HasMap;
import com.google.gwt.maps.client.base.HasInfoWindow;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.overlay.HasMarker;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;
import com.google.inject.Inject;

@RunWith(JukitoRunner.class)
public class AccessionPresenterTest extends PresenterTestBase {

	public static class Module extends PresenterTestModule {

		@Override
		protected void configurePresenterTest() {
			HasData<Accession> hasData = mock(HasData.class);
			SingleSelectionModel<Accession> selectionModel = mock(SingleSelectionModel.class);
			AccessionPresenter.MyView view = mock(AccessionPresenter.MyView.class);
			bindMock(ClientData.class).asEagerSingleton();
			bindMock(Accession.class);
			bindMock(AccessionPresenter.SearchKeyUpHandler.class);
			given(view.getDisplay()).willReturn(hasData);
			doReturn(selectionModel).when(hasData).getSelectionModel();
			bind(AccessionPresenter.MyView.class).toInstance(view);
		}
	}
	
	@Inject
	AccessionPresenter presenter;
	
	@Inject
	AccessionPresenter.MyView view;
	
	@Inject
	HasSearchHandlers hasSearchHandlers;
	
	@Inject
	HasSelectionChangedHandlers hasSelectionChangeHandlers;
	
	@Inject
	HandlerRegistration handlerRegistration;
	
	@Inject
	HasMarker hasMarker;
	
	@Inject 
	HasInfoWindow hasInfoWindow;
	
	@Inject 
	HasLatLng hasLatLng;
	
	@Inject
	HasMap hasMap;
	
	@Inject
	ClientData clientData;
	
	@Inject
	Accession accession;

	
	//@Inject
	//SingleSelectionModel<Accession> selectionModel;
	
	@Inject
	AccessionPresenter.SearchKeyUpHandler searchKeyUpHandler;

	List<Accession> accessions = new ArrayList<Accession>();
	
	
	
	@Before
	public void setUp() {
		
		given(accession.getAccessionId()).willReturn(1);
		given(accession.getName()).willReturn("name");
		given(accession.getLatitude()).willReturn(10.0);
		given(accession.getLongitude()).willReturn(10.0);
		given(accession.getLabelForMapMarker()).willReturn("1 / name");
		accessions.add(accession);
		HasData<Accession> hasData = view.getDisplay();
		given(clientData.getAccessions()).willReturn(accessions);
		given(view.getDisplay()).willReturn(hasData);
		HandlerRegistration registration = mock(HandlerRegistration.class);
		given(view.getDisplay().getSelectionModel().addSelectionChangeHandler(any(SelectionChangeEvent.Handler.class))).willReturn(registration);
		given(view.getMap()).willReturn(hasMap);
		given(view.getSearchNameHandlers()).willReturn(hasSearchHandlers);
		given(view.getSearchCollectorHandlers()).willReturn(hasSearchHandlers);
		given(view.getSearchCountryHandlers()).willReturn(hasSearchHandlers);
		
		given(view.createLatLng(anyDouble(), anyDouble())).willReturn(hasLatLng);
		given(view.createMarkerAt(any(HasLatLng.class))).willReturn(hasMarker);
		given(view.createInfoWindow(anyString())).willReturn(hasInfoWindow);
		
		given(hasData.getVisibleRange()).willReturn(new Range(0,50));
		given(hasSearchHandlers.addKeyUpHandler(any(KeyUpHandler.class))).willReturn(handlerRegistration);
		//doReturn(selectionModel).when(hasData).getSelectionModel();
	}
	
	@Test
	public void onBindAddHandler() {
		presenter.bind();
		
		verify(hasSearchHandlers, times(3)).addKeyUpHandler(any(SearchKeyUpHandler.class));
		
		presenter.unbind();
		verify(handlerRegistration,times(3)).removeHandler();
		
	}
	
	@Test
    public void onHideUnloadEverything() {
		presenter.bind();
		presenter.onHide();
		int markerSize = presenter.accessionId2Marker.size();
		verify(view,atLeastOnce()).unloadMap();
		verify(view,times(markerSize)).clearInstanceListeners(any(HasMarker.class));
		assertEquals(0,presenter.accessionId2Marker.size());
	}
	
	
	@Test
	public void onRevealInitMarkersAndTable() {
		presenter.bind();
		presenter.onReveal();
		int markerCount = accessions.size();
		verify(view,times(1)).initMap();
		verify(view,atLeastOnce()).initTable();
		assertEquals(accessions.size(),presenter.accessionDataProvider.getList().size());
		assertEquals(accessions.size(),presenter.accessionId2Marker.size());
		verify(hasMarker,times(markerCount)).setTitle(anyString());
	}
	

	@Test
	public void onKeyUpInSearchBox() {
		
		final KeyUpEvent keyUpEvent = mock(KeyUpEvent.class);
		given(keyUpEvent.getSource()).willReturn(hasSearchHandlers);
		given(hasSearchHandlers.getText()).willReturn("Test");
		AccessionPredicate predicate = presenter.accessionPredicates.get(AccessionPredicate.CRITERIA.Name);
		final AccessionPresenter.SearchKeyUpHandler handler = presenter.new SearchKeyUpHandler(predicate);
		handler.onKeyUp(keyUpEvent);
		
		verify(hasSearchHandlers).getText();
		assertEquals("Test", predicate.getValue());
		//verify(presenter.accessionDataProvider).setList(any(List.class));
	}
	
	
	@Test
	public void onSelectionChanged() {
		
		final SelectionChangeEvent event = mock(SelectionChangeEvent.class);
		final AccessionPresenter.TableSelectionChangeHandler handler = presenter.new TableSelectionChangeHandler();
		Accession accession = accessions.get(0);
		SingleSelectionModel<Accession> selectionModel = (SingleSelectionModel)view.getDisplay().getSelectionModel();
		given(selectionModel.getSelectedObject()).willReturn(accession);
		given(hasMarker.getTitle()).willReturn("1");
		given(clientData.getAccessionForId(anyInt())).willReturn(accession);

		presenter.onBind();
		presenter.onReveal();
		handler.onSelectionChange(event);
		
		assertEquals(accession,selectionModel.getSelectedObject());
		verify(view.getMap()).setZoom(8);
		verify(view.getMap()).setCenter(any(HasLatLng.class));
		verify(hasInfoWindow).open(eq(hasMap), any(HasMarker.class));
	}
	
	
}
