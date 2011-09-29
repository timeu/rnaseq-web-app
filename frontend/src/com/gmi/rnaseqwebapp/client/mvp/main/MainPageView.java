package com.gmi.rnaseqwebapp.client.mvp.main;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gmi.rnaseqwebapp.client.ui.LoadingIndicator;
import com.gmi.rnaseqwebapp.client.ui.SlidingPanel;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainPageView extends ViewImpl implements MainPagePresenter.MyView {

	private final Widget widget;
	
	interface MyStyle extends CssResource {
	    String current_page_item();
	}

	public interface Binder extends UiBinder<Widget, MainPageView> {
	}
	
	@UiField MyStyle style;
	@UiField InlineHyperlink homeLink;
	@UiField InlineHyperlink helpLink;
	@UiField InlineHyperlink analysisLink;
	@UiField InlineHyperlink accessionLink;
	@UiField LoadingIndicator loadingIndicator;
	@UiField SlidingPanel contentContainer;

	@Inject
	public MainPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		loadingIndicator.setVisible(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setActiveNavigationItem(String nameToken) {
		String currentPageItemStyleName = style.current_page_item();
		homeLink.removeStyleName(currentPageItemStyleName);
		helpLink.removeStyleName(currentPageItemStyleName);
		accessionLink.removeStyleName(style.current_page_item());
		analysisLink.removeStyleName(style.current_page_item());
		InlineHyperlink currentLink = null;
		if (nameToken.equals(homeLink.getTargetHistoryToken())) 
			currentLink = homeLink;
		else if (nameToken.equals(analysisLink.getTargetHistoryToken()))
			currentLink = analysisLink;
		/*else if (nameToken.equals(phenotypeLink.getTargetHistoryToken()))
			currentLink = phenotypeLink;*/
		else if (nameToken.equals(helpLink.getTargetHistoryToken()))
			currentLink = helpLink;
		else if (nameToken.equals(accessionLink.getTargetHistoryToken())) 
			currentLink = accessionLink;
		if (currentLink != null)
			currentLink.addStyleName(currentPageItemStyleName);
	}

	@Override
	public void showLoadingIndicator(boolean visible) {
		loadingIndicator.setVisible(visible);
	}
	
	@Override
	public void setInSlot(Object slot,Widget content) {
		if (slot == MainPagePresenter.TYPE_SetMainContent) {
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
}
