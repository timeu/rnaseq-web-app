package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.List;

import com.gmi.rnaseqwebapp.client.dto.CisVsTransStat;
import com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype.CisVsTransView.CHART_TYPE;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;


public class CisVsTransPresenter extends PresenterWidget<CisVsTransPresenter.MyView> {
	
	
	public interface MyView extends com.gwtplatform.mvp.client.View {

		void drawChart(AbstractDataTable data,CHART_TYPE type);
		void detachCharts();
		void setTitle(String title);
		HasData<CisVsTransStat> getDisplay();
	}
	
	class TableSelectionChangeHandler  implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			
			SingleSelectionModel<CisVsTransStat> selectionModel = (SingleSelectionModel<CisVsTransStat>)getView().getDisplay().getSelectionModel();
			CisVsTransStat selectedObj = selectionModel.getSelectedObject();
			if (selectedObj == null) {
				getView().drawChart(dataTable,CHART_TYPE.LINE);
			}
			else {
				getView().drawChart(createDataTable(cisVsTransList, selectedObj), CHART_TYPE.COLUMN);
			}
		}
	}
	
	private ListDataProvider<CisVsTransStat> dataProvider = new ListDataProvider<CisVsTransStat>();
	protected DataTable dataTable;
	private List<CisVsTransStat> cisVsTransList;
	
	@Inject
	public CisVsTransPresenter(EventBus eventBus, MyView view) {
		super(eventBus, view);
		dataProvider.addDataDisplay(getView().getDisplay());
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().getDisplay().getSelectionModel().addSelectionChangeHandler(new TableSelectionChangeHandler()));
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		dataProvider.setList(cisVsTransList);
		getView().drawChart(dataTable,CHART_TYPE.LINE);
		
	}

	public void setData(List<CisVsTransStat> list,String title) {
		getView().setTitle(title);
		this.cisVsTransList = list;
		dataTable = createDataTable(list,null);
	}
	
	public static DataTable createDataTable(List<CisVsTransStat> list,CisVsTransStat selected) {
		DataTable data = DataTable.create();
		if (selected == null) {
			data.addColumn(ColumnType.STRING,"Distance");
			data.addColumn(ColumnType.NUMBER,"Perc. Variance Expl. Global");
			data.addColumn(ColumnType.NUMBER,"Perc. Variance Expl. Local");
			data.addColumn(ColumnType.NUMBER,"Pseudo Heritability Global");
			data.addColumn(ColumnType.NUMBER,"Pseudo Heritability Global-Local");
			data.addRows(list.size());
			for (int i = 0;i< list.size();i++)  {
				CisVsTransStat dataset = list.get(i);
				data.setValue(i, 0, dataset.getDistance().toString());
				data.setValue(i, 1, dataset.getPercVarGlobal());
				data.setValue(i, 2, dataset.getPercVarLocal());
				data.setValue(i, 3, dataset.getPseudoHeritabilityGlobal());
				data.setValue(i, 4, dataset.getPseudoHeritabilityGlobalLocal());
			}
		}
		else
		{
			data.addColumn(ColumnType.STRING,"Type");
			data.addColumn(ColumnType.NUMBER,"Global");
			data.addColumn(ColumnType.NUMBER,"Local");
			data.addRows(2);
			data.setValue(0, 0, "Perc. Variance Expl.");
			data.setValue(0, 1, selected.getPercVarGlobal());
			data.setValue(0, 2, selected.getPercVarLocal());
			
			data.setValue(1, 0, "Pseudo-Heritability");
			data.setValue(1, 1, selected.getPseudoHeritabilityGlobal());
			data.setValue(1, 2, selected.getPseudoHeritabilityGlobalLocal());
			
		}
		return data;
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		getView().detachCharts();
	}
	
	
	

}
