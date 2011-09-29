package com.gmi.rnaseqwebapp.client.gin;

import com.gmi.rnaseqwebapp.client.command.GetPhenotypersActionHandler;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class RNASeqClientActionHandlerRegistry extends
		DefaultClientActionHandlerRegistry {
	
	@Inject
	public RNASeqClientActionHandlerRegistry(GetPhenotypersActionHandler getPhenotypesActionHandler) {
		register(getPhenotypesActionHandler);
	}
	

}
