package com.gmi.rnaseqwebapp.client.mvp.help;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class HelpView extends ViewImpl implements HelpPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HelpView> {
	}

	@Inject
	public HelpView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
