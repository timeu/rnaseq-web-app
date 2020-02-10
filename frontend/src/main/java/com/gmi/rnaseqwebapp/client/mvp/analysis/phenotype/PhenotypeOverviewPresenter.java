package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.List;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.dto.Dataset;
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
	private DataTable mRNAHistogram;
	private DataTable scoreDataTable;
	private DataTable percVarDataTable;
	private DataTable bsScoreDataTable;
	private DataTable bsPercVarDataTable;
	private final DispatchAsync dispatch;
	protected final ClientData clientData;
	private DataTable bsHistogram;
	
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
		getView().setData(mRNAHistogram,scoreDataTable,percVarDataTable);
		getView().scheduledLayout();
	}
	
	private DataTable createScoreDataTable(Dataset dataset) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING,"Analysis");
		data.addColumn(ColumnType.NUMBER,"Score");
		data.addRows(3);
		data.setValue(0,0, "10°C" );
		data.setValue(0,1, dataset.getMaxScore10C());
		data.setValue(1,0, "16°C" );
		data.setValue(1,1, dataset.getMaxScore16C());
		data.setValue(2,0, "Full" );
		data.setValue(2,1, dataset.getMaxScoreFull());
		return data;
	}
	
	private DataTable createPercVarDataTable(Dataset dataset) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING,"Analysis");
		data.addColumn(ColumnType.NUMBER,"Score");
		data.addRows(3);
		data.setValue(0,0, "10°C" );
		data.setValue(0,1, dataset.getPseudoHeritability10C());
		data.setValue(1,0, "16°C" );
		data.setValue(1,1, dataset.getPseudoHeritability16C());
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

	public void setData(Phenotype phenotype, DataTable mRNAHistogram, DataTable bsHistogram) {
		boolean isNew = false;
		if (phenotype != this.phenotype) 
			isNew = true;
		this.phenotype = phenotype;
		this.mRNAHistogram = mRNAHistogram;
		this.bsHistogram  = bsHistogram;
		if (isNew) {
			this.scoreDataTable = createScoreDataTable(phenotype.getMRNADataset());
			this.percVarDataTable = createPercVarDataTable(phenotype.getMRNADataset());
			this.bsPercVarDataTable = createPercVarDataTable(phenotype.getBsDataset());
			this.bsScoreDataTable = createScoreDataTable(phenotype.getBsDataset());
		}
	}
}
