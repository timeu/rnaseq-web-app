package com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist;

import java.util.HashMap;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.command.GetSNPResultsAction;
import com.gmi.rnaseqwebapp.client.command.GetSNPResultsActionResult;
import com.gmi.rnaseqwebapp.client.dispatch.CustomCallback;
import com.gmi.rnaseqwebapp.client.dto.Readers.SNPResultsReader;
import com.gmi.rnaseqwebapp.client.dto.SNPResult;
import com.gmi.rnaseqwebapp.client.dto.SNPResult.SNPResultGeneChrPredicate;
import com.gmi.rnaseqwebapp.client.dto.SNPResult.SNPResultPredicate;
import com.gmi.rnaseqwebapp.client.dto.SNPResult.SNPResultPredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.dto.SNPResults;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisPresenter;
import com.gmi.rnaseqwebapp.client.ui.HasSearchHandlers;
import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class TopSNPsListPresenter extends Presenter<TopSNPsListPresenter.MyView, TopSNPsListPresenter.MyProxy> implements TopSNPsListUiHAndlers {
	
	public interface MyView extends View,HasUiHandlers<TopSNPsListUiHAndlers> {

		HasSearchHandlers getSearchNameHandlers();
		HasSearchHandlers getScoreHandlers();
		void setSearchTerms(HashMap<CRITERIA, SNPResultPredicate<?>> snpresultPredicates);
		void initDataGrid();
		HasData<SNPResult> getDisplay();
		void setDownloadLink(String link);
		void setGeneViewerRegion(int chr, int start, int end);
		void setEnvironResult(String environType, String resultType);
		void setActiveChromosomeFilter(String chr);
	}
	
	public class SNPResultDataProvider extends AsyncDataProvider<SNPResult> {

		@Override
		protected void onRangeChanged(HasData<SNPResult> display) {
			if (currentSNPResults == null || !currentSNPResults.isContained(display.getVisibleRange().getStart(),display.getVisibleRange().getLength()))
				requestResults();
			else
				updateView();
		}
		
	}
	
	class RequestSNPResultsCallback extends CustomCallback<GetSNPResultsActionResult> {

		public RequestSNPResultsCallback(EventBus eventBus) {
			super(eventBus);
		}

		@Override
		public void onSuccess(GetSNPResultsActionResult result) {
			currentSNPResults = result.getSNPResults();
			updateView();
		}
		
	}
	
	class SearchKeyUpHandler<T extends SNPResultPredicate<String>> implements KeyUpHandler {
	
		T predicate;
		
		public SearchKeyUpHandler(T predicate) {
			this.predicate = predicate;
		}
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE)
				isSearchTermExpanded = true;
			isSearchModified = true;
			String value = getValue(event);
			predicate.setValue(value);
			if ((event.getNativeKeyCode() == KeyCodes.KEY_ENTER) && isSearchModified) 
				requestResults();
		}
		
		public String getValue(KeyUpEvent event) {
			HasSearchHandlers getSearchValue = (HasSearchHandlers)event.getSource();
			String value = getSearchValue.getText();
			return value;
		}
	}
	 
	class SearchBlurHandler implements  BlurHandler {

			@Override
			public void onBlur(BlurEvent event) {
				if (isSearchModified)
					requestResults();
			}
	}
	
	class DataGridSelectionChangeHandler implements SelectionChangeEvent.Handler {
		static final int range = 10000;
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			SNPResult snpResult = ((SingleSelectionModel<SNPResult>)getView().getDisplay().getSelectionModel()).getSelectedObject();
			getView().setGeneViewerRegion(snpResult.getSnpChr(), snpResult.getSnpPos()-range,snpResult.getSnpPos()+range);
		}
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.topsnpslistpage)
	public interface MyProxy extends ProxyPlace<TopSNPsListPresenter> {
	}
	protected SNPResultDataProvider dataProvider = new SNPResultDataProvider();
	protected HashMap<CRITERIA,SNPResultPredicate<?>> snpresultPredicates = new HashMap<CRITERIA,SNPResultPredicate<?>>();
	protected boolean isPolling = false;
	protected boolean isSearchModified = false;
	protected boolean isSearchTermExpanded = false;
	protected SNPResults currentSNPResults;
	protected final DispatchAsync dispatch; 
	protected final SNPResultsReader snpsresultsReader;
	protected final PlaceManager placeManager;
	protected String environ_type ="";
	protected String result_type ="";
	
	@Inject
	public TopSNPsListPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			final DispatchAsync dispatch, final PlaceManager placeManager,
			final SNPResultsReader snpsresultsReader) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		this.dispatch = dispatch;
		this.placeManager = placeManager;
		this.snpsresultsReader = snpsresultsReader;
		snpresultPredicates.put(CRITERIA.Gene,new SNPResult.SNPResultGenePredicate(""));
		snpresultPredicates.put(CRITERIA.Gene_Chr,new SNPResult.SNPResultGeneChrPredicate(""));
		snpresultPredicates.put(CRITERIA.Min_Score,new SNPResult.SNPResultMinScorePredicate(null));
		snpresultPredicates.put(CRITERIA.SNP_Chr,new SNPResult.SNPResultSNPChrPredicate(""));
		getView().setSearchTerms(snpresultPredicates);
		dataProvider.addDataDisplay(getView().getDisplay());
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, AnalysisPresenter.TYPE_SetMainContent, this);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		getView().initDataGrid();
		SearchBlurHandler seachBlurHandler = new SearchBlurHandler();
		registerHandler(getView().getSearchNameHandlers().addKeyUpHandler(new SearchKeyUpHandler(snpresultPredicates.get(CRITERIA.Gene))));
		registerHandler(getView().getScoreHandlers().addKeyUpHandler(new SearchKeyUpHandler(snpresultPredicates.get(CRITERIA.Min_Score))));
		registerHandler(getView().getDisplay().getSelectionModel().addSelectionChangeHandler(new DataGridSelectionChangeHandler()));
		//registerHandler(getView().getSearchNameHandlers().addBlurHandler(seachBlurHandler));
		//registerHandler(getView().getScoreHandlers().addBlurHandler(seachBlurHandler));
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		
	}
	
	
	@Override
	public void prepareFromRequest(PlaceRequest placeRequest) {
	    super.prepareFromRequest(placeRequest);
	    String new_environ_type = placeRequest.getParameter("environ_type", "");
		String new_result_type = placeRequest.getParameter("result_type", "");
		if (environ_type != new_environ_type || result_type != new_result_type ) {
			isSearchTermExpanded = true;
			isSearchModified = true;
		}
		environ_type = new_environ_type;
		result_type = new_result_type;
		String title ="";
		if(environ_type.equals("T10C"))
			title = "10°C";
		else if (environ_type.equals("T16C"))
			title = "16°C";
		else 
			title = "GxE";
		if (result_type == "EX")
			title = title +" | EMMAX";
		else
			title = title + " | " + result_type;
		getView().setEnvironResult(environ_type, result_type);
		requestResults();
	}
	
	private void requestResults() {
		if (!isPolling && environ_type != "" && result_type != "") {
			isPolling = true;
			final Range range = getView().getDisplay().getVisibleRange();
			if (currentSNPResults == null || isSearchTermExpanded || (currentSNPResults.getCount() > currentSNPResults.getList().size()))
				dispatch.execute(new GetSNPResultsAction(environ_type,result_type,range.getStart(),2000, snpresultPredicates.values(),snpsresultsReader), new RequestSNPResultsCallback(getEventBus()));
			else {
				currentSNPResults.update(AbstractDtoPredicate.filter(currentSNPResults.getList(), snpresultPredicates.values()));
				updateView();
			}
		}
	}
	
	private void updateView() {
		dataProvider.updateRowCount(currentSNPResults.getCount(), true);
		dataProvider.updateRowData(currentSNPResults.getStart(), currentSNPResults.getList());
		isSearchModified = false;
		isPolling = false;
		isSearchTermExpanded = false;
		String baseDownloadLink = "/gwas/downloadTopResults?environ_type="+environ_type+"&result_type="+result_type;
		getView().setDownloadLink(baseDownloadLink+AbstractDtoPredicate.toRequestString(snpresultPredicates.values()));
	}

	@Override
	public void setActiveChromosomeFilter(String chromosome) {
		SNPResultGeneChrPredicate predicate =  (SNPResultGeneChrPredicate)snpresultPredicates.get(CRITERIA.Gene_Chr);
		if (currentSNPResults.getCount() == 0 || (!predicate.getValue().equals(chromosome)));
			isSearchTermExpanded = true;
		predicate.setValue(chromosome);
		requestResults();
		getView().setActiveChromosomeFilter(chromosome);
	}
	
}
