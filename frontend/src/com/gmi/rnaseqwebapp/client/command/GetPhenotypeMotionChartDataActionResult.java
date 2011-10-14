package com.gmi.rnaseqwebapp.client.command;

import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.dispatch.shared.Result;

public class GetPhenotypeMotionChartDataActionResult implements Result{

	private final DataTable motionChartDataTable;
	
	public GetPhenotypeMotionChartDataActionResult(DataTable motionChartDataTable) {
		this.motionChartDataTable = motionChartDataTable;
	}
	
	public DataTable getMotionChartDataTable() {
		return this.motionChartDataTable;
	}
	
}
