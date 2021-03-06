package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.ui.SlidingPanel;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class PhenotypeView extends ViewImpl implements
		PhenotypePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PhenotypeView> {
	}
	
	@UiField InlineLabel phenotype_info;
	@UiField InlineLabel phenotype_name;
	
	@UiField SlidingPanel container;
	@UiField InlineHyperlink overviewLink;
	@UiField InlineHyperlink T16Link;
	@UiField InlineHyperlink T10Link;
	@UiField InlineHyperlink GxELink;
	@UiField InlineHyperlink bisulfiteLink;
	@UiField InlineHyperlink mRNALink;
	@UiField LIElement overview_item;
	@UiField LIElement t16_item;
	@UiField LIElement t10_item;
	@UiField LIElement gxe_item;
	@UiField LIElement mRNA_item;
	@UiField LIElement bisulfite_item;
	@UiField UListElement sub_menu;
	
	@UiField(provided=true)final MyResources mainRes;
	
	public enum NAV_ITEMS {Overview,T16C,T10C,GxE};
	public enum SUB_NAV_ITEMS  {mRNA,bisulfite};
	
	private final PlaceManager placeManager;

	@Inject
	public PhenotypeView(final Binder binder, final PlaceManager placeManager, final MyResources resources) {
		this.mainRes = resources;
		widget = binder.createAndBindUi(this);
		this.placeManager = placeManager;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void showPhenotypeInfo(Phenotype phenotype) {
		String info = "Chr: "+phenotype.getChr() + " | " + phenotype.getStart() + " - " + phenotype.getEnd();
		phenotype_info.setText(info);
		phenotype_name.setText(phenotype.getName()+": ");
		PlaceRequest request = new PlaceRequest(NameTokens.phenotypepage).with("id", phenotype.getName());
		overviewLink.setTargetHistoryToken(placeManager.buildHistoryToken(request));
		T16Link.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("env", NAV_ITEMS.T16C.toString())));
		T10Link.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("env", NAV_ITEMS.T10C.toString())));
		GxELink.setTargetHistoryToken(placeManager.buildHistoryToken(request.with("env", NAV_ITEMS.GxE.toString())));
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == PhenotypePresenter.TYPE_SetMainContent) {
			container.setWidget(content);
		}
		else {
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public void setActiveLink(NAV_ITEMS item,SUB_NAV_ITEMS sub_item) {
		String selected_class_name = mainRes.style().indicator_small_icon();
		overview_item.removeClassName(selected_class_name);
		t16_item.removeClassName(selected_class_name);
		t10_item.removeClassName(selected_class_name);
		gxe_item.removeClassName(selected_class_name);
		mRNA_item.removeClassName(selected_class_name);
		bisulfite_item.removeClassName(selected_class_name);
		if (item == NAV_ITEMS.Overview)
			sub_menu.setAttribute("style", "display:none");
		else
			sub_menu.setAttribute("style", "display:inline");
		switch (item) {
			case Overview:
				overview_item.addClassName(selected_class_name);
				break;
			case T10C:
				t10_item.addClassName(selected_class_name);
				break;
			case T16C:
				t16_item.addClassName(selected_class_name);
				break;
			case GxE:
				gxe_item.addClassName(selected_class_name);
		}
		switch (sub_item) {
			case mRNA:
				mRNA_item.addClassName(selected_class_name);
				break;
			case bisulfite:
				bisulfite_item.addClassName(selected_class_name);
				break;
		}
	}
}
