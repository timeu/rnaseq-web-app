package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.List;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.command.GetGWASDataAction;
import com.gmi.rnaseqwebapp.client.command.GetGWASDataActionResult;
import com.gmi.rnaseqwebapp.client.dispatch.CustomCallback;
import com.gmi.rnaseqwebapp.client.dto.Cofactor;
import com.gmi.rnaseqwebapp.client.dto.GWASResult;
import com.gmi.rnaseqwebapp.client.dto.ResultData;
import com.gmi.rnaseqwebapp.client.events.DisplayNotificationEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ResultPresenter extends PresenterWidget<ResultPresenter.MyView> implements ResultUiHandlers{

	public interface MyView extends View,HasUiHandlers<ResultUiHandlers> {
		void setDownloadURL(String url);
		void drawAssociationCharts(List<DataTable> dataTables,List<Cofactor> cofactors,
				List<Integer> chrLengths, double maxScore,
				double bonferroniThreshold);
		void drawStatisticPlots(DataView view);
		HasData<Cofactor> getCofactorDisplay();
		void reset();
		void detachCharts();
	}
	private boolean refresh = false;
	private GWASResult gwasResult;
	private List<Cofactor> cofactors;
	protected List<DataTable> dataTables = null;
	private final DispatchAsync dispatch;
	private final ClientData clientData;
	protected ListDataProvider<Cofactor> cofactorDataProvider = new ListDataProvider<Cofactor>();
	protected DataTable statistics_data = null;

	@Inject
	public ResultPresenter(final EventBus eventBus, final MyView view,
			final DispatchAsync dispatch, final ClientData clientData) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.dispatch = dispatch;
		this.clientData = clientData;
		cofactorDataProvider.addDataDisplay(getView().getCofactorDisplay());
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		this.initStatistics();
		if (gwasResult == null) {
			DisplayNotificationEvent.fireError(getEventBus(), "Error", "No GWAS result found");
		}
		else if (refresh)
		{
			String download_url = "/gwas/downloadAssociationData?phenotype="+gwasResult.getPhenotype()+"&environment="+gwasResult.getEnvironment()+"&dataset="+gwasResult.getDataset()+"&transformation="+gwasResult.getTransformation()+"&result"+gwasResult.getName();
			getView().setDownloadURL(download_url);
			dispatch.execute(new GetGWASDataAction(gwasResult), new CustomCallback<GetGWASDataActionResult>(getEventBus()) {
	
				@Override
				public void onSuccess(GetGWASDataActionResult result) {
					ResultData info = result.getResultData();
					dataTables = info.getAssociationTables();
					getView().drawAssociationCharts(dataTables,cofactors,clientData.getChrSizes(),info.getMaxScore(),info.getBonferroniThreshold());
					cofactorDataProvider.setList(cofactors);
				}
				
			});
		}
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		getView().detachCharts();
	}
	
	
	@Override
	protected void onReveal() {
		super.onReveal();
	}
	
	@Override
	public void loadStatisticChart(String value) {
		DataView view = DataView.create(statistics_data);
		view.setColumns(new int[]{0, Integer.parseInt(value)});
		getView().drawStatisticPlots(view);
	}

	public void setData(GWASResult result,List<Cofactor> cofactors) {
		if (gwasResult != result) {
			refresh = true;
			getView().reset();
		}
		else
			refresh = false;
		
		this.gwasResult = result;
		this.cofactors = cofactors;
	}
	
	private void initStatistics() {
		if (cofactors != null) {
			statistics_data = DataTable.create();
			statistics_data.addColumn(ColumnType.STRING, "Step");
			statistics_data.addColumn(ColumnType.NUMBER, "BIC");
			statistics_data.addColumn(ColumnType.NUMBER, "mBIC");
			statistics_data.addColumn(ColumnType.NUMBER, "eBIC");
			statistics_data.addColumn(ColumnType.NUMBER, "mBonf");
			statistics_data.addColumn(ColumnType.NUMBER, "pseudo-heritability");
			for (Cofactor cofactor:cofactors) 
			{
				 int index = statistics_data.addRow();
				 statistics_data.setValue(index, 0, cofactor.getStep().toString());
				 statistics_data.setValue(index, 1, cofactor.getBic());
				 statistics_data.setValue(index, 2, cofactor.getMbic());
				 statistics_data.setValue(index, 3, cofactor.getEbic());
				 statistics_data.setValue(index, 4, cofactor.getMbonf());
				 statistics_data.setValue(index, 5, cofactor.getPseudoHeritability());
			}
		}
	}
}
