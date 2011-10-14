package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.ui.SlidingPanel;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class PhenotypeView extends ViewImpl implements
		PhenotypePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PhenotypeView> {
	}
	
	@UiField InlineLabel phenotype_info;
	@UiField SlidingPanel container;
	@UiField InlineHyperlink overviewLink;
	@UiField InlineHyperlink T16Link;
	@UiField InlineHyperlink T10Link;
	
	private final PlaceManager placeManager;

	@Inject
	public PhenotypeView(final Binder binder, final PlaceManager placeManager) {
		widget = binder.createAndBindUi(this);
		this.placeManager = placeManager;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void showPhenotypeInfo(Phenotype phenotype) {
		String info = phenotype.getName() + " (Chr: "+phenotype.getChr() + " | " + phenotype.getStart() + " - " + phenotype.getEnd() + ")";
		phenotype_info.setText(info);
		overviewLink.setTargetHistoryToken(placeManager.buildHistoryToken(new PlaceRequest(NameTokens.phenotypepage).with("id", phenotype.getName())));
		T16Link.setTargetHistoryToken(placeManager.buildHistoryToken(placeManager.getCurrentPlaceRequest().with("env", "T16C")));
		T10Link.setTargetHistoryToken(placeManager.buildHistoryToken(placeManager.getCurrentPlaceRequest().with("env", "T10C")));
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == PhenotypePresenter.TYPE_SetMainContent) {
			//container.clear();
			container.setWidget(content);
		}
		else {
			super.setInSlot(slot, content);
		}
		
	}
}
