package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gmi.rnaseqwebapp.client.command.GetPhenotypeMotionChartDataAction;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypeMotionChartDataActionResult;
import com.gmi.rnaseqwebapp.client.dispatch.CustomCallback;
import com.gmi.rnaseqwebapp.client.dto.Environment;
import com.google.gwt.event.shared.EventBus;
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
	
	private final DispatchAsync dispatch;
	private Environment environment;
	private DataTable histogramDataTable;

	@Inject
	public EnvironmentDetailPresenter(final EventBus eventBus, final MyView view,
									final DispatchAsync dispatch) {
		super(eventBus, view);
		this.dispatch = dispatch;
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
	}
	
	@Override
	protected void onReset() {
		super.onReset();
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
