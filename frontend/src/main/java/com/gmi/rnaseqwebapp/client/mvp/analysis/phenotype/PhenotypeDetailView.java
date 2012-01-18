package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.Environment;
import com.gmi.rnaseqwebapp.client.dto.GWASResult;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.gmi.rnaseqwebapp.client.dto.Transformation;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.ui.PhenotypeSuggestOracle;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.InlineHyperlink;
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
	
	
	@UiField SimpleLayoutPanel container;
	@UiField(provided=true) SuggestBox search_phenotypes;
	@UiField(provided=true) MyResources mainRes;
	@UiField Hyperlink environmentOverviewLink;
	@UiField Hyperlink KWlink;
	@UiField Hyperlink EXlink;
	@UiField Hyperlink LMlink;
	@UiField HTMLPanel step_wise_container;
	
	//public enum NAV_ITEMS {Overview,KW,LM,EX};
	
	private final PlaceManager placeManager;

	@Inject
	public PhenotypeDetailView(final Binder binder, final PlaceManager placeManager, 
			final PhenotypesReader phenotypesReader,final MyResources resources) {
		this.mainRes = resources;
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
		step_wise_container.clear();
		step_wise_container.getElement().setInnerHTML("");
		UListElement step_wise_list = Document.get().createULElement();
		step_wise_container.getElement().appendChild(step_wise_list);
		step_wise_list.setClassName(mainRes.style().nav_list());
		Transformation transformation = environment.getDatasets().get(0).getTransformations().get(0);
		for (GWASResult result: transformation.getGWASResults()) {
			String resultName = result.getName();
			if (resultName.equals("KW"))
				KWlink.setVisible(true);
			else if (resultName.equals("LM"))
				LMlink.setVisible(true);
			else if (resultName.equals("EX"))
				EXlink.setVisible(true);
			else {
				LIElement li = Document.get().createLIElement();
				li.setClassName(mainRes.style().nav_item());
				step_wise_list.appendChild(li);
				InlineHyperlink link = new InlineHyperlink();
				//AnchorElement link = Document.get().createAnchorElement();
				link.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("result", result.getName())));
				link.getElement().setAttribute("result", result.getName());
				link.setText("Step " + result.getStep());
				li.appendChild(link.getElement());
			}
		}
	}
	
	@Override
	public void setActiveLink(String link) {
		String nav_item_selected = mainRes.style().nav_item_selected();
		environmentOverviewLink.removeStyleName(nav_item_selected);
		KWlink.removeStyleName(nav_item_selected);
		LMlink.removeStyleName(nav_item_selected);
		EXlink.removeStyleName(nav_item_selected);
		removeStyleNameForStepWise();
		if (link.equals("Overview")) 
			environmentOverviewLink.addStyleName(nav_item_selected);
		else if (link.equals("KW"))
			KWlink.addStyleName(nav_item_selected);
		else if (link.equals("LM"))
			LMlink.addStyleName(nav_item_selected);
		else if (link.equals("EX")) 		
			EXlink.addStyleName(nav_item_selected);
		else {
			setStepWiseSelected(link);
		}
		
	}
	
	private void removeStyleNameForStepWise() {
		UListElement li_elem = step_wise_container.getElement().getFirstChildElement().cast();
		NodeList<Node> li_items = li_elem.getChildNodes();
		for (int i = 0;i<li_items.getLength();i++) {
			Element elem = li_items.getItem(i).cast();
			AnchorElement link = elem.getFirstChild().cast();
			//InlineHyperlink link =  elem.getFirstChildElement()
			//link.removeStyleName(style.nav_item_selected());
			link.setClassName("");
		}
	}
	
	private void setStepWiseSelected(String selected_link) {
		UListElement li_elem = step_wise_container.getElement().getFirstChildElement().cast();
		NodeList<Node> li_items = li_elem.getChildNodes();
		for (int i = 0;i<li_items.getLength();i++) {
			Element elem = li_items.getItem(i).cast();
			AnchorElement link = elem.getFirstChild().cast();
			if (link.getAttribute("result").equals(selected_link))
				link.setClassName(mainRes.style().nav_item_selected());
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
