package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gmi.rnaseqwebapp.client.dto.Environment;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypePresenter;

public class PhenotypeDetailPresenter
		extends
		PresenterWidget<PhenotypeDetailPresenter.MyView> {

	public interface MyView extends View {

		void setLinkParameter(Environment environment);
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
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		PlaceRequest currentPlace = placeManager.getCurrentPlaceRequest();
		String result = currentPlace.getParameter("result", "");
		getView().setLinkParameter(environment);
		if (result.equals("")) {
			environmentDetailPresenter.setData(environment,histogramDataTable);
			setInSlot(TYPE_SetMainContent,environmentDetailPresenter);
		}
		else {
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
