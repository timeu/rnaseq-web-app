package com.gmi.rnaseqwebapp.client.mvp.analysis;

import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.ui.SlidingPanel;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AnalysisView extends ViewImpl implements AnalysisPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, AnalysisView> {
	}
	
	@UiField SlidingPanel contentContainer;
	
	@UiField InlineHyperlink browseLink;
	@UiField InlineHyperlink EX10Link;
	@UiField InlineHyperlink LM10Link;
	@UiField InlineHyperlink KW10Link;
	@UiField InlineHyperlink EX16Link;
	@UiField InlineHyperlink LM16Link;
	@UiField InlineHyperlink KW16Link;
	@UiField InlineHyperlink GxEFullLink;
	@UiField InlineHyperlink GxEGeneticLink;
	@UiField InlineHyperlink GxEEnvironLink;
	@UiField MyResources mainRes;
	
	public enum NAV_LINKS {browseLink,EX10Link,LM10Link,KW10Link,EX16Link,LM16Link,KW16Link,GxEFullLink,GxEGeneticLink,GxEEnvironLink}

	@Inject
	public AnalysisView(final Binder binder, final MyResources resources) {
		this.mainRes = resources;
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot,Widget content) {
		if (slot == AnalysisPresenter.TYPE_SetMainContent) {
			setMainContent(content);
		}
		else {
			super.setInSlot(slot,content);
		}
	}
	
	protected void setMainContent(Widget content) 
	{
		
	    if (content != null) {
	    	contentContainer.setWidget(content);
	    }
	}
	
	@Override
	public void setActiveLink(NAV_LINKS link) {
		String nav_item_selected = mainRes.style().nav_item_selected();
		browseLink.removeStyleName(nav_item_selected);
		EX10Link.removeStyleName(nav_item_selected);
		LM10Link.removeStyleName(nav_item_selected);
		KW10Link.removeStyleName(nav_item_selected);
		
		EX16Link.removeStyleName(nav_item_selected);
		LM16Link.removeStyleName(nav_item_selected);
		KW16Link.removeStyleName(nav_item_selected);
		
		GxEFullLink.removeStyleName(nav_item_selected);
		GxEGeneticLink.removeStyleName(nav_item_selected);
		GxEEnvironLink.removeStyleName(nav_item_selected);
		
		InlineHyperlink active_link = null;
		switch (link) {
			case browseLink:
				active_link = browseLink;
				break;
			case EX10Link:
				active_link = EX10Link;
				break;
			case LM10Link:
				active_link = LM10Link;
				break;
			case KW10Link:
				active_link = KW10Link;
				break;
				
			case EX16Link:
				active_link = EX16Link;
				break;
			case LM16Link:
				active_link = LM16Link;
				break;
			case KW16Link:
				active_link = KW16Link;
				break;
				
			case GxEFullLink:
				active_link = GxEFullLink;
				break;
			case GxEGeneticLink:
				active_link = GxEGeneticLink;
				break;
			case GxEEnvironLink:
				active_link = GxEEnvironLink;
				break;
		}
		
		active_link.addStyleName(nav_item_selected);
	}
}
