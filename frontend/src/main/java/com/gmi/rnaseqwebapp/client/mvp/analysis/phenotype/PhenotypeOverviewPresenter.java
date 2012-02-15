package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.List;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PhenotypeOverviewPresenter extends
		PresenterWidget<PhenotypeOverviewPresenter.MyView> {

	public interface MyView extends View {

		void setData(AbstractDataTable histogram,AbstractDataTable scoreDataTable,AbstractDataTable pseudoHeritDataTable);
		// TODO Put your view methods here
		void scheduledLayout();
		void initGeneViewer(Runnable runnable);
		void setGeneViewerRegion(int chr, int start, int end);
		void setChrSizes(List<Integer> chrSizes);
		void detachCharts();
	}
	
	
	private Phenotype phenotype;
	private DataTable histogram;
	private DataTable scoreDataTable;
	private DataTable percVarDataTable;
	private final DispatchAsync dispatch;
	protected final ClientData clientData;
	
	@Inject
	public PhenotypeOverviewPresenter(final EventBus eventBus, final MyView view,
			final DispatchAsync dispatch,final ClientData clientData) {
		super(eventBus, view);
		this.dispatch = dispatch;
		this.clientData = clientData;
		getView().setChrSizes(clientData.getChrSizes());
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().initGeneViewer(new Runnable() {
			
			@Override
			public void run() {
				//registerHandler(getView().getGeneViewerZoomHandlers().addZoomResizeHandler(new GeneZoomResizeHandler()));
				//registerHandler(getView().getGeneViewerClickGeneHandlers().addClickGeneHandler(new GeneClickHandler()));
			}
		});
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		getView().setGeneViewerRegion(phenotype.getChr(),phenotype.getStart()-50,phenotype.getEnd()+50);
		getView().setData(histogram,scoreDataTable,percVarDataTable);
		getView().scheduledLayout();
	}
	
	private DataTable createScoreDataTable() {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING,"Analysis");
		data.addColumn(ColumnType.NUMBER,"Score");
		data.addRows(3);
		data.setValue(0,0, "10°C" );
		data.setValue(0,1, phenotype.getMaxScore10C());
		data.setValue(1,0, "16°C" );
		data.setValue(1,1, phenotype.getMaxScore16C());
		data.setValue(2,0, "Full" );
		data.setValue(2,1, phenotype.getMaxScoreFull());
		return data;
	}
	
	private DataTable createPercVarDataTable() {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING,"Analysis");
		data.addColumn(ColumnType.NUMBER,"Score");
		data.addRows(3);
		data.setValue(0,0, "10°C" );
		data.setValue(0,1, phenotype.getPseudoHeritability10C());
		data.setValue(1,0, "16°C" );
		data.setValue(1,1, phenotype.getPseudoHeritability16C());
		return data;
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		/*dispatch.execute(new GetPhenotypeOverviewDataAction(phenotype), new CustomCallback<GetPhenotypeOverviewDataActionResult>(getEventBus()) {
			
		});*/
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		getView().detachCharts();
	}

	public void setData(Phenotype phenotype, DataTable histogram) {
		boolean isNew = false;
		if (phenotype != this.phenotype) 
			isNew = true;
		this.phenotype = phenotype;
		this.histogram = histogram;
		if (isNew) {
			this.scoreDataTable = createScoreDataTable();
			this.percVarDataTable = createPercVarDataTable();
		}
	}
}
