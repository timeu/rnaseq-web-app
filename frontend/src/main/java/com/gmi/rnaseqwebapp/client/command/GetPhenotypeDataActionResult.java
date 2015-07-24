package com.gmi.rnaseqwebapp.client.command;

import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.dispatch.shared.Result;

public class GetPhenotypeDataActionResult implements Result{
	
	private final Phenotype phenotype;
	private final DataTable bsHistogramDataTable;
	private final DataTable mRNAHistogramDataTable;
	
	public GetPhenotypeDataActionResult(final Phenotype phenotype,final DataTable bsHistogramDataTable,final DataTable mRNAHistogramDataTable) {
		this.phenotype = phenotype;
		this.bsHistogramDataTable = bsHistogramDataTable;
		this.mRNAHistogramDataTable = mRNAHistogramDataTable;
	}
	
	public Phenotype getPhenotype() {
		return phenotype;
	}
	
	public DataTable getBsHistogramDataTable() {
		return bsHistogramDataTable;
	}
	public DataTable getmRNAHistogramDataTable() {
		return mRNAHistogramDataTable;
	}
}
