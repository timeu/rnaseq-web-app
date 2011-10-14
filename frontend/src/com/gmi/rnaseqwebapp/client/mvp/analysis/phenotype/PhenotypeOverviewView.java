package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.GeneViewer;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gmi.rnaseqwebapp.client.ui.ResizeableColumnChart;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.inject.Inject;

public class PhenotypeOverviewView extends ViewImpl implements
		PhenotypeOverviewPresenter.MyView {

	
	public interface Binder extends UiBinder<Widget, PhenotypeOverviewView> {
	}
	
	private final Widget widget;
	private ResizeableColumnChart histogram_chart;
	private DataTable histogram;
	private List<Integer> chrSizes;
	
	@UiField ResizeLayoutPanel phenotype_container;
	@UiField GeneViewer geneViewer;
	@UiField LayoutPanel container;
	

	@Inject
	public PhenotypeOverviewView(final Binder binder,final DataSource jBrowseDataSource) {
		widget = binder.createAndBindUi(this);
		geneViewer.setSize(0, 250);
		geneViewer.setDataSource(jBrowseDataSource);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setHistogramData(DataTable histogram) {
		this.histogram = histogram;
	}
	
	private void drawHistogram() {
		if (histogram_chart == null) {
			histogram_chart = new ResizeableColumnChart(histogram, createColumnchartOptions());
			phenotype_container.add(histogram_chart);
		}
		else
			histogram_chart.draw(histogram, createColumnchartOptions());
	}
	
	
	private Options createColumnchartOptions()
	{
		Options options = Options.create();
		options.setTitle("Phenotype Histogram");
		options.setHeight(400);
		options.setBackgroundColor("#FAFAFA");
		/*ChartArea area = ChartArea.create();
		area.setLeft("10%");
		area.setTop("20%");
		area.setWidth("75%");
		options.setChartArea(area);*/
		return options;
	}

	@Override
	public void forceLayout() {
		drawHistogram();
		//container.forceLayout();
	}

	@Override
	public void initGeneViewer(Runnable runnable) {
		try {
			geneViewer.load(runnable);
		}
		catch (Exception ex) {
			
		}
	}
	
	@Override
	public void setGeneViewerRegion(int chr, int start, int end) {
		geneViewer.setChromosome("Chr"+chr);
		geneViewer.setViewRegion(0, chrSizes.get(chr-1));
		geneViewer.updateZoom(start, end);
	}

	@Override
	public void setChrSizes(List<Integer> chrSizes) {
		this.chrSizes = chrSizes;
	}
	
	@Override
	public void detachCharts()  {
		phenotype_container.clear();
		histogram_chart = null;
	}
	
	
}
