package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.visualization.client.DataTable;

public class PhenotypeOverviewPresenter extends
		PresenterWidget<PhenotypeOverviewPresenter.MyView> {

	public interface MyView extends View {

		void setHistogramData(DataTable histogram);
		// TODO Put your view methods here
		void forceLayout();
		void initGeneViewer(Runnable runnable);
		void setGeneViewerRegion(int chr, int start, int end);
		void setChrSizes(List<Integer> chrSizes);
		void detachCharts();
		
	}
	
	
	private Phenotype phenotype;
	private DataTable histogram;
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
		getView().setHistogramData(histogram);
		getView().forceLayout();
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
		this.phenotype = phenotype;
		this.histogram = histogram;
	}
}
