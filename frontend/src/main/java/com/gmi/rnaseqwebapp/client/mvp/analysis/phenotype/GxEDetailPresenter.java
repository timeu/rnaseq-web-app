package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.GxEResult;
import com.gmi.rnaseqwebapp.client.dto.GxEResult.TYPE;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class GxEDetailPresenter extends
		PresenterWidget<GxEDetailPresenter.MyView> implements GxEUiHandlers {

	public interface MyView extends View,HasUiHandlers<GxEUiHandlers> {

		void setActiveLink(TYPE type);

		void setLinkParameter(Phenotype phenotype);
	}
	
	private final ResultPresenter resultPresenter;
	private final PlaceManager placeManager;
	private Phenotype phenotype;
	
	public static final Object TYPE_SetMainContent = new Object();

	@Inject
	public GxEDetailPresenter(final EventBus eventBus, final MyView view,
			final PlaceManager placeManager,final ResultPresenter resultPresenter) {
		super(eventBus, view);
		this.placeManager = placeManager;
		this.resultPresenter = resultPresenter;
		getView().setUiHandlers(this);
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
		GxEResult gxeResult = null;
		if (result.equals("")) {
			result ="combined";
			gxeResult = new GxEResult(phenotype.getName(),GxEResult.TYPE.Combined);
		}
		else
			gxeResult = phenotype.getGxEResultFromType(GxEResult.TYPE.valueOf(result));
		getView().setLinkParameter(phenotype);
		getView().setActiveLink(gxeResult.getType());
		resultPresenter.setData(gxeResult,phenotype);
		setInSlot(TYPE_SetMainContent,resultPresenter);
	}

	@Override
	public void searchGene(String gene) {
		PlaceRequest request = new PlaceRequest(NameTokens.phenotypepage).with("id", gene);
		placeManager.revealPlace(request);
	}
	
	void setData(Phenotype phenotype) {
		this.phenotype = phenotype;
	}
	
	
}
