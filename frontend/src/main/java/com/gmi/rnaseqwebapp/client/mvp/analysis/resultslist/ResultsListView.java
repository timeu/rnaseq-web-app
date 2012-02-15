package com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist;

import java.util.HashMap;
import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.GeneViewer;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.Gene;
import at.gmi.nordborglab.widgets.geneviewer.client.event.HasClickGeneHandlers;
import at.gmi.nordborglab.widgets.geneviewer.client.event.HasZoomResizeHandlers;
import at.gmi.nordborglab.widgets.geneviewer.client.event.HighlightGeneEvent;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.Phenotype;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate;
import com.gmi.rnaseqwebapp.client.dto.Phenotype.PhenotypePredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.ChrColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.DetailsColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.EndColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.MaxScore10Column;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.MaxScore16Column;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.MaxScoreFullColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.NameColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.PhenotypeIdColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.PseudoHeritability10Column;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.PseudoHeritability16Column;
import com.gmi.rnaseqwebapp.client.mvp.analysis.resultslist.ResultsListDataGridColumns.StartColumn;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.ui.HasSearchHandlers;
import com.gmi.rnaseqwebapp.client.ui.SearchTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;


public class ResultsListView extends ViewImpl implements
		ResultsListPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ResultsListView> {
	}
	
	@UiField(provided=true) SimplePager pager;
	@UiField DockLayoutPanel container;
	@UiField(provided=true) DataGrid<Phenotype> dataGrid;
	@UiField SearchTextBox Search_Name;
	@UiField SearchTextBox Search_Start;
	@UiField SearchTextBox Search_End;
	@UiField Button btn_filter_chr_all;
	@UiField Button btn_filter_chr1;
	@UiField Button btn_filter_chr2;
	@UiField Button btn_filter_chr3;
	@UiField Button btn_filter_chr4;
	@UiField Button btn_filter_chr5;
	@UiField MyResources mainRes;
	@UiField GeneViewer geneViewer;
	@UiField SimpleCheckBox cb_real_time;
	
	private int currentChr = -1;
	private SelectionModel<Phenotype> selectionModel = new SingleSelectionModel<Phenotype>(Phenotype.KEY_PROVIDER);
	
	private HashMap<CRITERIA, PhenotypePredicate<?>> searchTerms;
	private List<Integer> chrSizes;

	@Inject
	public ResultsListView(final Binder binder,final MyResources resources,final DataSource jBrowseDataSource) {
		this.mainRes = resources;
		dataGrid = new DataGrid<Phenotype>(50,Phenotype.KEY_PROVIDER);
		dataGrid.setWidth("100%");
		dataGrid.setEmptyTableWidget(new Label("No Records found"));
		dataGrid.setSelectionModel(selectionModel);
		// Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(dataGrid);
		widget = binder.createAndBindUi(this);
		geneViewer.setHeight("300px");
		btn_filter_chr_all.addStyleName(mainRes.style().round_button_selected());
		Search_Start.getElement().setAttribute("placeHolder", "Start (Search on Enter)");
		Search_End.getElement().setAttribute("placeHolder", "End (Search on Enter)");
		Search_Name.getElement().setAttribute("placeHolder", "Name (Search on Enter)");
		geneViewer.setDataSource(jBrowseDataSource);
		
		cb_real_time.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String search_text = "Name";
				search_text = search_text + " (" + (cb_real_time.getValue() ? "Search on KeyPress" :"Search on Enter") + ")";
				Search_Name.getElement().setAttribute("placeHolder",search_text);
			}
		});
	}
	


	@Override
	public Widget asWidget() {
		return widget;
	}



	@Override
	public void initDataGrid() {
		dataGrid.addColumn(new PhenotypeIdColumn(),"ID");
		dataGrid.addColumn(new NameColumn(searchTerms.get(CRITERIA.Name)),"Name");
		dataGrid.addColumn(new MaxScore10Column(),"S10C");
		dataGrid.addColumn(new MaxScore16Column(),"S16C");
		dataGrid.addColumn(new MaxScoreFullColumn(),"SFull");
		dataGrid.addColumn(new PseudoHeritability10Column(),"P10C");
		dataGrid.addColumn(new PseudoHeritability16Column(),"P10C");
		dataGrid.addColumn(new ChrColumn(searchTerms.get(CRITERIA.Chr)),"Chr");
		dataGrid.addColumn(new StartColumn(),"Start");
		dataGrid.addColumn(new EndColumn(),"End");
		dataGrid.addColumn(new DetailsColumn(NameTokens.phenotypepage),"Action");
		
		dataGrid.setColumnWidth(dataGrid.getColumn(0),80,Unit.PX );
		dataGrid.setColumnWidth(dataGrid.getColumn(1),285, Unit.PX);
		
		/*dataGrid.setColumnWidth(dataGrid.getColumn(3),20, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(4),20, Unit.PCT);*/
		dataGrid.setColumnWidth(dataGrid.getColumn(2),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(3),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(4),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(5),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(6),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(7),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(7),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(8),285, Unit.PX);
		dataGrid.setColumnWidth(dataGrid.getColumn(9),285, Unit.PX);
		dataGrid.setColumnWidth(dataGrid.getColumn(10),100,Unit.PX);
	}
	
	@Override
	public void initGeneViewer(Runnable run) {
		try {
			geneViewer.load(run);
		}
		catch (Exception ex) {
			
		}
	}



	@Override
	public void setSearchTerms(HashMap<CRITERIA, PhenotypePredicate<?>> searchTerms) {
		this.searchTerms = searchTerms;
	}



	@Override
	public HasData<Phenotype> getDisplay() {
		return dataGrid;
	}



	@Override
	public HasSearchHandlers getSearchNameHandlers() {
		return Search_Name;
		//LayoutPanel
	}



	@Override
	public HasSearchHandlers getSearchStartHandlers() {
		return Search_Start;
	}



	@Override
	public HasSearchHandlers getSearchEndHandlers() {
		return Search_End;
	}



	@Override
	public HasClickHandlers getFilterChromosomeAllHandlers() {
		return btn_filter_chr_all;
	}



	@Override
	public HasClickHandlers getFilterChromosome1Handlers() {
		return btn_filter_chr1;
	}



	@Override
	public HasClickHandlers getFilterChromosome2Handlers() {
		return btn_filter_chr2;
	}



	@Override
	public HasClickHandlers getFilterChromosome3Handlers() {
		return btn_filter_chr3;
	}



	@Override
	public HasClickHandlers getFilterChromosome4Handlers() {
		return btn_filter_chr4;
	}



	@Override
	public HasClickHandlers getFilterChromosome5Handlers() {
		return btn_filter_chr5;
	}



	@Override
	public void setActiveChromosomeFilter(Object chr) {
		chr = chr.toString().toLowerCase();
		String selected_style = mainRes.style().round_button_selected();
		btn_filter_chr_all.removeStyleName(selected_style);
		btn_filter_chr1.removeStyleName(selected_style);
		btn_filter_chr2.removeStyleName(selected_style);
		btn_filter_chr3.removeStyleName(selected_style);
		btn_filter_chr4.removeStyleName(selected_style);
		btn_filter_chr5.removeStyleName(selected_style);
		if (chr.equals("1")) 
			btn_filter_chr1.addStyleName(selected_style);
		else if (chr.equals("2")) 
			btn_filter_chr2.addStyleName(selected_style);
		else if (chr.equals("3")) 
			btn_filter_chr3.addStyleName(selected_style);
		else if (chr.equals("4")) 
			btn_filter_chr4.addStyleName(selected_style);
		else if (chr.equals("5")) 
			btn_filter_chr5.addStyleName(selected_style);
		else
		 	btn_filter_chr_all.addStyleName(selected_style);
	}



	@Override
	public void setGeneViewerRegion(int chr, int start, int end) {
		if (currentChr != chr)  {
			currentChr = chr;
			geneViewer.setChromosome("Chr"+currentChr);
			geneViewer.setViewRegion(0, chrSizes.get(chr-1));
		}
		geneViewer.updateZoom(start, end);
	}



	@Override
	public void setGeneViewerZoom(int start, int stop) {
		geneViewer.updateZoom(start, stop);
	}



	@Override
	public void forceLayout() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				geneViewer.onResize();
			}
		});
	}



	@Override
	public void highLightGene(Gene gene) {
		geneViewer.onHightlightGene(new HighlightGeneEvent(gene, 0, 0));
	}



	@Override
	public HasZoomResizeHandlers getGeneViewerZoomHandlers() {
		return geneViewer;
	}



	@Override
	public HasClickGeneHandlers getGeneViewerClickGeneHandlers() {
		return geneViewer;
	}



	@Override
	public TakesValue<Boolean> getRealTimeCBValue() {
		return cb_real_time;
	}



	@Override
	public void setChrSizes(List<Integer> chrSizes) {
		this.chrSizes = chrSizes;
		
	}



	@Override
	public void scrollDataGridItemIntoView(Phenotype phenotype) {
		dataGrid.getRowElement(dataGrid.getVisibleItems().indexOf(phenotype)).scrollIntoView();
	}
	
	
}
