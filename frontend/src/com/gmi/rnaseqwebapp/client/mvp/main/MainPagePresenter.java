package com.gmi.rnaseqwebapp.client.mvp.main;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gmi.rnaseqwebapp.client.event.LoadingIndicatorEvent;
import com.gmi.rnaseqwebapp.client.event.LoadingIndicatorEvent.LoadingIndicatorHandler;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;

public class MainPagePresenter extends
		Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> implements LoadingIndicatorHandler{

	public interface MyView extends View {
		void setActiveNavigationItem(String nameToken);
		void showLoadingIndicator(boolean visible);
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

	@ProxyStandard
	public interface MyProxy extends Proxy<MainPagePresenter> {
	}
	
	private PlaceManager placeManager;

	@Inject
	public MainPagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy,final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(LoadingIndicatorEvent.getType(),this);
	}
	
	@Override 
	protected void onReset() {
		super.onReset();
		PlaceRequest request = placeManager.getCurrentPlaceRequest();
		if (request != null)
			getView().setActiveNavigationItem(request.getNameToken());
	}

	@Override
	public void onProcessLoadingIndicator(LoadingIndicatorEvent event) {
		getView().showLoadingIndicator(event.getShow());
	}
}
