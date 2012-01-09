package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;


import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.google.inject.Inject;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.visualization.client.DataTable;
import com.gmi.rnaseqwebapp.client.dto.Environment;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypeDetailView.NAV_ITEMS;


public class PhenotypeDetailPresenter
		extends
		PresenterWidget<PhenotypeDetailPresenter.MyView> {

	public interface MyView extends View {

		void setLinkParameter(Environment environment);

		void setActiveLink(NAV_ITEMS link);

		HasSelectionHandlers<Suggestion> getSeachBoxHandler();
	}

	public static final Object TYPE_SetMainContent = new Object();
	
	private DataTable histogramDataTable;
	private Phenotype phenotype;
	private Environment environment;
	private final PlaceManager placeManager;
	
	private final EnvironmentDetailPresenter environmentDetailPresenter;
	private final ResultPresenter resultPresenter;


	@Inject
	public PhenotypeDetailPresenter(final EventBus eventBus, final MyView view,
			final  EnvironmentDetailPresenter environmentDetailPresenter, 
			final PlaceManager placeManager,final ResultPresenter resultPresenter) {
		super(eventBus, view);
		this.environmentDetailPresenter = environmentDetailPresenter;
		this.placeManager = placeManager;
		this.resultPresenter = resultPresenter;
	}


	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().getSeachBoxHandler().addSelectionHandler(new SelectionHandler<Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				PlaceRequest request = new PlaceRequest(NameTokens.phenotypepage).with("id", event.getSelectedItem().getReplacementString()).with("env", environment.getName());
				placeManager.revealPlace(request);
			}
		}));
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		PlaceRequest currentPlace = placeManager.getCurrentPlaceRequest();
		String result = currentPlace.getParameter("result", "");
		getView().setLinkParameter(environment);
		if (result.equals("")) {
			getView().setActiveLink(NAV_ITEMS.Overview);
			environmentDetailPresenter.setData(environment,histogramDataTable);
			setInSlot(TYPE_SetMainContent,environmentDetailPresenter);
			
		}
		else {
			getView().setActiveLink(NAV_ITEMS.valueOf(result));
			resultPresenter.setData(environment.getDatasets().get(0).getTransformations().get(0).getResultFromName(result));
			setInSlot(TYPE_SetMainContent,resultPresenter);
		}
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
	}


	public void setData(Phenotype phenotype, Environment environment, DataTable histogramDataTable) {
		this.histogramDataTable = histogramDataTable;
		this.phenotype = phenotype;
		this.environment = environment;
	}
		
}
