package com.gmi.rnaseqwebapp.client.mvp.analysis.phenotype;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypeDataAction;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypeDataActionResult;
import com.gmi.rnaseqwebapp.client.dispatch.CustomCallback;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypeReader;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisPresenter;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListPresenter;
import com.gmi.rnaseqwebapp.client.mvp.main.MainPagePresenter;

public class PhenotypePresenter extends
		Presenter<PhenotypePresenter.MyView, PhenotypePresenter.MyProxy> {

	public interface MyView extends View {

		void showPhenotypeInfo(Phenotype phenotype);
		// TODO Put your view methods here
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.phenotypepage)
	public interface MyProxy extends ProxyPlace<PhenotypePresenter> {
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

	private final PlaceManager placeManager;
	private final DispatchAsync dispatch;
	private final PhenotypeReader phenotypeReader;
	private  final PhenotypeOverviewPresenter phenotypeOverviewPresenter;
	private final PhenotypeDetailPresenter phenotypeDetailPresenter;
	
	private Phenotype phenotype;
	private DataTable histogramDataTable;

	@Inject
	public PhenotypePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final PlaceManager placeManager, 
			final DispatchAsync dispatch, final PhenotypeReader phenotypeReader,
			final PhenotypeOverviewPresenter phenotypeOverviewPresenter,
			final PhenotypeDetailPresenter phenotypeDetailPresenter) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.dispatch = dispatch;
		this.phenotypeReader = phenotypeReader;
		this.phenotypeOverviewPresenter = phenotypeOverviewPresenter;
		this.phenotypeDetailPresenter = phenotypeDetailPresenter;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		PlaceRequest currentPlace = placeManager.getCurrentPlaceRequest();
		if (phenotype != null) {
			getView().showPhenotypeInfo(phenotype);
			if (currentPlace.getParameterNames().size() == 1) {
				phenotypeOverviewPresenter.setData(phenotype,histogramDataTable);
				setInSlot(TYPE_SetMainContent, phenotypeOverviewPresenter);
			}
			else {
				String env = currentPlace.getParameter("env", "");
				phenotypeDetailPresenter.setData(phenotype,phenotype.getEnvironmentFromName(env),getSingleHistogramDataTable(env));
				setInSlot(TYPE_SetMainContent, phenotypeDetailPresenter);
			}
		}
	}

	/*private DataTable getCombinedDataTable() {
		DataTable combined = DataTable.create();
		combined.addColumn(ColumnType.STRING, "Phenotype Value","x-axis");
		combined.addColumn(ColumnType.NUMBER,"10 °C","y-axis");
		combined.addColumn(ColumnType.NUMBER,"16 °C","y-axis");
		combined.addRows(hist10dataTable.getNumberOfRows());
		for (int i = 0;i<hist10dataTable.getNumberOfRows();i++) {
			combined.setValue(i, 0, hist10dataTable.getValueString(i, 0));
			combined.setValue(i, 1, hist10dataTable.getValueInt(i, 1));
			combined.setValue(i, 1, hist16dataTable.getValueInt(i, 1));
		}
		return combined;
	}*/

	private DataTable getSingleHistogramDataTable(String env) {
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "Phenotype Value","x-axis");
		dataTable.addColumn(ColumnType.NUMBER,"Frequency","y-axis");
		dataTable.addRows(histogramDataTable.getNumberOfRows());
		for (int i = 0;i<histogramDataTable.getNumberOfRows();i++) {
			dataTable.setValue(i, 0, histogramDataTable.getValueString(i, 0));
			dataTable.setValue(i, 1, histogramDataTable.getValueInt(i, (env.equals("T16C") ? 2 : 1)));
		}
		return dataTable;
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	public boolean useManualReveal() {
		return true;
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		PlaceRequest currentPlace = placeManager.getCurrentPlaceRequest();
		String phenotype_id = currentPlace.getParameter("id",""); 
		if (phenotype_id.equals(""))
			getProxy().manualReveal(PhenotypePresenter.this);
		else {
			dispatch.execute(new GetPhenotypeDataAction(phenotype_id, phenotypeReader), new CustomCallback<GetPhenotypeDataActionResult>(getEventBus()) {

				@Override
				public void onSuccess(GetPhenotypeDataActionResult result) {
					PhenotypePresenter.this.phenotype = result.getPhenotype();
					PhenotypePresenter.this.histogramDataTable = result.gethistogramdataTable();
					getProxy().manualReveal(PhenotypePresenter.this);
				}

				@Override
				public void onFailure(Throwable caught) {
					getProxy().manualReveal(PhenotypePresenter.this);
					super.onFailure(caught);
					//getProxy().manualRevealFailed();
				}
			});
		}
	}
}
