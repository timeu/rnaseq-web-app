package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dto.SNPResults;
import com.gwtplatform.dispatch.shared.Result;

public class GetSNPResultsActionResult implements Result {

	private final SNPResults snpresults;
	
	public GetSNPResultsActionResult(SNPResults snpresults) {
		this.snpresults = snpresults;
	}
	
	public SNPResults getSNPResults() {
		return snpresults;
	}
}
