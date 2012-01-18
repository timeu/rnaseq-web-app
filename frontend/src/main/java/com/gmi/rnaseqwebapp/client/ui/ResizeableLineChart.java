package com.gmi.rnaseqwebapp.client.ui;

import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

public class ResizeableLineChart extends LineChart implements RequiresResize{

	protected Options options;
	protected AbstractDataTable dataTable;
	
	public ResizeableLineChart(AbstractDataTable dataTable, Options options) {
		super(dataTable, options);
		this.dataTable = dataTable;
		this.options = options;
	}

	@Override
	public void onResize() {
		options.setWidth(getParent().getOffsetWidth());
		options.setHeight(getParent().getOffsetHeight());
		draw(dataTable, options);
	}
	
	public void draw2(AbstractDataTable dataTable,Options options) {
		this.options = options;
		this.dataTable = dataTable;
		draw(dataTable,options);
	}

}
