package com.gmi.rnaseqwebapp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoadingIndicator extends Composite {


	public interface LoadingIndicatorUiBinder extends
			UiBinder<Widget, LoadingIndicator> {
	}
	
	private static LoadingIndicatorUiBinder uiBinder = GWT
			.create(LoadingIndicatorUiBinder.class);
	

	
	public LoadingIndicator() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
