package com.gmi.rnaseqwebapp.client.gin;

import com.gmi.rnaseqwebapp.client.command.GetGWASDataActionHandler;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypeDataActionHandler;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypeMotionChartDataActionHandler;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypesActionHandler;
import com.gmi.rnaseqwebapp.client.command.GetSNPResultsActionHandler;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class RNASeqClientActionHandlerRegistry extends
		DefaultClientActionHandlerRegistry {
	
	@Inject
	public RNASeqClientActionHandlerRegistry(GetPhenotypesActionHandler getPhenotypesActionHandler,
											GetPhenotypeDataActionHandler getPhenotypeActionHandler,
											GetPhenotypeMotionChartDataActionHandler getPhenotypeMotionChartActionHandler,
											GetGWASDataActionHandler getGWASDataActionHandler, 
											GetSNPResultsActionHandler getSNPResultsActionHandler) {
		register(getPhenotypesActionHandler);
		register(getPhenotypeActionHandler);
		register(getPhenotypeMotionChartActionHandler);
		register(getGWASDataActionHandler);
		register(getSNPResultsActionHandler);
	}
	

}
