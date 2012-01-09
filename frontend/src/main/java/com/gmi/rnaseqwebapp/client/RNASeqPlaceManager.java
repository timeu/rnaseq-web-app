package com.gmi.rnaseqwebapp.client;

import at.gmi.nordborglab.widgets.geochart.client.GeoChart;

import com.gmi.rnaseqwebapp.client.gin.DefaultPlace;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.MotionChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class RNASeqPlaceManager extends PlaceManagerImpl {
	
	private final PlaceRequest defaultPlaceRequest;

	@Inject
	public RNASeqPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, @DefaultPlace String defaultNameToken) {
		super(eventBus, tokenFormatter);
		this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(defaultPlaceRequest);
	}
	
	@Override
	public void revealCurrentPlace() {
		VisualizationUtils.loadVisualizationApi(new Runnable() {
			
			@Override
			public void run() {
				RNASeqPlaceManager.super.revealCurrentPlace();
				
			}
		}, CoreChart.PACKAGE, GeoChart.PACKAGE,MotionChart.PACKAGE);	
	}

}
