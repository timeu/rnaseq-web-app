package com.gmi.rnaseqwebapp.client.mvp.analysis;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisView.NAV_LINKS;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPagePresenter;

public class AnalysisPresenter extends
		Presenter<AnalysisPresenter.MyView, AnalysisPresenter.MyProxy> {

	public interface MyView extends View {

		void setActiveLink(NAV_LINKS link);
		// TODO Put your view methods here
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<AnalysisPresenter> {
	}
	
	protected final PlaceManager placeManager;

	@Inject
	public AnalysisPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy,final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}
	
	@Override
	protected void onReset() {
		PlaceRequest placeRequest = placeManager.getCurrentPlaceRequest();
		NAV_LINKS link = null;
	    if (placeRequest.matchesNameToken(NameTokens.resultslistpage))
	    	link = NAV_LINKS.browseLink;
	    else
	    {
	    	String environ_type = placeRequest.getParameter("environ_type", "");
	    	String result_type = placeRequest.getParameter("result_type", "");
	    	if (environ_type.equals("T10C"))
	    		link = NAV_LINKS.valueOf(result_type+"10Link");
	    	else if (environ_type.equals("T16C"))
	    		link = NAV_LINKS.valueOf(result_type+"16Link");
	    	else
	    		link = NAV_LINKS.valueOf(environ_type+result_type+"Link");
	    }
	    getView().setActiveLink(link);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest placeRequest) {
	    super.prepareFromRequest(placeRequest);
	    
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
