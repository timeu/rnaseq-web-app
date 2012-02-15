package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.dto.Environment;
import com.gmi.rnaseqwebapp.client.resources.CellTableResources;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.MotionChart;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class EnvironmentDetailView extends ViewImpl implements
		EnvironmentDetailPresenter.MyView {

	

	public interface Binder extends UiBinder<Widget, EnvironmentDetailView> {
	}
	
	private final Widget widget;
	@UiField ResizeLayoutPanel motionChart_container;
	@UiField ResizeLayoutPanel phenotype_container;
	@UiField LayoutPanel cvt_container;
	
	private ColumnChart histogram_chart;
	
	
	private DataTable motionchartData;
	private DataTable histogramData;
	private MotionChart motionChart;
	private String environment;
	
	private HandlerRegistration resizeHandlerRegistration;
	
	

	@Inject
	public EnvironmentDetailView(final Binder binder, final CellTableResources cellTableResources) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setData(DataTable histogramData,DataTable motionchartData,String environment) {
		this.histogramData = histogramData;
		this.motionchartData = motionchartData;
		this.environment = environment;
	}
	
	
	private void drawHistogram() {
		if (histogramData == null)
			return;
		if (histogram_chart == null) {
			histogram_chart = new ColumnChart(histogramData, createColumnchartOptions());
			phenotype_container.add(histogram_chart);
		}
		else
			histogram_chart.draw(histogramData, createColumnchartOptions());
	}
	
	
	
	@Override
	public void drawMotionChart() {
		motionChart_container.clear();
		motionChart = null;
		if (motionChart == null) {
			motionChart = new MotionChart();
			motionChart_container.add(motionChart);
		}
		motionChart.draw(motionchartData,createMotionchartOptions());
	}
	
	private Options createColumnchartOptions()
	{
		Options options = Options.create();
		options.setTitle("Phenotype Histogram");
		options.setBackgroundColor("#FAFAFA");
		options.setHeight(400);
		options.setWidth(phenotype_container.getOffsetWidth());
		if (environment.equals(Environment.TYPES.T16C.toString()))
			options.setColors("#dc3912");
		return options;
	}
	
	private MotionChart.Options createMotionchartOptions() {
		MotionChart.Options options = MotionChart.Options.create();
		options.set("state", "%7B%22time%22%3A%22notime%22%2C%22iconType%22%3A%22BUBBLE%22%2C%22xZoomedDataMin%22%3Anull%2C%22yZoomedDataMax%22%3Anull%2C%22xZoomedIn%22%3Afalse%2C%22iconKeySettings%22%3A%5B%5D%2C%22showTrails%22%3Atrue%2C%22xAxisOption%22%3A%222%22%2C%22colorOption%22%3A%224%22%2C%22yAxisOption%22%3A%223%22%2C%22playDuration%22%3A15%2C%22xZoomedDataMax%22%3Anull%2C%22orderedByX%22%3Afalse%2C%22duration%22%3A%7B%22multiplier%22%3A1%2C%22timeUnit%22%3A%22none%22%7D%2C%22xLambda%22%3A1%2C%22orderedByY%22%3Afalse%2C%22sizeOption%22%3A%22_UNISIZE%22%2C%22yZoomedDataMin%22%3Anull%2C%22nonSelectedAlpha%22%3A0.4%2C%22stateVersion%22%3A3%2C%22dimensions%22%3A%7B%22iconDimensions%22%3A%5B%22dim0%22%5D%7D%2C%22yLambda%22%3A1%2C%22yZoomedIn%22%3Afalse%7D%3B");
		options.setHeight(600);
		options.setWidth(motionChart_container.getOffsetWidth());
		return options;
	}

	@Override
	public void drawCharts() {
		drawHistogram();
		drawMotionChart();
	}
	
	@Override
	public void detachCharts() {
		phenotype_container.clear();
		histogram_chart = null;
		motionChart_container.clear();
		motionChart = null;
		
		if (resizeHandlerRegistration != null) {
			resizeHandlerRegistration.removeHandler();
			resizeHandlerRegistration = null;
		}
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == EnvironmentDetailPresenter.TYPE_RadiusContent) {
			cvt_container.add(content);
			cvt_container.setWidgetLeftWidth(content, 0, Unit.PX, 50, Unit.PCT);
		}
		else if (slot == EnvironmentDetailPresenter.TYPE_TSSContent) {
			cvt_container.add(content);
			cvt_container.setWidgetRightWidth(content, 0, Unit.PX, 50, Unit.PCT);
		}
		else {
			super.setInSlot(slot, content);
		}
	}
	
	
}
