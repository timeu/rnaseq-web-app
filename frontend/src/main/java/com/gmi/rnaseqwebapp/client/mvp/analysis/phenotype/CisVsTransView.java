package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.dto.CisVsTransStat;
import com.gmi.rnaseqwebapp.client.resources.CellTableResources;
import com.gmi.rnaseqwebapp.client.ui.ColoredCell;
import com.gmi.rnaseqwebapp.client.ui.ResizeableColumnChart;
import com.gmi.rnaseqwebapp.client.ui.ResizeableLineChart;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class CisVsTransView extends ViewImpl implements CisVsTransPresenter.MyView {


	public interface Binder extends UiBinder<Widget, CisVsTransView> {
	}
	
	
	public enum CHART_TYPE  {COLUMN,LINE};
	private static  Integer chartHeight = 280;
	private final CellTableResources cellTableResources;
	private final Widget widget;
	private String title;
	private ResizeableLineChart line_chart;
	protected final SingleSelectionModel<CisVsTransStat> selectionModel = new SingleSelectionModel<CisVsTransStat>(CisVsTransStat.KEY_PROVIDER);
	private ResizeableColumnChart column_chart;
	@UiField ResizeLayoutPanel chart_container;
	@UiField(provided = true) CellTable<CisVsTransStat> table;
	
	@Inject
	public CisVsTransView(final Binder binder, final CellTableResources cellTableResources) {
		this.cellTableResources = cellTableResources;
		table = new CellTable<CisVsTransStat>(15,cellTableResources,CisVsTransStat.KEY_PROVIDER);
		table.addColumn(new TextColumn<CisVsTransStat>() {

			@Override
			public String getValue(CisVsTransStat object) {
				return object.getDistance().toString();
			}
		},"Distance");
		table.addColumn(new TextColumn<CisVsTransStat>() {

			@Override
			public String getValue(CisVsTransStat object) {
				if (object.getPercVarGlobal() != null)
					return object.getPercVarGlobal().toString();
				else return "";
			}
		},"PVG.");
		table.addColumn(new TextColumn<CisVsTransStat>(){

			@Override
			public String getValue(CisVsTransStat object) {
				if (object.getPercVarGlobal() != null)
					return object.getPercVarGlobal().toString();
				else
					return "";
			}
			
		}, "PVG_L");
		table.addColumn(new TextColumn<CisVsTransStat>(){

			@Override
			public String getValue(CisVsTransStat object) {
				if (object.getPseudoHeritabilityGlobal() != null)
					return object.getPseudoHeritabilityGlobal().toString();
				else
					return "";
			}
			
		}, "pHg");
		table.addColumn(new TextColumn<CisVsTransStat>(){

			@Override
			public String getValue(CisVsTransStat object) {
				if (object.getPseudoHeritabilityGlobalLocal() != null)
					return object.getPseudoHeritabilityGlobalLocal().toString();
				else
					return "";
			}
			
		}, "pHg_L");
		table.addColumn(new Column<CisVsTransStat,Number>(new ColoredCell(new Float(1.3))) {

			@Override
			public Number getValue(CisVsTransStat object) {
				return object.getScore();
			}
			
		}
		,"score");
		
		table.setSelectionModel(selectionModel);
		
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	private Options createLineChartOptions()
	{
		Options options = Options.create();
		options.setTitle(title);
		options.setBackgroundColor("#FAFAFA");
		options.setHeight(chartHeight);
		options.setWidth(chart_container.getOffsetWidth());
		options.set("focusTarget","category");
		options.setPointSize(6);
		return options;
	}
	
	private Options createColumnChartOptions()
	{
		Options options = Options.create();
		options.setTitle(title);
		options.setBackgroundColor("#FAFAFA");
		options.setHeight(chartHeight);
		options.setWidth(chart_container.getOffsetWidth());
		Options animation_options = Options.create();
		animation_options.set("duration", 1000.0);
		animation_options.set("easing", "out");
		options.set("animation", animation_options);
		return options;
	}
	
	
	private void drawColumnChart(AbstractDataTable data) {
		if (line_chart != null) {
			chart_container.clear();
			line_chart = null;
		}
		if (column_chart == null) {
			column_chart = new ResizeableColumnChart(data, createColumnChartOptions());
			chart_container.add(column_chart);
		}
		else
			column_chart.draw2(data, createColumnChartOptions());
		
	}
	
	private void drawLineChart(AbstractDataTable data) {
		if (column_chart != null) {
			chart_container.clear();
			column_chart = null;
		}
		if (line_chart == null) {
			line_chart = new ResizeableLineChart(data, createLineChartOptions());
			chart_container.add(line_chart);
		}
		else
			line_chart.draw2(data, createLineChartOptions());
		
	}


	@Override
	public void detachCharts() {
		chart_container.clear();
		line_chart = null;
		column_chart = null;
	}
	
	@Override
	public HasData<CisVsTransStat> getDisplay() {
		return table;
	}

	@Override
	public void drawChart(AbstractDataTable data, CHART_TYPE type) {
		switch (type) {
			case COLUMN:
				drawColumnChart(data);
				break;
			case LINE:
				drawLineChart(data);
		}
		
	}
	
	@Override
	public void setTitle(String title) {
		this.title = title;
		
	}
}
