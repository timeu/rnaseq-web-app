package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.Environment;
import com.gmi.rnaseqwebapp.client.dto.GWASResult;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.gmi.rnaseqwebapp.client.dto.Transformation;
import com.gmi.rnaseqwebapp.client.ui.PhenotypeSuggestOracle;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle.MultiWordSuggestion;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class PhenotypeDetailView extends ViewImpl implements
		PhenotypeDetailPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PhenotypeDetailView> {
	}
	
	interface MyStyle extends CssResource {
	    String nav_item_selected();
	}
	
	@UiField SimpleLayoutPanel container;
	@UiField(provided=true) SuggestBox search_phenotypes;
	@UiField Hyperlink environmentOverviewLink;
	@UiField Hyperlink KWlink;
	@UiField Hyperlink EXlink;
	@UiField Hyperlink LMlink;
	@UiField MyStyle style;
	
	public enum NAV_ITEMS {Overview,KW,LM,EX};
	
	private final PlaceManager placeManager;

	@Inject
	public PhenotypeDetailView(final Binder binder, final PlaceManager placeManager, final PhenotypesReader phenotypesReader) {
		search_phenotypes = new SuggestBox(new PhenotypeSuggestOracle(phenotypesReader));
		this.placeManager = placeManager;
		widget = binder.createAndBindUi(this);
		search_phenotypes.getElement().setAttribute("placeHolder", "Search");
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
	public void setActiveLink(NAV_ITEMS link) {
		String nav_item_selected = style.nav_item_selected();
		environmentOverviewLink.removeStyleName(nav_item_selected);
		KWlink.removeStyleName(nav_item_selected);
		LMlink.removeStyleName(nav_item_selected);
		EXlink.removeStyleName(nav_item_selected);
		switch (link) {
			case Overview:
				environmentOverviewLink.addStyleName(nav_item_selected);
				break;
			case KW:
				KWlink.addStyleName(nav_item_selected);
				break;
			case LM:
				LMlink.addStyleName(nav_item_selected);
				break;
			case EX:
				EXlink.addStyleName(nav_item_selected);
				break;
		}
		
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == PhenotypeDetailPresenter.TYPE_SetMainContent)
			container.setWidget(content);
		else
			super.setInSlot(slot, content);
	}
	
	@Override
	public HasSelectionHandlers<Suggestion> getSeachBoxHandler() {
		return search_phenotypes;
	}
}
