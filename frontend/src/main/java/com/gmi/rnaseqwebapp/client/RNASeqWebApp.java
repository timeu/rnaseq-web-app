package com.gmi.rnaseqwebapp.client;


import com.gmi.rnaseqwebapp.client.gin.ClientGinjector;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

public class RNASeqWebApp implements EntryPoint {
	
	private final ClientGinjector ginjector = GWT.create(ClientGinjector.class);

	@Override
	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);
		MyResources resource = ginjector.getResource();
		resource.style().ensureInjected();
		ClientData clientData = ginjector.getClientData();
		clientData.loadData();
		ginjector.getPlaceManager().revealCurrentPlace();
	}

}
