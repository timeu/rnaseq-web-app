package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class GetPhenotypeDataActionHandler extends AbstractRequestBuilderCacheClientActionHandler<GetPhenotypeDataAction, GetPhenotypeDataActionResult> {

	@Inject
	protected GetPhenotypeDataActionHandler(Cache cache, EventBus eventBus) {
		super(GetPhenotypeDataAction.class, cache, eventBus, true, true, false);
	}
}
