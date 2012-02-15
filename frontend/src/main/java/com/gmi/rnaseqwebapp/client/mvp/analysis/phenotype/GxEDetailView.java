package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.GxEResult;
import com.gmi.rnaseqwebapp.client.dto.GxEResult.TYPE;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.PhenotypeView.NAV_ITEMS;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.ui.PhenotypeSuggestOracle;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class GxEDetailView extends ViewWithUiHandlers<GxEUiHandlers> implements
		GxEDetailPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, GxEDetailView> {
	}
	
	@UiField SimpleLayoutPanel container;
	@UiField(provided=true) SuggestBox search_phenotypes;
	@UiField(provided=true) MyResources mainRes;
	@UiField Hyperlink combinedLink;
	@UiField Hyperlink fullLink;
	@UiField Hyperlink geneticLink;
	@UiField Hyperlink environLink;

	
	private final PlaceManager placeManager;
	
	@Inject
	public GxEDetailView(final Binder binder,final PlaceManager placeManager, 
			final PhenotypesReader phenotypesReader,final MyResources resources) {
		this.mainRes = resources;
		search_phenotypes = new SuggestBox(new PhenotypeSuggestOracle(phenotypesReader));
		search_phenotypes.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				getUiHandlers().searchGene(event.getSelectedItem().getReplacementString());
			}
		});
		this.placeManager = placeManager;
		widget = binder.createAndBindUi(this);
		search_phenotypes.getElement().setAttribute("placeHolder", "Search");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == GxEDetailPresenter.TYPE_SetMainContent)
			container.setWidget(content);
		else
			super.setInSlot(slot, content);
	}
	
	@Override
	public void setActiveLink(TYPE type) {
		String nav_item_selected = mainRes.style().nav_item_selected();
		combinedLink.removeStyleName(nav_item_selected);
		fullLink.removeStyleName(nav_item_selected);
		environLink.removeStyleName(nav_item_selected);
		geneticLink.removeStyleName(nav_item_selected);
		if (type == TYPE.Combined) 
			combinedLink.addStyleName(nav_item_selected);
		else if (type== TYPE.Full)
			fullLink.addStyleName(nav_item_selected);
		else if (type== TYPE.Environ)
			environLink.addStyleName(nav_item_selected);
		else if (type== TYPE.Genetic) 		
			geneticLink.addStyleName(nav_item_selected);
	}
	
	@Override 
	public void setLinkParameter(Phenotype phenotype) {
		PlaceRequest request = new PlaceRequest(NameTokens.phenotypepage).with("id", phenotype.getName()).with("env",NAV_ITEMS.GxE.toString());
		combinedLink.setTargetHistoryToken(placeManager.buildHistoryToken(request));
		fullLink.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("result", GxEResult.TYPE.Full.toString())));
		fullLink.setVisible(false);
		geneticLink.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("result", GxEResult.TYPE.Genetic.toString())));
		geneticLink.setVisible(false);
		environLink.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("result", GxEResult.TYPE.Environ.toString())));
		environLink.setVisible(false);
		for (GxEResult result: phenotype.getGxEResults()) {
			if (result.getType() == GxEResult.TYPE.Full)
				fullLink.setVisible(true);
			else if (result.getType() ==  GxEResult.TYPE.Genetic)
				geneticLink.setVisible(true);
			else if (result.getType() ==  GxEResult.TYPE.Environ)
				environLink.setVisible(true);
		}
	}
	
	
}
