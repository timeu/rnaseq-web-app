package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.GeneViewer;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gmi.rnaseqwebapp.client.dto.CisVsTransStat;
import com.gmi.rnaseqwebapp.client.dto.Cofactor;
import com.gmi.rnaseqwebapp.client.resources.CellTableResources;
import com.gmi.rnaseqwebapp.client.ui.ResizeableBarChart;
import com.gmi.rnaseqwebapp.client.ui.ResizeableColumnChart;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.inject.Inject;

public class PhenotypeOverviewView extends ViewImpl implements
		PhenotypeOverviewPresenter.MyView {

	
	public interface Binder extends UiBinder<Widget, PhenotypeOverviewView> {
	}
	
	private final Widget widget;
	private ResizeableColumnChart histogram_chart;
	private ResizeableBarChart score_chart;
	private ResizeableBarChart pseudoherit_chart;
	private AbstractDataTable histogram;
	private AbstractDataTable scoreDataTable;
	private AbstractDataTable pseudoHeritDataTable;
	private List<Integer> chrSizes;
	
	@UiField SimpleLayoutPanel phenotype_container;
	@UiField GeneViewer geneViewer;
	@UiField LayoutPanel container;
	@UiField SimpleLayoutPanel score_chart_container;
	@UiField SimpleLayoutPanel pseudoherit_chart_container;
	
	private final ScheduledCommand layoutCmd = new ScheduledCommand() {
    	public void execute() {
    		layoutScheduled = false;
		    forceLayout();
		}
    };
	private boolean layoutScheduled = false;
	

	@Inject
	public PhenotypeOverviewView(final Binder binder,final DataSource jBrowseDataSource) {
		
		widget = binder.createAndBindUi(this);
		geneViewer.setHeight("250px");
		geneViewer.setDataSource(jBrowseDataSource);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setData(AbstractDataTable histogram,AbstractDataTable scoreDataTable,AbstractDataTable pseudoHeritDataTable) {
		this.histogram = histogram;
		this.pseudoHeritDataTable = pseudoHeritDataTable;
		this.scoreDataTable = scoreDataTable;
	}
	
	private void drawHistogram() {
		if (histogram_chart == null) {
			histogram_chart = new ResizeableColumnChart(histogram, createColumnchartOptions());
			phenotype_container.add(histogram_chart);
		}
		else
			histogram_chart.draw2(histogram, createColumnchartOptions());
	}
	
	private void drawScoreChart() {
		if (score_chart == null) {
			score_chart = new ResizeableBarChart(scoreDataTable, createBarchartOptions("Max-Score"));
			score_chart_container.add(score_chart);
		}
		else {
			score_chart.draw2(scoreDataTable,createBarchartOptions("Max-Score"));
		}
	}	
	
	private void drawPseudoHeritChart() {
		if (pseudoherit_chart == null) {
			pseudoherit_chart = new ResizeableBarChart(pseudoHeritDataTable, createBarchartOptions("Pseudo-Heritability"));
			pseudoherit_chart_container.add(pseudoherit_chart);
		}
		else {
			pseudoherit_chart.draw2(pseudoHeritDataTable,createBarchartOptions("Pseudo-Heritability"));
		}
	}		
	
	private void drawCharts()  {
		drawHistogram();
		drawPseudoHeritChart();
		drawScoreChart();
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
	
	private Options createBarchartOptions(String title) {
		Options options = Options.create();
		options.setTitle(title);
		options.setBackgroundColor("#FAFAFA");
		options.setWidth(score_chart_container.getOffsetWidth());
		return options;
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
		pseudoherit_chart_container.clear();
		pseudoherit_chart = null;
		score_chart_container.clear();
		score_chart= null;
	}
	
	
	private void forceLayout() {
		if (!widget.isAttached() || !widget.isVisible())
			return;
		drawCharts();
	}
	
	@Override
	public void scheduledLayout() {
	    if (widget.isAttached() && !layoutScheduled) {
	      layoutScheduled = true;
	      Scheduler.get().scheduleDeferred(layoutCmd);
	    }
	}
	
}
