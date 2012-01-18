package com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist;


import java.util.HashMap;

import at.gmi.nordborglab.widgets.geneviewer.client.GeneViewer;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.DataSource;

import com.gmi.rnaseqwebapp.client.NameTokens;
import com.gmi.rnaseqwebapp.client.dto.SNPResult;
import com.gmi.rnaseqwebapp.client.dto.SNPResult.SNPResultPredicate;
import com.gmi.rnaseqwebapp.client.dto.SNPResult.SNPResultPredicate.CRITERIA;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.DetailsColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.GeneChrColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.GeneColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.GeneMidPosColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.MACColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.MAFColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.PercVarExplColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.ScoreColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.SnpChrColumn;
import com.gmi.rnaseqwebapp.client.mvp.analysis.topsnpslist.TopSNPsListDataGridColumns.SnpPosColumn;
import com.gmi.rnaseqwebapp.client.resources.MyResources;
import com.gmi.rnaseqwebapp.client.ui.HasSearchHandlers;
import com.gmi.rnaseqwebapp.client.ui.SearchTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class TopSNPsListView extends ViewWithUiHandlers<TopSNPsListUiHAndlers> implements TopSNPsListPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, TopSNPsListView> {
	}
	
	@UiField(provided=true) SimplePager pager;
	@UiField(provided=true) DataGrid<SNPResult> dataGrid;
	@UiField SearchTextBox Search_Name;
	@UiField SearchTextBox Search_Score;
	@UiField SpanElement top_list;
	@UiField AnchorElement download_link;
	@UiField GeneViewer geneViewer;
	@UiField Button btn_filter_chr_all;
	@UiField Button btn_filter_chr1;
	@UiField Button btn_filter_chr2;
	@UiField Button btn_filter_chr3;
	@UiField Button btn_filter_chr4;
	@UiField Button btn_filter_chr5;
	
	@UiField MyResources mainRes;
	private SelectionModel<SNPResult> selectionModel = new SingleSelectionModel<SNPResult>(SNPResult.KEY_PROVIDER);
	private HashMap<CRITERIA, SNPResultPredicate<?>> searchTerms;
	private String environType;
	private String resultType;

	@Inject
	public TopSNPsListView(final Binder binder,final MyResources mainRes,final DataSource jBrowseDataSource) {
		this.mainRes = mainRes;
		dataGrid = new DataGrid<SNPResult>(50,SNPResult.KEY_PROVIDER);
		dataGrid.setWidth("100%");
		dataGrid.setEmptyTableWidget(new Label("No Records found"));
		dataGrid.setSelectionModel(selectionModel);
		// Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(dataGrid);
		widget = binder.createAndBindUi(this);
		btn_filter_chr_all.addStyleName(mainRes.style().round_button_selected());
		geneViewer.setHeight("300px");
		geneViewer.setDataSource(jBrowseDataSource);
		Search_Name.getElement().setAttribute("placeHolder", "Name (Search on Enter)");
		Search_Score.getElement().setAttribute("placeHolder", "Score (Search on Enter)");
		try {
			geneViewer.load(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
				}
			});
		}
		catch (Exception exc) {
			
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void initDataGrid() {
		dataGrid.addColumn(new GeneColumn(searchTerms.get(CRITERIA.Gene)),"Phenotype");
		dataGrid.addColumn(new GeneChrColumn(searchTerms.get(CRITERIA.Gene_Chr)),"G_Chr");
		dataGrid.addColumn(new GeneMidPosColumn(),"G_Mid");
		dataGrid.addColumn(new SnpPosColumn(),"S_pos");
		dataGrid.addColumn(new SnpChrColumn(searchTerms.get(CRITERIA.SNP_Chr)),"S_chr");
		dataGrid.addColumn(new ScoreColumn(),"Score");
		dataGrid.addColumn(new MACColumn(),"MAC");
		dataGrid.addColumn(new MAFColumn(),"MAF");
		dataGrid.addColumn(new PercVarExplColumn(),"PVE");
		dataGrid.addColumn(new DetailsColumn(NameTokens.phenotypepage),"Action");
		
		dataGrid.setColumnWidth(dataGrid.getColumn(0),285, Unit.PX);
		dataGrid.setColumnWidth(dataGrid.getColumn(1),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(2),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(3),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(4),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(5),15, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(6),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(7),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(8),10, Unit.PCT);
		dataGrid.setColumnWidth(dataGrid.getColumn(9),100, Unit.PX);
	}

	@Override
	public void setSearchTerms(
			HashMap<CRITERIA, SNPResultPredicate<?>> searchTerms) {
		this.searchTerms = searchTerms;
		
	}

	@Override
	public HasData<SNPResult> getDisplay() {
		return dataGrid;
	}
	
	
	
	@Override
	public void setEnvironResult(String environType,String resultType) {
		this.environType = environType;
		this.resultType = resultType;
		top_list.setInnerHTML("Filter ("+environType+" | "+resultType+")");
		((DetailsColumn)dataGrid.getColumn(9)).setData(environType, resultType);
	}
	
	@Override
	public void setDownloadLink(String link) {
		download_link.setHref(link);
	}

	@Override
	public HasSearchHandlers getSearchNameHandlers() {
		return Search_Name;
	}

	@Override
	public HasSearchHandlers getScoreHandlers() {
		return Search_Score;
	}

	@Override
	public void setGeneViewerRegion(int chr, int start, int end) {
		geneViewer.setChromosome("Chr"+chr);
		geneViewer.setViewRegion(start, end);
		//geneViewer.updateZoom(start, end);
	}
	
	@UiHandler({"btn_filter_chr_all","btn_filter_chr1","btn_filter_chr2","btn_filter_chr3","btn_filter_chr4","btn_filter_chr5"})
	void handleClick(ClickEvent e) {
		String chr = "";
		Button button = (Button)e.getSource();
		if (button == btn_filter_chr1) 
			chr = "1";
		else if (button == btn_filter_chr2)
			chr = "2";
		else if (button == btn_filter_chr3)
			chr = "3";
		else if (button == btn_filter_chr4)
			chr = "4";
		else if (button == btn_filter_chr5)
			chr = "5";
		getUiHandlers().setActiveChromosomeFilter(chr);
		
	}
	
	@Override
	public void setActiveChromosomeFilter(String chr) {
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

}
