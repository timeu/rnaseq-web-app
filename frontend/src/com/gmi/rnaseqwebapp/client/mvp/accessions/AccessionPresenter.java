package com.gmi.rnaseqwebapp.client.mvp.accessions;

import java.util.HashMap;
import java.util.List;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.maps.client.HasJso;
import com.google.gwt.maps.client.HasMap;
import com.google.gwt.maps.client.base.HasInfoWindow;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.event.EventCallback;
import com.google.gwt.maps.client.overlay.HasMarker;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gmi.rnaseqwebapp.client.dto.Accession;
import com.gmi.rnaseqwebapp.client.dto.Accession.AccessionPredicate;
import com.gmi.rnaseqwebapp.client.dto.Accession.AccessionPredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPagePresenter;
import com.gmi.rnaseqwebapp.client.ui.HasSearchHandlers;
import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;

public class AccessionPresenter extends
		Presenter<AccessionPresenter.MyView, AccessionPresenter.MyProxy> {

	public interface MyView extends View {
		HasSearchHandlers getSearchNameHandlers();;
		HasSearchHandlers getSearchCountryHandlers();
		HasSearchHandlers getSearchCollectorHandlers();
		HasData<Accession> getDisplay();
		HasMap getMap();
		HasMarker createMarkerAt(HasLatLng position);
		HasInfoWindow createInfoWindow(String content);
		HasLatLng createLatLng(double lat, double lng);
		void addMapClickHandler(HasJso instance, String eventName, EventCallback callback);
		void clearInstanceListeners(HasJso instance);
		void initMap();
		void unloadMap();
		void setSearchTerms(HashMap<AccessionPredicate.CRITERIA, AccessionPredicate> searchTerms);
		void initTable();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.accessionspage)
	public interface MyProxy extends ProxyPlace<AccessionPresenter> {
	}
	
	protected HashMap<AccessionPredicate.CRITERIA,AccessionPredicate> accessionPredicates = new HashMap<AccessionPredicate.CRITERIA,AccessionPredicate>();
	protected final HashMap<Integer, HasMarker> accessionId2Marker = new HashMap<Integer, HasMarker>();
	protected ListDataProvider<Accession> accessionDataProvider = new ListDataProvider<Accession>();
	protected HasInfoWindow infoWindow;
	private final ClientData clientData;
	

	@Inject
	public AccessionPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy,final ClientData clientData) {
		super(eventBus, view, proxy);
		this.clientData  = clientData;
		accessionDataProvider.addDataDisplay(getView().getDisplay());
		accessionPredicates.put(AccessionPredicate.CRITERIA.Name,new Accession.AccessionNamePredicate(""));
		accessionPredicates.put(AccessionPredicate.CRITERIA.Country,new Accession.AccessionCountryPredicate(""));
		accessionPredicates.put(AccessionPredicate.CRITERIA.Collector,new Accession.AccessionCollectorPredicate(""));
		getView().setSearchTerms(accessionPredicates);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		infoWindow = getView().createInfoWindow("");
		getView().initTable();
		accessionDataProvider.setList(clientData.getAccessions());
		registerHandler(getView().getSearchNameHandlers().addKeyUpHandler(new SearchKeyUpHandler(accessionPredicates.get(AccessionPredicate.CRITERIA.Name))));
		registerHandler(getView().getSearchCountryHandlers().addKeyUpHandler(new SearchKeyUpHandler(accessionPredicates.get(AccessionPredicate.CRITERIA.Country))));
		registerHandler(getView().getSearchCollectorHandlers().addKeyUpHandler(new SearchKeyUpHandler(accessionPredicates.get(AccessionPredicate.CRITERIA.Collector))));
		registerHandler(getView().getDisplay().getSelectionModel().addSelectionChangeHandler(new TableSelectionChangeHandler()));
	}
	
	@Override 
	protected void onHide() {
		super.onHide();
		unloadMap();
	}
	@Override
	protected void onReveal() {
		super.onReveal();
		initMarkers();
	}
	
	@Override 
	protected void onUnbind() {
		super.onUnbind();
		unloadMap();
	}
	
	private void unloadMap() {
		for (HasMarker marker: accessionId2Marker.values()) {
			getView().clearInstanceListeners(marker);
		}
		accessionId2Marker.clear();
		getView().unloadMap();
	}

	private void initMarkers() {
		getView().initMap();
		accessionId2Marker.clear();
		for (final Accession accession:clientData.getAccessions()) {
			final HasMarker marker = getView().createMarkerAt(getView().createLatLng(accession.getLatitude(), accession.getLongitude()));
			marker.setTitle(accession.getAccessionId().toString());
			accessionId2Marker.put(accession.getAccessionId(),marker);
			getView().addMapClickHandler(marker, "click", new EventCallback() {

				@Override
				public void callback() {
					infoWindow.setContent(accession.getLabelForMapMarker());
					infoWindow.open(getView().getMap(), marker);
					int id = Integer.parseInt(marker.getTitle());
					Accession accession = clientData.getAccessionForId(id);
					getView().getDisplay().getSelectionModel().setSelected(accession, true);
					int selectedIndex = accessionDataProvider.getList().indexOf(accession);
					getView().getDisplay().setVisibleRange(selectedIndex, getView().getDisplay().getVisibleItemCount());
				}
			});
		}
	}
	
	class SearchKeyUpHandler implements KeyUpHandler {
		
		final AccessionPredicate predicate;
		
		public SearchKeyUpHandler(final AccessionPredicate predicate) {
			this.predicate = predicate;
		}
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			HasSearchHandlers getSearchValue = (HasSearchHandlers)event.getSource();
			String value = getSearchValue.getText();
			predicate.setValue(value);
			List<Accession> filtered_list = AbstractDtoPredicate.filter(clientData.getAccessions(), accessionPredicates.values());
			accessionDataProvider.setList(filtered_list);
		}
	}
	
	class TableSelectionChangeHandler implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			SingleSelectionModel<Accession> selectionModel = (SingleSelectionModel<Accession>)getView().getDisplay().getSelectionModel();
			final HasMarker marker = accessionId2Marker.get(selectionModel.getSelectedObject().getAccessionId());
			if (marker != null) {
				int id = Integer.parseInt(marker.getTitle());
				Accession accession = clientData.getAccessionForId(id);
				infoWindow.setContent(accession.getLabelForMapMarker());
				infoWindow.open(getView().getMap(), marker);
				getView().getMap().setCenter(getView().createLatLng(accession.getLatitude(), accession.getLongitude()));
				getView().getMap().setZoom(8);
			}
		}
	}
}
