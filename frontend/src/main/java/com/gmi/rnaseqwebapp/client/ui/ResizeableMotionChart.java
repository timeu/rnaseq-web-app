package com.gmi.rnaseqwebapp.client.ui;

import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.visualizations.MotionChart;

public class ResizeableMotionChart extends MotionChart implements RequiresResize {

	protected Options options;
	protected AbstractDataTable data;
	
	
	public ResizeableMotionChart() {
		super();
	}
	

	@Override
	public void onResize() {
		options.setWidth(getParent().getOffsetWidth());
		options.setHeight(getParent().getOffsetHeight());
		draw(data,options);
	}
	

}
