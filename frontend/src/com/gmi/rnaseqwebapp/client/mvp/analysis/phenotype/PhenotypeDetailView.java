package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.Environment;
import com.gmi.rnaseqwebapp.client.dto.GWASResult;
import com.gmi.rnaseqwebapp.client.dto.Transformation;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class PhenotypeDetailView extends ViewImpl implements
		PhenotypeDetailPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PhenotypeDetailView> {
	}
	
	@UiField SimpleLayoutPanel container;
	@UiField Hyperlink environmentOverviewLink;
	@UiField Hyperlink KWlink;
	@UiField Hyperlink EXlink;
	@UiField Hyperlink LMlink;
	
	
	private final PlaceManager placeManager;

	@Inject
	public PhenotypeDetailView(final Binder binder, final PlaceManager placeManager) {
		this.placeManager = placeManager;
		widget = binder.createAndBindUi(this);
	}
	

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override 
	public void setLinkParameter(Environment environment) {
		PlaceRequest request = new PlaceRequest(NameTokens.phenotypepage).with("id", environment.getPhenotype()).with("env",environment.getName());
		environmentOverviewLink.setTargetHistoryToken(placeManager.buildHistoryToken(request));
		KWlink.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("result", "KW")));
		KWlink.setVisible(false);
		LMlink.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("result", "LM")));
		LMlink.setVisible(false);
		EXlink.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("result", "EX")));
		EXlink.setVisible(false);
		Transformation transformation = environment.getDatasets().get(0).getTransformations().get(0);
		for (GWASResult result: transformation.getGWASResults()) {
			String resultName = result.getName();
			if (resultName.equals("KW"))
				KWlink.setVisible(true);
			else if (resultName.equals("LM"))
				LMlink.setVisible(true);
			else if (resultName.equals("EX"))
				EXlink.setVisible(true);
		}
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == PhenotypeDetailPresenter.TYPE_SetMainContent)
			container.setWidget(content);
		else
			super.setInSlot(slot, content);
	}
}
