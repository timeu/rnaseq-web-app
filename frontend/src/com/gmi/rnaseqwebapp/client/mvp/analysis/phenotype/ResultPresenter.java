package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import java.util.List;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.command.GetGWASDataAction;
import com.gmi.rnaseqwebapp.client.command.GetGWASDataActionResult;
import com.gmi.rnaseqwebapp.client.dispatch.CustomCallback;
import com.gmi.rnaseqwebapp.client.dto.Cofactor;
import com.gmi.rnaseqwebapp.client.dto.GWASResult;
import com.gmi.rnaseqwebapp.client.dto.ResultData;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.visualization.client.DataTable;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ResultPresenter extends PresenterWidget<ResultPresenter.MyView> {

	public interface MyView extends View {
		void setDownloadURL(String url);
		void drawAssociationCharts(List<DataTable> dataTables,List<Cofactor> cofactors,
				List<Integer> chrLengths, double maxScore,
				double bonferroniThreshold);
	}
	
	private GWASResult gwasResult;
	protected List<DataTable> dataTables = null;
	private final DispatchAsync dispatch;
	private final ClientData clientData;
	

	@Inject
	public ResultPresenter(final EventBus eventBus, final MyView view,
			final DispatchAsync dispatch, final ClientData clientData) {
		super(eventBus, view);
		this.dispatch = dispatch;
		this.clientData = clientData;
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		String download_url = "/gwas/downloadAssociationData?phenotype="+gwasResult.getPhenotype()+"&environment="+gwasResult.getEnvironment()+"&dataset="+gwasResult.getDataset()+"&transformation="+gwasResult.getTransformation()+"&result"+gwasResult.getName();
		getView().setDownloadURL(download_url);
		dispatch.execute(new GetGWASDataAction(gwasResult), new CustomCallback<GetGWASDataActionResult>(getEventBus()) {

			@Override
			public void onSuccess(GetGWASDataActionResult result) {
				ResultData info = result.getResultData();
				dataTables = info.getAssociationTables();
				getView().drawAssociationCharts(dataTables,gwasResult.getCofactors(),clientData.getChrSizes(),info.getMaxScore(),info.getBonferroniThreshold());
			}
			
		});
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
	}

	public void setData(GWASResult result) {
		this.gwasResult = result;
	}
}
