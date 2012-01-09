package com.gmi.rnaseqwebapp.client.mvp.analysis;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPagePresenter;
import com.gmi.rnaseqwebapp.client.ui.SlidingPanel;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AnalysisView extends ViewImpl implements AnalysisPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, AnalysisView> {
	}
	
	@UiField SlidingPanel contentContainer;

	@Inject
	public AnalysisView(final Binder binder) {
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
}
