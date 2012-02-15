package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.command.GetPhenotypeMotionChartDataAction;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypeMotionChartDataActionResult;
import com.gmi.rnaseqwebapp.client.dispatch.CustomCallback;
import com.gmi.rnaseqwebapp.client.dto.Environment;
import com.gmi.rnaseqwebapp.client.dto.Transformation;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.visualization.client.DataTable;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class EnvironmentDetailPresenter extends
		PresenterWidget<EnvironmentDetailPresenter.MyView> {

	public interface MyView extends View {

		void setData(DataTable histogramData, DataTable motionchartData, String environment);
		void drawCharts();
		void drawMotionChart();
		void detachCharts();
	}
	public enum SOURCE {TSS,RADIUS};
	
	public static final Object TYPE_RadiusContent = new Object();
	public static final Object TYPE_TSSContent = new Object();
	
	private final DispatchAsync dispatch;
	private Environment environment;
	private DataTable histogramDataTable;
	private final CisVsTransPresenter radiusCisVsTransPresenter;
	private final CisVsTransPresenter tssCisVsTransPresenter;
	
	

	@Inject
	public EnvironmentDetailPresenter(final EventBus eventBus, final MyView view,
									final DispatchAsync dispatch, 
									final CisVsTransPresenter radiusCisVsTransPresenter,final CisVsTransPresenter tssCisVsTransPresenter ) {
		super(eventBus, view);
		this.dispatch = dispatch;
		this.radiusCisVsTransPresenter = radiusCisVsTransPresenter;
		this.tssCisVsTransPresenter = tssCisVsTransPresenter;
	}
	

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	public void setData(DataTable histogramDataTable) {
		this.histogramDataTable = histogramDataTable;
	}
	
	public void setData(Environment environment,DataTable histogramDataTable) {
		this.environment = environment;
		this.histogramDataTable = histogramDataTable;
		Transformation transformation = environment.getDatasets().get(0).getTransformations().get(0);
		radiusCisVsTransPresenter.setData(transformation.getRadius(),"Radius");
		tssCisVsTransPresenter.setData(transformation.getTssUpstream(),"TSS-Upstream");
	}
	
	
	
	
	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(TYPE_RadiusContent, radiusCisVsTransPresenter);
		setInSlot(TYPE_TSSContent, tssCisVsTransPresenter);
		dispatch.execute(new GetPhenotypeMotionChartDataAction(environment.getPhenotype(),environment.getName(),"Fullset","raw"), new CustomCallback<GetPhenotypeMotionChartDataActionResult>(getEventBus()) {
			
			@Override
			public void onSuccess(GetPhenotypeMotionChartDataActionResult result) {
				getView().setData(histogramDataTable,result.getMotionChartDataTable(),environment.getName());
				getView().drawCharts();
			}
		});		
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		getView().detachCharts();
	}
	
	
}
