package com.gmi.rnaseqwebapp.client.gin;

import com.gmi.rnaseqwebapp.client.command.GetGWASDataActionHandler;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypeDataActionHandler;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypeMotionChartDataActionHandler;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypersActionHandler;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class RNASeqClientActionHandlerRegistry extends
		DefaultClientActionHandlerRegistry {
	
	@Inject
	public RNASeqClientActionHandlerRegistry(GetPhenotypersActionHandler getPhenotypesActionHandler,
											GetPhenotypeDataActionHandler getPhenotypeActionHandler,
											GetPhenotypeMotionChartDataActionHandler getPhenotypeMotionChartActionHandler,
											GetGWASDataActionHandler getGWASDataActionHandler) {
		register(getPhenotypesActionHandler);
		register(getPhenotypeActionHandler);
		register(getPhenotypeMotionChartActionHandler);
		register(getGWASDataActionHandler);
	}
	

}
