package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class GetSNPResultsActionHandler extends AbstractRequestBuilderCacheClientActionHandler<GetSNPResultsAction, GetSNPResultsActionResult> {

	@Inject
	protected GetSNPResultsActionHandler(Cache cache, EventBus eventBus) {
		super(GetSNPResultsAction.class, cache, eventBus, true, false, false);
	}
}
