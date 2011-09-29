package com.gmi.rnaseqwebapp.client.mvp.accessions;

import java.util.HashMap;

import at.gmi.nordborglab.visualizations.geochart.client.GeoChart;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gmi.rnaseqwebapp.client.dto.Accession;
import com.gmi.rnaseqwebapp.client.dto.Accession.AccessionPredicate;
import com.gmi.rnaseqwebapp.client.dto.Accession.AccessionPredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.resources.CellTableResources;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.ui.HasSearchHandlers;
import com.gmi.rnaseqwebapp.client.ui.SearchTextBox;
import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;
import com.gmi.rnaseqwebapp.client.util.SearchTermHighlight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.HasJso;
import com.google.gwt.maps.client.HasMap;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.HasInfoWindow;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.InfoWindow;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.EventCallback;
import com.google.gwt.maps.client.overlay.HasMarker;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.inject.Inject;

public class AccessionView extends ViewImpl implements
		AccessionPresenter.MyView {

	private final Widget widget;
	
	final MyResources mainRes;
	@UiField(provided = true) CellTable<Accession> table;
	@UiField(provided=true) SimplePager pager;
	//@UiField FlowPanel geoChartContainer;
	@UiField SearchTextBox Search_Name;
	@UiField SearchTextBox Search_Country;
	@UiField SearchTextBox Search_Collector;
	private MapWidget map;
	@UiField HTMLPanel mapContainer;

	private HashMap<AccessionPredicate.CRITERIA, AccessionPredicate> searchTerms;
	protected final SingleSelectionModel<Accession> selectionModel = new SingleSelectionModel<Accession>(Accession.KEY_PROVIDER);
	//@UiField GeoChart geoChart;
	
	

	public interface Binder extends UiBinder<Widget, AccessionView> {
	}

	@Inject
	public AccessionView(final Binder binder,final MyResources resources,final CellTableResources cellTableResources) {
		this.mainRes = resources;
		table = new CellTable<Accession>(getPageSizeFromResolution(),cellTableResources,Accession.KEY_PROVIDER);
		table.setWidth("100%", true);
		table.setHeight("100%");
		table.setSelectionModel(selectionModel);
		pager = new SimplePager();
		pager.setDisplay(table);
		widget = binder.createAndBindUi(this);
		Search_Collector.getElement().setAttribute("placeHolder", "Collector");
		Search_Country.getElement().setAttribute("placeHolder", "Country");
		Search_Name.getElement().setAttribute("placeHolder", "Name");
	}


	private void initMapWidget() {
		
		final MapOptions options = new MapOptions();
	    // Zoom level. Required
	    options.setZoom(3);
	    options.setCenter(new LatLng(56.267761, -23.232425));
	    // Map type. Required.
	    options.setMapTypeId(new MapTypeId().getTerrain());
	    // Enable maps drag feature. Disabled by default.
	    options.setDraggable(true);
	    options.setScrollwheel(true);
	    // Enable and add default navigation control. Disabled by default.
	    options.setNavigationControl(true);
	    // Enable and add map type control. Disabled by default.
	    options.setMapTypeControl(true);
		map = new MapWidget(options);
		map.setSize("100%", "100%");
		mapContainer.add(map);
	}

	private void initCellTable() {
		
		//SearchTerms
		
		table.addColumn(new AccessionCellTableColumns.AccessionIdColumn(),"ID");
		table.addColumn(new AccessionCellTableColumns.NameColumn(searchTerms.get(AccessionPredicate.CRITERIA.Name)),"Name");
		table.addColumn(new AccessionCellTableColumns.LongitudeColunn(),"Lon");
		table.addColumn(new AccessionCellTableColumns.LatitudeColumn(),"Lat");
		table.addColumn(new AccessionCellTableColumns.CountryColumn(searchTerms.get(AccessionPredicate.CRITERIA.Country)),"Country");
		table.addColumn(new AccessionCellTableColumns.CollectionDateColumn(),"Date");
		table.addColumn(new AccessionCellTableColumns.CollectorColumn(searchTerms.get(AccessionPredicate.CRITERIA.Collector)),"Collector");
		
		table.setColumnWidth(table.getColumn(0),5, Unit.PCT);
		table.setColumnWidth(table.getColumn(1),15, Unit.PCT);
		table.setColumnWidth(table.getColumn(2),8, Unit.PCT);
		table.setColumnWidth(table.getColumn(3),8, Unit.PCT);
		table.setColumnWidth(table.getColumn(4),15, Unit.PCT);
		//
		
		
	}

	private int getPageSizeFromResolution() {
		int rowHeight=12;
		int pageSize = 30;
		int headerHeight = 135;
		int pagerHeight = 30;
		int topBarHeight = 92;
		int screen_width = Window.getClientWidth();
		if (screen_width <= 1210)
			rowHeight = 53;
		else if (screen_width <=1810)
			rowHeight = 38;
		else
			rowHeight = 24;
		if (pageSize >=33)
			pageSize = pageSize -1;
		pageSize = Math.round((Window.getClientHeight()-headerHeight - pagerHeight - topBarHeight) / rowHeight);
		return pageSize;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public HasSearchHandlers getSearchNameHandlers() {
		return Search_Name;
	}

	@Override
	public HasSearchHandlers getSearchCountryHandlers() {
		return Search_Country;
	}

	@Override
	public HasSearchHandlers getSearchCollectorHandlers() {
		return Search_Collector;
	}


	@Override
	public HasData<Accession> getDisplay() {
		return table;
	}

	@Override
	public HasMap getMap() {
		return map.getMap();
	}

	@Override
	public HasMarker createMarkerAt(HasLatLng position) {
		final Marker marker = new Marker();
		marker.setMap(getMap());
		marker.setPosition(position);
		return marker;
	}

	@Override
	public HasInfoWindow createInfoWindow(String content) {
		final InfoWindow infoWindow = new InfoWindow();
		infoWindow.setContent(content);
		return infoWindow;
	}

	@Override
	public HasLatLng createLatLng(double lat, double lng) {
		return new LatLng(lat,lng);
	}

	@Override
	public void addMapClickHandler(HasJso instance, String eventName,
			EventCallback callback) {
		Event.addListener(instance, eventName, callback);
	}

	@Override
	public void clearInstanceListeners(HasJso instance) {
		Event.clearInstanceListeners(instance);
	}
	
	private GeoChart.Options createGeoChartOptions() {
		final GeoChart.Options options = GeoChart.Options.create();
		options.setHeight(600);
		options.setWidth(600);
		return options;
	}

	@Override
	public void initMap() {
		initMapWidget();
	}
	
	@Override
	public void initTable() {
		initCellTable();
	}

	@Override
	public void unloadMap() {
		mapContainer.clear();
		map = null;
		
	}

	@Override
	public void setSearchTerms(HashMap<AccessionPredicate.CRITERIA, AccessionPredicate> searchTerms) {
		this.searchTerms = searchTerms; 
		
	}
}
