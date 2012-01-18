package com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.Gene;
import at.gmi.nordborglab.widgets.geneviewer.client.event.ClickGeneEvent;
import at.gmi.nordborglab.widgets.geneviewer.client.event.ClickGeneHandler;
import at.gmi.nordborglab.widgets.geneviewer.client.event.HasClickGeneHandlers;
import at.gmi.nordborglab.widgets.geneviewer.client.event.HasZoomResizeHandlers;
import at.gmi.nordborglab.widgets.geneviewer.client.event.ZoomResizeEvent;
import at.gmi.nordborglab.widgets.geneviewer.client.event.ZoomResizeHandler;

import com.gmi.rnaseqwebapp.client.ClientData;
import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypesAction;
import com.gmi.rnaseqwebapp.client.command.GetPhenotypesActionResult;
import com.gmi.rnaseqwebapp.client.dispatch.CustomCallback;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypeChrPredicate;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypeEndPredicate;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypeNamePredicate;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypeStartPredicate;
import com.gmi.rnaseqwebapp.client.dto.Phenotypes;
import com.gmi.rnaseqwebapp.client.dto.Readers.PhenotypesReader;
import com.gmi.rnaseqwebapp.client.mvp.analysis.AnalysisPresenter;
import com.gmi.rnaseqwebapp.client.ui.HasSearchHandlers;
import com.gmi.rnaseqwebapp.client.util.AbstractDtoPredicate;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class ResultsListPresenter extends
		Presenter<ResultsListPresenter.MyView, ResultsListPresenter.MyProxy> {

	public interface MyView extends View {

		void initDataGrid();
		HasData<Phenotype> getDisplay();
		void setSearchTerms(HashMap<CRITERIA,PhenotypePredicate<?>> searchTerms);
		HasSearchHandlers getSearchNameHandlers();
		HasSearchHandlers getSearchStartHandlers();
		HasSearchHandlers getSearchEndHandlers();
		HasClickHandlers getFilterChromosomeAllHandlers();
		HasClickHandlers getFilterChromosome1Handlers();
		HasClickHandlers getFilterChromosome2Handlers();
		HasClickHandlers getFilterChromosome3Handlers();
		HasClickHandlers getFilterChromosome4Handlers();
		HasClickHandlers getFilterChromosome5Handlers();
		HasZoomResizeHandlers getGeneViewerZoomHandlers();
		TakesValue<Boolean> getRealTimeCBValue();
		HasClickGeneHandlers getGeneViewerClickGeneHandlers();
		void setGeneViewerRegion(int chr, int start, int end);
		void initGeneViewer(Runnable runnable);
		void setGeneViewerZoom(int start, int stop);
		void setActiveChromosomeFilter(Object chr);
		void highLightGene(Gene gene);
		void forceLayout();
		void setChrSizes(List<Integer> chrSizes);
		void scrollDataGridItemIntoView(Phenotype phenotype);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.resultslistpage)
	public interface MyProxy extends ProxyPlace<ResultsListPresenter> {
	}
	
	protected HashMap<CRITERIA,PhenotypePredicate<?>> phenotypePredicates = new HashMap<CRITERIA,PhenotypePredicate<?>>();
	protected PhenotypeDataProvider dataProvider = new PhenotypeDataProvider();
	protected final DispatchAsync dispatch;
	protected final PhenotypesReader phenotypesReader;
	protected boolean isSearchModified = false;
	protected boolean isPolling = false;
	protected boolean isSearchTermExpanded = false;
	protected final ClientData clientData;
	protected Phenotypes currentPhenotypes;
	protected final PlaceManager placeManager;

	@Inject
	public ResultsListPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy,final DispatchAsync dispatch,final PhenotypesReader phenotypesReader,
			final ClientData clientData,final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.dispatch = dispatch;
		this.phenotypesReader = phenotypesReader;
		this.clientData = clientData;
		this.placeManager = placeManager;
		phenotypePredicates.put(CRITERIA.Name,new Phenotype.PhenotypeNamePredicate(""));
		phenotypePredicates.put(CRITERIA.Chr,new Phenotype.PhenotypeChrPredicate(""));
		phenotypePredicates.put(CRITERIA.Start,new Phenotype.PhenotypeStartPredicate(null));
		phenotypePredicates.put(CRITERIA.End,new Phenotype.PhenotypeEndPredicate(null));
		getView().setSearchTerms(phenotypePredicates);
		getView().setChrSizes(clientData.getChrSizes());
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, AnalysisPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		dataProvider.addDataDisplay(getView().getDisplay());
		getView().initDataGrid();
		getView().initGeneViewer(new Runnable() {
			
			@Override
			public void run() {
				registerHandler(getView().getGeneViewerZoomHandlers().addZoomResizeHandler(new GeneZoomResizeHandler()));
				registerHandler(getView().getGeneViewerClickGeneHandlers().addClickGeneHandler(new GeneClickHandler()));
			}
		});
		SearchBlurHandler seachBlurHandler = new SearchBlurHandler();
		registerHandler(getView().getSearchNameHandlers().addKeyUpHandler(new NameSearchKeyUphandler((PhenotypeNamePredicate) phenotypePredicates.get(PhenotypePredicate.CRITERIA.Name))));
		registerHandler(getView().getSearchStartHandlers().addKeyUpHandler(new RangeSearchKeyUpHandler((PhenotypeStartPredicate)phenotypePredicates.get(PhenotypePredicate.CRITERIA.Start))));
		registerHandler(getView().getSearchEndHandlers().addKeyUpHandler(new RangeSearchKeyUpHandler((PhenotypeEndPredicate)phenotypePredicates.get(PhenotypePredicate.CRITERIA.End))));
		registerHandler(getView().getSearchStartHandlers().addBlurHandler(seachBlurHandler));
		registerHandler(getView().getSearchEndHandlers().addBlurHandler(seachBlurHandler));
		registerHandler(getView().getFilterChromosomeAllHandlers().addClickHandler(new FilterClickHandler("")));
		registerHandler(getView().getFilterChromosome1Handlers().addClickHandler(new FilterClickHandler("1")));
		registerHandler(getView().getFilterChromosome2Handlers().addClickHandler(new FilterClickHandler("2")));
		registerHandler(getView().getFilterChromosome3Handlers().addClickHandler(new FilterClickHandler("3")));
		registerHandler(getView().getFilterChromosome4Handlers().addClickHandler(new FilterClickHandler("4")));
		registerHandler(getView().getFilterChromosome5Handlers().addClickHandler(new FilterClickHandler("5")));
		registerHandler(getView().getDisplay().getSelectionModel().addSelectionChangeHandler(new DataGridSelectionChangeHandler()));
	}
	
	
	@Override
	protected void onReveal() {
		super.onReveal();
		getView().forceLayout();
	}
	
	public class PhenotypeDataProvider extends AsyncDataProvider<Phenotype> {

		@Override
		protected void onRangeChanged(HasData<Phenotype> display) {
			if (currentPhenotypes == null || !currentPhenotypes.isContained(display.getVisibleRange().getStart(),display.getVisibleRange().getLength()))
				requestResults();
			else
				updateView();
		}
		
	}
	
	private void requestResults() {
		if (!isPolling) {
			isPolling = true;
			final Range range = getView().getDisplay().getVisibleRange();
			if (currentPhenotypes == null || isSearchTermExpanded || (currentPhenotypes.getCount() > currentPhenotypes.getPhenotypes().size()))
				dispatch.execute(new GetPhenotypesAction(range.getStart(),2000, phenotypePredicates.values(),phenotypesReader), new RequestPhenotypeCallback(getEventBus()));
			else {
				currentPhenotypes.update(AbstractDtoPredicate.filter(currentPhenotypes.getPhenotypes(), phenotypePredicates.values()));
				updateView();
			}
		}
	}
	
	private void updateView() {
		dataProvider.updateRowCount(currentPhenotypes.getCount(), true);
		dataProvider.updateRowData(currentPhenotypes.getStart(), currentPhenotypes.getPhenotypes());
		isSearchModified = false;
		isPolling = false;
		isSearchTermExpanded = false;
		getView().setActiveChromosomeFilter(phenotypePredicates.get(CRITERIA.Chr).getValue());
		GeneRange gene_range = getChrRange();
		if (gene_range != null)
			getView().setGeneViewerRegion(gene_range.getChr(),gene_range.getStart()-1000, gene_range.getStop()+1000 );
	}
	
	class RequestPhenotypeCallback extends CustomCallback<GetPhenotypesActionResult> {

		public RequestPhenotypeCallback(EventBus eventBus) {
			super(eventBus);
		}

		@Override
		public void onSuccess(GetPhenotypesActionResult result) {
			currentPhenotypes = result.getPhenotypes();
			updateView();
		}
		
	}
	
	class RangeSearchKeyUpHandler extends SearchKeyUpHandler<PhenotypePredicate<Number>> {

		public RangeSearchKeyUpHandler(PhenotypePredicate<Number> predicate) {
			super(predicate);
		}
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			super.onKeyUp(event);
			predicate.setValue(getNumberFromString(getValue(event)));
			if ((event.getNativeKeyCode() == KeyCodes.KEY_ENTER) && isSearchModified) 
				requestResults();
		}
		
		protected Number getNumberFromString(String value) {
			value = value.toLowerCase();
			Integer range = null;
			try 
			{
				range =  Integer.parseInt( value );  
			      
			}
			catch (Exception e) {
				try {
					int multi = 1;
					if (value.endsWith("m"))
						multi = (int)Math.pow(10, 6);
					else if (value.endsWith("k"))
						multi = (int)Math.pow(10, 3);
					range = Integer.parseInt(value.substring(0,value.length()-1))*multi;
				}
				catch (Exception e2) {
					
				}
			}
			return range;
		}
	}
	
	
	class NameSearchKeyUphandler extends SearchKeyUpHandler<PhenotypeNamePredicate> {

		public NameSearchKeyUphandler(PhenotypeNamePredicate predicate) {
			super(predicate);
		}
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			super.onKeyUp(event);
			boolean isRealTimeSearch = getView().getRealTimeCBValue().getValue();
			String value = getValue(event);
			predicate.setValue(value);
			if ((value.equals("A") || value.equals("AT") && !isSearchTermExpanded)) 
				updateView();
			else if (isRealTimeSearch || ((event.getNativeKeyCode() == KeyCodes.KEY_ENTER) && isSearchModified ))
				requestResults();
		}
	}
	
	abstract class  SearchKeyUpHandler<T extends PhenotypePredicate> implements KeyUpHandler {
		
		final T predicate;
		
		public SearchKeyUpHandler(T predicate) {
			this.predicate = predicate;
		}
		
		
		public boolean checkSpecialKeys(KeyUpEvent event) {
			switch (event.getNativeKeyCode()) {
			case KeyCodes.KEY_ALT:
			case KeyCodes.KEY_CTRL:
			case KeyCodes.KEY_DOWN:
			case KeyCodes.KEY_END:
			case KeyCodes.KEY_ESCAPE:
			case KeyCodes.KEY_HOME:
			case KeyCodes.KEY_LEFT:
			case KeyCodes.KEY_PAGEDOWN:
			case KeyCodes.KEY_PAGEUP:
			case KeyCodes.KEY_RIGHT:
			case KeyCodes.KEY_SHIFT:
			case KeyCodes.KEY_TAB:
			case KeyCodes.KEY_UP:
				return true;
			}
			return false;
		}
		
		public String getValue(KeyUpEvent event) {
			HasSearchHandlers getSearchValue = (HasSearchHandlers)event.getSource();
			String value = getSearchValue.getText();
			return value;
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE)
				isSearchTermExpanded = true;
			isSearchModified = true;
		}
			
		

		/*@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE)
				isSearchTermExapended = true;
			
			HasSearchHandlers getSearchValue = (HasSearchHandlers)event.getSource();
			String value = getSearchValue.getText();
			predicate.setValue(value);
			if (value.equals("A") || value.equals("AT"))
				return;
			isSearchModified = true;
			
			if ((!searchOnEnter || event.getNativeKeyCode() == KeyCodes.KEY_ENTER) && isSearchModified) 
				requestResults();
		}*/
	}
	
	class SearchBlurHandler implements  BlurHandler {

		@Override
		public void onBlur(BlurEvent event) {
			if (isSearchModified)
				requestResults();
		}
	}
	
	class FilterClickHandler implements ClickHandler {
		
		final String chr;
	
		public FilterClickHandler(String chr) {
			this.chr = chr;
		}

		@Override
		public void onClick(ClickEvent event) {
			PhenotypeChrPredicate predicate =  (PhenotypeChrPredicate)phenotypePredicates.get(CRITERIA.Chr);
			if (currentPhenotypes.getCount() == 0 || (!predicate.getValue().equals(chr)));
				isSearchTermExpanded = true;
			predicate.setValue(chr);
			requestResults();
		}
	}
	
	private GeneRange getChrRange()  {
		
		Iterator<Phenotype> iterator =  getView().getDisplay().getVisibleItems().iterator();
		if (!iterator.hasNext())
			return null;
		Phenotype start_phenotype = iterator.next();
		Phenotype stop_phenotype = start_phenotype;
		while (iterator.hasNext()) {
			Phenotype phenotype = iterator.next();
			if (phenotype.getChr() == start_phenotype.getChr()) {
				if (phenotype.getStart() < start_phenotype.getStart())
					start_phenotype = phenotype;
				if (phenotype.getEnd() > stop_phenotype.getEnd())
					stop_phenotype = phenotype;
			}
		}
		return new GeneRange(start_phenotype.getChr(),start_phenotype.getStart(),stop_phenotype.getEnd());
	}
	
	class DataGridSelectionChangeHandler implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			Phenotype phenotype = ((SingleSelectionModel<Phenotype>)getView().getDisplay().getSelectionModel()).getSelectedObject();
			Gene gene = new Gene(phenotype.getName()+".1",phenotype.getChr().toString(),0,0,null);
			//getView().highLightGene(gene);
		}
	}
	
	class GeneZoomResizeHandler implements ZoomResizeHandler {

		@Override
		public void onZoomResize(ZoomResizeEvent event) {
			Integer start =  event.start;
			Integer end = event.stop;
			PhenotypePredicate<Number> predicate = (PhenotypeStartPredicate)phenotypePredicates.get(CRITERIA.Start);
			predicate.setValue(start);
			getView().getSearchEndHandlers().setText(end.toString());
			getView().getSearchStartHandlers().setText(start.toString());
			predicate = (PhenotypeEndPredicate)phenotypePredicates.get(CRITERIA.End);
			predicate.setValue(end);
			requestResults();
		}
		
	}
	
	class GeneClickHandler implements ClickGeneHandler {

		@Override
		public void onClickGene(ClickGeneEvent event) {
			String name = event.getGene().getName();
			String names[] = name.split("\\.");
			Iterator<Phenotype> iterator = getView().getDisplay().getVisibleItems().iterator();
			while (iterator.hasNext()) {
				Phenotype phenotype = iterator.next();
				if (phenotype.getName().equals(names[0])) {
					getView().getDisplay().getSelectionModel().setSelected(phenotype, true);
					getView().scrollDataGridItemIntoView(phenotype);
					return;
				}
			}
		}
		
	}
	
	
	
	static class GeneRange {
		
		protected final int chr;
		protected final int start;
		protected final int stop;
		
		public GeneRange(int chr,int start, int stop) {
			this.chr = chr;
			this.start = start;
			this.stop = stop;
		}

		public int getChr() {
			return chr;
		}

		public int getStart() {
			return start;
		}

		public int getStop() {
			return stop;
		}
	}
}
