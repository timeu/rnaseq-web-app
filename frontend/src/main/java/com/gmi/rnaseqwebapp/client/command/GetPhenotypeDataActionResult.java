package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.dispatch.shared.Result;

public class GetPhenotypeDataActionResult implements Result{
	
	private final Phenotype phenotype;
	private final DataTable histogramdataTable;
	
	public GetPhenotypeDataActionResult(final Phenotype phenotype,final DataTable histogramdataTable) {
		this.phenotype = phenotype;
		this.histogramdataTable = histogramdataTable;
	}
	
	public Phenotype getPhenotype() {
		return phenotype;
	}
	
	public DataTable gethistogramdataTable() {
		return histogramdataTable;
	}
}
