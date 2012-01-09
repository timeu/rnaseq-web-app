package com.gmi.rnaseqwebapp.client.command;



import com.gmi.rnaseqwebapp.client.dto.Phenotypes;
import com.gwtplatform.dispatch.shared.Result;

public class GetPhenotypesActionResult  implements Result {

	private final Phenotypes phenotypes;
	
	public GetPhenotypesActionResult(Phenotypes phenotypes) {
		this.phenotypes = phenotypes;
	}
	
	public Phenotypes getPhenotypes() {
		return phenotypes;
	}
}
