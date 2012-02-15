package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class GetPhenotypesActionHandler
		extends
		AbstractRequestBuilderCacheClientActionHandler<GetPhenotypesAction, GetPhenotypesActionResult> {

	@Inject
	protected GetPhenotypesActionHandler(Cache cache, EventBus eventBus) {
		super(GetPhenotypesAction.class, cache, eventBus, true, false, false);
	}
}
