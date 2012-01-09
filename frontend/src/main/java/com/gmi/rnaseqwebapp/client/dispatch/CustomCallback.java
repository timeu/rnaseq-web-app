package com.gmi.rnaseqwebapp.client.dispatch;

import com.gmi.rnaseqwebapp.client.events.DisplayNotificationEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public abstract class CustomCallback<T> implements AsyncCallback<T>{
	
	private final EventBus eventBus;


	@Inject
	public CustomCallback(EventBus eventBus) {
		this.eventBus = eventBus;
	}


	@Override
	public void onFailure(Throwable caught) {
		DisplayNotificationEvent.fireError(eventBus, "Backend-Error", caught.getMessage());
	}
	
}
